package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$MultiConditionsContext extends RuleParserParser$ConditionContext {
   public List<RuleParserParser$ConditionContext> condition() {
      return this.getRuleContexts(RuleParserParser$ConditionContext.class);
   }

   public RuleParserParser$ConditionContext condition(int number) {
      return (RuleParserParser$ConditionContext)this.getRuleContext(RuleParserParser$ConditionContext.class, number);
   }

   public List<RuleParserParser$JoinContext> join() {
      return this.getRuleContexts(RuleParserParser$JoinContext.class);
   }

   public RuleParserParser$JoinContext join(int number) {
      return (RuleParserParser$JoinContext)this.getRuleContext(RuleParserParser$JoinContext.class, number);
   }

   public RuleParserParser$MultiConditionsContext(RuleParserParser$ConditionContext ruleParserParser$ConditionContext) {
      this.copyFrom(ruleParserParser$ConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitMultiConditions(this) : parseTreeVisitor.visitChildren(this));
   }
}
