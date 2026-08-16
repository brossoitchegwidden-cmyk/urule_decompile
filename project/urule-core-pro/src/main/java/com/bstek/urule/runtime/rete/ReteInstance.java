package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rete.RuleData;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReteInstance {
   private List<RuleData> a;
   private String b = UUID.randomUUID().toString();
   private List<ObjectTypeActivity> c;
   private Map<String, List<ReteInstanceUnit>> d;
   private Map<String, List<ReteInstanceUnit>> e;

   public ReteInstance(List<ObjectTypeActivity> var1, Map<String, List<ReteInstanceUnit>> var2, Map<String, List<ReteInstanceUnit>> var3, List<RuleData> var4) {
      this.c = var1;
      this.d = var2;
      this.e = var3;
      this.a = var4;
   }

   public Collection<FactTracker> enter(EvaluationContext var1, Object var2) {
      Collection var3 = null;

      for (ObjectTypeActivity var5 : this.c) {
         if (var5.support(var2)) {
            var3 = var5.enter(var1, var2, new FactTracker());
            break;
         }
      }

      return var3;
   }

   public Map<String, List<ReteInstanceUnit>> getMutexGroupReteInstancesMap() {
      return this.d;
   }

   public Map<String, List<ReteInstanceUnit>> getPendedGroupReteInstancesMap() {
      return this.e;
   }

   public List<RuleData> getAllRuleData() {
      return this.a;
   }

   public String getId() {
      return this.b;
   }
}
