package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$ImportConstantLibraryContext extends ParserRuleContext {
   public TerminalNode STRING() {
      return this.getToken(97, 0);
   }

   public RuleParserParser$ImportConstantLibraryContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 9;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitImportConstantLibrary(this) : parseTreeVisitor.visitChildren(this));
   }
}
