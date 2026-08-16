package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$MethodInvokeContext extends ParserRuleContext {
   public RuleParserParser$BeanMethodContext beanMethod() {
      return (RuleParserParser$BeanMethodContext)this.getRuleContext(RuleParserParser$BeanMethodContext.class, 0);
   }

   public RuleParserParser$ActionParametersContext actionParameters() {
      return (RuleParserParser$ActionParametersContext)this.getRuleContext(RuleParserParser$ActionParametersContext.class, 0);
   }

   public RuleParserParser$MethodInvokeContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 54;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitMethodInvoke(this) : var1.visitChildren(this));
   }
}
