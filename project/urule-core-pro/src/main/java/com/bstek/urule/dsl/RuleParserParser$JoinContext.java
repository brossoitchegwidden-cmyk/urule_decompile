package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$JoinContext extends ParserRuleContext {
   public TerminalNode AND() {
      return this.getToken(72, 0);
   }

   public TerminalNode OR() {
      return this.getToken(73, 0);
   }

   public RuleParserParser$JoinContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 47;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitJoin(this) : parseTreeVisitor.visitChildren(this));
   }
}
