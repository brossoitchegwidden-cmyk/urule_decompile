package com.bstek.urule.console.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class HttpSessionKnowledgeCache {
   private Map a = new HashMap();

   public Object get(HttpServletRequest var1, String var2) {
      SessionObject var3 = this.a(var1);
      return var3.get(var2);
   }

   public void put(HttpServletRequest var1, String var2, Object var3) {
      SessionObject var4 = this.a(var1);
      var4.put(var2, var3);
   }

   public void remove(HttpServletRequest var1, String var2) {
      SessionObject var3 = this.a(var1);
      var3.remove(var2);
   }

   private SessionObject a(HttpServletRequest var1) {
      this.a();
      String var2 = var1.getSession().getId();
      SessionObject var3 = null;
      if (this.a.containsKey(var2)) {
         var3 = (SessionObject)this.a.get(var2);
      } else {
         var3 = new SessionObject();
         this.a.put(var2, var3);
      }

      return var3;
   }

   private void a() {
      ArrayList var1 = new ArrayList();

      for(String var3 : (Iterable<String>)(Iterable<?>)(this.a.keySet())) {
         SessionObject var4 = (SessionObject)this.a.get(var3);
         if (var4.isExpired()) {
            var1.add(var3);
         }
      }

      for(String var6 : (Iterable<String>)(Iterable<?>)(var1)) {
         this.a.remove(var6);
      }

   }
}
