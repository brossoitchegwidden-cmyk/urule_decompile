package com.bstek.urule.runtime.service;

import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.runtime.KnowledgePackage;
import java.io.IOException;

public interface KnowledgePackageService {
   String BEAN_ID = "urule.knowledgePackageService";

   KnowledgePackage buildKnowledgePackage(String packageInfo) throws IOException;

   KnowledgeBase buildKnowledgeBase(String packageInfo) throws IOException;

   KnowledgePackage verifyKnowledgePackage(String packageInfo, long timestamp) throws IOException;
}
