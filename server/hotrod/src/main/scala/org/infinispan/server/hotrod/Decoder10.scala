package org.infinispan.server.hotrod

import org.infinispan.server.core.Operation._
import HotRodOperation._
import OperationStatus._
import org.infinispan.Cache
import org.infinispan.stats.Stats
import org.infinispan.server.core._
import collection.mutable
import collection.immutable
import org.infinispan.util.concurrent.TimeoutException
import java.io.IOException
import org.infinispan.context.Flag.SKIP_REMOTE_LOOKUP
import org.infinispan.util.ByteArrayKey
import org.jboss.netty.buffer.ChannelBuffer
import org.infinispan.server.core.transport.ExtendedChannelBuffer._

/**
 * HotRod protocol decoder specific for specification version 1.0.
 *
 * @author Galder Zamarreño
 * @since 4.1
 */
object Decoder10 extends AbstractVersionedDecoder with Logging {
   import OperationResponse._
   import ProtocolFlag._
   type SuitableHeader = HotRodHeader

   override def readHeader(buffer: ChannelBuffer, messageId: Long): (HotRodHeader, Boolean) = {
      val streamOp = buffer.readUnsignedByte
      val (op, endOfOp) = streamOp match {
         case 0x01 => (PutRequest, false)
         case 0x03 => (GetRequest, false)
         case 0x05 => (PutIfAbsentRequest, false)
         case 0x07 => (ReplaceRequest, false)
         case 0x09 => (ReplaceIfUnmodifiedRequest, false)
         case 0x0B => (RemoveRequest, false)
         case 0x0D => (RemoveIfUnmodifiedRequest, false)
         case 0x0F => (ContainsKeyRequest, false)
         case 0x11 => (GetWithVersionRequest, false)
         case 0x13 => (ClearRequest, true)
         case 0x15 => (StatsRequest, true)
         case 0x17 => (PingRequest, true)
         case 0x19 => (BulkGetRequest, false)
         case _ => throw new HotRodUnknownOperationException("Unknown operation: " + streamOp, messageId)
      }
      if (isTraceEnabled) trace("Operation code: %d has been matched to %s", streamOp, op)
      
      val cacheName = readString(buffer)
      val flag = readUnsignedInt(buffer) match {
         case 0 => NoFlag
         case 1 => ForceReturnPreviousValue
      }
      val clientIntelligence = buffer.readUnsignedByte
      val topologyId = readUnsignedInt(buffer)
      // TODO: Use these once transaction support is added
      val txId = buffer.readByte
      if (txId != 0) throw new UnsupportedOperationException("Transaction types other than 0 (NO_TX) is not supported at this stage.  Saw TX_ID of " + txId)

      (new HotRodHeader(op, messageId, cacheName, flag, clientIntelligence, topologyId, this), endOfOp)
   }

   override def readKey(h: HotRodHeader, buffer: ChannelBuffer): (ByteArrayKey, Boolean) = {
      val k = readKey(buffer)
      h.op match {
         case RemoveRequest => (k, true)
         case _ => (k, false)
      }
   }

   private def readKey(buffer: ChannelBuffer): ByteArrayKey = new ByteArrayKey(readRangedBytes(buffer))

   override def readParameters(header: HotRodHeader, buffer: ChannelBuffer): RequestParameters = {
      header.op match {
         case RemoveRequest => null
         case RemoveIfUnmodifiedRequest => new RequestParameters(null, -1, -1, buffer.readLong)
         case ReplaceIfUnmodifiedRequest => {
            val lifespan = readLifespanOrMaxIdle(buffer)
            val maxIdle = readLifespanOrMaxIdle(buffer)
            val version = buffer.readLong
            new RequestParameters(readRangedBytes(buffer), lifespan, maxIdle, version)
         }
         case _ => {
            val lifespan = readLifespanOrMaxIdle(buffer)
            val maxIdle = readLifespanOrMaxIdle(buffer)
            new RequestParameters(readRangedBytes(buffer), lifespan, maxIdle, -1)
         }
      }
   }

