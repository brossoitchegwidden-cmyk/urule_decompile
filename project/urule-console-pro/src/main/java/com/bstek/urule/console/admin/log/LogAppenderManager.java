package com.bstek.urule.console.admin.log;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.model.URuleLog;
import java.util.ArrayList;
import java.util.Collection;

public class LogAppenderManager {
   private Collection logAppenders = Utils.getApplicationContext().getBeansOfType(LogAppender.class).values();

   public LogAppenderManager() {
      if (this.logAppenders.size() == 0) {
         this.logAppenders = new ArrayList();
         this.logAppenders.add(new DBStoreAppender());
      }

   }

   public void putLog(URuleLog log) throws InterruptedException {
      if (this.logAppenders.size() > 0) {
         for(LogAppender logAppender : (Iterable<LogAppender>)(Iterable<?>)(this.logAppenders)) {
            logAppender.putLog(log);
         }
      }

   }
}
