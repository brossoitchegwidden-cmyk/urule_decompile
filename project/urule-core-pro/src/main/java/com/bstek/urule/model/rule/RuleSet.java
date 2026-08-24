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

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public boolean isAlone() {
      return this.alone;
   }

   public void setAlone(boolean alone) {
      this.alone = alone;
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

   public void addLibrary(Library library) {
      if (this.libraries == null) {
         this.libraries = new ArrayList<>();
      }

      this.libraries.add(library);
   }

   public List<Rule> getRules() {
      return this.rules;
   }

   public void setRules(List<Rule> rules) {
      this.rules = rules;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   public List<ParentFile> getParents() {
      return this.parents;
   }

   public void setParents(List<ParentFile> parents) {
      this.parents = parents;
   }

   public PredefineGroupDefinition getPredefineGroup() {
      return this.predefineGroup;
   }

   public void setPredefineGroup(PredefineGroupDefinition predefineGroup) {
      this.predefineGroup = predefineGroup;
   }
}
