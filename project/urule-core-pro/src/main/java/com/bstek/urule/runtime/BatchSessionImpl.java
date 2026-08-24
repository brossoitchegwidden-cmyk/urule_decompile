package com.bstek.urule.runtime;

import com.bstek.urule.exception.RuleException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BatchSessionImpl implements BatchSession {
   private ExecutorService executorService;
   private int batchSize;
   private List<Business> business = new ArrayList<>();
   private KnowledgePackage knowledgePackage;
   private KnowledgePackage[] knowledgePackages;

   public BatchSessionImpl(KnowledgePackage knowledgePackage, int threadSize, int batchSize) {
      this.executorService = Executors.newFixedThreadPool(threadSize);
      this.knowledgePackage = knowledgePackage;
      this.batchSize = batchSize;
   }

   public BatchSessionImpl(KnowledgePackage[] knowledgePackages, int threadSize, int batchSize) {
      this.executorService = Executors.newFixedThreadPool(threadSize);
      this.knowledgePackages = knowledgePackages;
      this.batchSize = batchSize;
   }
   @Override
   public void addBusiness(Business business) {
      if (this.business != null) {
         if (this.business.size() >= this.batchSize) {
            this.initializeState();
            this.business = new ArrayList<>();
         }
      } else {
         this.business = new ArrayList<>();
      }

      this.business.add(business);
   }

   private void initializeState() {
      Runnable runnable = null;
      if (this.knowledgePackage != null) {
         runnable = new BatchThread(this.knowledgePackage, this.business);
      } else {
         if (this.knowledgePackages == null) {
            throw new RuleException("KnowledgePackage can not be null.");
         }

         runnable = new BatchThread(this.knowledgePackages, this.business);
      }

      this.executorService.execute(runnable);
      this.business = null;
   }
   @Override
   public void waitForCompletion() {
      if (this.business != null && this.business.size() > 0) {
         this.initializeState();
      }

      this.executorService.shutdown();

      try {
         while (!this.executorService.awaitTermination(300L, TimeUnit.MILLISECONDS)) {
         }
      } catch (InterruptedException interruptedException) {
         Thread.currentThread().interrupt();
         java.util.logging.Logger.getLogger(BatchSessionImpl.class.getName()).log(java.util.logging.Level.SEVERE, interruptedException.getMessage(), interruptedException);
         throw new RuleException(interruptedException);
      }
   }
}
