package com.bstek.urule.runtime;

import java.util.List;

public class BatchThread implements Runnable {
   private List<Business> businesses;
   private KnowledgeSession knowledgeSession;

   public BatchThread(KnowledgePackage knowledgePackage, List<Business> businesses) {
      this.knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
      this.businesses = businesses;
   }

   public BatchThread(KnowledgePackage[] knowledgePackages, List<Business> businesses) {
      this.knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackages);
      this.businesses = businesses;
   }

   @Override
   public void run() {
      Thread thread = Thread.currentThread();
      String name = thread.getName();
      thread.setName("urule-" + name);

      try {
         int number = this.businesses.size();

         for (int index = 0; index < number; index++) {
            Business business = this.businesses.get(index);
            business.execute(this.knowledgeSession);
         }
      } finally {
         thread.setName(name);
      }
   }
}
