package com.bstek.urule.dsl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class RuleParserParser$AssignActionContext extends ParserRuleContext {
   public RuleParserParser$ComplexValueContext complexValue() {
      return (RuleParserParser$ComplexValueContext)this.getRuleContext(RuleParserParser$ComplexValueContext.class, 0);
   }

   public RuleParserParser$VariableContext variable() {
      return (RuleParserParser$VariableContext)this.getRuleContext(RuleParserParser$VariableContext.class, 0);
   }

   public RuleParserParser$NamedVariableContext namedVariable() {
      return (RuleParserParser$NamedVariableContext)this.getRuleContext(RuleParserParser$NamedVariableContext.class, 0);
   }

   public RuleParserParser$ParameterContext parameter() {
      return (RuleParserParser$ParameterContext)this.getRuleContext(RuleParserParser$ParameterContext.class, 0);
   }

   public RuleParserParser$AssignActionContext(ParserRuleContext parserRuleContext, int number) {
      super(parserRuleContext, number);
   }

   public int getRuleIndex() {
      return 52;
   }

   public <T> T accept(ParseTreeVisitor<? extends T> parseTreeVisitor) {
      return (T)(parseTreeVisitor instanceof RuleParserVisitor ? ((RuleParserVisitor)parseTreeVisitor).visitAssignAction(this) : parseTreeVisitor.visitChildren(this));
   }
}
