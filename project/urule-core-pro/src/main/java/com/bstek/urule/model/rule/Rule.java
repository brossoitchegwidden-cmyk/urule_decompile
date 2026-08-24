package com.bstek.urule.model.rule;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

public class Rule implements Comparable<Rule> {
   private String id;
   private String name;
   private String file;
   private Integer salience;
   private Date effectiveDate;
   private Date expiresDate;
   private Boolean enabled;
   private Boolean debug;
   @JsonIgnore
   private boolean debugFromGlobal;
   private String mutexGroup;
   private String pendedGroup;
   private Boolean autoFocus;
   private Lhs lhs;
   private Rhs rhs;
   private Other other;
   private Boolean loop;
   private Boolean loopRule = false;
   private String remark;
   private boolean withElse;
   @JsonIgnore
   private Rule elseRule;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getFile() {
      return this.file;
   }

   public void setFile(String file) {
      this.file = file;
   }

   public boolean isTargetResource(ResourceType type) {
      return this.file.startsWith(type.name() + ":");
   }

   public Date getEffectiveDate() {
      return this.effectiveDate;
   }

   public Integer getSalience() {
      return this.salience;
   }

   public void setSalience(Integer salience) {
      this.salience = salience;
   }

   public Boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(Boolean enabled) {
      this.enabled = enabled;
   }

   public Boolean getDebug() {
      return this.debug;
   }

   public void setDebug(Boolean debug) {
      this.debug = debug;
   }

   public boolean isDebugFromGlobal() {
      return this.debugFromGlobal;
   }

   public void setDebugFromGlobal(boolean debugFromGlobal) {
      this.debugFromGlobal = debugFromGlobal;
   }

   public Boolean getAutoFocus() {
      return this.autoFocus;
   }

   public void setAutoFocus(Boolean autoFocus) {
      this.autoFocus = autoFocus;
   }

   public void setEffectiveDate(Date effectiveDate) {
      this.effectiveDate = effectiveDate;
   }

   public Date getExpiresDate() {
      return this.expiresDate;
   }

   public void setExpiresDate(Date expiresDate) {
      this.expiresDate = expiresDate;
   }

   public String getMutexGroup() {
      return this.mutexGroup;
   }

   public void setMutexGroup(String mutexGroup) {
      this.mutexGroup = mutexGroup;
   }

   public String getPendedGroup() {
      return this.pendedGroup;
   }

   public void setPendedGroup(String pendedGroup) {
      this.pendedGroup = pendedGroup;
   }

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public Lhs getLhs() {
      return this.lhs;
   }

   public void setLhs(Lhs lhs) {
      this.lhs = lhs;
   }

   public Rhs getRhs() {
      return this.rhs;
   }

   public void setRhs(Rhs rhs) {
      this.rhs = rhs;
   }

   public Other getOther() {
      return this.other;
   }

   public void setOther(Other other) {
      this.other = other;
   }

   public Boolean getLoop() {
      return this.loop;
   }

   public void setLoop(Boolean loop) {
      this.loop = loop;
   }

   public Boolean isLoopRule() {
      return this.loopRule;
   }

   public void setLoopRule(Boolean loopRule) {
      this.loopRule = loopRule;
   }

   public boolean isWithElse() {
      return this.withElse;
   }

   public void setWithElse(boolean withElse) {
      this.withElse = withElse;
   }

   public Rule getElseRule() {
      return this.elseRule;
   }

   public void setElseRule(Rule elseRule) {
      this.elseRule = elseRule;
   }

   public int compareTo(Rule rule) {
      Integer salience = rule.getSalience();
      Integer salience2 = this.getSalience();
      if (salience != null && salience2 != null) {
         return salience - salience2;
      } else if (salience != null) {
         return 1;
      } else {
         return salience2 != null ? -1 : 0;
      }
   }
}
