package com.bstek.urule.console.editor.execute.test;

import com.bstek.urule.runtime.log.DataLog;
import com.bstek.urule.runtime.log.Log;
import com.bstek.urule.runtime.log.LogWriter;
import com.bstek.urule.runtime.log.UnitLog;
import java.io.IOException;
import java.util.List;

public class ConsoleLogWriter implements LogWriter {
   private StringBuilder logMsg = new StringBuilder();

   public void write(List logs) throws IOException {
      this.appendLogsHtml(this.logMsg, logs);
   }

   private void appendLogsHtml(StringBuilder stringBuilder, List items) {
      for(Log log : (Iterable<Log>)(Iterable<?>)(items)) {
         if (log instanceof UnitLog) {
            stringBuilder.append("<div style=\"margin:8px;border:solid 1px black;border-radius:5px;padding:5px\">");
            UnitLog unitLog = (UnitLog)log;
            List logs = unitLog.getLogs();
            this.appendLogsHtml(stringBuilder, logs);
            stringBuilder.append("</div>");
         } else if (log instanceof DataLog) {
            DataLog dataLog = (DataLog)log;
            String htmlMsg = dataLog.getHtmlMsg();
            stringBuilder.append(htmlMsg);
         }
      }

   }

   public StringBuilder getLogMsg() {
      return this.logMsg;
   }
}
