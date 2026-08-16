package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTree;

public class CellScriptRuleParserBaseVisitor extends RuleParserBaseVisitor<String> {
   private String a;

   public CellScriptRuleParserBaseVisitor(String var1) {
      this.a = var1;
   }

   public String visitSingleCellCondition(RuleParserParser$SingleCellConditionContext var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(this.a);
      var2.append(" ");
      String var3 = var1.op().getText();
      var2.append(var3);
      var2.append(" ");
      if (var1.complexValue() != null) {
         var2.append(var1.complexValue().getText());
      } else {
         var2.append(var1.nullValue().getText());
      }

      var2.append(" ");
      return var2.toString();
   }

   public String visitMultiCellConditions(RuleParserParser$MultiCellConditionsContext var1) {
      StringBuffer var2 = new StringBuffer();

      for (ParseTree var5 : var1.children) {
         var2.append(" ");
         this.a(var2, var5);
      }

      return var2.toString();
   }

   public String visitParenCellConditions(RuleParserParser$ParenCellConditionsContext var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(" ");
      var2.append(var1.leftParen().getText());
      RuleParserParser$DecisionTableCellConditionContext var3 = var1.decisionTableCellCondition();
      this.a(var2, var3);
      var2.append(var1.rightParen().getText());
      return var2.toString();
   }

   private void a(StringBuffer var1, ParseTree var2) {
      if (var2 instanceof RuleParserParser$SingleCellConditionContext) {
         RuleParserParser$SingleCellConditionContext var3 = (RuleParserParser$SingleCellConditionContext)var2;
         var1.append(this.visitSingleCellCondition(var3));
      } else if (var2 instanceof RuleParserParser$ParenCellConditionsContext) {
         RuleParserParser$ParenCellConditionsContext var4 = (RuleParserParser$ParenCellConditionsContext)var2;
         var1.append(this.visitParenCellConditions(var4));
      } else if (var2 instanceof RuleParserParser$MultiCellConditionsContext) {
         RuleParserParser$MultiCellConditionsContext var5 = (RuleParserParser$MultiCellConditionsContext)var2;
         var1.append(this.visitMultiCellConditions(var5));
      } else {
         var1.append(var2.getText());
      }
   }
}
