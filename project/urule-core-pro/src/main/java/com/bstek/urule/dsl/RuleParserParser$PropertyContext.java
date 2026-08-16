package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$PropertyContext extends ParserRuleContext {
   public List<TerminalNode> Identifier() {
      return this.getTokens(96);
   }

   public TerminalNode Identifier(int var1) {
      return this.getToken(96, var1);
   }

   public RuleParserParser$PropertyContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 64;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitProperty(this) : var1.visitChildren(this));
   }
}
