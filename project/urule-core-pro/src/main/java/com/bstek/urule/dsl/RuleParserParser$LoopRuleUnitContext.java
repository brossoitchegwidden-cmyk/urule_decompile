package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$LoopRuleUnitContext extends ParserRuleContext {
   public RuleParserParser$LeftContext left() {
      return (RuleParserParser$LeftContext)this.getRuleContext(RuleParserParser$LeftContext.class, 0);
   }

   public RuleParserParser$RightContext right() {
      return (RuleParserParser$RightContext)this.getRuleContext(RuleParserParser$RightContext.class, 0);
   }

   public TerminalNode STRING() {
      return this.getToken(97, 0);
   }

   public RuleParserParser$OtherContext other() {
      return (RuleParserParser$OtherContext)this.getRuleContext(RuleParserParser$OtherContext.class, 0);
   }

   public RuleParserParser$LoopRuleUnitContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 16;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitLoopRuleUnit(this) : parseTreeVisitor.visitChildren(this));
   }
}
