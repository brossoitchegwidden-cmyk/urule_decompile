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

   public RuleParserParser$FunctionParameterContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 13;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitFunctionParameter(this) : var1.visitChildren(this));
   }
}
