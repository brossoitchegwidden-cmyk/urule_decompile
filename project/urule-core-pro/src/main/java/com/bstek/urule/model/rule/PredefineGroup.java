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

   public PredefineGroup(boolean alone) {
      this.alone = alone;
   }

   public Predefine getPredefine() {
      return this.predefine;
   }

   public void setPredefine(Predefine predefine) {
      this.predefine = predefine;
   }

   public PredefineGroup getParent() {
      return this.parent;
   }

   public void setParent(PredefineGroup parent) {
      this.parent = parent;
   }

   public List<Rule> getRules() {
      return this.rules;
   }

   public void setRules(List<Rule> rules) {
      this.rules = rules;
   }

   public PredefineGroup getNextGroup() {
      return this.nextGroup;
   }

   public void setNextGroup(PredefineGroup nextGroup) {
      this.nextGroup = nextGroup;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   /**此方法供代码中设置使用KnowledgePackageWrapper使用*/
   public void resetKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
      this.knowledgePackageWrapper = knowledgePackageWrapper;
   }

   /**此方法专供Jackson反序列化时自动填充时使用，因为反序列化时需要执行KnowledgePackageWrapper的buildDeserialize方法*/
   public void setKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
      knowledgePackageWrapper.buildDeserialize();
      this.knowledgePackageWrapper = knowledgePackageWrapper;
   }

   public boolean isAlone() {
      return this.alone;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public void setFilePath(String filePath) {
      this.filePath = filePath;
   }
}
