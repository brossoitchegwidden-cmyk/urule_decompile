package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ActionContext extends ParserRuleContext {
   public RuleParserParser$AssignActionContext assignAction() {
      return (RuleParserParser$AssignActionContext)this.getRuleContext(RuleParserParser$AssignActionContext.class, 0);
   }

   public RuleParserParser$OutActionContext outAction() {
      return (RuleParserParser$OutActionContext)this.getRuleContext(RuleParserParser$OutActionContext.class, 0);
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

   public RuleParserParser$ActionContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 51;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitAction(this) : var1.visitChildren(this));
   }
}
