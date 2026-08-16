package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$NamedConditionSetContext extends ParserRuleContext {
   public RuleParserParser$RefObjectContext refObject() {
      return (RuleParserParser$RefObjectContext)this.getRuleContext(RuleParserParser$RefObjectContext.class, 0);
   }

   public RuleParserParser$LeftParenContext leftParen() {
      return (RuleParserParser$LeftParenContext)this.getRuleContext(RuleParserParser$LeftParenContext.class, 0);
   }

   public RuleParserParser$NamedConditionContext namedCondition() {
      return (RuleParserParser$NamedConditionContext)this.getRuleContext(RuleParserParser$NamedConditionContext.class, 0);
   }

   public RuleParserParser$RightParenContext rightParen() {
      return (RuleParserParser$RightParenContext)this.getRuleContext(RuleParserParser$RightParenContext.class, 0);
   }

   public RuleParserParser$RefNameContext refName() {
      return (RuleParserParser$RefNameContext)this.getRuleContext(RuleParserParser$RefNameContext.class, 0);
   }

   public RuleParserParser$ColonContext colon() {
      return (RuleParserParser$ColonContext)this.getRuleContext(RuleParserParser$ColonContext.class, 0);
   }

   public RuleParserParser$NamedConditionSetContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 33;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitNamedConditionSet(this) : var1.visitChildren(this));
   }
}