   private def readLifespanOrMaxIdle(buffer: ChannelBuffer): Int = {
      val stream = readUnsignedInt(buffer)
      if (stream <= 0) -1 else stream
   }

   override def createValue(params: RequestParameters, nextVersion: Long): CacheValue =
      new CacheValue(params.data, nextVersion)

   override def createSuccessResponse(header: HotRodHeader, prev: CacheValue): AnyRef =
      createResponse(header, toResponse(header.op), Success, prev)

   override def createNotExecutedResponse(header: HotRodHeader, prev: CacheValue): AnyRef =
      createResponse(header, toResponse(header.op), OperationNotExecuted, prev)

   override def createNotExistResponse(header: HotRodHeader): AnyRef =
      createResponse(header, toResponse(header.op), KeyDoesNotExist, null)

   private def createResponse(h: HotRodHeader, op: OperationResponse, st: OperationStatus, prev: CacheValue): AnyRef = {
      if (h.flag == ForceReturnPreviousValue)
         new ResponseWithPrevious(h.messageId, h.cacheName, h.clientIntel, op, st, h.topologyId,
            if (prev == null) None else Some(prev.data))
      else
         new Response(h.messageId, h.cacheName, h.clientIntel, op, st, h.topologyId)
   }

   override def createGetResponse(h: HotRodHeader, v: CacheValue): AnyRef = {
      val op = h.op
      if (v != null && op == GetRequest)
         new GetResponse(h.messageId, h.cacheName, h.clientIntel, GetResponse, Success, h.topologyId, Some(v.data))
      else if (v != null && op == GetWithVersionRequest)
         new GetWithVersionResponse(h.messageId, h.cacheName, h.clientIntel, GetWithVersionResponse, Success,
            h.topologyId, Some(v.data), v.version)
      else if (op == GetRequest)
         new GetResponse(h.messageId, h.cacheName, h.clientIntel, GetResponse, KeyDoesNotExist, h.topologyId, None)
      else
         new GetWithVersionResponse(h.messageId, h.cacheName, h.clientIntel, GetWithVersionResponse, KeyDoesNotExist,
            h.topologyId, None, 0)
   }

   override def customReadHeader(h: HotRodHeader, buffer: ChannelBuffer, cache: Cache[ByteArrayKey, CacheValue]): AnyRef = {
      h.op match {
         case ClearRequest => {
            // Get an optimised cache in case we can make the operation more efficient
            getOptimizedCache(h, cache).clear
            new Response(h.messageId, h.cacheName, h.clientIntel, ClearResponse, Success, h.topologyId)
         }
         case PingRequest => new Response(h.messageId, h.cacheName, h.clientIntel, PingResponse, Success, h.topologyId)
      }
   }

   override def customReadKey(h: HotRodHeader, buffer: ChannelBuffer, cache: Cache[ByteArrayKey, CacheValue]): AnyRef = {
      h.op match {
         case RemoveIfUnmodifiedRequest => {
            val k = readKey(buffer)
            val params = readParameters(h, buffer)
            val prev = cache.get(k)
            if (prev != null) {
               if (prev.version == params.streamVersion) {
                  val removed = cache.remove(k, prev);
                  if (removed)
                     createResponse(h, RemoveIfUnmodifiedResponse, Success, prev)
                  else
                     createResponse(h, RemoveIfUnmodifiedResponse, OperationNotExecuted, prev)
               } else {
                  createResponse(h, RemoveIfUnmodifiedResponse, OperationNotExecuted, prev)
               }
            } else {
               createResponse(h, RemoveIfUnmodifiedResponse, KeyDoesNotExist, prev)
            }
         }
         case ContainsKeyRequest => {
            val k = readKey(buffer)
            if (cache.containsKey(k))
               new Response(h.messageId, h.cacheName, h.clientIntel, ContainsKeyResponse, Success, h.topologyId)
            else
               new Response(h.messageId, h.cacheName, h.clientIntel, ContainsKeyResponse, KeyDoesNotExist, h.topologyId)
         }
         case BulkGetRequest => {
            val count = readUnsignedInt(buffer)
            if (isTraceEnabled) trace("About to create bulk response, count = " + count)
            new BulkGetResponse(h.messageId, h.cacheName, h.clientIntel, BulkGetResponse, Success, h.topologyId, count)
         }
      }
   }

