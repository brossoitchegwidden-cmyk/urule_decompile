package com.bstek.urule.runtime.percent;

import com.bstek.urule.model.flow.DecisionItem;
import com.bstek.urule.model.flow.ProcessDefinition;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryPercentDataStore implements PercentDataStore {
   private Map<String, PercentUnit> a = new ConcurrentHashMap<>();

   @Override
   public PercentUnit getDecisionNodePercent(ProcessDefinition var1, List<DecisionItem> var2, String var3) {
      int var4 = this.a(var2);
      String var5 = this.a(var1, var3);
      if (this.a.containsKey(var5)) {
         PercentUnit var6 = this.a.get(var5);
         if (var6.getUnitId() == var4 && var6.getTotal() + 100L < Long.MAX_VALUE) {
            return var6;
         }

         this.a.remove(var5);
      }

      PercentUnit var7 = new PercentUnit();
      var7.setUnitId(var4);
      var7.setDecisionNodeName(var3);
      var7.setFlowId(var1.getId());
      var7.setTotal(0L);
      this.a.put(var5, var7);
      return var7;
   }

   private String a(ProcessDefinition var1, String var2) {
      return var1.getFile() == null ? "" : var1.getFile() + "." + var1.getId() + "." + var2;
   }

   private int a(List<DecisionItem> var1) {
      StringBuilder var2 = new StringBuilder();

      for (DecisionItem var4 : var1) {
         var2.append(var4.getTo() + var4.getPercent());
      }

      return var2.toString().hashCode();
   }
}
