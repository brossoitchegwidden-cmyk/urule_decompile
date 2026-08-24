package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$LoopAttributeContext extends ParserRuleContext {
   public TerminalNode Boolean() {
      return this.getToken(95, 0);
   }

   public RuleParserParser$LoopAttributeContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 21;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitLoopAttribute(this) : parseTreeVisitor.visitChildren(this));
   }
}
