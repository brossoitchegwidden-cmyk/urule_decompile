package com.bstek.urule.console.type;

public enum ProjectRoleEnum {
   Manager("项目管理员"),
   User("项目普通成员"),
   Knowledge("知识包管理员"),
   Batch("批处理管理员"),
   Library("库管理员"),
   RuleSet("决策集管理员"),
   DecisionTable("决策表管理员"),
   DecisionTree("决策树管理员"),
   Scorecard("评分卡管理员"),
   Flow("决策流管理员"),
   ConditionTemplate("条件模版管理员"),
   ActionTemplate("动作模版管理员");

   private String name;

   private ProjectRoleEnum(String text) {
      this.name = text;
   }

   public String getName() {
      return this.name;
   }
}
