package com.bstek.urule.runtime;

import java.util.TimerTask;

class IntervalTask extends TimerTask {
   private DynamicSpringConfigLoader dynamicSpringConfigLoader;
   private RemoteDynamicJarsBuilder remoteDynamicJarsBuilder;

   public IntervalTask(DynamicSpringConfigLoader dynamicSpringConfigLoader2, RemoteDynamicJarsBuilder remoteDynamicJarsBuilder2) {
      this.dynamicSpringConfigLoader = dynamicSpringConfigLoader2;
      this.remoteDynamicJarsBuilder = remoteDynamicJarsBuilder2;
   }

   @Override
   public void run() {
      String dynamicJarsStoreDirectPath = this.dynamicSpringConfigLoader.buildDynamicJarsStoreDirectPath();

      try {
         boolean flag = this.remoteDynamicJarsBuilder.requestRemoteJars(dynamicJarsStoreDirectPath);
         if (!flag) {
            return;
         }

         this.dynamicSpringConfigLoader.loadDynamicJars(dynamicJarsStoreDirectPath);
      } catch (Exception exception) {
         java.util.logging.Logger.getLogger(IntervalTask.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
      }
   }
}
