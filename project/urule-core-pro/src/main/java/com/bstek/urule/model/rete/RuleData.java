package com.bstek.urule.model.rete;

import com.bstek.urule.model.rule.Rule;
import java.util.Date;

public class RuleData {
   private Integer salience;
   private String name;
   private String file;
   private Date effectiveDate;
   private Date expiresDate;
   private Boolean enabled;
   private Boolean debug;
   private String mutexGroup;
   private String pendedGroup;

   public RuleData(Rule rule) {
      this.name = rule.getName();
      this.file = rule.getFile();
      this.salience = rule.getSalience();
      this.effectiveDate = rule.getEffectiveDate();
      this.expiresDate = rule.getExpiresDate();
      this.enabled = rule.getEnabled();
      this.debug = rule.getDebug();
      this.mutexGroup = rule.getMutexGroup();
      this.pendedGroup = rule.getPendedGroup();
   }

   public String getName() {
      return this.name;
   }

   public String getFile() {
      return this.file;
   }

   public String getMutexGroup() {
      return this.mutexGroup;
   }

   public String getPendedGroup() {
      return this.pendedGroup;
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

   public Integer getSalience() {
      return this.salience;
   }

   @Override
   public String toString() {
      return "RuleData [name=" + this.name + ", file=" + this.file + "]";
   }
}
