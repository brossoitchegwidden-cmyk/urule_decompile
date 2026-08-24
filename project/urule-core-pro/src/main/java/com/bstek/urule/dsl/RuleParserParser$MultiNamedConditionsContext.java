package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$MultiNamedConditionsContext extends RuleParserParser$NamedConditionContext {
   public List<RuleParserParser$NamedConditionContext> namedCondition() {
      return this.getRuleContexts(RuleParserParser$NamedConditionContext.class);
   }

   public RuleParserParser$NamedConditionContext namedCondition(int number) {
      return (RuleParserParser$NamedConditionContext)this.getRuleContext(RuleParserParser$NamedConditionContext.class, number);
   }

   public List<RuleParserParser$JoinContext> join() {
      return this.getRuleContexts(RuleParserParser$JoinContext.class);
   }

   public RuleParserParser$JoinContext join(int number) {
      return (RuleParserParser$JoinContext)this.getRuleContext(RuleParserParser$JoinContext.class, number);
   }

   public RuleParserParser$MultiNamedConditionsContext(RuleParserParser$NamedConditionContext ruleParserParser$NamedConditionContext) {
      this.copyFrom(ruleParserParser$NamedConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitMultiNamedConditions(this) : parseTreeVisitor.visitChildren(this));
   }
}
