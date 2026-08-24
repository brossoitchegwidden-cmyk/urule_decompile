package com.bstek.urule.dsl;

import java.util.ArrayList;
import java.util.List;

public class SyntaxErrorReportor {
   private List<String> strings = new ArrayList<>();

   public void addError(int line, int charPositionInLine, Object offendingSymbol, String msg) {
      this.strings.add(line + "行," + charPositionInLine + "列，" + offendingSymbol + "字符处，存在语法错误:" + msg);
   }

   public String getSyntaxErrorMessage() {
      StringBuffer stringBuffer = new StringBuffer();

      for (String text : this.strings) {
         stringBuffer.append(text);
         stringBuffer.append("\n");
      }

      return stringBuffer.toString();
   }
}
