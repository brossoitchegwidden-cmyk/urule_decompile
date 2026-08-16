package com.bstek.urule.console.database.service.file;

import com.bstek.urule.console.database.model.RuleFile;
import java.util.HashMap;
import java.util.Map;

public class CopyContextHolder {
   private static final ThreadLocal a = new ThreadLocal();
   private static final ThreadLocal b = new ThreadLocal();

   public static Map getIdMap() {
      Object var0 = (Map)a.get();
      if (var0 == null) {
         var0 = new HashMap();
         a.set(var0);
      }

      return (Map)var0;
   }

   public static Map getFileMap() {
      Object var0 = (Map)b.get();
      if (var0 == null) {
         var0 = new HashMap();
         b.set(var0);
      }

      return (Map)var0;
   }

   public static void addId(long var0, long var2) {
      Map var4 = getIdMap();
      String var5 = "id=\"" + var0 + "\"";
      String var6 = "id=\"" + var2 + "\"";
      var4.put(var5, var6);
      var5 = "file=\"" + var0 + "\"";
      var6 = "file=\"" + var2 + "\"";
      var4.put(var5, var6);
   }

   public static void addFile(long var0, RuleFile var2) {
      Map var3 = getFileMap();
      var3.put(var0, var2);
   }

   public static void clear() {
      a.remove();
      b.remove();
   }
}
