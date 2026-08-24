package com.bstek.urule.runtime.log;

import com.bstek.urule.Utils;
import java.math.BigDecimal;

public class ConsoleOutputLog extends DataLog {
   public ConsoleOutputLog(Object content) {
      content = this.normalizeDisplayValue(content);
      String text = this.isEnglishLanguage() ? "Print: %s" : "控制台输出：%s";
      content = content == null ? "null" : content.toString();
      this.msg = "☢☢☢" + String.format(text, content);
   }

   private Object normalizeDisplayValue(Object objectValue) {
      if (objectValue == null) {
         return objectValue;
      } else if (objectValue instanceof Number) {
         BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
         return decimalValue.stripTrailingZeros().toPlainString();
      } else {
         return objectValue;
      }
   }
}
