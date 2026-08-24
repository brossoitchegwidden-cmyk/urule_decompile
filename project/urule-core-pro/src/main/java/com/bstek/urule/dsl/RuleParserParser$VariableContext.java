package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$VariableContext extends ParserRuleContext {
   public RuleParserParser$VariableCategoryContext variableCategory() {
      return (RuleParserParser$VariableCategoryContext)this.getRuleContext(RuleParserParser$VariableCategoryContext.class, 0);
   }

   public RuleParserParser$PropertyContext property() {
      return (RuleParserParser$PropertyContext)this.getRuleContext(RuleParserParser$PropertyContext.class, 0);
   }

   public RuleParserParser$VariableContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 62;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitVariable(this) : parseTreeVisitor.visitChildren(this));
   }
}
