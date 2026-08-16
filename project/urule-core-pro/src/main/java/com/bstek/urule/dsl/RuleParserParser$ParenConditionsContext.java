package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ParenConditionsContext extends RuleParserParser$ConditionContext {
   public RuleParserParser$LeftParenContext leftParen() {
      return (RuleParserParser$LeftParenContext)this.getRuleContext(RuleParserParser$LeftParenContext.class, 0);
   }

   public RuleParserParser$ConditionContext condition() {
      return (RuleParserParser$ConditionContext)this.getRuleContext(RuleParserParser$ConditionContext.class, 0);
   }

   public RuleParserParser$RightParenContext rightParen() {
      return (RuleParserParser$RightParenContext)this.getRuleContext(RuleParserParser$RightParenContext.class, 0);
   }

   public RuleParserParser$ParenConditionsContext(RuleParserParser$ConditionContext var1) {
      this.copyFrom(var1);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitParenConditions(this) : var1.visitChildren(this));
   }
}
