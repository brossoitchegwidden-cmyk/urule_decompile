package com.bstek.urule.console.editor.store;

import java.util.HashMap;
import java.util.Map;

class ObjectData {
   private Map valuesByName = new HashMap();
   private long lastAccessTime = System.currentTimeMillis();

   public String getObject(String text) {
      this.lastAccessTime = System.currentTimeMillis();
      return (String)this.valuesByName.get(text);
   }

   public Object removeObject(String text) {
      this.lastAccessTime = System.currentTimeMillis();
      return this.valuesByName.remove(text);
   }

   public void putObject(String text, String text2) {
      this.lastAccessTime = System.currentTimeMillis();
      this.valuesByName.put(text, text2);
   }

   public boolean overdue() {
      long currentTime = System.currentTimeMillis();
      long elapsedSeconds = (currentTime - this.lastAccessTime) / 1000L;
      return elapsedSeconds >= 1200L;
   }
}
