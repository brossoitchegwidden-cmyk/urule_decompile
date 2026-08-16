package com.bstek.urule.runtime.log;

import com.bstek.urule.model.flow.FlowNode;

public class DecisionNodeMatchLog extends DataLog {
   private static final String b = "决策节点【%s】所有分支上的条件都不满足.";
   private static final String c = "None of the conditions on any of the branches of decision node 【%s】 are match.";
   private static final String d = "决策节点【%s】分支【%s】上的条件满足.";
   private static final String e = "The condition on the branch 【%s】 of the decision node 【a】 is satisfied.";
   private String f;
   private String g;
   private String h;

   public DecisionNodeMatchLog(FlowNode var1, String var2, String var3) {
      this.g = var1.getName();
      this.f = var2;
      this.h = var3;
      if (var3 == null) {
         String var4 = this.a() ? "None of the conditions on any of the branches of decision node 【%s】 are match." : "决策节点【%s】所有分支上的条件都不满足.";
         this.a = String.format(var4, this.g);
      } else if (this.a()) {
         this.a = String.format("The condition on the branch 【%s】 of the decision node 【a】 is satisfied.", var3, this.g);
      } else {
         this.a = String.format("决策节点【%s】分支【%s】上的条件满足.", this.g, var3);
      }
   }

   public String getFile() {
      return this.f;
   }

   public void setFile(String var1) {
      this.f = var1;
   }

   public String getNodeName() {
      return this.g;
   }

   public void setNodeName(String var1) {
      this.g = var1;
   }

   public String getTo() {
      return this.h;
   }

   public void setTo(String var1) {
      this.h = var1;
   }
}
