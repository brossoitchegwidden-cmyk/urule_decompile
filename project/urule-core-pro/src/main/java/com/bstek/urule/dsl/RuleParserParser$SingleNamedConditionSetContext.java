package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$SingleNamedConditionSetContext extends RuleParserParser$ConditionContext {
   public RuleParserParser$NamedConditionSetContext namedConditionSet() {
      return (RuleParserParser$NamedConditionSetContext)this.getRuleContext(RuleParserParser$NamedConditionSetContext.class, 0);
   }

   public RuleParserParser$SingleNamedConditionSetContext(RuleParserParser$ConditionContext var1) {
      this.copyFrom(var1);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitSingleNamedConditionSet(this) : var1.visitChildren(this));
   }
}
