package org.infinispan.loaders;

import org.infinispan.test.fwk.CleanupAfterMethod;
import org.testng.annotations.Test;

/**
 * Test if keys are properly passivated and reloaded in local mode (to ensure fix for ISPN-2712 did no break local mode).
 *
 * @author anistor@redhat.com
 * @since 5.2
 */
@Test(groups = "functional", testName = "loaders.LocalModeNoPassivationTest")
@CleanupAfterMethod
public class LocalModeNoPassivationTest extends LocalModePassivationTest {

   LocalModeNoPassivationTest() {
      super(false);
   }
}
