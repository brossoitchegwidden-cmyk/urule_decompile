package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ParenCellConditionsContext extends RuleParserParser$DecisionTableCellConditionContext {
   public RuleParserParser$LeftParenContext leftParen() {
      return (RuleParserParser$LeftParenContext)this.getRuleContext(RuleParserParser$LeftParenContext.class, 0);
   }

   public RuleParserParser$DecisionTableCellConditionContext decisionTableCellCondition() {
      return (RuleParserParser$DecisionTableCellConditionContext)this.getRuleContext(RuleParserParser$DecisionTableCellConditionContext.class, 0);
   }

   public RuleParserParser$RightParenContext rightParen() {
      return (RuleParserParser$RightParenContext)this.getRuleContext(RuleParserParser$RightParenContext.class, 0);
   }

   public RuleParserParser$ParenCellConditionsContext(RuleParserParser$DecisionTableCellConditionContext ruleParserParser$DecisionTableCellConditionContext) {
      this.copyFrom(ruleParserParser$DecisionTableCellConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitParenCellConditions(this) : parseTreeVisitor.visitChildren(this));
   }
}
