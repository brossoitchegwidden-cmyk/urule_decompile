package com.bstek.urule.console.editor.store;

import com.bstek.urule.console.RequestHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SessionClipboardStore implements ClipboardStore {
   private Map a = new HashMap();

   protected SessionClipboardStore() {
   }

   public void set(String var1, String var2) {
      this.a();
      String var3 = RequestHolder.getRequest().getSession().getId();
      ObjectData var4 = null;
      if (this.a.containsKey(var3)) {
         var4 = (ObjectData)this.a.get(var3);
      } else {
         var4 = new ObjectData();
         this.a.put(var3, var4);
      }

      var4.putObject(var1, var2);
   }

   public String get(String var1) {
      this.a();
      String var2 = RequestHolder.getRequest().getSession().getId();
      if (this.a.containsKey(var2)) {
         ObjectData var3 = (ObjectData)this.a.get(var2);
         return var3.getObject(var1);
      } else {
         return null;
      }
   }

   public void remove(String var1) {
      this.a();
      String var2 = RequestHolder.getRequest().getSession().getId();
      if (this.a.containsKey(var2)) {
         ObjectData var3 = (ObjectData)this.a.get(var2);
         var3.removeObject(var1);
      }

   }

   private void a() {
      ArrayList var1 = new ArrayList();

      for(String var3 : (Iterable<String>)(Iterable<?>)(this.a.keySet())) {
         ObjectData var4 = (ObjectData)this.a.get(var3);
         if (var4.overdue()) {
            var1.add(var3);
         }
      }

      for(String var6 : (Iterable<String>)(Iterable<?>)(var1)) {
         this.a.remove(var6);
      }

   }
}
