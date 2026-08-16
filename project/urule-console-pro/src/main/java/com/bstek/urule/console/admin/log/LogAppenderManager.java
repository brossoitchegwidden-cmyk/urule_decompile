package com.bstek.urule.console.admin.log;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.model.URuleLog;
import java.util.ArrayList;
import java.util.Collection;

public class LogAppenderManager {
   private Collection a = Utils.getApplicationContext().getBeansOfType(LogAppender.class).values();

   public LogAppenderManager() {
      if (this.a.size() == 0) {
         this.a = new ArrayList();
         this.a.add(new DBStoreAppender());
      }

   }

   public void putLog(URuleLog var1) throws InterruptedException {
      if (this.a.size() > 0) {
         for(LogAppender var3 : (Iterable<LogAppender>)(Iterable<?>)(this.a)) {
            var3.putLog(var1);
         }
      }

   }
}
