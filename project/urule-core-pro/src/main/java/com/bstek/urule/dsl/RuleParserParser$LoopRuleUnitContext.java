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

   public RuleParserParser$LoopRuleUnitContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 16;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitLoopRuleUnit(this) : var1.visitChildren(this));
   }
}
