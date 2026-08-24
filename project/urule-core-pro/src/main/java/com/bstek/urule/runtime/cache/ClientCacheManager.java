package com.bstek.urule.runtime.cache;

import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import com.bstek.urule.runtime.service.RemoteService;
import java.util.logging.Logger;

public class ClientCacheManager {
   private Logger logger = Logger.getLogger(ClientCacheManager.class.getName());
   private RemoteService remoteService;
   private DynamicSpringConfigLoader dynamicSpringConfigLoader;
   private RemoteDynamicJarsBuilder remoteDynamicJarsBuilder;

   public RemoteService getRemoteService() {
      return this.remoteService;
   }

   public void setRemoteService(RemoteService remoteService) {
      this.remoteService = remoteService;
   }

   public DynamicSpringConfigLoader getDynamicSpringConfigLoader() {
      return this.dynamicSpringConfigLoader;
   }

   public void setDynamicSpringConfigLoader(DynamicSpringConfigLoader dynamicSpringConfigLoader) {
      this.dynamicSpringConfigLoader = dynamicSpringConfigLoader;
   }

   public RemoteDynamicJarsBuilder getRemoteDynamicJarsBuilder() {
      return this.remoteDynamicJarsBuilder;
   }

   public void setRemoteDynamicJarsBuilder(RemoteDynamicJarsBuilder remoteDynamicJarsBuilder) {
      this.remoteDynamicJarsBuilder = remoteDynamicJarsBuilder;
   }

   public void enableKnowledge(String packetId) {
      CacheUtils.getKnowledgeCache().enable(packetId, true);
   }

   public void disableKnowledge(String packetId) {
      CacheUtils.getKnowledgeCache().enable(packetId, false);
   }

   public void reloadDynamicJars() {
      String dynamicJarsStoreDirectPath = this.dynamicSpringConfigLoader.buildDynamicJarsStoreDirectPath();

      try {
         boolean flag = this.remoteDynamicJarsBuilder.requestRemoteJars(dynamicJarsStoreDirectPath);
         if (!flag) {
            return;
         }

         this.dynamicSpringConfigLoader.loadDynamicJars(dynamicJarsStoreDirectPath);
      } catch (Exception exception) {
         java.util.logging.Logger.getLogger(ClientCacheManager.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
      }
   }

   public void reloadKnowledge(String packetId) {
      KnowledgePackage knowledge = CacheUtils.getKnowledgeCache().getKnowledge(packetId);
      KnowledgePackage knowledge2 = this.remoteService.getKnowledge(packetId, String.valueOf(knowledge.getTimestamp()));
      if (knowledge2 == null) {
         knowledge.resetTimestamp();
         CacheUtils.getKnowledgeCache().putKnowledge(packetId, knowledge);
      } else {
         this.logger.info("Update remote knowledgepackage.");
         knowledge2.resetTimestamp();
         CacheUtils.getKnowledgeCache().putKnowledge(packetId, knowledge2);
      }
   }
}
