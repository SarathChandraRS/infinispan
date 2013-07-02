package org.infinispan.spring;

import java.util.ArrayList;
import java.util.List;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.eviction.EvictionThreadPolicy;
import org.infinispan.interceptors.base.CommandInterceptor;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.transaction.lookup.TransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * <p>
 * Test {@link ConfigurationOverrides}.
 * </p>
 *
 * @author <a href="mailto:olaf DOT bergner AT gmx DOT de">Olaf Bergner</a>
 *
 */
@Test(groups = "unit", testName = "spring.ConfigurationOverridesTest",
      enabled = false, description = "Disabled temporarily, see https://issues.jboss.org/browse/ISPN-2701")
public class ConfigurationOverridesTest {

   public final void configurationOverridesShouldOverrideDeadlockSpinDetectionDurationPropIfExplicitlySet() throws Exception {
      final long expectedDeadlockSpinDetectionDuration = 100000L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setDeadlockDetectionSpinDuration(expectedDeadlockSpinDetectionDuration);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set deadlockDetectionSpinDuration. However, it didn't.",
                  expectedDeadlockSpinDetectionDuration,
                  configuration.deadlockDetection().spinDuration());
   }

   public final void configurationOverridesShouldOverrideEnableDeadlockDetectionPropIfExplicitlySet()
         throws Exception {
      final boolean expectedEnableDeadlockDetection = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setEnableDeadlockDetection(expectedEnableDeadlockDetection);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set enableDeadlockDetection property. However, it didn't.",
                  expectedEnableDeadlockDetection,
                  configuration.deadlockDetection().enabled());
   }

   public final void configurationOverridesShouldOverrideUseLockStripingPropIfExplicitlySet()
         throws Exception {
      final boolean expectedUseLockStriping = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setUseLockStriping(expectedUseLockStriping);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set useLockStriping property. However, it didn't.",
                  expectedUseLockStriping,
                  configuration.locking().useLockStriping());
   }

   public final void configurationOverridesShouldOverrideUnsafeUnreliableReturnValuesPropIfExplicitlySet()
         throws Exception {
      final boolean expectedUnsafeUnreliableReturnValues = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setUnsafeUnreliableReturnValues(expectedUnsafeUnreliableReturnValues);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();

      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set unsafeUnreliableReturnValues property. However, it didn't.",
                  expectedUnsafeUnreliableReturnValues,
                  configuration.unsafe().unreliableReturnValues());
   }

   public final void configurationOverridesShouldOverrideRehashRpcTimeoutPropIfExplicitlySet()
         throws Exception {
      final long expectedRehashRpcTimeout = 100000L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setRehashRpcTimeout(expectedRehashRpcTimeout);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set rehashRpcTimeout property. However, it didn't.",
                  expectedRehashRpcTimeout,
                  configuration.clustering().stateTransfer().timeout());
   }

   public final void configurationOverridesShouldOverrideWriteSkewCheckPropIfExplicitlySet()
         throws Exception {
      final boolean expectedWriteSkewCheck = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setWriteSkewCheck(expectedWriteSkewCheck);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set writeSkewCheck property. However, it didn't.",
                  expectedWriteSkewCheck,
                  configuration.locking().writeSkewCheck());
   }

   public final void configurationOverridesShouldOverrideConcurrencyLevelPropIfExplicitlySet()
         throws Exception {
      final int expectedConcurrencyLevel = 10000;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setConcurrencyLevel(expectedConcurrencyLevel);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set ConcurrencyLevel property. However, it didn't.",
                  expectedConcurrencyLevel,
                  configuration.locking().concurrencyLevel());
   }

   public final void configurationOverridesShouldOverrideReplQueueMaxElementsPropIfExplicitlySet()
         throws Exception {
      final int expectedReplQueueMaxElements = 10000;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setReplQueueMaxElements(expectedReplQueueMaxElements);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set ReplQueueMaxElements property. However, it didn't.",
                  expectedReplQueueMaxElements,
                  configuration.clustering().async().replQueueMaxElements());
   }

   public final void configurationOverridesShouldOverrideReplQueueIntervalPropIfExplicitlySet()
         throws Exception {
      final long expectedReplQueueInterval = 10000L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setReplQueueInterval(expectedReplQueueInterval);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set ReplQueueInterval property. However, it didn't.",
                  expectedReplQueueInterval,
                  configuration.clustering().async().replQueueInterval());
   }

   public final void configurationOverridesShouldOverrideReplQueueClassPropIfExplicitlySet()
         throws Exception {
      final String expectedReplQueueClass = "repl.queue.Class";//FIXME create one

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setReplQueueClass(expectedReplQueueClass);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set ReplQueueClass property. However, it didn't.",
                  expectedReplQueueClass,
                  configuration.clustering().async().replQueue().getClass());
   }

   public final void configurationOverridesShouldOverrideExposeJmxStatisticsPropIfExplicitlySet() throws Exception {
      final boolean expectedExposeJmxStatistics = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setExposeJmxStatistics(expectedExposeJmxStatistics);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set ExposeJmxStatistics property. However, it didn't.",
                  expectedExposeJmxStatistics,
                  configuration.jmxStatistics().enabled());
   }

   public final void configurationOverridesShouldOverrideInvocationBatchingEnabledPropIfExplicitlySet()
         throws Exception {
      final boolean expectedInvocationBatchingEnabled = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setInvocationBatchingEnabled(expectedInvocationBatchingEnabled);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set InvocationBatchingEnabled property. However, it didn't.",
                  expectedInvocationBatchingEnabled,
                  configuration.invocationBatching().enabled());
   }

   public final void configurationOverridesShouldOverrideFetchInMemoryStatePropIfExplicitlySet()
         throws Exception {
      final boolean expectedFetchInMemoryState = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setFetchInMemoryState(expectedFetchInMemoryState);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set FetchInMemoryState property. However, it didn't.",
                  expectedFetchInMemoryState,
                  configuration.clustering().stateTransfer().fetchInMemoryState());
   }

   public final void configurationOverridesShouldOverrideAlwaysProvideInMemoryStatePropIfExplicitlySet()
         throws Exception {
      final boolean expectedAlwaysProvideInMemoryState = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setAlwaysProvideInMemoryState(expectedAlwaysProvideInMemoryState);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set AlwaysProvideInMemoryState property. However, it didn't.",
                  expectedAlwaysProvideInMemoryState,
                  configuration.clustering().stateTransfer().fetchInMemoryState());//FIXME
   }

   public final void configurationOverridesShouldOverrideLockAcquisitionTimeoutPropIfExplicitlySet()
         throws Exception {
      final long expectedLockAcquisitionTimeout = 1000000L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setLockAcquisitionTimeout(expectedLockAcquisitionTimeout);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set LockAcquisitionTimeout property. However, it didn't.",
                  expectedLockAcquisitionTimeout,
                  configuration.locking().lockAcquisitionTimeout());
   }

   public final void configurationOverridesShouldOverrideSyncReplTimeoutPropIfExplicitlySet()
         throws Exception {
      final long expectedSyncReplTimeout = 100000L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setSyncReplTimeout(expectedSyncReplTimeout);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set SyncReplTimeout property. However, it didn't.",
                  expectedSyncReplTimeout,
                  configuration.clustering().stateTransfer().timeout());
   }

   public final void configurationOverridesShouldOverrideCacheModeStringPropIfExplicitlySet()
         throws Exception {
      final String expectedCacheModeString = CacheMode.LOCAL.name();

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setCacheModeString(expectedCacheModeString);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set CacheModeString property. However, it didn't.",
                  expectedCacheModeString,
                  configuration.clustering().cacheModeString());
   }

   public final void configurationOverridesShouldOverrideEvictionWakeUpIntervalPropIfExplicitlySet()
         throws Exception {
      final long expectedExpirationWakeUpInterval = 100000L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setExpirationWakeUpInterval(expectedExpirationWakeUpInterval);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set EvictionWakeUpInterval property. However, it didn't.",
                  expectedExpirationWakeUpInterval,
                  configuration.expiration().wakeUpInterval());
   }

   public final void configurationOverridesShouldOverrideEvictionStrategyPropIfExplicitlySet()
         throws Exception {
      final EvictionStrategy expectedEvictionStrategy = EvictionStrategy.LIRS;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setEvictionStrategy(expectedEvictionStrategy);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set EvictionStrategy property. However, it didn't.",
                  expectedEvictionStrategy,
                  configuration.eviction().strategy());
   }

   public final void configurationOverridesShouldOverrideEvictionStrategyClassPropIfExplicitlySet()
         throws Exception {
      final String expectedEvictionStrategyClass = "LRU";

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setEvictionStrategyClass(expectedEvictionStrategyClass);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set EvictionStrategyClass property. However, it didn't.",
                  EvictionStrategy.LRU,
                  configuration.eviction().strategy());
   }

   public final void configurationOverridesShouldOverrideEvictionThreadPolicyPropIfExplicitlySet()
         throws Exception {
      final EvictionThreadPolicy expectedEvictionThreadPolicy = EvictionThreadPolicy.PIGGYBACK;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setEvictionThreadPolicy(expectedEvictionThreadPolicy);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set EvictionThreadPolicy property. However, it didn't.",
                  expectedEvictionThreadPolicy,
                  configuration.eviction().threadPolicy());
   }

   public final void configurationOverridesShouldOverrideEvictionThreadPolicyClassPropIfExplicitlySet()
         throws Exception {
      final String expectedEvictionThreadPolicyClass = "PIGGYBACK";

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setEvictionThreadPolicyClass(expectedEvictionThreadPolicyClass);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set EvictionThreadPolicyClass property. However, it didn't.",
                  EvictionThreadPolicy.PIGGYBACK,
                  configuration.eviction().threadPolicy());
   }

   public final void configurationOverridesShouldOverrideEvictionMaxEntriesPropIfExplicitlySet()
         throws Exception {
      final int expectedEvictionMaxEntries = 1000000;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setEvictionMaxEntries(expectedEvictionMaxEntries);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set EvictionMaxEntries property. However, it didn't.",
                  expectedEvictionMaxEntries,
                  configuration.eviction().maxEntries());
   }

   public final void configurationOverridesShouldOverrideExpirationLifespanPropIfExplicitlySet()
         throws Exception {
      final long expectedExpirationLifespan = 1000000L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setExpirationLifespan(expectedExpirationLifespan);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set ExpirationLifespan property. However, it didn't.",
                  expectedExpirationLifespan,
                  configuration.expiration().lifespan());
   }

   public final void configurationOverridesShouldOverrideExpirationMaxIdlePropIfExplicitlySet()
         throws Exception {
      final long expectedExpirationMaxIdle = 100000L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setExpirationMaxIdle(expectedExpirationMaxIdle);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set ExpirationMaxIdle property. However, it didn't.",
                  expectedExpirationMaxIdle,
                  configuration.expiration().maxIdle());
   }

   public final void configurationOverridesShouldOverrideTransactionManagerLookupClassPropIfExplicitlySet()
         throws Exception {
      final String expectedTransactionManagerLookupClass = "expected.transaction.manager.lookup.Class";

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setTransactionManagerLookupClass(expectedTransactionManagerLookupClass);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set TransactionManagerLookupClass property. However, it didn't.",
                  expectedTransactionManagerLookupClass,
                  configuration.transaction().transactionManagerLookup());
   }

   public final void configurationOverridesShouldOverrideTransactionManagerLookupPropIfExplicitlySet()
         throws Exception {
      final TransactionManagerLookup expectedTransactionManagerLookup = new GenericTransactionManagerLookup();

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setTransactionManagerLookup(expectedTransactionManagerLookup);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set TransactionManagerLookup property. However, it didn't.",
                  expectedTransactionManagerLookup,
                  configuration.transaction().transactionManagerLookup());
   }

   public final void configurationOverridesShouldOverrideSyncCommitPhasePropIfExplicitlySet()
         throws Exception {
      final boolean expectedSyncCommitPhase = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setSyncCommitPhase(expectedSyncCommitPhase);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set SyncCommitPhase property. However, it didn't.",
                  expectedSyncCommitPhase,
                  configuration.transaction().syncCommitPhase());
   }

   public final void configurationOverridesShouldOverrideSyncRollbackPhasePropIfExplicitlySet()
         throws Exception {
      final boolean expectedSyncRollbackPhase = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setSyncRollbackPhase(expectedSyncRollbackPhase);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set SyncRollbackPhase property. However, it didn't.",
                  expectedSyncRollbackPhase,
                  configuration.transaction().syncRollbackPhase());
   }

   public final void configurationOverridesShouldOverrideUseEagerLockingPropIfExplicitlySet()
         throws Exception {

      final LockingMode expectedLockingMode = LockingMode.PESSIMISTIC;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setUseEagerLocking(true);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set UseEagerLocking property. However, it didn't.",
                  expectedLockingMode,
                  configuration.transaction().lockingMode());
   }

   public final void configurationOverridesShouldOverrideUseReplQueuePropIfExplicitlySet() throws Exception {
      final boolean expectedUseReplQueue = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setUseReplQueue(expectedUseReplQueue);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set UseReplQueue property. However, it didn't.",
                  expectedUseReplQueue,
                  configuration.clustering().async().useReplQueue());
   }

   public final void configurationOverridesShouldOverrideIsolationLevelPropIfExplicitlySet()
         throws Exception {
      final IsolationLevel expectedIsolationLevel = IsolationLevel.SERIALIZABLE;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setIsolationLevel(expectedIsolationLevel);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set IsolationLevel property. However, it didn't.",
                  expectedIsolationLevel,
                  configuration.locking().isolationLevel());
   }

   public final void configurationOverridesShouldOverrideStateRetrievalTimeoutPropIfExplicitlySet()
         throws Exception {
      final long expectedStateRetrievalTimeout = 1000000L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setStateRetrievalTimeout(expectedStateRetrievalTimeout);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set StateRetrievalTimeout property. However, it didn't.",
                  expectedStateRetrievalTimeout,
                  configuration.clustering().stateTransfer().timeout());
   }

   public final void configurationOverridesShouldOverrideStateRetrievalChunkSizePropIfExplicitlySet()
         throws Exception {
      final int expectedStateRetrievalChunkSize = 123456;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest
            .setStateRetrievalChunkSize(expectedStateRetrievalChunkSize);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set StateRetrievalChunkSize property. However, it didn't.",
                  expectedStateRetrievalChunkSize,
                  configuration.clustering().stateTransfer().chunkSize());
   }

   public final void configurationOverridesShouldOverrideIsolationLevelClassPropIfExplicitlySet()
         throws Exception {
      final String expectedIsolationLevelClass = "REPEATABLE_READ";

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setIsolationLevelClass(expectedIsolationLevelClass);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set IsolationLevelClass property. However, it didn't.",
                  IsolationLevel.REPEATABLE_READ, configuration.locking().isolationLevel());
   }

   public final void configurationOverridesShouldOverrideUseLazyDeserializationPropIfExplicitlySet()
         throws Exception {
      final boolean expectedUseLazyDeserialization = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setUseLazyDeserialization(expectedUseLazyDeserialization);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set UseLazyDeserialization property. However, it didn't.",
                  expectedUseLazyDeserialization, configuration.storeAsBinary().enabled());
   }

   public final void configurationOverridesShouldOverrideL1CacheEnabledPropIfExplicitlySet()
         throws Exception {
      final boolean expectedL1CacheEnabled = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setL1CacheEnabled(expectedL1CacheEnabled);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set L1CacheEnabled property. However, it didn't.",
                  expectedL1CacheEnabled, configuration.clustering().l1().enabled());
   }

   public final void configurationOverridesShouldOverrideL1LifespanPropIfExplicitlySet()
         throws Exception {
      final long expectedL1Lifespan = 2300446L;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setL1Lifespan(expectedL1Lifespan);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set L1Lifespan property. However, it didn't.",
                  expectedL1Lifespan, configuration.clustering().l1().lifespan());
   }

   public final void configurationOverridesShouldOverrideL1OnRehashPropIfExplicitlySet()
         throws Exception {
      final boolean expectedL1OnRehash = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setL1OnRehash(expectedL1OnRehash);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set L1OnRehash property. However, it didn't.",
                  expectedL1OnRehash, configuration.clustering().l1().onRehash());
   }

   public final void configurationOverridesShouldOverrideConsistentHashClassPropIfExplicitlySet()
         throws Exception {
      final String expectedConsistentHashClass = "expected.consistent.hash.Class";

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setConsistentHashClass(expectedConsistentHashClass);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set ConsistentHashClass property. However, it didn't.",
                  expectedConsistentHashClass, configuration.clustering().hash().consistentHashFactory().getClass().getName());
   }

   public final void configurationOverridesShouldOverrideNumOwnersPropIfExplicitlySet()
         throws Exception {
      final int expectedNumOwners = 675443;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setNumOwners(expectedNumOwners);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set NumOwners property. However, it didn't.",
                  expectedNumOwners, configuration.clustering().hash().numOwners());
   }

   public final void configurationOverridesShouldOverrideRehashEnabledPropIfExplicitlySet()
         throws Exception {
      final boolean expectedRehashEnabled = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setRehashEnabled(expectedRehashEnabled);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set RehashEnabled property. However, it didn't.",
                  expectedRehashEnabled, configuration.clustering().stateTransfer().fetchInMemoryState());
   }

   public final void configurationOverridesShouldOverrideUseAsyncMarshallingPropIfExplicitlySet()
         throws Exception {
      final boolean expectedUseAsyncMarshalling = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setUseAsyncMarshalling(expectedUseAsyncMarshalling);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set UseAsyncMarshalling property. However, it didn't.",
                  expectedUseAsyncMarshalling, configuration.clustering().async().asyncMarshalling());
   }

   public final void configurationOverridesShouldOverrideIndexingEnabledPropIfExplicitlySet()
         throws Exception {
      final boolean expectedIndexingEnabled = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setIndexingEnabled(expectedIndexingEnabled);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set IndexingEnabled property. However, it didn't.",
                  expectedIndexingEnabled, configuration.indexing().enabled());
   }

   public final void configurationOverridesShouldOverrideIndexLocalOnlyPropIfExplicitlySet()
         throws Exception {
      final boolean expectedIndexLocalOnly = true;

      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setIndexLocalOnly(expectedIndexLocalOnly);
      final ConfigurationBuilder defaultConfiguration = new ConfigurationBuilder();
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set IndexLocalOnly property. However, it didn't.",
                  expectedIndexLocalOnly, configuration.indexing().indexLocalOnly());
   }

   public final void configurationOverridesShouldOverrideCustomInterceptorsPropIfExplicitlySet()
         throws Exception {

      DummyInterceptor dummyInterceptor = new DummyInterceptor();
      final List<CommandInterceptor> expectedCustomInterceptors = new ArrayList<CommandInterceptor>();
      expectedCustomInterceptors.add(dummyInterceptor);
      final ConfigurationOverrides objectUnderTest = new ConfigurationOverrides();
      objectUnderTest.setCustomInterceptors(expectedCustomInterceptors);
      final ConfigurationBuilder defaultConfiguration = TestCacheManagerFactory.getDefaultCacheConfiguration(false);
      objectUnderTest.applyOverridesTo(defaultConfiguration);
      Configuration configuration = defaultConfiguration.build();

      final ConfigurationBuilder dummyBuilder = TestCacheManagerFactory.getDefaultCacheConfiguration(false);
      dummyBuilder.customInterceptors()
            .addInterceptor()
            .interceptor(dummyInterceptor);
      Configuration dummyConfiguration = dummyBuilder.build();

      AssertJUnit
            .assertEquals(
                  "ConfigurationOverrides should have overridden default value with explicitly set CustomInterceptors property. However, it didn't.",
                  dummyConfiguration.customInterceptors().interceptors(), configuration.customInterceptors().interceptors());
   }

   private class DummyInterceptor extends CommandInterceptor {
   }
}
