package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$EffectiveDateAttributeContext extends ParserRuleContext {
   public TerminalNode STRING() {
      return this.getToken(97, 0);
   }

   public RuleParserParser$EffectiveDateAttributeContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 23;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitEffectiveDateAttribute(this) : parseTreeVisitor.visitChildren(this));
   }
}
