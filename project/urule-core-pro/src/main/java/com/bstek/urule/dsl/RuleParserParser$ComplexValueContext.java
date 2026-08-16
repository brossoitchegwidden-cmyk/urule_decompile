package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$ComplexValueContext extends ParserRuleContext {
   public RuleParserParser$ValueContext value() {
      return (RuleParserParser$ValueContext)this.getRuleContext(RuleParserParser$ValueContext.class, 0);
   }

   public RuleParserParser$VariableContext variable() {
      return (RuleParserParser$VariableContext)this.getRuleContext(RuleParserParser$VariableContext.class, 0);
   }

   public RuleParserParser$NamedVariableContext namedVariable() {
      return (RuleParserParser$NamedVariableContext)this.getRuleContext(RuleParserParser$NamedVariableContext.class, 0);
   }

   public RuleParserParser$ConstantContext constant() {
      return (RuleParserParser$ConstantContext)this.getRuleContext(RuleParserParser$ConstantContext.class, 0);
   }

   public RuleParserParser$VariableCategoryContext variableCategory() {
      return (RuleParserParser$VariableCategoryContext)this.getRuleContext(RuleParserParser$VariableCategoryContext.class, 0);
   }

   public RuleParserParser$ParameterContext parameter() {
      return (RuleParserParser$ParameterContext)this.getRuleContext(RuleParserParser$ParameterContext.class, 0);
   }

   public RuleParserParser$MethodInvokeContext methodInvoke() {
      return (RuleParserParser$MethodInvokeContext)this.getRuleContext(RuleParserParser$MethodInvokeContext.class, 0);
   }

   public RuleParserParser$FunctionInvokeContext functionInvoke() {
      return (RuleParserParser$FunctionInvokeContext)this.getRuleContext(RuleParserParser$FunctionInvokeContext.class, 0);
   }

   public RuleParserParser$CommonFunctionContext commonFunction() {
      return (RuleParserParser$CommonFunctionContext)this.getRuleContext(RuleParserParser$CommonFunctionContext.class, 0);
   }

   public RuleParserParser$LeftParenContext leftParen() {
      return (RuleParserParser$LeftParenContext)this.getRuleContext(RuleParserParser$LeftParenContext.class, 0);
   }

   public List<RuleParserParser$ComplexValueContext> complexValue() {
      return this.getRuleContexts(RuleParserParser$ComplexValueContext.class);
   }

   public RuleParserParser$ComplexValueContext complexValue(int var1) {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, var1);
   }

   public RuleParserParser$RightParenContext rightParen() {
      return (RuleParserParser$RightParenContext)this.getRuleContext(RuleParserParser$RightParenContext.class, 0);
   }

   public List<TerminalNode> ARITH() {
      return this.getTokens(93);
   }

   public TerminalNode ARITH(int var1) {
      return this.getToken(93, var1);
   }

   public RuleParserParser$ComplexValueContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 58;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitComplexValue(this) : var1.visitChildren(this));
   }
}
