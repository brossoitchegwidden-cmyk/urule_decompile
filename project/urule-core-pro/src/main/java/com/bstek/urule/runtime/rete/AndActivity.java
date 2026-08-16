package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.lhs.Criteria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AndActivity extends JoinActivity {
   @Override
   public Collection<FactTracker> enter(EvaluationContext var1, Object var2, FactTracker var3) {
      AndActivityState var4 = var1.getAndActivityState(this.a);
      Set var5 = var4.getTokensSet();
      var5.addAll(var3.getTokens());
      this.a(var3, var4);
      if (!var4.isPassed() && !this.a(var1)) {
         return null;
      }

      var4.setPassed(true);
      Set var6 = this.c(var3, var4);
      List var7 = this.b(var3, var4);
      ArrayList var8 = new ArrayList();

      for (Map var10 : (Iterable<Map>)(Iterable<?>)(var7)) {
         FactTracker var11 = new FactTracker();
         var11.setTokens(var5);
         var11.addFactMap(var10);
         var11.addCriterias(var6);
         List var12 = this.a(var1, var2, var11);
         if (var12 != null) {
            var8.addAll(var12);
         }
      }

      return var8;
   }

   private void a(FactTracker var1, AndActivityState var2) {
      Path var3 = var1.getCurrentPath();
      Map var4 = var2.getPathFactMaps();
      Map var5 = var2.getPathCriteriaMap();
      var5.put(var3, var1.getCriterias());
      Map var6 = var1.getFactMap();
      if (var6.size() > 0) {
         List var7 = null;
         if (var4.containsKey(var3)) {
            var7 = (List)var4.get(var3);
         } else {
            var7 = new ArrayList();
            var4.put(var3, var7);
         }

         var7.add(var6);
      }
   }

   private List<Map<String, Object>> b(FactTracker var1, AndActivityState var2) {
      Path var3 = var1.getCurrentPath();
      Map var4 = var2.getPathFactMaps();
      List var5 = new ArrayList();
      var5.add(var1.getFactMap());
      Iterator var6 = var4.keySet().iterator();

      while (var6.hasNext() && var5.size() != 0) {
         var5 = this.a(var3, var6, var5, var4);
      }

      return var5;
   }

   private List<Map<String, Object>> a(Path var1, Iterator<Path> var2, List<Map<String, Object>> var3, Map<Path, List<Map<String, Object>>> var4) {
      Path var5 = (Path)var2.next();
      if (var5 == var1) {
         return var3;
      }

      ArrayList var6 = new ArrayList();
      List var7 = (List)var4.get(var5);

      for (Map var9 : var3) {
         for (Map var11 : (Iterable<Map>)(Iterable<?>)(var7)) {
            boolean var12 = this.a(var9, var11);
            if (var12) {
               HashMap var13 = new HashMap();
               var13.putAll(var9);
               var13.putAll(var11);
               var6.add(var13);
            }
         }
      }

      return var6;
   }

   private boolean a(Map<String, Object> var1, Map<String, Object> var2) {
      boolean var3 = true;

      for (String var5 : var2.keySet()) {
         if (var1.containsKey(var5)) {
            Object var6 = var1.get(var5);
            Object var7 = var2.get(var5);
            if (var6 != var7) {
               var3 = false;
               break;
            }
         }
      }

      return var3;
   }

   private Set<Criteria> c(FactTracker var1, AndActivityState var2) {
      Map var3 = var2.getPathCriteriaMap();
      HashSet var4 = new HashSet();
      var4.addAll(var1.getCriterias());
      Path var5 = var1.getCurrentPath();

      for (Path var7 : (Iterable<Path>)(Iterable<?>)(var3.keySet())) {
         if (var7 != var5) {
            var4.addAll((Collection)var3.get(var7));
         }
      }

      return var4;
   }
}
