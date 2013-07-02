package org.infinispan.jmx;

import org.infinispan.config.Configuration;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.test.SingleCacheManagerTest;
import org.infinispan.test.TestingUtil;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.infinispan.transaction.lookup.DummyTransactionManagerLookup;
import org.infinispan.transaction.tm.DummyTransactionManager;
import org.testng.annotations.Test;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.transaction.TransactionManager;

import static org.infinispan.test.TestingUtil.checkMBeanOperationParameterNaming;
import static org.infinispan.test.TestingUtil.getCacheObjectName;

/**
 * Test the JMX functionality in {@link org.infinispan.util.concurrent.locks.LockManagerImpl}.
 *
 * @author Mircea.Markus@jboss.com
 * @author Galder Zamarreño
 */
@Test(groups = "functional", testName = "jmx.MvccLockManagerMBeanTest")
public class MvccLockManagerMBeanTest extends SingleCacheManagerTest {
   public static final int CONCURRENCY_LEVEL = 129;

   private ObjectName lockManagerObjName;
   private MBeanServer threadMBeanServer;
   private static final String JMX_DOMAIN = "MvccLockManagerMBeanTest";

   @Override
   protected EmbeddedCacheManager createCacheManager() throws Exception {
      GlobalConfiguration globalConfiguration = GlobalConfiguration.getNonClusteredDefault().fluent()
            .globalJmxStatistics()
               .mBeanServerLookupClass(PerThreadMBeanServerLookup.class)
               .jmxDomain(JMX_DOMAIN)
            .build();

      cacheManager = TestCacheManagerFactory.createCacheManagerEnforceJmxDomain(globalConfiguration);

      Configuration configuration = getDefaultStandaloneConfig(true).fluent()
            .jmxStatistics()
            .locking()
               .concurrencyLevel(CONCURRENCY_LEVEL)
               .useLockStriping(true)
            .transaction()
               .transactionManagerLookup(new DummyTransactionManagerLookup())
            .build();

      cacheManager.defineConfiguration("test", configuration);
      cache = cacheManager.getCache("test");
      lockManagerObjName = getCacheObjectName(JMX_DOMAIN, "test(local)", "LockManager");

      threadMBeanServer = PerThreadMBeanServerLookup.getThreadMBeanServer();
      return cacheManager;
   }

   public void testJmxOperationMetadata() throws Exception {
      checkMBeanOperationParameterNaming(lockManagerObjName);
   }

   public void testConcurrencyLevel() throws Exception {
      assertAttributeValue("ConcurrencyLevel", CONCURRENCY_LEVEL);
   }

   public void testNumberOfLocksHeld() throws Exception {
      DummyTransactionManager tm = (DummyTransactionManager) TestingUtil.extractComponent(cache, TransactionManager.class);
      tm.begin();
      cache.put("key", "value");
      tm.getTransaction().runPrepare();
      assertAttributeValue("NumberOfLocksHeld", 1);
      tm.getTransaction().runCommitTx();
      assertAttributeValue("NumberOfLocksHeld", 0);
   }

   public void testNumberOfLocksAvailable() throws Exception {
      DummyTransactionManager tm = (DummyTransactionManager) TestingUtil.extractComponent(cache, TransactionManager.class);
      int initialAvailable = getAttrValue("NumberOfLocksAvailable");
      tm.begin();
      cache.put("key", "value");
      tm.getTransaction().runPrepare();

      assertAttributeValue("NumberOfLocksHeld", 1);
      assertAttributeValue("NumberOfLocksAvailable", initialAvailable - 1);
      tm.rollback();
      assertAttributeValue("NumberOfLocksAvailable", initialAvailable);
      assertAttributeValue("NumberOfLocksHeld", 0);
   }

   private void assertAttributeValue(String attrName, int expectedVal) throws Exception {
      int cl = getAttrValue(attrName);
      assert cl == expectedVal : "expected " + expectedVal + ", but received " + cl;
   }

   private int getAttrValue(String attrName) throws Exception {
      return Integer.parseInt(threadMBeanServer.getAttribute(lockManagerObjName, attrName).toString());
   }
}