   override def customReadValue(header: HotRodHeader, buffer: ChannelBuffer, cache: Cache[ByteArrayKey, CacheValue]): AnyRef = null

   override def createStatsResponse(h: HotRodHeader, cacheStats: Stats): AnyRef = {
      val stats = mutable.Map.empty[String, String]
      stats += ("timeSinceStart" -> cacheStats.getTimeSinceStart.toString)
      stats += ("currentNumberOfEntries" -> cacheStats.getCurrentNumberOfEntries.toString)
      stats += ("totalNumberOfEntries" -> cacheStats.getTotalNumberOfEntries.toString)
      stats += ("stores" -> cacheStats.getStores.toString)
      stats += ("retrievals" -> cacheStats.getRetrievals.toString)
      stats += ("hits" -> cacheStats.getHits.toString)
      stats += ("misses" -> cacheStats.getMisses.toString)
      stats += ("removeHits" -> cacheStats.getRemoveHits.toString)
      stats += ("removeMisses" -> cacheStats.getRemoveMisses.toString)
      new StatsResponse(h.messageId, h.cacheName, h.clientIntel, immutable.Map[String, String]() ++ stats, h.topologyId)
   }

   override def createErrorResponse(h: HotRodHeader, t: Throwable): ErrorResponse = {
      t match {
         case i: IOException =>
            new ErrorResponse(h.messageId, h.cacheName, h.clientIntel, ParseError, h.topologyId, i.toString)
         case t: TimeoutException =>
            new ErrorResponse(h.messageId, h.cacheName, h.clientIntel, OperationTimedOut, h.topologyId, t.toString)
         case t: Throwable =>
            new ErrorResponse(h.messageId, h.cacheName, h.clientIntel, ServerError, h.topologyId, t.toString)
      }
   }

   override def getOptimizedCache(h: HotRodHeader, c: Cache[ByteArrayKey, CacheValue]): Cache[ByteArrayKey, CacheValue] = {
      if (c.getConfiguration.getCacheMode.isDistributed && h.flag != ForceReturnPreviousValue) {
         c.getAdvancedCache.withFlags(SKIP_REMOTE_LOOKUP)
      } else {
         c
      }
   }

   def toResponse(request: Enumeration#Value): OperationResponse = {
      request match {
         case PutRequest => PutResponse
         case GetRequest => GetResponse
         case PutIfAbsentRequest => PutIfAbsentResponse
         case ReplaceRequest => ReplaceResponse
         case ReplaceIfUnmodifiedRequest => ReplaceIfUnmodifiedResponse
         case RemoveRequest => RemoveResponse
         case RemoveIfUnmodifiedRequest => RemoveIfUnmodifiedResponse
         case ContainsKeyRequest => ContainsKeyResponse
         case GetWithVersionRequest => GetWithVersionResponse
         case ClearRequest => ClearResponse
         case StatsRequest => StatsResponse
         case PingRequest => PingResponse
         case BulkGetRequest => BulkGetResponse
      }
   }

}

object OperationResponse extends Enumeration {
   type OperationResponse = Enumeration#Value
   val PutResponse = Value(0x02)
   val GetResponse = Value(0x04)
   val PutIfAbsentResponse = Value(0x06)
   val ReplaceResponse = Value(0x08)
   val ReplaceIfUnmodifiedResponse = Value(0x0A)
   val RemoveResponse = Value(0x0C)
   val RemoveIfUnmodifiedResponse = Value(0x0E)
   val ContainsKeyResponse = Value(0x10)
   val GetWithVersionResponse = Value(0x12)
   val ClearResponse = Value(0x14)
   val StatsResponse = Value(0x16)
   val PingResponse = Value(0x18)
   val BulkGetResponse = Value(0x1A)
   val ErrorResponse = Value(0x50)
}

object ProtocolFlag extends Enumeration {
   type ProtocolFlag = Enumeration#Value
   val NoFlag = Value
   val ForceReturnPreviousValue = Value
}
