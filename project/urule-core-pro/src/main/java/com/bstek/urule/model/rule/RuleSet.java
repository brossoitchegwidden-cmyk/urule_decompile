package com.bstek.urule.model.rule;

import java.util.ArrayList;
import java.util.List;

public class RuleSet {
   private boolean alone;
   private boolean debug;
   private List<ParentFile> parents;
   private String remark;
   private String quickTestData;
   private List<Library> libraries;
   private List<Rule> rules;
   private PredefineGroupDefinition predefineGroup;

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String var1) {
      this.remark = var1;
   }

   public boolean isAlone() {
      return this.alone;
   }

   public void setAlone(boolean var1) {
      this.alone = var1;
   }

   public String getQuickTestData() {
      return this.quickTestData;
   }

   public void setQuickTestData(String var1) {
      this.quickTestData = var1;
   }

   public List<Library> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<Library> var1) {
      this.libraries = var1;
   }

   public void addLibrary(Library var1) {
      if (this.libraries == null) {
         this.libraries = new ArrayList<>();
      }

      this.libraries.add(var1);
   }

   public List<Rule> getRules() {
      return this.rules;
   }

   public void setRules(List<Rule> var1) {
      this.rules = var1;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public List<ParentFile> getParents() {
      return this.parents;
   }

   public void setParents(List<ParentFile> var1) {
      this.parents = var1;
   }

   public PredefineGroupDefinition getPredefineGroup() {
      return this.predefineGroup;
   }

   public void setPredefineGroup(PredefineGroupDefinition var1) {
      this.predefineGroup = var1;
   }
}
