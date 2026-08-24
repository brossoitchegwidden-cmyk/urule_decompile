package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$SingleNamedConditionsContext extends RuleParserParser$NamedConditionContext {
   public RuleParserParser$PropertyContext property() {
      return (RuleParserParser$PropertyContext)this.getRuleContext(RuleParserParser$PropertyContext.class, 0);
   }

   public RuleParserParser$OpContext op() {
      return (RuleParserParser$OpContext)this.getRuleContext(RuleParserParser$OpContext.class, 0);
   }

   public RuleParserParser$ComplexValueContext complexValue() {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, 0);
   }

   public RuleParserParser$NullValueContext nullValue() {
      return (RuleParserParser$NullValueContext)this.getRuleContext(RuleParserParser$NullValueContext.class, 0);
   }

   public RuleParserParser$SingleNamedConditionsContext(RuleParserParser$NamedConditionContext ruleParserParser$NamedConditionContext) {
      this.copyFrom(ruleParserParser$NamedConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitSingleNamedConditions(this) : parseTreeVisitor.visitChildren(this));
   }
}
