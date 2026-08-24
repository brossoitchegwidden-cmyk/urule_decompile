package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.Rule;
import java.util.Date;

public class AddRuleToExecuteQueueLog extends DataLog {
   private Integer salience;
   private String ruleName;
   private String ruleFile;
   private Date effectiveDate;
   private Date expiresDate;
   private Boolean enabled;
   private Boolean debug;
   private String activationGroup;
   private String agendaGroup;
   private boolean add;

   public AddRuleToExecuteQueueLog(Rule rule, boolean add) {
      this.ruleName = rule.getName();
      this.ruleFile = rule.getFile();
      this.salience = rule.getSalience();
      this.effectiveDate = rule.getEffectiveDate();
      this.expiresDate = rule.getExpiresDate();
      this.enabled = rule.getEnabled();
      this.debug = rule.getDebug();
      this.activationGroup = rule.getMutexGroup();
      this.agendaGroup = rule.getPendedGroup();
      this.add = add;
      String text = "";
      if (add) {
         text = this.isEnglishLanguage() ? "The rule 【%s】(%s) has been added to the execution queue" : "规则 【%s】(%s)，已被添加到执行队列";
      } else {
         text = this.isEnglishLanguage()
            ? "Rule 【%s】(%s)，which was not added to the execution queue because the current match operation occurred with a workspace update, and the current rule already exists in the execution queue!"
            : "规则 【%s】(%s)，未被添加到执行队列,因当前匹配操作在工作区更新情况下发生，且当前规则在执行队列中已存在！";
      }

      this.msg = "》》》" + String.format(text, rule.getName(), this.ruleFile);
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

   public boolean isAdd() {
      return this.add;
   }
}
