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

   public RuleParserParser$MethodInvokeContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 54;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitMethodInvoke(this) : parseTreeVisitor.visitChildren(this));
   }
}
