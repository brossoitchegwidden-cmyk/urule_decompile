package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ConstantContext extends ParserRuleContext {
   public RuleParserParser$ConstantCategoryContext constantCategory() {
      return (RuleParserParser$ConstantCategoryContext)this.getRuleContext(RuleParserParser$ConstantCategoryContext.class, 0);
   }

   public RuleParserParser$PropertyContext property() {
      return (RuleParserParser$PropertyContext)this.getRuleContext(RuleParserParser$PropertyContext.class, 0);
   }

   public RuleParserParser$ConstantContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 61;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitConstant(this) : parseTreeVisitor.visitChildren(this));
   }
}
