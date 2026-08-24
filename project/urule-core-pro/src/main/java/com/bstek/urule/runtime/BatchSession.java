package com.bstek.urule.runtime;

public interface BatchSession {
   int DEFAULT_BATCH_SIZE = 100;
   int DEFAULT_THREAD_SIZE = 10;

   /**添加一个具体要执行Business对象*/
   void addBusiness(Business business);

   /**等待线程池中所有业务线程执行完成，在进行批处理操作时一定要以此方法作为方法调用结尾*/
   void waitForCompletion();
}
