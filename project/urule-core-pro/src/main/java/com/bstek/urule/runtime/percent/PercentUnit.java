package com.bstek.urule.runtime.percent;

import com.bstek.urule.model.flow.Branch;
import com.bstek.urule.model.flow.DecisionItem;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PercentUnit {
   private long total;
   private int unitId;
   private String flowId;
   private String decisionNodeName;
   private Map<String, Branch> branchesByPath = new ConcurrentHashMap<>();

   public Branch getBranch(DecisionItem item) {
      String text = this.flowId + "." + this.decisionNodeName + "." + item.getTo();
      if (this.branchesByPath.containsKey(text)) {
         return this.branchesByPath.get(text);
      }

      Branch branch = new Branch();
      branch.setName(item.getTo());
      branch.setPercent(item.getPercent());
      branch.setTotal(0L);
      this.branchesByPath.put(text, branch);
      return branch;
   }

   public int getUnitId() {
      return this.unitId;
   }

   public void setUnitId(int unitId) {
      this.unitId = unitId;
   }

   public long getTotal() {
      return this.total;
   }

   public void setTotal(long total) {
      this.total = total;
   }

   public String getFlowId() {
      return this.flowId;
   }

   public void setFlowId(String flowId) {
      this.flowId = flowId;
   }

   public String getDecisionNodeName() {
      return this.decisionNodeName;
   }

   public void setDecisionNodeName(String decisionNodeName) {
      this.decisionNodeName = decisionNodeName;
   }

   public Collection<Branch> getBranchs() {
      return this.branchesByPath.values();
   }
}
