package com.bstek.urule.runtime.cache;

import com.bstek.urule.runtime.KnowledgePackage;

public class KnowledgePackageData {
   private boolean a;
   private KnowledgePackage b;

   public KnowledgePackageData(KnowledgePackage var1, boolean var2) {
      this.b = var1;
      this.a = var2;
   }

   public KnowledgePackage getKnowledgePackage() {
      return this.b;
   }

   public boolean isEnable() {
      return this.a;
   }

   public void setEnable(boolean var1) {
      this.a = var1;
   }
}
