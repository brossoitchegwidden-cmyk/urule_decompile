package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$ResourceContext extends ParserRuleContext {
   public RuleParserParser$ImportVariableLibraryContext importVariableLibrary() {
      return (RuleParserParser$ImportVariableLibraryContext)this.getRuleContext(RuleParserParser$ImportVariableLibraryContext.class, 0);
   }

   public RuleParserParser$ImportActionLibraryContext importActionLibrary() {
      return (RuleParserParser$ImportActionLibraryContext)this.getRuleContext(RuleParserParser$ImportActionLibraryContext.class, 0);
   }

   public RuleParserParser$ImportConstantLibraryContext importConstantLibrary() {
      return (RuleParserParser$ImportConstantLibraryContext)this.getRuleContext(RuleParserParser$ImportConstantLibraryContext.class, 0);
   }

   public RuleParserParser$ImportParameterLibraryContext importParameterLibrary() {
      return (RuleParserParser$ImportParameterLibraryContext)this.getRuleContext(RuleParserParser$ImportParameterLibraryContext.class, 0);
   }

   public RuleParserParser$ResourceContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 6;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitResource(this) : parseTreeVisitor.visitChildren(this));
   }
}
