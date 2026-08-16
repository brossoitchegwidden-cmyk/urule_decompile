package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$RefObjectContext extends ParserRuleContext {
   public RuleParserParser$VariableCategoryContext variableCategory() {
      return (RuleParserParser$VariableCategoryContext)this.getRuleContext(RuleParserParser$VariableCategoryContext.class, 0);
   }

   public RuleParserParser$ParameterNameContext parameterName() {
      return (RuleParserParser$ParameterNameContext)this.getRuleContext(RuleParserParser$ParameterNameContext.class, 0);
   }

   public RuleParserParser$RefObjectContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 37;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitRefObject(this) : var1.visitChildren(this));
   }
}
