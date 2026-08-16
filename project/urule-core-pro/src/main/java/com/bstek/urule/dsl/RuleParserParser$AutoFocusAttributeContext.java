package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$AutoFocusAttributeContext extends ParserRuleContext {
   public TerminalNode Boolean() {
      return this.getToken(95, 0);
   }

   public RuleParserParser$AutoFocusAttributeContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 29;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitAutoFocusAttribute(this) : var1.visitChildren(this));
   }
}
