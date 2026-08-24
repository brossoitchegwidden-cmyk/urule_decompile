package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$CommonFunctionContext extends ParserRuleContext {
   public TerminalNode Identifier() {
      return this.getToken(96, 0);
   }

   public RuleParserParser$LeftParenContext leftParen() {
      return (RuleParserParser$LeftParenContext)this.getRuleContext(RuleParserParser$LeftParenContext.class, 0);
   }

   public RuleParserParser$ComplexValueContext complexValue() {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, 0);
   }

   public RuleParserParser$RightParenContext rightParen() {
      return (RuleParserParser$RightParenContext)this.getRuleContext(RuleParserParser$RightParenContext.class, 0);
   }

   public RuleParserParser$PropertyContext property() {
      return (RuleParserParser$PropertyContext)this.getRuleContext(RuleParserParser$PropertyContext.class, 0);
   }

   public RuleParserParser$CommonFunctionContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 40;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitCommonFunction(this) : parseTreeVisitor.visitChildren(this));
   }
}
