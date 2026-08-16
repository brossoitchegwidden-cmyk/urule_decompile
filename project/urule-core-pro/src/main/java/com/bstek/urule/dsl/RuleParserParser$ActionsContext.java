package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ActionsContext extends ParserRuleContext {
   public List<RuleParserParser$ActionContext> action() {
      return this.getRuleContexts(RuleParserParser$ActionContext.class);
   }

   public RuleParserParser$ActionContext action(int var1) {
      return (RuleParserParser$ActionContext)this.getRuleContext(RuleParserParser$ActionContext.class, var1);
   }

   public RuleParserParser$ActionsContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 50;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitActions(this) : var1.visitChildren(this));
   }
}
