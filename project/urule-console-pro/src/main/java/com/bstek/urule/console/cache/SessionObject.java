package com.bstek.urule.console.cache;

import java.util.HashMap;
import java.util.Map;

public class SessionObject {
   private long lastAccessTime = System.currentTimeMillis();
   private static final long EXPIRATION_MILLIS = 1200000L;
   private Map attributes = new HashMap();

   public void put(String key, Object obj) {
      this.lastAccessTime = System.currentTimeMillis();
      if (this.attributes.containsKey(key)) {
         this.attributes.remove(key);
      }

      this.attributes.put(key, obj);
   }

   public Object get(String key) {
      this.lastAccessTime = System.currentTimeMillis();
      return this.attributes.get(key);
   }

   public void remove(String key) {
      this.lastAccessTime = System.currentTimeMillis();
      this.attributes.remove(key);
   }

   public boolean isExpired() {
      long longValue = System.currentTimeMillis();
      long elapsedMillis = longValue - this.lastAccessTime;
      return elapsedMillis >= EXPIRATION_MILLIS;
   }
}
