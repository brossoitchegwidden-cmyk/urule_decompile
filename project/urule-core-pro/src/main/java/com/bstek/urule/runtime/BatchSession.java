package com.bstek.urule.runtime;

public interface BatchSession {
   int DEFAULT_BATCH_SIZE = 100;
   int DEFAULT_THREAD_SIZE = 10;

   void addBusiness(Business var1);

   void waitForCompletion();
}
