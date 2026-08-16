package com.bstek.urule.runtime.rete;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.EvaluateResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CriteriaActivity extends AbstractActivity {
   private boolean b;
   private Criteria c;

   public CriteriaActivity(Criteria var1, boolean var2) {
      this.c = var1;
      this.b = var2;
   }

   public List<FactTracker> enter(EvaluationContext var1, Object var2, FactTracker var3) {
      CriteriaActivityState var4 = var1.getActivityState(this.a);
      Set var5 = var4.getTokensSet();
      var5.addAll(var3.getTokens());
      this.b(var1, var2, var3);
      Set var6 = var4.getClassSet();
      if (!var4.isPassed() && this.c.necessaryClassEval(var6)) {
         var4.setPassed(true);
         var3.setTokens(var5);
         ArrayList var7 = new ArrayList();

         for (Map var10 : var4.getFactMapList()) {
            EvaluateResponse var11 = this.c.evaluate(var1, var10);
            if (this.b) {
               var1.getLogger().logCriteria(this.c, var11);
            }

            if (var11.getResult()) {
               FactTracker var12 = var3.newSubFactTracker();
               var12.addCriteria(this.c);
               var12.addFactMap(var10);
               List var13 = this.a(var1, var2, var12);
               if (var13 != null) {
                  var7.addAll(var13);
               }
            }
         }

         return var7;
      } else {
         return null;
      }
   }

   private void b(EvaluationContext var1, Object var2, FactTracker var3) {
      Map var4 = var3.getFactMap();
      CriteriaActivityState var5 = var1.getActivityState(this.a);
      Set var6 = var5.getClassSet();
      List var7 = var5.getFactMapList();
      if (var4.size() == 0) {
         String var8 = Utils.getClassName(var2);
         if (!var5.isPassed() && var6.contains(var8)) {
            HashMap var15 = new HashMap();
            var15.putAll((Map)var7.get(0));
            var15.put(var8, var2);
            var7.add(var15);
         } else {
            var6.add(var8);
            if (var7.size() == 0) {
               HashMap var9 = new HashMap();
               var9.put(var8, var2);
               var7.add(var9);
            } else {
               for (Map var10 : (Iterable<Map>)(Iterable<?>)(var7)) {
                  var10.put(var8, var2);
               }
            }
         }
      } else if (var6.size() == 0) {
         var6.addAll(var4.keySet());
         HashMap var12 = new HashMap();
         var12.putAll(var4);
         var7.add(var12);
      } else {
         for (String var16 : (Iterable<String>)(Iterable<?>)(var4.keySet())) {
            for (Map var11 : (Iterable<Map>)(Iterable<?>)(var7)) {
               var11.put(var16, var4.get(var16));
            }
         }
      }
   }
}
