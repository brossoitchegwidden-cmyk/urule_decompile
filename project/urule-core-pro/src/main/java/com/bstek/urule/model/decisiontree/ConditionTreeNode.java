package com.bstek.urule.model.decisiontree;

import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.Value;
import java.util.List;

public class ConditionTreeNode extends TreeNode {
   private Value value;
   private Op op;
   private List<ConditionTreeNode> conditionTreeNodes;
   private List<VariableTreeNode> variableTreeNodes;
   private List<ActionTreeNode> actionTreeNodes;

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   public Op getOp() {
      return this.op;
   }

   public void setOp(Op op) {
      this.op = op;
   }

   public List<ConditionTreeNode> getConditionTreeNodes() {
      return this.conditionTreeNodes;
   }

   public void setConditionTreeNodes(List<ConditionTreeNode> conditionTreeNodes) {
      this.conditionTreeNodes = conditionTreeNodes;
   }

   public List<VariableTreeNode> getVariableTreeNodes() {
      return this.variableTreeNodes;
   }

   public void setVariableTreeNodes(List<VariableTreeNode> variableTreeNodes) {
      this.variableTreeNodes = variableTreeNodes;
   }

   public List<ActionTreeNode> getActionTreeNodes() {
      return this.actionTreeNodes;
   }

   public void setActionTreeNodes(List<ActionTreeNode> actionTreeNodes) {
      this.actionTreeNodes = actionTreeNodes;
   }
}
