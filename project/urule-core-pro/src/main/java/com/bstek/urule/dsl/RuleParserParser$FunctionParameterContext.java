package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$FunctionParameterContext extends ParserRuleContext {
   public TerminalNode Datatype() {
      return this.getToken(74, 0);
   }

   public TerminalNode Identifier() {
      return this.getToken(96, 0);
   }

   public RuleParserParser$FunctionParameterContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 13;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitFunctionParameter(this) : parseTreeVisitor.visitChildren(this));
   }
}
