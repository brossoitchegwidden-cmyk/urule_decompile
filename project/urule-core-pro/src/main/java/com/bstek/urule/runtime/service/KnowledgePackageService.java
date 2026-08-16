package com.bstek.urule.runtime.service;

import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.runtime.KnowledgePackage;
import java.io.IOException;

public interface KnowledgePackageService {
   String BEAN_ID = "urule.knowledgePackageService";

   KnowledgePackage buildKnowledgePackage(String var1) throws IOException;

   KnowledgeBase buildKnowledgeBase(String var1) throws IOException;

   KnowledgePackage verifyKnowledgePackage(String var1, long var2) throws IOException;
}
