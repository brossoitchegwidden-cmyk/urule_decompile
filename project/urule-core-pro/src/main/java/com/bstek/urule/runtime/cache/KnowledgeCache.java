package com.bstek.urule.runtime.cache;

import com.bstek.urule.runtime.KnowledgePackage;

public interface KnowledgeCache {
   String BEAN_ID = "urule.knowledgeCache";

   KnowledgePackage getKnowledge(String packageId);

   void putKnowledge(String packageId, KnowledgePackage knowledgePackage);

   void removeKnowledge(String packageId);

   void enable(String packageId, boolean enable);

   void clean();
}
