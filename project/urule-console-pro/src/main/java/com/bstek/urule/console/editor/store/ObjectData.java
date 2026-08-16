package com.bstek.urule.console.editor.store;

import java.util.HashMap;
import java.util.Map;

class ObjectData {
   private Map a = new HashMap();
   private long b = System.currentTimeMillis();

   public String getObject(String var1) {
      return (String)this.a.get(var1);
   }

   public Object removeObject(String var1) {
      return this.a.remove(var1);
   }

   public void putObject(String var1, String var2) {
      this.a.put(var1, var2);
   }

   public boolean overdue() {
      long var1 = System.currentTimeMillis();
      long var3 = (var1 - this.b) / 1000L;
      return var3 >= 1200L;
   }
}
