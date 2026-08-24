package com.bstek.urule.dsl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class SyntaxErrorListener extends BaseErrorListener {
   private SyntaxErrorReportor reportor;

   public SyntaxErrorListener(SyntaxErrorReportor reportor) {
      this.reportor = reportor;
   }

   public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException recognitionException) {
      this.reportor.addError(line, charPositionInLine, offendingSymbol, msg);
   }
}
