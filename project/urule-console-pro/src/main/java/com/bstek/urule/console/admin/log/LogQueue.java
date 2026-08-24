package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.database.model.URuleLog;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogQueue {
   private static BlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(10000);

   public static URuleLog pollLog() {
      return (URuleLog)LogQueue.linkedBlockingQueue.poll();
   }

   protected static void putLog(URuleLog log) throws InterruptedException {
      LogQueue.linkedBlockingQueue.put(log);
   }

   public static boolean isQueueEmpty() {
      return LogQueue.linkedBlockingQueue.isEmpty();
   }
}
