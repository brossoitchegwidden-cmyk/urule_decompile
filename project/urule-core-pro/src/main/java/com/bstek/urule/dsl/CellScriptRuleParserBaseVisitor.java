package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTree;

public class CellScriptRuleParserBaseVisitor extends RuleParserBaseVisitor<String> {
   private String propertyName;

   public CellScriptRuleParserBaseVisitor(String propertyName) {
      this.propertyName = propertyName;
   }
   public String visitSingleCellCondition(RuleParserParser$SingleCellConditionContext ctx) {
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append(this.propertyName);
      stringBuffer.append(" ");
      String text = ctx.op().getText();
      stringBuffer.append(text);
      stringBuffer.append(" ");
      if (ctx.complexValue() != null) {
         stringBuffer.append(ctx.complexValue().getText());
      } else {
         stringBuffer.append(ctx.nullValue().getText());
      }

      stringBuffer.append(" ");
      return stringBuffer.toString();
   }
   public String visitMultiCellConditions(RuleParserParser$MultiCellConditionsContext ctx) {
      StringBuffer stringBuffer = new StringBuffer();

      for (ParseTree parseTree : ctx.children) {
         stringBuffer.append(" ");
         this.buildChildren(stringBuffer, parseTree);
      }

      return stringBuffer.toString();
   }
   public String visitParenCellConditions(RuleParserParser$ParenCellConditionsContext ctx) {
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append(" ");
      stringBuffer.append(ctx.leftParen().getText());
      RuleParserParser$DecisionTableCellConditionContext ruleParserParser$DecisionTableCellConditionContext = ctx.decisionTableCellCondition();
      this.buildChildren(stringBuffer, ruleParserParser$DecisionTableCellConditionContext);
      stringBuffer.append(ctx.rightParen().getText());
      return stringBuffer.toString();
   }

   private void buildChildren(StringBuffer stringBuffer, ParseTree parseTree) {
      if (parseTree instanceof RuleParserParser$SingleCellConditionContext) {
         RuleParserParser$SingleCellConditionContext ruleParserParser$SingleCellConditionContext = (RuleParserParser$SingleCellConditionContext)parseTree;
         stringBuffer.append(this.visitSingleCellCondition(ruleParserParser$SingleCellConditionContext));
      } else if (parseTree instanceof RuleParserParser$ParenCellConditionsContext) {
         RuleParserParser$ParenCellConditionsContext ruleParserParser$ParenCellConditionsContext = (RuleParserParser$ParenCellConditionsContext)parseTree;
         stringBuffer.append(this.visitParenCellConditions(ruleParserParser$ParenCellConditionsContext));
      } else if (parseTree instanceof RuleParserParser$MultiCellConditionsContext) {
         RuleParserParser$MultiCellConditionsContext ruleParserParser$MultiCellConditionsContext = (RuleParserParser$MultiCellConditionsContext)parseTree;
         stringBuffer.append(this.visitMultiCellConditions(ruleParserParser$MultiCellConditionsContext));
      } else {
         stringBuffer.append(parseTree.getText());
      }
   }
}
