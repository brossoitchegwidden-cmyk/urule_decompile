package com.bstek.urule.console.database;

import com.bstek.urule.console.database.util.AcquireDbidAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IDGenerator {
   private static Log a = LogFactory.getLog(IDGenerator.class);
   private int b = 100;
   private static Random c = new Random();
   private Map d = new HashMap();
   private Map e = new HashMap();
   private int f = 5;
   private static IDGenerator g = new IDGenerator();

   public synchronized long nextId(IDType var1) {
      String var2 = "URULE_" + var1.name().toUpperCase();
      if (!this.d.containsKey(var2)) {
         this.d.put(var2, 0L);
      }

      if (!this.e.containsKey(var2)) {
         this.e.put(var2, -1L);
      }

      Long var3 = (Long)this.e.get(var2);
      Long var4 = (Long)this.d.get(var2);
      if (var3 < var4) {
         for(int var5 = this.f; var5 > 0; --var5) {
            try {
               var4 = AcquireDbidAction.execute(var2, this.b);
               var3 = var4 + (long)this.b - 1L;
               break;
            } catch (Exception var10) {
               --var5;
               if (var5 == 0) {
                  throw new IllegalStateException("couldn't acquire block of ids, tried " + this.f + " times, category is:" + var2);
               }

               int var7 = 20 + c.nextInt(200);
               a.debug("optimistic locking failure while trying to acquire id block.  retrying in " + var7 + " millis");

               try {
                  Thread.sleep((long)var7);
               } catch (InterruptedException var9) {
                  a.debug("waiting after id block locking failure got interrupted");
               }
            }
         }
      }

      this.e.put(var2, var3);
      this.d.put(var2, var4 + 1L);
      return var4;
   }

   public void clean() {
      this.e.clear();
      this.d.clear();
   }

   public static IDGenerator getInstance() {
      return g;
   }
}
