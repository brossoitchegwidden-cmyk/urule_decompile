package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$RuleSetHeaderContext extends ParserRuleContext {
   public List<RuleParserParser$ResourceContext> resource() {
      return this.getRuleContexts(RuleParserParser$ResourceContext.class);
   }

   public RuleParserParser$ResourceContext resource(int var1) {
      return (RuleParserParser$ResourceContext)this.getRuleContext(RuleParserParser$ResourceContext.class, var1);
   }

   public List<RuleParserParser$FunctionImportContext> functionImport() {
      return this.getRuleContexts(RuleParserParser$FunctionImportContext.class);
   }

   public RuleParserParser$FunctionImportContext functionImport(int var1) {
      return (RuleParserParser$FunctionImportContext)this.getRuleContext(RuleParserParser$FunctionImportContext.class, var1);
   }

   public RuleParserParser$RuleSetHeaderContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 1;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitRuleSetHeader(this) : var1.visitChildren(this));
   }
}
