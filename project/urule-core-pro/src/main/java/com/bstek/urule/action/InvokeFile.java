package com.bstek.urule.action;

import com.bstek.urule.runtime.KnowledgePackageWrapper;

public class InvokeFile {
   private long id;
   private String path;
   private String version;
   private KnowledgePackageWrapper knowledgePackageWrapper;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
      this.knowledgePackageWrapper = knowledgePackageWrapper;
   }
}
