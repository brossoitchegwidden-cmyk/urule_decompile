package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$FunctionDefContext extends ParserRuleContext {
   public TerminalNode Identifier() {
      return this.getToken(96, 0);
   }

   public RuleParserParser$ExpressionBodyContext expressionBody() {
      return (RuleParserParser$ExpressionBodyContext)this.getRuleContext(RuleParserParser$ExpressionBodyContext.class, 0);
   }

   public RuleParserParser$FunctionParametersContext functionParameters() {
      return (RuleParserParser$FunctionParametersContext)this.getRuleContext(RuleParserParser$FunctionParametersContext.class, 0);
   }

   public RuleParserParser$FunctionDefContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 11;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitFunctionDef(this) : parseTreeVisitor.visitChildren(this));
   }
}
