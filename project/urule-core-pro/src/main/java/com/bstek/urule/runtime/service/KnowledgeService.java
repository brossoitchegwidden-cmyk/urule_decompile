package com.bstek.urule.runtime.service;

import com.bstek.urule.runtime.KnowledgePackage;
import java.io.IOException;

public interface KnowledgeService {
   String BEAN_ID = "urule.knowledgeService";

   /**根据给定的资源包ID获取对应的KnowledgePackage对象*/
   KnowledgePackage getKnowledge(String packageId) throws IOException;

   /**根据给定的一个或多个资源包ID获取对应的KnowledgePackage对象的集合*/
   KnowledgePackage[] getKnowledges(String[] packageIds) throws IOException;

   /**根据给定的资源包ID刷新缓存里的对应的KnowledgePackage对象*/
   void reloadKnowledge(String packageId) throws IOException;
}
