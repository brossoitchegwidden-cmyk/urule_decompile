package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$RuleDefContext extends ParserRuleContext {
   public TerminalNode STRING() {
      return this.getToken(97, 0);
   }

   public RuleParserParser$LeftContext left() {
      return (RuleParserParser$LeftContext)this.getRuleContext(RuleParserParser$LeftContext.class, 0);
   }

   public RuleParserParser$RightContext right() {
      return (RuleParserParser$RightContext)this.getRuleContext(RuleParserParser$RightContext.class, 0);
   }

   public List<RuleParserParser$AttributeContext> attribute() {
      return this.getRuleContexts(RuleParserParser$AttributeContext.class);
   }

   public RuleParserParser$AttributeContext attribute(int number) {
      return (RuleParserParser$AttributeContext)this.getRuleContext(RuleParserParser$AttributeContext.class, number);
   }

   public RuleParserParser$OtherContext other() {
      return (RuleParserParser$OtherContext)this.getRuleContext(RuleParserParser$OtherContext.class, 0);
   }

   public RuleParserParser$RuleDefContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 14;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitRuleDef(this) : parseTreeVisitor.visitChildren(this));
   }
}
