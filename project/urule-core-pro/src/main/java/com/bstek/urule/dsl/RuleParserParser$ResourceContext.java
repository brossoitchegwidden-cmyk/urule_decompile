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

   public RuleParserParser$ResourceContext(ParserRuleContext var1, int var2) {
      super(var1, var2);
   }

   public int getRuleIndex() {
      return 6;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> var1) {
      return (T)(var1 instanceof RuleParserVisitor ? ((RuleParserVisitor)var1).visitResource(this) : var1.visitChildren(this));
   }
}
