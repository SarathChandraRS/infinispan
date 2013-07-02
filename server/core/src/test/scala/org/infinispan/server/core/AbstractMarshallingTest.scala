package org.infinispan.server.core

import org.testng.annotations.{AfterTest, BeforeTest}
import java.util.Random
import java.io.{ObjectOutputStream, ByteArrayOutputStream}
import org.infinispan.test.TestingUtil
import org.infinispan.commons.marshall.AbstractDelegatingMarshaller
import org.infinispan.manager.EmbeddedCacheManager
import org.infinispan.test.fwk.TestCacheManagerFactory

/**
 * Abstract class to help marshalling tests in different server modules.
 *
 * @author Galder Zamarreño
 * @since 4.1
 */
abstract class AbstractMarshallingTest {

   var marshaller : AbstractDelegatingMarshaller = _
   var cm : EmbeddedCacheManager = _

   @BeforeTest(alwaysRun=true)
   def setUp() {
      // Manual addition of externalizers to replication what happens in fully functional tests
      cm = TestCacheManagerFactory.createLocalCacheManager(false)
      marshaller = TestingUtil.extractCacheMarshaller(cm.getCache())
   }

   @AfterTest(alwaysRun=true)
   def tearDown() {
     if (cm != null) cm.stop()
   }

   protected def getBigByteArray: Array[Byte] = {
      val value = new String(randomByteArray(1000))
      val result = new ByteArrayOutputStream(1000)
      val oos = new ObjectOutputStream(result)
      oos.writeObject(value)
      result.toByteArray
   }

   private def randomByteArray(i: Int): Array[Byte] = {
      val r = new Random
      val result = new Array[Byte](i)
      r.nextBytes(result)
      result
   }

}