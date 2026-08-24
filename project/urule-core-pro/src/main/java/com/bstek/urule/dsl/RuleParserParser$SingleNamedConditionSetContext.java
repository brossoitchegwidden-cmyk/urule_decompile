package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$SingleNamedConditionSetContext extends RuleParserParser$ConditionContext {
   public RuleParserParser$NamedConditionSetContext namedConditionSet() {
      return (RuleParserParser$NamedConditionSetContext)this.getRuleContext(RuleParserParser$NamedConditionSetContext.class, 0);
   }

   public RuleParserParser$SingleNamedConditionSetContext(RuleParserParser$ConditionContext ruleParserParser$ConditionContext) {
      this.copyFrom(ruleParserParser$ConditionContext);
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitSingleNamedConditionSet(this) : parseTreeVisitor.visitChildren(this));
   }
}
