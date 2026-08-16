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
   private boolean a = true;
   private long b;
   private RemoteService c;
   private KnowledgePackageService d;
   private KnowledgePackageFileService e;
   private Logger f = Logger.getLogger(KnowledgeServiceImpl.class.getName());

   @Override
   public KnowledgePackage[] getKnowledges(String[] var1) throws IOException {
      KnowledgePackage[] var2 = new KnowledgePackage[var1.length];

      for (int var3 = 0; var3 < var1.length; var3++) {
         String var4 = var1[var3];
         var2[var3] = this.getKnowledge(var4);
      }

      return var2;
   }

   @Override
   public void reloadKnowledge(String var1) throws IOException {
      KnowledgePackage var2 = this.a(var1);
      CacheUtils.getKnowledgeCache().putKnowledge(var1, var2);
   }

   @Override
   public KnowledgePackage getKnowledge(String var1) throws IOException {
      if (this.d != null) {
         return this.d.buildKnowledgePackage(var1);
      }

      if (this.b == 0L) {
         return this.a(var1);
      }

      KnowledgePackage var2 = CacheUtils.getKnowledgeCache().getKnowledge(var1);
      if (this.b != 1L) {
         if (var2 == null) {
            var2 = this.a(var1);
            CacheUtils.getKnowledgeCache().putKnowledge(var1, var2);
         } else {
            long var11 = var2.getTimestamp();
            if (this.e.isEnable()) {
               KnowledgePackage var13 = this.e.verifyKnowledgePackage(var1, var11);
               if (var13 != null) {
                  var2 = var13;
                  CacheUtils.getKnowledgeCache().putKnowledge(var1, var2);
                  return var2;
               }
            } else if (this.d != null) {
               KnowledgePackage var14 = this.d.verifyKnowledgePackage(var1, var11);
               if (var14 != null) {
                  return var14;
               }
            }

            long var15 = System.currentTimeMillis();
            long var7 = var15 - var11;
            if (var7 >= this.b) {
               KnowledgePackage var9 = this.c.getKnowledge(var1, String.valueOf(var2.getTimestamp()));
               if (var9 == null) {
                  var2.resetTimestamp();
                  CacheUtils.getKnowledgeCache().putKnowledge(var1, var2);
               } else {
                  this.f.info("Update remote knowledgepackage.");
                  var9.resetTimestamp();
                  var2 = var9;
                  CacheUtils.getKnowledgeCache().putKnowledge(var1, var9);
               }
            }
         }

         return var2;
      } else {
         if (var2 == null) {
            var2 = this.a(var1);
            CacheUtils.getKnowledgeCache().putKnowledge(var1, var2);
         } else {
            long var3 = var2.getTimestamp();
            if (this.e.isEnable() && this.a) {
               KnowledgePackage var12 = this.e.verifyKnowledgePackage(var1, var3);
               if (var12 != null) {
                  var2 = var12;
                  CacheUtils.getKnowledgeCache().putKnowledge(var1, var2);
               }
            } else if (this.d != null && this.a) {
               KnowledgePackage var5 = this.d.verifyKnowledgePackage(var1, var3);
               if (var5 != null) {
                  return var5;
               }
            }
         }

         return var2;
      }
   }

   private KnowledgePackage a(String var1) throws IOException {
      KnowledgePackage var2 = this.e.loadKnowledgePackage(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = this.c.getKnowledge(var1, null);
         if (var2 != null) {
            return var2;
         } else if (this.d != null) {
            return this.d.buildKnowledgePackage(var1);
         } else {
            throw new RuleException("Remote server/local repository/local data file all unavailable,can't load knowledgepackage[" + var1 + "]!");
         }
      }
   }

   public void setRemoteService(RemoteService var1) {
      this.c = var1;
   }

   public void setKnowledgeUpdateCycle(long var1) {
      System.out.println("urule.knowledgeUpdateCycle:" + var1);
      this.b = var1;
   }

   public void setKnowledgePackageFileService(KnowledgePackageFileService var1) {
      this.e = var1;
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      boolean var2 = var1.containsBean("urule.knowledgePackageService");
      if (var2) {
         this.d = (KnowledgePackageService)var1.getBean("urule.knowledgePackageService");
      }

      Collection var3 = var1.getBeansOfType(KnowledgePackageFileService.class).values();
      if (var3.size() == 1) {
         this.e = (KnowledgePackageFileService)var3.iterator().next();
      } else {
         for (KnowledgePackageFileService var5 : (Iterable<KnowledgePackageFileService>)(Iterable<?>)(var3)) {
            if (var5.isEnable()) {
               this.e = var5;
               break;
            }
         }

         if (this.e == null) {
            this.e = (KnowledgePackageFileService)var3.iterator().next();
         }
      }
   }

   public void setKnowledgeSyncCheck(boolean var1) {
      this.a = var1;
   }
}
