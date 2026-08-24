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

   public RuleParserParser$RulesContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 3;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitRules(this) : parseTreeVisitor.visitChildren(this));
   }
}
