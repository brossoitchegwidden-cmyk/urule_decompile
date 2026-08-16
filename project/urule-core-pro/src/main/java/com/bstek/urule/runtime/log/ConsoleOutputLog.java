package com.bstek.urule.runtime.log;

import com.bstek.urule.Utils;
import java.math.BigDecimal;

public class ConsoleOutputLog extends DataLog {
   private static final String b = "控制台输出：%s";
   private static final String c = "Print: %s";

   public ConsoleOutputLog(Object var1) {
      var1 = this.a(var1);
      String var2 = this.a() ? "Print: %s" : "控制台输出：%s";
      var1 = var1 == null ? "null" : var1.toString();
      this.a = "☢☢☢" + String.format(var2, var1);
   }

   private Object a(Object var1) {
      if (var1 == null) {
         return var1;
      } else if (var1 instanceof Number) {
         BigDecimal var2 = Utils.toBigDecimal(var1);
         return var2.stripTrailingZeros().toPlainString();
      } else {
         return var1;
      }
   }
}
