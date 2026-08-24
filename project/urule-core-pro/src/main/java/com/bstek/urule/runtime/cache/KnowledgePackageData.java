package com.bstek.urule.runtime.cache;

import com.bstek.urule.runtime.KnowledgePackage;

public class KnowledgePackageData {
   private boolean enable;
   private KnowledgePackage knowledgePackage;

   public KnowledgePackageData(KnowledgePackage knowledgePackage, boolean enable) {
      this.knowledgePackage = knowledgePackage;
      this.enable = enable;
   }

   public KnowledgePackage getKnowledgePackage() {
      return this.knowledgePackage;
   }

   public boolean isEnable() {
      return this.enable;
   }

   public void setEnable(boolean enable) {
      this.enable = enable;
   }
}
