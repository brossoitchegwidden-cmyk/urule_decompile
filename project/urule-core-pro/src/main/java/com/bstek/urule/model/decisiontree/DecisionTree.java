package com.bstek.urule.model.decisiontree;

import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import java.util.Date;
import java.util.List;

public class DecisionTree {
   private Integer salience;
   private Date effectiveDate;
   private Date expiresDate;
   private Boolean enabled;
   private Boolean debug;
   private String remark;
   private String quickTestData;
   private List<Library> libraries;
   private VariableTreeNode variableTreeNode;
   private PredefineGroupDefinition predefineGroup;

   public Integer getSalience() {
      return this.salience;
   }

   public void setSalience(Integer salience) {
      this.salience = salience;
   }

   public Date getEffectiveDate() {
      return this.effectiveDate;
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

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public String getQuickTestData() {
      return this.quickTestData;
   }

   public void setQuickTestData(String quickTestData) {
      this.quickTestData = quickTestData;
   }

   public List<Library> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<Library> libraries) {
      this.libraries = libraries;
   }

   public VariableTreeNode getVariableTreeNode() {
      return this.variableTreeNode;
   }

   public void setVariableTreeNode(VariableTreeNode variableTreeNode) {
      this.variableTreeNode = variableTreeNode;
   }

   public PredefineGroupDefinition getPredefineGroup() {
      return this.predefineGroup;
   }

   public void setPredefineGroup(PredefineGroupDefinition predefineGroup) {
      this.predefineGroup = predefineGroup;
   }
}
