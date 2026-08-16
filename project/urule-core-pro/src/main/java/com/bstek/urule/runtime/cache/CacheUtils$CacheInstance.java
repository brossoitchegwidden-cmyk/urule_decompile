package com.bstek.urule.runtime.cache;

public class CacheUtils$CacheInstance {
   private static KnowledgeCache a = new MemoryKnowledgeCache();

   static KnowledgeCache a() {
      return a;
   }
}
