package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$SingleConditionContext extends RuleParserParser$ConditionContext {
   public RuleParserParser$ComplexValueContext complexValue() {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, 0);
   }

   public RuleParserParser$NullValueContext nullValue() {
      return (RuleParserParser$NullValueContext)this.getRuleContext(RuleParserParser$NullValueContext.class, 0);
   }

   public RuleParserParser$ConditionLeftContext conditionLeft() {
      return (RuleParserParser$ConditionLeftContext)this.getRuleContext(RuleParserParser$ConditionLeftContext.class, 0);
   }

   public RuleParserParser$OpContext op() {
      return (RuleParserParser$OpContext)this.getRuleContext(RuleParserParser$OpContext.class, 0);
   }

   public RuleParserParser$SingleConditionContext(RuleParserParser$ConditionContext ruleParserParser$ConditionContext) {
      this.copyFrom(ruleParserParser$ConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitSingleCondition(this) : parseTreeVisitor.visitChildren(this));
   }
}
