package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$RuleSetContext extends ParserRuleContext {
   public RuleParserParser$RuleSetHeaderContext ruleSetHeader() {
      return (RuleParserParser$RuleSetHeaderContext)this.getRuleContext(RuleParserParser$RuleSetHeaderContext.class, 0);
   }

   public RuleParserParser$RuleSetBodyContext ruleSetBody() {
      return (RuleParserParser$RuleSetBodyContext)this.getRuleContext(RuleParserParser$RuleSetBodyContext.class, 0);
   }

   public RuleParserParser$RuleSetContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 0;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitRuleSet(this) : parseTreeVisitor.visitChildren(this));
   }
}
