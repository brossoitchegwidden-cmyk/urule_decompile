package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$FunctionParametersContext extends ParserRuleContext {
   public List<RuleParserParser$FunctionParameterContext> functionParameter() {
      return this.getRuleContexts(RuleParserParser$FunctionParameterContext.class);
   }

   public RuleParserParser$FunctionParameterContext functionParameter(int var1) {
      return (RuleParserParser$FunctionParameterContext)this.getRuleContext(RuleParserParser$FunctionParameterContext.class, var1);
   }

   public RuleParserParser$FunctionParametersContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 12;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitFunctionParameters(this) : var1.visitChildren(this));
   }
}
