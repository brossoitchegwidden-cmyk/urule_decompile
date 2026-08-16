package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$RuleSetBodyContext extends ParserRuleContext {
   public List<RuleParserParser$RulesContext> rules() {
      return this.getRuleContexts(RuleParserParser$RulesContext.class);
   }

   public RuleParserParser$RulesContext rules(int var1) {
      return (RuleParserParser$RulesContext)this.getRuleContext(RuleParserParser$RulesContext.class, var1);
   }

   public RuleParserParser$RuleSetBodyContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 2;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitRuleSetBody(this) : var1.visitChildren(this));
   }
}
