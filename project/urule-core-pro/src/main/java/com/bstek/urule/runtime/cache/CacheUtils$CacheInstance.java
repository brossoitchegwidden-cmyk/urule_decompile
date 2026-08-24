package com.bstek.urule.runtime.cache;

public class CacheUtils$CacheInstance {
   private static KnowledgeCache memoryKnowledgeCache = new MemoryKnowledgeCache();

   static KnowledgeCache getMemoryKnowledgeCache() {
      return CacheUtils$CacheInstance.memoryKnowledgeCache;
   }
}
