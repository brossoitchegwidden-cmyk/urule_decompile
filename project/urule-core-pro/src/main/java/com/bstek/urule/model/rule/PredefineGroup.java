package com.bstek.urule.model.rule;

import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

public class PredefineGroup {
   private Predefine predefine;
   @JsonIgnore
   private PredefineGroup parent;
   @JsonIgnore
   private boolean alone;
   private String filePath;
   @JsonIgnore
   private List<Rule> rules = new ArrayList<>();
   private PredefineGroup nextGroup;
   private KnowledgePackageWrapper knowledgePackageWrapper;

   public PredefineGroup() {
   }

   public PredefineGroup(boolean var1) {
      this.alone = var1;
   }

   public Predefine getPredefine() {
      return this.predefine;
   }

   public void setPredefine(Predefine var1) {
      this.predefine = var1;
   }

   public PredefineGroup getParent() {
      return this.parent;
   }

   public void setParent(PredefineGroup var1) {
      this.parent = var1;
   }

   public List<Rule> getRules() {
      return this.rules;
   }

   public void setRules(List<Rule> var1) {
      this.rules = var1;
   }

   public PredefineGroup getNextGroup() {
      return this.nextGroup;
   }

   public void setNextGroup(PredefineGroup var1) {
      this.nextGroup = var1;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   public void resetKnowledgePackageWrapper(KnowledgePackageWrapper var1) {
      this.knowledgePackageWrapper = var1;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper var1) {
      var1.buildDeserialize();
      this.knowledgePackageWrapper = var1;
   }

   public boolean isAlone() {
      return this.alone;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public void setFilePath(String var1) {
      this.filePath = var1;
   }
}
