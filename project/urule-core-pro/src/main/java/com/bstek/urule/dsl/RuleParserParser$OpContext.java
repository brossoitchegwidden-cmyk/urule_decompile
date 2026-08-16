package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$OpContext extends ParserRuleContext {
   public TerminalNode GreaterThen() {
      return this.getToken(75, 0);
   }

   public TerminalNode GreaterThenOrEquals() {
      return this.getToken(76, 0);
   }

   public TerminalNode LessThen() {
      return this.getToken(77, 0);
   }

   public TerminalNode LessThenOrEquals() {
      return this.getToken(78, 0);
   }

   public TerminalNode Equals() {
      return this.getToken(79, 0);
   }

   public TerminalNode NotEquals() {
      return this.getToken(80, 0);
   }

   public TerminalNode EndWith() {
      return this.getToken(81, 0);
   }

   public TerminalNode NotEndWith() {
      return this.getToken(82, 0);
   }

   public TerminalNode StartWith() {
      return this.getToken(83, 0);
   }

   public TerminalNode NotStartWith() {
      return this.getToken(84, 0);
   }

   public TerminalNode In() {
      return this.getToken(85, 0);
   }

   public TerminalNode NotIn() {
      return this.getToken(86, 0);
   }

   public TerminalNode Match() {
      return this.getToken(87, 0);
   }

   public TerminalNode NotMatch() {
      return this.getToken(88, 0);
   }

   public TerminalNode EqualsIgnoreCase() {
      return this.getToken(91, 0);
   }

   public TerminalNode NotEqualsIgnoreCase() {
      return this.getToken(92, 0);
   }

   public TerminalNode Contain() {
      return this.getToken(89, 0);
   }

   public TerminalNode NotContain() {
      return this.getToken(90, 0);
   }

   public RuleParserParser$OpContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 69;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitOp(this) : var1.visitChildren(this));
   }
}
