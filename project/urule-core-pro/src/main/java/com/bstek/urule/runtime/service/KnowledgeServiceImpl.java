package com.bstek.urule.runtime.service;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.cache.CacheUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class KnowledgeServiceImpl implements KnowledgeService, ApplicationContextAware {
   private boolean knowledgeSyncCheck = true;
   private long knowledgeUpdateCycle;
   private RemoteService remoteService;
   private KnowledgePackageService knowledgePackageService;
   private KnowledgePackageFileService knowledgePackageFileService;
   private Logger logger = Logger.getLogger(KnowledgeServiceImpl.class.getName());
   @Override
   public KnowledgePackage[] getKnowledges(String[] packageIds) throws IOException {
      KnowledgePackage[] knowledgePackage = new KnowledgePackage[packageIds.length];

      for (int index = 0; index < packageIds.length; index++) {
         String text = packageIds[index];
         knowledgePackage[index] = this.getKnowledge(text);
      }

      return knowledgePackage;
   }
   @Override
   public void reloadKnowledge(String packageId) throws IOException {
      KnowledgePackage knowledgePackage = this.resolveKnowledgePackage(packageId);
      CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledgePackage);
   }
   @Override
   public KnowledgePackage getKnowledge(String packageId) throws IOException {
      if (this.knowledgePackageService != null) {
         return this.knowledgePackageService.buildKnowledgePackage(packageId);
      }

      if (this.knowledgeUpdateCycle == 0L) {
         return this.resolveKnowledgePackage(packageId);
      }

      KnowledgePackage knowledge = CacheUtils.getKnowledgeCache().getKnowledge(packageId);
      if (this.knowledgeUpdateCycle != 1L) {
         if (knowledge == null) {
            knowledge = this.resolveKnowledgePackage(packageId);
            CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledge);
         } else {
            long timestamp = knowledge.getTimestamp();
            if (this.knowledgePackageFileService.isEnable()) {
               KnowledgePackage knowledgePackage = this.knowledgePackageFileService.verifyKnowledgePackage(packageId, timestamp);
               if (knowledgePackage != null) {
                  knowledge = knowledgePackage;
                  CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledge);
                  return knowledge;
               }
            } else if (this.knowledgePackageService != null) {
               KnowledgePackage knowledge2 = this.knowledgePackageService.verifyKnowledgePackage(packageId, timestamp);
               if (knowledge2 != null) {
                  return knowledge2;
               }
            }

            long longValue = System.currentTimeMillis();
            long longValue2 = longValue - timestamp;
            if (longValue2 >= this.knowledgeUpdateCycle) {
               KnowledgePackage knowledge3 = this.remoteService.getKnowledge(packageId, String.valueOf(knowledge.getTimestamp()));
               if (knowledge3 == null) {
                  knowledge.resetTimestamp();
                  CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledge);
               } else {
                  this.logger.info("Update remote knowledgepackage.");
                  knowledge3.resetTimestamp();
                  knowledge = knowledge3;
                  CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledge3);
               }
            }
         }

         return knowledge;
      } else {
         if (knowledge == null) {
            knowledge = this.resolveKnowledgePackage(packageId);
            CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledge);
         } else {
            long timestamp2 = knowledge.getTimestamp();
            if (this.knowledgePackageFileService.isEnable() && this.knowledgeSyncCheck) {
               KnowledgePackage knowledgePackage2 = this.knowledgePackageFileService.verifyKnowledgePackage(packageId, timestamp2);
               if (knowledgePackage2 != null) {
                  knowledge = knowledgePackage2;
                  CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledge);
               }
            } else if (this.knowledgePackageService != null && this.knowledgeSyncCheck) {
               KnowledgePackage knowledge4 = this.knowledgePackageService.verifyKnowledgePackage(packageId, timestamp2);
               if (knowledge4 != null) {
                  return knowledge4;
               }
            }
         }

         return knowledge;
      }
   }

   private KnowledgePackage resolveKnowledgePackage(String text) throws IOException {
      KnowledgePackage knowledgePackage = this.knowledgePackageFileService.loadKnowledgePackage(text);
      if (knowledgePackage != null) {
         return knowledgePackage;
      } else {
         knowledgePackage = this.remoteService.getKnowledge(text, null);
         if (knowledgePackage != null) {
            return knowledgePackage;
         } else if (this.knowledgePackageService != null) {
            return this.knowledgePackageService.buildKnowledgePackage(text);
         } else {
            throw new RuleException("Remote server/local repository/local data file all unavailable,can't load knowledgepackage[" + text + "]!");
         }
      }
   }

   public void setRemoteService(RemoteService remoteService) {
      this.remoteService = remoteService;
   }

   public void setKnowledgeUpdateCycle(long knowledgeUpdateCycle) {
      System.out.println("urule.knowledgeUpdateCycle:" + knowledgeUpdateCycle);
      this.knowledgeUpdateCycle = knowledgeUpdateCycle;
   }

   /**供外部设置基于数据库存储知识包文件方式实现*/
   public void setKnowledgePackageFileService(KnowledgePackageFileService knowledgePackageFileService) {
      this.knowledgePackageFileService = knowledgePackageFileService;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      boolean flag = applicationContext.containsBean("urule.knowledgePackageService");
      if (flag) {
         this.knowledgePackageService = (KnowledgePackageService)applicationContext.getBean("urule.knowledgePackageService");
      }

      Collection knowledgePackageFileServices = applicationContext.getBeansOfType(KnowledgePackageFileService.class).values();
      if (knowledgePackageFileServices.size() == 1) {
         this.knowledgePackageFileService = (KnowledgePackageFileService)knowledgePackageFileServices.iterator().next();
      } else {
         for (KnowledgePackageFileService knowledgePackageFileService : (Iterable<KnowledgePackageFileService>)(Iterable<?>)(knowledgePackageFileServices)) {
            if (knowledgePackageFileService.isEnable()) {
               this.knowledgePackageFileService = knowledgePackageFileService;
               break;
            }
         }

         if (this.knowledgePackageFileService == null) {
            this.knowledgePackageFileService = (KnowledgePackageFileService)knowledgePackageFileServices.iterator().next();
         }
      }
   }

   public void setKnowledgeSyncCheck(boolean knowledgeSyncCheckValue) {
      this.knowledgeSyncCheck = knowledgeSyncCheckValue;
   }
}
