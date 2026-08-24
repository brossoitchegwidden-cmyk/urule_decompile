package com.bstek.urule.model.decisiontree;

import com.bstek.urule.model.rule.lhs.Left;
import java.util.List;

public class VariableTreeNode extends TreeNode {
   private Left left;
   private List<ConditionTreeNode> conditionTreeNodes;

   public Left getLeft() {
      return this.left;
   }

   public void setLeft(Left left) {
      this.left = left;
   }

   public List<ConditionTreeNode> getConditionTreeNodes() {
      return this.conditionTreeNodes;
   }

   public void setConditionTreeNodes(List<ConditionTreeNode> conditionTreeNodes) {
      this.conditionTreeNodes = conditionTreeNodes;
   }
}
