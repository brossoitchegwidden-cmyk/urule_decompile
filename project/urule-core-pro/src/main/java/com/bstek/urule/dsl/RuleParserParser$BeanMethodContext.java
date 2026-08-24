package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$BeanMethodContext extends ParserRuleContext {
   public List<TerminalNode> Identifier() {
      return this.getTokens(96);
   }

   public TerminalNode Identifier(int number) {
      return this.getToken(96, number);
   }

   public RuleParserParser$BeanMethodContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 57;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitBeanMethod(this) : parseTreeVisitor.visitChildren(this));
   }
}
