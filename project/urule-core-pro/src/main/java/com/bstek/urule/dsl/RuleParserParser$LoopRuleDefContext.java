package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class RuleParserParser$LoopRuleDefContext extends ParserRuleContext {
   public TerminalNode STRING() {
      return this.getToken(97, 0);
   }

   public RuleParserParser$LoopTargetContext loopTarget() {
      return (RuleParserParser$LoopTargetContext)this.getRuleContext(RuleParserParser$LoopTargetContext.class, 0);
   }

   public List<RuleParserParser$AttributeContext> attribute() {
      return this.getRuleContexts(RuleParserParser$AttributeContext.class);
   }

   public RuleParserParser$AttributeContext attribute(int var1) {
      return (RuleParserParser$AttributeContext)this.getRuleContext(RuleParserParser$AttributeContext.class, var1);
   }

   public RuleParserParser$LoopStartContext loopStart() {
      return (RuleParserParser$LoopStartContext)this.getRuleContext(RuleParserParser$LoopStartContext.class, 0);
   }

   public List<RuleParserParser$LoopRuleUnitContext> loopRuleUnit() {
      return this.getRuleContexts(RuleParserParser$LoopRuleUnitContext.class);
   }

   public RuleParserParser$LoopRuleUnitContext loopRuleUnit(int var1) {
      return (RuleParserParser$LoopRuleUnitContext)this.getRuleContext(RuleParserParser$LoopRuleUnitContext.class, var1);
   }

   public RuleParserParser$LoopEndContext loopEnd() {
      return (RuleParserParser$LoopEndContext)this.getRuleContext(RuleParserParser$LoopEndContext.class, 0);
   }

   public RuleParserParser$LoopRuleDefContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 15;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitLoopRuleDef(this) : var1.visitChildren(this));
   }
}
