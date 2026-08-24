package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ParenNamedConditionsContext extends RuleParserParser$NamedConditionContext {
   public RuleParserParser$LeftParenContext leftParen() {
      return (RuleParserParser$LeftParenContext)this.getRuleContext(RuleParserParser$LeftParenContext.class, 0);
   }

   public RuleParserParser$NamedConditionContext namedCondition() {
      return (RuleParserParser$NamedConditionContext)this.getRuleContext(RuleParserParser$NamedConditionContext.class, 0);
   }

   public RuleParserParser$RightParenContext rightParen() {
      return (RuleParserParser$RightParenContext)this.getRuleContext(RuleParserParser$RightParenContext.class, 0);
   }

   public RuleParserParser$ParenNamedConditionsContext(RuleParserParser$NamedConditionContext ruleParserParser$NamedConditionContext) {
      this.copyFrom(ruleParserParser$NamedConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitParenNamedConditions(this) : parseTreeVisitor.visitChildren(this));
   }
}
