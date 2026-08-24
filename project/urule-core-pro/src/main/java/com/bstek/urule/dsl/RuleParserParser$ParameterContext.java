package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$ParameterContext extends ParserRuleContext {
   public RuleParserParser$ParameterNameContext parameterName() {
      return (RuleParserParser$ParameterNameContext)this.getRuleContext(RuleParserParser$ParameterNameContext.class, 0);
   }

   public TerminalNode Identifier() {
      return this.getToken(96, 0);
   }

   public RuleParserParser$ParameterContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 59;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitParameter(this) : parseTreeVisitor.visitChildren(this));
   }
}
