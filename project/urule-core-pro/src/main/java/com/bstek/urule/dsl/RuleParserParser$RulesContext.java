package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$RulesContext extends ParserRuleContext {
   public RuleParserParser$RuleDefContext ruleDef() {
      return (RuleParserParser$RuleDefContext)this.getRuleContext(RuleParserParser$RuleDefContext.class, 0);
   }

   public RuleParserParser$LoopRuleDefContext loopRuleDef() {
      return (RuleParserParser$LoopRuleDefContext)this.getRuleContext(RuleParserParser$LoopRuleDefContext.class, 0);
   }

   public RuleParserParser$RulesContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 3;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitRules(this) : var1.visitChildren(this));
   }
}
