package com.bstek.urule.console.database.service.jar;

import com.bstek.urule.console.config.bootstrap.BootstrapManager;
import com.bstek.urule.console.database.manager.jar.DynamicJarManager;
import com.bstek.urule.runtime.DynamicJarCreator;

public class DynamicJarCreatorImpl implements DynamicJarCreator {
   public boolean doCreate(String var1) {
      if (!BootstrapManager.get().isBootstrapped()) {
         return false;
      } else {
         return DynamicJarManager.ins.createJarFiles(var1) > 0;
      }
   }
}
