package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$FunctionImportContext extends ParserRuleContext {
   public RuleParserParser$PackageDefContext packageDef() {
      return (RuleParserParser$PackageDefContext)this.getRuleContext(RuleParserParser$PackageDefContext.class, 0);
   }

   public RuleParserParser$FunctionImportContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 4;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitFunctionImport(this) : var1.visitChildren(this));
   }
}
