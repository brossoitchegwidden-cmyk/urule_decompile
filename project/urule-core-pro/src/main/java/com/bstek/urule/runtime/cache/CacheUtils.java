package com.bstek.urule.runtime.cache;

public class CacheUtils {
   public static KnowledgeCache getKnowledgeCache() {
      return CacheUtils$CacheInstance.a();
   }
}
