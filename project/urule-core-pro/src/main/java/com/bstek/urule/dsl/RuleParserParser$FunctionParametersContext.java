package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$FunctionParametersContext extends ParserRuleContext {
   public List<RuleParserParser$FunctionParameterContext> functionParameter() {
      return this.getRuleContexts(RuleParserParser$FunctionParameterContext.class);
   }

   public RuleParserParser$FunctionParameterContext functionParameter(int number) {
      return (RuleParserParser$FunctionParameterContext)this.getRuleContext(RuleParserParser$FunctionParameterContext.class, number);
   }

   public RuleParserParser$FunctionParametersContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 12;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitFunctionParameters(this) : parseTreeVisitor.visitChildren(this));
   }
}
