package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$FunctionInvokeContext extends ParserRuleContext {
   public TerminalNode Identifier() {
      return this.getToken(96, 0);
   }

   public RuleParserParser$ActionParametersContext actionParameters() {
      return (RuleParserParser$ActionParametersContext)this.getRuleContext(RuleParserParser$ActionParametersContext.class, 0);
   }

   public RuleParserParser$FunctionInvokeContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 55;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitFunctionInvoke(this) : parseTreeVisitor.visitChildren(this));
   }
}
