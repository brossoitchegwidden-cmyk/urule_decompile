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

   public RuleParserParser$NamedVariableContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 63;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitNamedVariable(this) : parseTreeVisitor.visitChildren(this));
   }
}
