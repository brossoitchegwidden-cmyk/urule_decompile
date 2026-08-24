package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$LeftContext extends ParserRuleContext {
   public RuleParserParser$ConditionContext condition() {
      return (RuleParserParser$ConditionContext)this.getRuleContext(RuleParserParser$ConditionContext.class, 0);
   }

   public RuleParserParser$LeftContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 31;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitLeft(this) : parseTreeVisitor.visitChildren(this));
   }
}
