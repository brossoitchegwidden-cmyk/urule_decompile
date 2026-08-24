package com.bstek.urule.runtime.cache;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryKnowledgeCache implements KnowledgeCache {
   private Map<String, KnowledgePackageData> knowledgePackagesById = new ConcurrentHashMap<>();

   protected MemoryKnowledgeCache() {
   }

   @Override
   public KnowledgePackage getKnowledge(String packageId) {
      if (packageId.startsWith("/")) {
         packageId = packageId.substring(1, packageId.length());
      }

      KnowledgePackageData knowledgePackageData = this.knowledgePackagesById.get(packageId);
      if (knowledgePackageData == null) {
         return null;
      } else if (knowledgePackageData.isEnable()) {
         return knowledgePackageData.getKnowledgePackage();
      } else {
         throw new RuleException("Knowledge package [" + packageId + "] is disabled.");
      }
   }

   @Override
   public void putKnowledge(String packageId, KnowledgePackage knowledgePackage) {
      if (packageId.startsWith("/")) {
         packageId = packageId.substring(1, packageId.length());
      }

      this.knowledgePackagesById.put(packageId, new KnowledgePackageData(knowledgePackage, true));
   }

   @Override
   public void enable(String packageId, boolean enable) {
      if (packageId.startsWith("/")) {
         packageId = packageId.substring(1, packageId.length());
      }

      KnowledgePackageData knowledgePackageData = this.knowledgePackagesById.get(packageId);
      if (knowledgePackageData != null) {
         knowledgePackageData.setEnable(enable);
      }
   }

   @Override
   public void removeKnowledge(String packageId) {
      this.knowledgePackagesById.remove(packageId);
   }

   @Override
   public void clean() {
      this.knowledgePackagesById.clear();
   }
}
