package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$MultiCellConditionsContext extends RuleParserParser$DecisionTableCellConditionContext {
   public List<RuleParserParser$DecisionTableCellConditionContext> decisionTableCellCondition() {
      return this.getRuleContexts(RuleParserParser$DecisionTableCellConditionContext.class);
   }

   public RuleParserParser$DecisionTableCellConditionContext decisionTableCellCondition(int var1) {
      return (RuleParserParser$DecisionTableCellConditionContext)this.getRuleContext(RuleParserParser$DecisionTableCellConditionContext.class, var1);
   }

   public List<RuleParserParser$JoinContext> join() {
      return this.getRuleContexts(RuleParserParser$JoinContext.class);
   }

   public RuleParserParser$JoinContext join(int var1) {
      return (RuleParserParser$JoinContext)this.getRuleContext(RuleParserParser$JoinContext.class, var1);
   }

   public RuleParserParser$MultiCellConditionsContext(RuleParserParser$DecisionTableCellConditionContext var1) {
      this.copyFrom(var1);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitMultiCellConditions(this) : var1.visitChildren(this));
   }
}
