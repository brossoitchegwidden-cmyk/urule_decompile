package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ActionsContext extends ParserRuleContext {
   public List<RuleParserParser$ActionContext> action() {
      return this.getRuleContexts(RuleParserParser$ActionContext.class);
   }

   public RuleParserParser$ActionContext action(int number) {
      return (RuleParserParser$ActionContext)this.getRuleContext(RuleParserParser$ActionContext.class, number);
   }

   public RuleParserParser$ActionsContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 50;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitActions(this) : parseTreeVisitor.visitChildren(this));
   }
}
