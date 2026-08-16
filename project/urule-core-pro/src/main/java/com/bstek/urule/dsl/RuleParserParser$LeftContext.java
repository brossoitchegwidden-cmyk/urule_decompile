package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$LeftContext extends ParserRuleContext {
   public RuleParserParser$ConditionContext condition() {
      return (RuleParserParser$ConditionContext)this.getRuleContext(RuleParserParser$ConditionContext.class, 0);
   }

   public RuleParserParser$LeftContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 31;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitLeft(this) : var1.visitChildren(this));
   }
}
