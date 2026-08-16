package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.config.Configure;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.database.model.URuleLog;

public class DBStoreAppender implements LogAppender {
   private static boolean a = false;
   private static boolean b = false;
   private static boolean c = false;

   public DBStoreAppender() {
      a = Configure.getConfigure().getBoolean("urule.store.log.login", true);
      b = Configure.getConfigure().getBoolean("urule.store.log.opertion", true);
      c = Configure.getConfigure().getBoolean("urule.store.log.knowledge", true);
   }

   public void putLog(URuleLog var1) {
      try {
         if (var1 instanceof LoginLog && a) {
            LogQueue.a(var1);
         } else if (var1 instanceof OperationLog && b) {
            LogQueue.a(var1);
         } else if (var1 instanceof KnowledgeLog && c) {
            LogQueue.a(var1);
         }
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }
}
