package com.bstek.urule.dsl;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$RuleSetHeaderContext extends ParserRuleContext {
   public List<RuleParserParser$ResourceContext> resource() {
      return this.getRuleContexts(RuleParserParser$ResourceContext.class);
   }

   public RuleParserParser$ResourceContext resource(int number) {
      return (RuleParserParser$ResourceContext)this.getRuleContext(RuleParserParser$ResourceContext.class, number);
   }

   public List<RuleParserParser$FunctionImportContext> functionImport() {
      return this.getRuleContexts(RuleParserParser$FunctionImportContext.class);
   }

   public RuleParserParser$FunctionImportContext functionImport(int number) {
      return (RuleParserParser$FunctionImportContext)this.getRuleContext(RuleParserParser$FunctionImportContext.class, number);
   }

   public RuleParserParser$RuleSetHeaderContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 1;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitRuleSetHeader(this) : parseTreeVisitor.visitChildren(this));
   }
}
