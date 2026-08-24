package com.bstek.urule.runtime;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.rete.Context;
import java.util.Base64;
import java.util.Map;

public class KnowledgeSessionFactory {
   private static long licenseLimit;
   private static boolean registered;

   protected static void resetReg(boolean reg) {
      registered = reg;
   }

   protected static void resetLimit(Map<?, ?> map) {
      licenseLimit = Long.valueOf(map.get(new String(Base64.getDecoder().decode("bGltaXQ=".getBytes()))).toString());
   }

   /**创建一个普通的KnowledgeSession对象*/
   public static KnowledgeSession newKnowledgeSession(KnowledgePackage knowledgePackage) {
      return new KnowledgeSessionImpl(knowledgePackage, registered, licenseLimit);
   }

   /**创建一个普通的KnowledgeSession对象，同时将父级KnowledgeSession传入*/
   public static KnowledgeSession newKnowledgeSession(KnowledgePackage knowledgePackage, KnowledgeSession parentSession) {
      return new KnowledgeSessionImpl(knowledgePackage, parentSession, registered, licenseLimit);
   }

   /**根据KnowledgePackageWrapper、Context以及父KnowledgeSession创建一个新的（或从缓存里取）KnowledgeSession对象*/
   public static KnowledgeSession newKnowledgeSession(KnowledgePackageWrapper wrapper, Context context, KnowledgeSession parentSession) {
      if (context == null) {
         throw new RuleException("Context cannot be null.");
      }

      if (wrapper == null) {
         throw new RuleException("KnowledgePackageWrapper cannot be null.");
      }

      String packageId = wrapper.getId();
      KnowledgeSession knowledgeSession = context.getWorkingMemory().getKnowledgeSession(packageId);
      if (knowledgeSession == null) {
         knowledgeSession = newKnowledgeSession(wrapper.getKnowledgePackage(), parentSession);
         context.getWorkingMemory().putKnowledgeSession(packageId, knowledgeSession);
      } else {
         knowledgeSession.initFromParentSession(parentSession);
      }

      return knowledgeSession;
   }

   /**创建一个普通的KnowledgeSession对象*/
   public static KnowledgeSession newKnowledgeSession(KnowledgePackage[] knowledgePackages) {
      return new KnowledgeSessionImpl(knowledgePackages, null, registered, licenseLimit);
   }

   /**创建一个用于批处理的BatchSession对象，这里默认将开启10个普通的线程池来运行提交的批处理任务，默认将每100个任务放在一个线程里处理*/
   public static BatchSession newBatchSession(KnowledgePackage knowledgePackage) {
      return new BatchSessionImpl(knowledgePackage, 10, 100);
   }

   /**创建一个用于批处理的BatchSession对象，第二个参数来指定线程池中可用线程个数，默认将每100个任务放在一个线程里处理*/
   public static BatchSession newBatchSessionByThreadSize(KnowledgePackage knowledgePackage, int threadSize) {
      return new BatchSessionImpl(knowledgePackage, threadSize, 100);
   }

   /**创建一个用于批处理的BatchSession对象，这里默认将开启10个普通的线程池来运行提交的批处理任务，第二个参数用来决定单个线程处理的任务数*/
   public static BatchSession newBatchSessionByBatchSize(KnowledgePackage knowledgePackage, int batchSize) {
      return new BatchSessionImpl(knowledgePackage, 10, batchSize);
   }

   /**创建一个用于批处理的BatchSession对象，第二个参数来指定线程池中可用线程个数，第三个参数用来决定单个线程处理的任务数*/
   public static BatchSession newBatchSession(KnowledgePackage knowledgePackage, int threadSize, int batchSize) {
      return new BatchSessionImpl(knowledgePackage, threadSize, batchSize);
   }

   /**创建一个用于批处理的BatchSession对象，这里默认将开启10个普通的线程池来运行提交的批处理任务，默认将每100个任务放在一个线程里处理*/
   public static BatchSession newBatchSession(KnowledgePackage[] knowledgePackages) {
      return new BatchSessionImpl(knowledgePackages, 10, 100);
   }

   /**创建一个用于批处理的BatchSession对象，第二个参数来指定线程池中可用线程个数，默认将每100个任务放在一个线程里处理*/
   public static BatchSession newBatchSessionByThreadSize(KnowledgePackage[] knowledgePackages, int threadSize) {
      return new BatchSessionImpl(knowledgePackages, threadSize, 100);
   }

   /**创建一个用于批处理的BatchSession对象，这里默认将开启10个普通的线程池来运行提交的批处理任务，第二个参数用来决定单个线程处理的任务数*/
   public static BatchSession newBatchSessionByBatchSize(KnowledgePackage[] knowledgePackages, int batchSize) {
      return new BatchSessionImpl(knowledgePackages, 10, batchSize);
   }

   /**创建一个用于批处理的BatchSession对象，第二个参数来指定线程池中可用线程个数，第三个参数用来决定单个线程处理的任务数*/
   public static BatchSession newBatchSession(KnowledgePackage[] knowledgePackages, int threadSize, int batchSize) {
      return new BatchSessionImpl(knowledgePackages, threadSize, batchSize);
   }
}
