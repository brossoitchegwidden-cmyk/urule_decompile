package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$AttributeContext extends ParserRuleContext {
   public RuleParserParser$LoopAttributeContext loopAttribute() {
      return (RuleParserParser$LoopAttributeContext)this.getRuleContext(RuleParserParser$LoopAttributeContext.class, 0);
   }

   public RuleParserParser$SalienceAttributeContext salienceAttribute() {
      return (RuleParserParser$SalienceAttributeContext)this.getRuleContext(RuleParserParser$SalienceAttributeContext.class, 0);
   }

   public RuleParserParser$EffectiveDateAttributeContext effectiveDateAttribute() {
      return (RuleParserParser$EffectiveDateAttributeContext)this.getRuleContext(RuleParserParser$EffectiveDateAttributeContext.class, 0);
   }

   public RuleParserParser$ExpiresDateAttributeContext expiresDateAttribute() {
      return (RuleParserParser$ExpiresDateAttributeContext)this.getRuleContext(RuleParserParser$ExpiresDateAttributeContext.class, 0);
   }

   public RuleParserParser$EnabledAttributeContext enabledAttribute() {
      return (RuleParserParser$EnabledAttributeContext)this.getRuleContext(RuleParserParser$EnabledAttributeContext.class, 0);
   }

   public RuleParserParser$DebugAttributeContext debugAttribute() {
      return (RuleParserParser$DebugAttributeContext)this.getRuleContext(RuleParserParser$DebugAttributeContext.class, 0);
   }

   public RuleParserParser$ActivationGroupAttributeContext activationGroupAttribute() {
      return (RuleParserParser$ActivationGroupAttributeContext)this.getRuleContext(RuleParserParser$ActivationGroupAttributeContext.class, 0);
   }

   public RuleParserParser$AgendaGroupAttributeContext agendaGroupAttribute() {
      return (RuleParserParser$AgendaGroupAttributeContext)this.getRuleContext(RuleParserParser$AgendaGroupAttributeContext.class, 0);
   }

   public RuleParserParser$AutoFocusAttributeContext autoFocusAttribute() {
      return (RuleParserParser$AutoFocusAttributeContext)this.getRuleContext(RuleParserParser$AutoFocusAttributeContext.class, 0);
   }

   public RuleParserParser$RuleflowGroupAttributeContext ruleflowGroupAttribute() {
      return (RuleParserParser$RuleflowGroupAttributeContext)this.getRuleContext(RuleParserParser$RuleflowGroupAttributeContext.class, 0);
   }

   public RuleParserParser$AttributeContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 20;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitAttribute(this) : var1.visitChildren(this));
   }
}
