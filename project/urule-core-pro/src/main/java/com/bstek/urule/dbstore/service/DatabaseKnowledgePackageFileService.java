package com.bstek.urule.dbstore.service;

import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.service.KnowledgePackageFileService;

public class DatabaseKnowledgePackageFileService implements KnowledgePackageFileService {
   private DbService dbService;

   public DatabaseKnowledgePackageFileService(DbService dbService) {
      this.dbService = dbService;
   }

   @Override
   public boolean isEnable() {
      return true;
   }

   @Override
   public KnowledgePackage loadKnowledgePackage(String packageId) {
      int number = packageId.lastIndexOf("/");
      if (number > 0) {
         packageId = packageId.replaceFirst("/", "#");
      }

      return this.dbService.loadKnowledgePackage(packageId);
   }
   @Override
   public KnowledgePackage verifyKnowledgePackage(String packageId, long fileModifyDate) {
      int number = packageId.lastIndexOf("/");
      if (number > 0) {
         packageId = packageId.replaceFirst("/", "#");
      }

      return this.dbService.verifyKnowledgePackage(packageId, fileModifyDate);
   }
}
