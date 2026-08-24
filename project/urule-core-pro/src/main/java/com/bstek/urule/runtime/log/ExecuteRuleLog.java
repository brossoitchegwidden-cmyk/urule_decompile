package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.Rule;
import java.util.Date;

public class ExecuteRuleLog extends DataLog {
   private Integer salience;
   private String ruleName;
   private String ruleFile;
   private Date effectiveDate;
   private Date expiresDate;
   private Boolean enabled;
   private Boolean debug;
   private String activationGroup;
   private String agendaGroup;

   public ExecuteRuleLog(Rule rule) {
      this.ruleName = rule.getName();
      this.ruleFile = rule.getFile();
      this.salience = rule.getSalience();
      this.effectiveDate = rule.getEffectiveDate();
      this.expiresDate = rule.getExpiresDate();
      this.enabled = rule.getEnabled();
      this.debug = rule.getDebug();
      this.activationGroup = rule.getMutexGroup();
      this.agendaGroup = rule.getPendedGroup();
      String text = this.isEnglishLanguage()
         ? "----Execute the action in rule 【%s】, located in the file [%s], with priority of [%s]----"
         : "----执行规则【%s】中动作，位于文件[%s]中，优先级为[%s]----";
      this.msg = String.format(text, rule.getName(), this.ruleFile, this.salience);
   }

   public String getRuleFile() {
      return this.ruleFile;
   }

   public String getRuleName() {
      return this.ruleName;
   }

   public Integer getSalience() {
      return this.salience;
   }

   public String getActivationGroup() {
      return this.activationGroup;
   }

   public String getAgendaGroup() {
      return this.agendaGroup;
   }

   public Boolean getDebug() {
      return this.debug;
   }

   public Date getEffectiveDate() {
      return this.effectiveDate;
   }

   public Boolean getEnabled() {
      return this.enabled;
   }

   public Date getExpiresDate() {
      return this.expiresDate;
   }

   @Override
   public String toString() {
      return "ExecuteRuleLog [salience=" + this.salience + ", ruleName=" + this.ruleName + ", ruleFile=" + this.ruleFile + "]";
   }
}
