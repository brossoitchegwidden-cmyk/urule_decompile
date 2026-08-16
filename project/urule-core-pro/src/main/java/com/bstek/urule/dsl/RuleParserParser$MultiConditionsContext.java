package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$MultiConditionsContext extends RuleParserParser$ConditionContext {
   public List<RuleParserParser$ConditionContext> condition() {
      return this.getRuleContexts(RuleParserParser$ConditionContext.class);
   }

   public RuleParserParser$ConditionContext condition(int var1) {
      return (RuleParserParser$ConditionContext)this.getRuleContext(RuleParserParser$ConditionContext.class, var1);
   }

   public List<RuleParserParser$JoinContext> join() {
      return this.getRuleContexts(RuleParserParser$JoinContext.class);
   }

   public RuleParserParser$JoinContext join(int var1) {
      return (RuleParserParser$JoinContext)this.getRuleContext(RuleParserParser$JoinContext.class, var1);
   }

   public RuleParserParser$MultiConditionsContext(RuleParserParser$ConditionContext var1) {
      this.copyFrom(var1);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitMultiConditions(this) : var1.visitChildren(this));
   }
}
