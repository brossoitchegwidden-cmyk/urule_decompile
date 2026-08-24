package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ActionParametersContext extends ParserRuleContext {
   public List<RuleParserParser$ComplexValueContext> complexValue() {
      return this.getRuleContexts(RuleParserParser$ComplexValueContext.class);
   }

   public RuleParserParser$ComplexValueContext complexValue(int number) {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, number);
   }

   public RuleParserParser$ActionParametersContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 56;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitActionParameters(this) : parseTreeVisitor.visitChildren(this));
   }
}
