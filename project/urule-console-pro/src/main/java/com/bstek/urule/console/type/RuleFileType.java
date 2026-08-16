package com.bstek.urule.console.type;

public enum RuleFileType {
   Knowledge("知识包", "rule_knowledge", "知识包管理员"),
   Batch("批处理", "rule_batch", "批处理管理员"),
   Library("库", "rule_library", "库管理员"),
   VariableLibrary("变量库", "rule_library", "库管理员"),
   ActionLibrary("动作库", "rule_library", "库管理员"),
   ConstantLibrary("常量库", "rule_library", "库管理员"),
   ParameterLibrary("参数库", "rule_library", "库管理员"),
   RuleSet("决策集", "rule_ruleset", "决策集管理员"),
   DecisionTable("决策表", "rule_decisiontable", "决策表管理员"),
   CrossDecisionTable("交叉决策表", "rule_decisiontable", "决策表管理员"),
   DecisionTree("决策树", "rule_decisiontree", "决策树管理员"),
   Scorecard("评分卡", "rule_scorecard", "评分卡管理员"),
   ComplexScorecard("复杂评分卡", "rule_scorecard", "评分卡管理员"),
   Flow("决策流", "rule_flow", "决策流管理员"),
   ConditionTemplate("条件模版", "rule_conditiontemplate", "条件模版管理员"),
   ActionTemplate("动作模版", "rule_actiontemplate", "动作模版管理员"),
   General("通用目录", "rule_generaldir", "自定义目录管理员");

   private String a;
   private String b;
   private String c;

   private RuleFileType(String var3, String var4, String var5) {
      this.a = var3;
      this.b = var4;
      this.c = var5;
   }

   public String getLabel() {
      return this.a;
   }

   public String getModel() {
      return this.b;
   }

   public String getRole() {
      return this.c;
   }

   public static RuleFileType getRuleFileType(String var0) {
      for(RuleFileType var4 : values()) {
         if (var4.name().toLowerCase().equals(var0.toLowerCase())) {
            return var4;
         }
      }

      return null;
   }

   public static String getModel(String var0) {
      for(RuleFileType var4 : values()) {
         if (var4.name().toLowerCase().equals(var0.toLowerCase())) {
            return var4.b;
         }
      }

      return null;
   }
}
