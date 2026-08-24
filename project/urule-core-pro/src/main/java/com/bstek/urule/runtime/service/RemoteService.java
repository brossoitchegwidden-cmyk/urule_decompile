package com.bstek.urule.runtime.service;

import com.bstek.urule.runtime.KnowledgePackage;

public interface RemoteService {
   String BEAN_ID = "urule.remoteService";

   KnowledgePackage getKnowledge(String packageId, String timestamp);
}
