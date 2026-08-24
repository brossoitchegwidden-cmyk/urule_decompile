package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.Criteria;
import java.util.Date;
import java.util.Set;

public class MatchedRuleLog extends DataLog {
   private static final String RULE_S_IS_MATCHED_S = "√√Rule【%s】 is matched，(%s)";
   private static final String CONDITIONS = "，conditions：";
   private static final String CONDITIONS_NONE = "，conditions：none";
   private Integer salience;
   private String ruleName;
   private String ruleFile;
   private Date effectiveDate;
   private Date expiresDate;
   private Boolean enabled;
   private Boolean debug;
   private String activationGroup;
   private String agendaGroup;

   public MatchedRuleLog(Rule rule, Set<Criteria> criterias) {
      this.ruleName = rule.getName();
      this.ruleFile = rule.getFile();
      this.salience = rule.getSalience();
      this.effectiveDate = rule.getEffectiveDate();
      this.expiresDate = rule.getExpiresDate();
      this.enabled = rule.getEnabled();
      this.debug = rule.getDebug();
      this.activationGroup = rule.getMutexGroup();
      this.agendaGroup = rule.getPendedGroup();
      String text = this.isEnglishLanguage() ? "√√Rule【%s】 is matched，(%s)" : "√√规则【%s】匹配，(%s)";
      this.msg = String.format(text, rule.getName(), this.ruleFile);
      if (criterias.size() > 0) {
         this.msg = this.msg + (this.isEnglishLanguage() ? "，conditions：" : "，条件：" + this.joinCriteriaIds(criterias));
      } else {
         this.msg = this.msg + (this.isEnglishLanguage() ? "，conditions：none" : "，条件：无");
      }
   }

   private String joinCriteriaIds(Set<Criteria> criterias) {
      StringBuilder stringBuilder = new StringBuilder();

      for (Criteria criteria : criterias) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append("◆");
         }

         stringBuilder.append(criteria.getId());
      }

      return stringBuilder.toString();
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
      return "MatchedRuleLog [ruleName=" + this.ruleName + ", ruleFile=" + this.ruleFile + ", salience=" + this.salience + "]";
   }
}
