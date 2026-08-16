package com.bstek.urule.runtime;

import java.util.TimerTask;

class IntervalTask extends TimerTask {
   private DynamicSpringConfigLoader a;
   private RemoteDynamicJarsBuilder b;

   public IntervalTask(DynamicSpringConfigLoader var1, RemoteDynamicJarsBuilder var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public void run() {
      String var1 = this.a.buildDynamicJarsStoreDirectPath();

      try {
         boolean var2 = this.b.requestRemoteJars(var1);
         if (!var2) {
            return;
         }

         this.a.loadDynamicJars(var1);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }
}
