package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ParameterNameContext extends ParserRuleContext {
   public RuleParserParser$ParameterNameContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 60;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitParameterName(this) : parseTreeVisitor.visitChildren(this));
   }
}
