package org.infinispan.api.mvcc;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.test.fwk.CleanupAfterMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

@Test(groups = "functional", testName = "api.mvcc.PutForExternalReadReplNonTxTest")
@CleanupAfterMethod
public class PutForExternalReadReplNonTxTest extends PutForExternalReadTest {

   @Override
   protected ConfigurationBuilder createCacheConfigBuilder() {
      return getDefaultClusteredCacheConfig(CacheMode.REPL_SYNC, false);
   }

   @Override
   public void testCacheModeLocalInTx(Method m) {
      // not applicable in non-tx mode
   }

   @Override
   public void testMemLeakOnSuspendedTransactions() {
      // not applicable in non-tx mode
   }

   @Override
   public void testTxSuspension() {
      // not applicable in non-tx mode
   }
}
