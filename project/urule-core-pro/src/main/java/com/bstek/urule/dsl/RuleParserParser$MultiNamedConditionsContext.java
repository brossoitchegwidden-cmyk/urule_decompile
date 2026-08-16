package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$MultiNamedConditionsContext extends RuleParserParser$NamedConditionContext {
   public List<RuleParserParser$NamedConditionContext> namedCondition() {
      return this.getRuleContexts(RuleParserParser$NamedConditionContext.class);
   }

   public RuleParserParser$NamedConditionContext namedCondition(int var1) {
      return (RuleParserParser$NamedConditionContext)this.getRuleContext(RuleParserParser$NamedConditionContext.class, var1);
   }

   public List<RuleParserParser$JoinContext> join() {
      return this.getRuleContexts(RuleParserParser$JoinContext.class);
   }

   public RuleParserParser$JoinContext join(int var1) {
      return (RuleParserParser$JoinContext)this.getRuleContext(RuleParserParser$JoinContext.class, var1);
   }

   public RuleParserParser$MultiNamedConditionsContext(RuleParserParser$NamedConditionContext var1) {
      this.copyFrom(var1);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitMultiNamedConditions(this) : var1.visitChildren(this));
   }
}
