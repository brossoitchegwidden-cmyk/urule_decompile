package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$LoopTargetContext extends ParserRuleContext {
   public RuleParserParser$ComplexValueContext complexValue() {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, 0);
   }

   public RuleParserParser$LoopTargetContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 17;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitLoopTarget(this) : parseTreeVisitor.visitChildren(this));
   }
}
