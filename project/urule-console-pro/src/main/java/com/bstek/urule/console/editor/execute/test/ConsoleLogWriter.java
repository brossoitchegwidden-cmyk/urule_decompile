package com.bstek.urule.console.editor.execute.test;

import com.bstek.urule.runtime.log.DataLog;
import com.bstek.urule.runtime.log.Log;
import com.bstek.urule.runtime.log.LogWriter;
import com.bstek.urule.runtime.log.UnitLog;
import java.io.IOException;
import java.util.List;

public class ConsoleLogWriter implements LogWriter {
   private StringBuilder a = new StringBuilder();

   public void write(List var1) throws IOException {
      this.a(this.a, var1);
   }

   private void a(StringBuilder var1, List var2) {
      for(Log var4 : (Iterable<Log>)(Iterable<?>)(var2)) {
         if (var4 instanceof UnitLog) {
            var1.append("<div style=\"margin:8px;border:solid 1px black;border-radius:5px;padding:5px\">");
            UnitLog var5 = (UnitLog)var4;
            List var6 = var5.getLogs();
            this.a(var1, var6);
            var1.append("</div>");
         } else if (var4 instanceof DataLog) {
            DataLog var7 = (DataLog)var4;
            String var8 = var7.getHtmlMsg();
            var1.append(var8);
         }
      }

   }

   public StringBuilder getLogMsg() {
      return this.a;
   }
}
