package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$SingleCellConditionContext extends RuleParserParser$DecisionTableCellConditionContext {
   public RuleParserParser$OpContext op() {
      return (RuleParserParser$OpContext)this.getRuleContext(RuleParserParser$OpContext.class, 0);
   }

   public RuleParserParser$ComplexValueContext complexValue() {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, 0);
   }

   public RuleParserParser$NullValueContext nullValue() {
      return (RuleParserParser$NullValueContext)this.getRuleContext(RuleParserParser$NullValueContext.class, 0);
   }

   public RuleParserParser$SingleCellConditionContext(RuleParserParser$DecisionTableCellConditionContext ruleParserParser$DecisionTableCellConditionContext) {
      this.copyFrom(ruleParserParser$DecisionTableCellConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitSingleCellCondition(this) : parseTreeVisitor.visitChildren(this));
   }
}
