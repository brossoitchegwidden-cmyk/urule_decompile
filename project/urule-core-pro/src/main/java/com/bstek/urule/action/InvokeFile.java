package com.bstek.urule.action;

import com.bstek.urule.runtime.KnowledgePackageWrapper;

public class InvokeFile {
   private long a;
   private String b;
   private String c;
   private KnowledgePackageWrapper d;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public String getPath() {
      return this.b;
   }

   public void setPath(String var1) {
      this.b = var1;
   }

   public String getVersion() {
      return this.c;
   }

   public void setVersion(String var1) {
      this.c = var1;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.d;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper var1) {
      this.d = var1;
   }
}
