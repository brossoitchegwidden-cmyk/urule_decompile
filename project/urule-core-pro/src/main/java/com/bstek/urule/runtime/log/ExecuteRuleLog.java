package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.Rule;
import java.util.Date;

public class ExecuteRuleLog extends DataLog {
   private static final String b = "----执行规则【%s】中动作，位于文件[%s]中，优先级为[%s]----";
   private static final String c = "----Execute the action in rule 【%s】, located in the file [%s], with priority of [%s]----";
   private Integer d;
   private String e;
   private String f;
   private Date g;
   private Date h;
   private Boolean i;
   private Boolean j;
   private String k;
   private String l;

   public ExecuteRuleLog(Rule var1) {
      this.e = var1.getName();
      this.f = var1.getFile();
      this.d = var1.getSalience();
      this.g = var1.getEffectiveDate();
      this.h = var1.getExpiresDate();
      this.i = var1.getEnabled();
      this.j = var1.getDebug();
      this.k = var1.getMutexGroup();
      this.l = var1.getPendedGroup();
      String var2 = this.a()
         ? "----Execute the action in rule 【%s】, located in the file [%s], with priority of [%s]----"
         : "----执行规则【%s】中动作，位于文件[%s]中，优先级为[%s]----";
      this.a = String.format(var2, var1.getName(), this.f, this.d);
   }

   public String getRuleFile() {
      return this.f;
   }

   public String getRuleName() {
      return this.e;
   }

   public Integer getSalience() {
      return this.d;
   }

   public String getActivationGroup() {
      return this.k;
   }

   public String getAgendaGroup() {
      return this.l;
   }

   public Boolean getDebug() {
      return this.j;
   }

   public Date getEffectiveDate() {
      return this.g;
   }

   public Boolean getEnabled() {
      return this.i;
   }

   public Date getExpiresDate() {
      return this.h;
   }

   @Override
   public String toString() {
      return "ExecuteRuleLog [salience=" + this.d + ", ruleName=" + this.e + ", ruleFile=" + this.f + "]";
   }
}
