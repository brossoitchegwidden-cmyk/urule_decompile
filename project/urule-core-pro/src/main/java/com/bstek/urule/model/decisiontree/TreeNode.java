package com.bstek.urule.model.decisiontree;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class TreeNode {
   @JsonIgnore
   private TreeNode parentNode;
   private TreeNodeType nodeType;

   public void setParentNode(TreeNode parentNode) {
      this.parentNode = parentNode;
   }

   public TreeNode getParentNode() {
      return this.parentNode;
   }

   public TreeNodeType getNodeType() {
      return this.nodeType;
   }

   public void setNodeType(TreeNodeType nodeType) {
      this.nodeType = nodeType;
   }
}
