package org.infinispan.commands;

import org.infinispan.context.InvocationContext;

/**
 * The core of the command-based cache framework.  Commands correspond to specific areas of functionality in the cache,
 * and can be replicated using the {@link org.infinispan.remoting.rpc.RpcManager}
 *
 * @author Mircea.Markus@jboss.com
 * @author Manik Surtani
 * @since 4.0
 */
public interface ReplicableCommand {
   /**
    * Performs the primary function of the command.  Please see specific implementation classes for details on what is
    * performed as well as return types. <b>Important</b>: this method will be invoked at the end of interceptors chain.
    * It should never be called directly from a custom interceptor.
    *
    * @param ctx invocation context
    * @return arbitrary return value generated by performing this command
    * @throws Throwable in the event of problems.
    */
   Object perform(InvocationContext ctx) throws Throwable;

   /**
    * Used by marshallers to convert this command into an id for streaming.
    *
    * @return the method id of this command.  This is compatible with pre-2.2.0 MethodCall ids.
    */
   byte getCommandId();

   /**
    * Used by marshallers to stream this command across a network
    *
    * @return an object array of arguments, compatible with pre-2.2.0 MethodCall args.
    */
   Object[] getParameters();

   /**
    * Used by the {@link CommandsFactory} to create a command from raw data read off a stream.
    *
    * @param commandId  command id to set.  This is usually unused but *could* be used in the event of a command having
    *                   multiple IDs, such as {@link org.infinispan.commands.write.PutKeyValueCommand}.
    * @param parameters object array of args
    */
   void setParameters(int commandId, Object[] parameters);

   /**
    * If true, a return value will be provided when performed remotely.  Otherwise, a remote {@link org.infinispan.remoting.responses.ResponseGenerator}
    * may choose to simply return null to save on marshalling costs.
    * @return true or false
    */
   boolean isReturnValueExpected();

   /**
    * If true, the command is processed asynchronously in a thread provided by an Infinispan thread pool. Otherwise,
    * the command is processed directly in the JGroups thread.
    * <p/>
    * This feature allows to avoid keep a JGroups thread busy that can originate discard of messages and
    * retransmissions. So, the commands that can block (waiting for some state, acquiring locks, etc.) should return
    * true.
    *
    * @return  {@code true} if the command can block/wait, {@code false} otherwise
    */
   boolean canBlock();
}
