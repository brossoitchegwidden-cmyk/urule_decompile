package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$ConditionLeftContext extends ParserRuleContext {
   public RuleParserParser$VariableContext variable() {
      return (RuleParserParser$VariableContext)this.getRuleContext(RuleParserParser$VariableContext.class, 0);
   }

   public RuleParserParser$ParameterContext parameter() {
      return (RuleParserParser$ParameterContext)this.getRuleContext(RuleParserParser$ParameterContext.class, 0);
   }

   public RuleParserParser$FunctionInvokeContext functionInvoke() {
      return (RuleParserParser$FunctionInvokeContext)this.getRuleContext(RuleParserParser$FunctionInvokeContext.class, 0);
   }

   public RuleParserParser$MethodInvokeContext methodInvoke() {
      return (RuleParserParser$MethodInvokeContext)this.getRuleContext(RuleParserParser$MethodInvokeContext.class, 0);
   }

   public RuleParserParser$CommonFunctionContext commonFunction() {
      return (RuleParserParser$CommonFunctionContext)this.getRuleContext(RuleParserParser$CommonFunctionContext.class, 0);
   }

   public List<TerminalNode> ARITH() {
      return this.getTokens(93);
   }

   public TerminalNode ARITH(int var1) {
      return this.getToken(93, var1);
   }

   public List<RuleParserParser$ValueContext> value() {
      return this.getRuleContexts(RuleParserParser$ValueContext.class);
   }

   public RuleParserParser$ValueContext value(int var1) {
      return (RuleParserParser$ValueContext)this.getRuleContext(RuleParserParser$ValueContext.class, var1);
   }

   public RuleParserParser$ConditionLeftContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 39;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitConditionLeft(this) : var1.visitChildren(this));
   }
}
