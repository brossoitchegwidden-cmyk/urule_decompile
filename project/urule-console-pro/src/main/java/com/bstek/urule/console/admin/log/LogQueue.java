package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.database.model.URuleLog;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogQueue {
   private static BlockingQueue a = new LinkedBlockingQueue(10000);

   public static URuleLog pollLog() {
      return (URuleLog)a.poll();
   }

   protected static void a(URuleLog var0) throws InterruptedException {
      a.put(var0);
   }

   public static boolean isQueueEmpty() {
      return a.isEmpty();
   }
}
