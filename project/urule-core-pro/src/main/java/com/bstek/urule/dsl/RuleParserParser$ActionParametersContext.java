package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ActionParametersContext extends ParserRuleContext {
   public List<RuleParserParser$ComplexValueContext> complexValue() {
      return this.getRuleContexts(RuleParserParser$ComplexValueContext.class);
   }

   public RuleParserParser$ComplexValueContext complexValue(int var1) {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, var1);
   }

   public RuleParserParser$ActionParametersContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 56;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitActionParameters(this) : var1.visitChildren(this));
   }
}
