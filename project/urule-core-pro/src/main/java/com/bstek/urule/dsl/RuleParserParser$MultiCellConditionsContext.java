package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$MultiCellConditionsContext extends RuleParserParser$DecisionTableCellConditionContext {
   public List<RuleParserParser$DecisionTableCellConditionContext> decisionTableCellCondition() {
      return this.getRuleContexts(RuleParserParser$DecisionTableCellConditionContext.class);
   }

   public RuleParserParser$DecisionTableCellConditionContext decisionTableCellCondition(int number) {
      return (RuleParserParser$DecisionTableCellConditionContext)this.getRuleContext(RuleParserParser$DecisionTableCellConditionContext.class, number);
   }

   public List<RuleParserParser$JoinContext> join() {
      return this.getRuleContexts(RuleParserParser$JoinContext.class);
   }

   public RuleParserParser$JoinContext join(int number) {
      return (RuleParserParser$JoinContext)this.getRuleContext(RuleParserParser$JoinContext.class, number);
   }

   public RuleParserParser$MultiCellConditionsContext(RuleParserParser$DecisionTableCellConditionContext ruleParserParser$DecisionTableCellConditionContext) {
      this.copyFrom(ruleParserParser$DecisionTableCellConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitMultiCellConditions(this) : parseTreeVisitor.visitChildren(this));
   }
}
