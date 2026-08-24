package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$RuleSetBodyContext extends ParserRuleContext {
   public List<RuleParserParser$RulesContext> rules() {
      return this.getRuleContexts(RuleParserParser$RulesContext.class);
   }

   public RuleParserParser$RulesContext rules(int number) {
      return (RuleParserParser$RulesContext)this.getRuleContext(RuleParserParser$RulesContext.class, number);
   }

   public RuleParserParser$RuleSetBodyContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 2;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitRuleSetBody(this) : parseTreeVisitor.visitChildren(this));
   }
}
