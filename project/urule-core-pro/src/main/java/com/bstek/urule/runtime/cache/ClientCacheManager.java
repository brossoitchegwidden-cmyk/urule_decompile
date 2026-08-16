package com.bstek.urule.runtime.cache;

import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import com.bstek.urule.runtime.service.RemoteService;
import java.util.logging.Logger;

public class ClientCacheManager {
   private Logger a = Logger.getLogger(ClientCacheManager.class.getName());
   private RemoteService b;
   private DynamicSpringConfigLoader c;
   private RemoteDynamicJarsBuilder d;

   public RemoteService getRemoteService() {
      return this.b;
   }

   public void setRemoteService(RemoteService var1) {
      this.b = var1;
   }

   public DynamicSpringConfigLoader getDynamicSpringConfigLoader() {
      return this.c;
   }

   public void setDynamicSpringConfigLoader(DynamicSpringConfigLoader var1) {
      this.c = var1;
   }

   public RemoteDynamicJarsBuilder getRemoteDynamicJarsBuilder() {
      return this.d;
   }

   public void setRemoteDynamicJarsBuilder(RemoteDynamicJarsBuilder var1) {
      this.d = var1;
   }

   public void enableKnowledge(String var1) {
      CacheUtils.getKnowledgeCache().enable(var1, true);
   }

   public void disableKnowledge(String var1) {
      CacheUtils.getKnowledgeCache().enable(var1, false);
   }

   public void reloadDynamicJars() {
      String var1 = this.c.buildDynamicJarsStoreDirectPath();

      try {
         boolean var2 = this.d.requestRemoteJars(var1);
         if (!var2) {
            return;
         }

         this.c.loadDynamicJars(var1);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public void reloadKnowledge(String var1) {
      KnowledgePackage var2 = CacheUtils.getKnowledgeCache().getKnowledge(var1);
      KnowledgePackage var3 = this.b.getKnowledge(var1, String.valueOf(var2.getTimestamp()));
      if (var3 == null) {
         var2.resetTimestamp();
         CacheUtils.getKnowledgeCache().putKnowledge(var1, var2);
      } else {
         this.a.info("Update remote knowledgepackage.");
         var3.resetTimestamp();
         CacheUtils.getKnowledgeCache().putKnowledge(var1, var3);
      }
   }
}
