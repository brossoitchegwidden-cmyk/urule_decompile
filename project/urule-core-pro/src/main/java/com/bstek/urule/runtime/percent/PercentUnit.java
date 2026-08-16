package com.bstek.urule.runtime.percent;

import com.bstek.urule.model.flow.Branch;
import com.bstek.urule.model.flow.DecisionItem;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PercentUnit {
   private long a;
   private int b;
   private String c;
   private String d;
   private Map<String, Branch> e = new ConcurrentHashMap<>();

   public Branch getBranch(DecisionItem var1) {
      String var2 = this.c + "." + this.d + "." + var1.getTo();
      if (this.e.containsKey(var2)) {
         return this.e.get(var2);
      }

      Branch var3 = new Branch();
      var3.setName(var1.getTo());
      var3.setPercent(var1.getPercent());
      var3.setTotal(0L);
      this.e.put(var2, var3);
      return var3;
   }

   public int getUnitId() {
      return this.b;
   }

   public void setUnitId(int var1) {
      this.b = var1;
   }

   public long getTotal() {
      return this.a;
   }

   public void setTotal(long var1) {
      this.a = var1;
   }

   public String getFlowId() {
      return this.c;
   }

   public void setFlowId(String var1) {
      this.c = var1;
   }

   public String getDecisionNodeName() {
      return this.d;
   }

   public void setDecisionNodeName(String var1) {
      this.d = var1;
   }

   public Collection<Branch> getBranchs() {
      return this.e.values();
   }
}
