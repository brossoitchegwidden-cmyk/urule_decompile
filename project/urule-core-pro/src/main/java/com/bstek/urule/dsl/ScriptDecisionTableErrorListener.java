package com.bstek.urule.dsl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ScriptDecisionTableErrorListener extends BaseErrorListener {
   private StringBuffer stringBuffer;

   public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException recognitionException) {
      if (this.stringBuffer == null) {
         this.stringBuffer = new StringBuffer();
      }

      this.stringBuffer.append("[" + offendingSymbol + "] is invalid:" + msg);
      this.stringBuffer.append("\r\n");
   }

   public String getErrorMessage() {
      return this.stringBuffer == null ? null : this.stringBuffer.toString();
   }
}
