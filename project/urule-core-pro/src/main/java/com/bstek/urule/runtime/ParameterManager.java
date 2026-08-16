package com.bstek.urule.runtime;

import com.bstek.urule.model.library.Datatype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParameterManager {
   private static ParameterManager a = new ParameterManager();

   private ParameterManager() {
   }

   public static ParameterManager getInstance() {
      return a;
   }

   public void initKnowledgePackageParameters(KnowledgePackage var1, Map<String, Object> var2) {
      Map var3 = var1.getParameters();
      if (var3 != null) {
         for (String var5 : (Iterable<String>)(Iterable<?>)(var3.keySet())) {
            Datatype var6 = Datatype.valueOf((String)var3.get(var5));
            if (var6.equals(Datatype.Integer)) {
               var2.put(var5, 0);
            } else if (var6.equals(Datatype.Long)) {
               var2.put(var5, 0);
            } else if (var6.equals(Datatype.Double)) {
               var2.put(var5, 0);
            } else if (var6.equals(Datatype.Float)) {
               var2.put(var5, 0);
            } else if (var6.equals(Datatype.Boolean)) {
               var2.put(var5, false);
            } else if (var6.equals(Datatype.List)) {
               var2.put(var5, new ArrayList());
            } else if (var6.equals(Datatype.Set)) {
               var2.put(var5, new HashSet());
            } else if (var6.equals(Datatype.Map)) {
               var2.put(var5, new HashMap());
            }
         }
      }
   }

   public void clearInitParameters(Map<String, Object> var1) {
      ArrayList var2 = new ArrayList();

      for (String var4 : var1.keySet()) {
         Object var5 = var1.get(var4);
         if (var5 != null) {
            if (var5 instanceof List) {
               ((List)var5).clear();
            } else if (var5 instanceof Set) {
               ((Set)var5).clear();
            } else if (var5 instanceof Map) {
               ((Map)var5).clear();
            } else if (var5 instanceof Number) {
               var1.put(var4, 0);
            } else if (var5 instanceof Boolean) {
               var1.put(var4, false);
            } else if (var5 instanceof String) {
               var2.add(var4);
            }
         }
      }

      for (String var7 : (Iterable<String>)(Iterable<?>)(var2)) {
         var1.remove(var7);
      }
   }
}
