package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$FunctionImportContext extends ParserRuleContext {
   public RuleParserParser$PackageDefContext packageDef() {
      return (RuleParserParser$PackageDefContext)this.getRuleContext(RuleParserParser$PackageDefContext.class, 0);
   }

   public RuleParserParser$FunctionImportContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 4;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitFunctionImport(this) : parseTreeVisitor.visitChildren(this));
   }
}
