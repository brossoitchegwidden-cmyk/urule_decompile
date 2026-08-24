package com.bstek.urule.runtime.log;

import com.bstek.urule.model.flow.FlowNode;

public class DecisionNodeMatchLog extends DataLog {
   private String file;
   private String nodeName;
   private String to;

   public DecisionNodeMatchLog(FlowNode node, String file, String to) {
      this.nodeName = node.getName();
      this.file = file;
      this.to = to;
      if (to == null) {
         String text = this.isEnglishLanguage() ? "None of the conditions on any of the branches of decision node 【%s】 are match." : "决策节点【%s】所有分支上的条件都不满足.";
         this.msg = String.format(text, this.nodeName);
      } else if (this.isEnglishLanguage()) {
         this.msg = String.format("The condition on the branch 【%s】 of the decision node 【a】 is satisfied.", to, this.nodeName);
      } else {
         this.msg = String.format("决策节点【%s】分支【%s】上的条件满足.", this.nodeName, to);
      }
   }

   public String getFile() {
      return this.file;
   }

   public void setFile(String file) {
      this.file = file;
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public void setNodeName(String nodeName) {
      this.nodeName = nodeName;
   }

   public String getTo() {
      return this.to;
   }

   public void setTo(String to) {
      this.to = to;
   }
}
