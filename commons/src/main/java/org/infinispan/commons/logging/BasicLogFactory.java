package org.infinispan.commons.logging;

import org.infinispan.commons.logging.Log;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.NDC;

/**
 * Factory that creates {@link Log} instances.
 *
 * @author Manik Surtani
 * @since 4.0
 */
public class BasicLogFactory {

   public static BasicLogger getLog(Class<?> clazz) {
      return Logger.getLogger(clazz.getName());
   }

   public static <T> T getLog(Class<?> clazz, Class<T> logClass) {
      return Logger.getMessageLogger(logClass, clazz.getName());
   }

   public static void pushNDC(String cacheName, boolean isTrace) {
      if (isTrace)
         NDC.push(cacheName);
   }

   public static void popNDC(boolean isTrace) {
      if (isTrace)
         NDC.pop();
   }

}
