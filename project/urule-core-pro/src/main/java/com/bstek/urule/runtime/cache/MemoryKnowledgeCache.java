package com.bstek.urule.runtime.cache;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryKnowledgeCache implements KnowledgeCache {
   private Map<String, KnowledgePackageData> a = new ConcurrentHashMap<>();

   protected MemoryKnowledgeCache() {
   }

   @Override
   public KnowledgePackage getKnowledge(String var1) {
      if (var1.startsWith("/")) {
         var1 = var1.substring(1, var1.length());
      }

      KnowledgePackageData var2 = this.a.get(var1);
      if (var2 == null) {
         return null;
      } else if (var2.isEnable()) {
         return var2.getKnowledgePackage();
      } else {
         throw new RuleException("Knowledge package [" + var1 + "] is disabled.");
      }
   }

   @Override
   public void putKnowledge(String var1, KnowledgePackage var2) {
      if (var1.startsWith("/")) {
         var1 = var1.substring(1, var1.length());
      }

      this.a.put(var1, new KnowledgePackageData(var2, true));
   }

   @Override
   public void enable(String var1, boolean var2) {
      if (var1.startsWith("/")) {
         var1 = var1.substring(1, var1.length());
      }

      KnowledgePackageData var3 = this.a.get(var1);
      if (var3 != null) {
         var3.setEnable(var2);
      }
   }

   @Override
   public void removeKnowledge(String var1) {
      this.a.remove(var1);
   }

   @Override
   public void clean() {
      this.a.clear();
   }
}
