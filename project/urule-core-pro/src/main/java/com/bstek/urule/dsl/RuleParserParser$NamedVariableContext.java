package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$NamedVariableContext extends ParserRuleContext {
   public RuleParserParser$NamedVariableCategoryContext namedVariableCategory() {
      return (RuleParserParser$NamedVariableCategoryContext)this.getRuleContext(RuleParserParser$NamedVariableCategoryContext.class, 0);
   }

   public RuleParserParser$PropertyContext property() {
      return (RuleParserParser$PropertyContext)this.getRuleContext(RuleParserParser$PropertyContext.class, 0);
   }

   public RuleParserParser$NamedVariableContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 63;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitNamedVariable(this) : var1.visitChildren(this));
   }
}
