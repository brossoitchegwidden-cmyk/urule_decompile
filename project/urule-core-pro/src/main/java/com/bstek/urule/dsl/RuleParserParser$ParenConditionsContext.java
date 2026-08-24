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

   public RuleParserParser$ParenConditionsContext(RuleParserParser$ConditionContext ruleParserParser$ConditionContext) {
      this.copyFrom(ruleParserParser$ConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitParenConditions(this) : parseTreeVisitor.visitChildren(this));
   }
}
