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

   public RuleParserParser$RefObjectContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 37;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitRefObject(this) : parseTreeVisitor.visitChildren(this));
   }
}
