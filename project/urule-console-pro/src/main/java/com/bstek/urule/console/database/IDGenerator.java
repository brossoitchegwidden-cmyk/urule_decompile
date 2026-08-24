package com.bstek.urule.console.database;

import com.bstek.urule.console.database.util.AcquireDbidAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IDGenerator {
   private static Log logger = LogFactory.getLog(IDGenerator.class);
   private int allocationBlockSize = 100;
   private static Random random = new Random();
   private Map nextIdsByCategory = new HashMap();
   private Map blockEndIdsByCategory = new HashMap();
   private int maximumAllocationAttempts = 5;
   private static IDGenerator instance = new IDGenerator();

   public synchronized long nextId(IDType idType) {
      String text = "URULE_" + idType.name().toUpperCase();
      if (!this.nextIdsByCategory.containsKey(text)) {
         this.nextIdsByCategory.put(text, 0L);
      }

      if (!this.blockEndIdsByCategory.containsKey(text)) {
         this.blockEndIdsByCategory.put(text, -1L);
      }

      Long longValue = (Long)this.blockEndIdsByCategory.get(text);
      Long nextIdResult = (Long)this.nextIdsByCategory.get(text);
      if (longValue < nextIdResult) {
         for(int index = this.maximumAllocationAttempts; index > 0; --index) {
            try {
               nextIdResult = AcquireDbidAction.execute(text, this.allocationBlockSize);
               longValue = nextIdResult + (long)this.allocationBlockSize - 1L;
               break;
            } catch (Exception exception) {
               --index;
               if (index == 0) {
                  throw new IllegalStateException("couldn't acquire block of ids, tried " + this.maximumAllocationAttempts + " times, category is:" + text);
               }

               int number = 20 + IDGenerator.random.nextInt(200);
               IDGenerator.logger.debug("optimistic locking failure while trying to acquire id block.  retrying in " + number + " millis");

               try {
                  Thread.sleep((long)number);
               } catch (InterruptedException interruptedException) {
                  Thread.currentThread().interrupt();
                  IDGenerator.logger.debug("waiting after id block locking failure got interrupted");
                  break;
               }
            }
         }
      }

      this.blockEndIdsByCategory.put(text, longValue);
      this.nextIdsByCategory.put(text, nextIdResult + 1L);
      return nextIdResult;
   }

   public void clean() {
      this.blockEndIdsByCategory.clear();
      this.nextIdsByCategory.clear();
   }

   public static IDGenerator getInstance() {
      return IDGenerator.instance;
   }
}
