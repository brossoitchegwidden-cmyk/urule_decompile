package com.bstek.urule.runtime.service;

import com.bstek.urule.runtime.KnowledgePackage;

public interface KnowledgePackageFileService {
   boolean isEnable();

   KnowledgePackage loadKnowledgePackage(String packageId);

   /**通过与内存中缓存的知识包对象的时间戳与知识包ID对比，判断当前知识包有没有更新*/
   KnowledgePackage verifyKnowledgePackage(String packageId, long fileModifyDate);
}
