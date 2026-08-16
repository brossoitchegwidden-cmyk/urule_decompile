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

   public RuleParserParser$FunctionInvokeContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 55;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitFunctionInvoke(this) : var1.visitChildren(this));
   }
}
