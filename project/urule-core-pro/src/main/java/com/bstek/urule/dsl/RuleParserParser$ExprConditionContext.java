package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ExprConditionContext extends ParserRuleContext {
   public RuleParserParser$PropertyContext property() {
      return (RuleParserParser$PropertyContext)this.getRuleContext(RuleParserParser$PropertyContext.class, 0);
   }

   public RuleParserParser$OpContext op() {
      return (RuleParserParser$OpContext)this.getRuleContext(RuleParserParser$OpContext.class, 0);
   }

   public RuleParserParser$ComplexValueContext complexValue() {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, 0);
   }

   public RuleParserParser$NullValueContext nullValue() {
      return (RuleParserParser$NullValueContext)this.getRuleContext(RuleParserParser$NullValueContext.class, 0);
   }

   public List<RuleParserParser$ExprConditionContext> exprCondition() {
      return this.getRuleContexts(RuleParserParser$ExprConditionContext.class);
   }

   public RuleParserParser$ExprConditionContext exprCondition(int var1) {
      return (RuleParserParser$ExprConditionContext)this.getRuleContext(RuleParserParser$ExprConditionContext.class, var1);
   }

   public List<RuleParserParser$JoinContext> join() {
      return this.getRuleContexts(RuleParserParser$JoinContext.class);
   }

   public RuleParserParser$JoinContext join(int var1) {
      return (RuleParserParser$JoinContext)this.getRuleContext(RuleParserParser$JoinContext.class, var1);
   }

   public RuleParserParser$ExprConditionContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 41;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitExprCondition(this) : var1.visitChildren(this));
   }
}
