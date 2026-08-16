package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.database.model.URuleLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConsoleAppender implements LogAppender {
   private static Log a = LogFactory.getLog(ConsoleAppender.class.getSimpleName());

   public void putLog(URuleLog var1) {
      a.debug(var1.toString());
   }
}
