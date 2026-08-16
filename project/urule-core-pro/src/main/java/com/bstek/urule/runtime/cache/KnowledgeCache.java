package com.bstek.urule.runtime.cache;

import com.bstek.urule.runtime.KnowledgePackage;

public interface KnowledgeCache {
   String BEAN_ID = "urule.knowledgeCache";

   KnowledgePackage getKnowledge(String var1);

   void putKnowledge(String var1, KnowledgePackage var2);

   void removeKnowledge(String var1);

   void enable(String var1, boolean var2);

   void clean();
}
