package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.model.decisiontree.ActionTreeNode;
import com.bstek.urule.model.decisiontree.ConditionTreeNode;
import com.bstek.urule.model.decisiontree.DecisionTree;
import com.bstek.urule.model.decisiontree.TreeNode;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.model.rule.Library;
import java.util.ArrayList;
import java.util.List;

public class DecisionTreeReference extends PacketSupportReference {
   protected DecisionTreeReference() {
   }

   public boolean exist(Object obj, Long fileId) {
      DecisionTree decisionTree = (DecisionTree)obj;
      List libraries = decisionTree.getLibraries();
      if (libraries != null) {
         for(Library library : (Iterable<Library>)(Iterable<?>)(libraries)) {
            if (library.getId() == fileId) {
               return true;
            }
         }
      }

      VariableTreeNode variableTreeNode = decisionTree.getVariableTreeNode();
      List conditionTreeNodes = variableTreeNode.getConditionTreeNodes();
      if (conditionTreeNodes != null) {
         for(ConditionTreeNode conditionTreeNode : (Iterable<ConditionTreeNode>)(Iterable<?>)(conditionTreeNodes)) {
            boolean flag = this.containsFileReferenceInTree(conditionTreeNode, fileId);
            if (flag) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean existPacket(Object obj, Long packetId, String code) {
      DecisionTree decisionTree = (DecisionTree)obj;
      VariableTreeNode variableTreeNode = decisionTree.getVariableTreeNode();
      List conditionTreeNodes = variableTreeNode.getConditionTreeNodes();
      if (conditionTreeNodes != null) {
         for(ConditionTreeNode conditionTreeNode : (Iterable<ConditionTreeNode>)(Iterable<?>)(conditionTreeNodes)) {
            boolean flag = this.containsPacketReferenceInTree(conditionTreeNode, packetId, code);
            if (flag) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean containsFileReferenceInTree(ConditionTreeNode conditionTreeNode, Long longValue) {
      List actionTreeNodes = conditionTreeNode.getActionTreeNodes();
      if (actionTreeNodes != null) {
         for(ActionTreeNode actionTreeNode : (Iterable<ActionTreeNode>)(Iterable<?>)(actionTreeNodes)) {
            if (this.containsFileReference((List)actionTreeNode.getActions(), (Long)longValue)) {
               return true;
            }
         }
      }

      List variableTreeNodes = conditionTreeNode.getVariableTreeNodes();
      if (variableTreeNodes != null) {
         for(VariableTreeNode variableTreeNode : (Iterable<VariableTreeNode>)(Iterable<?>)(variableTreeNodes)) {
            for(ConditionTreeNode conditionTreeNode2 : variableTreeNode.getConditionTreeNodes()) {
               boolean flag = this.containsFileReferenceInTree(conditionTreeNode2, longValue);
               if (flag) {
                  return true;
               }
            }
         }
      }

      List conditionTreeNodes = conditionTreeNode.getConditionTreeNodes();
      if (conditionTreeNodes != null) {
         for(ConditionTreeNode conditionTreeNode3 : (Iterable<ConditionTreeNode>)(Iterable<?>)(conditionTreeNodes)) {
            boolean flag2 = this.containsFileReferenceInTree(conditionTreeNode3, longValue);
            if (flag2) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean containsPacketReferenceInTree(ConditionTreeNode conditionTreeNode, Long longValue, String text) {
      List actionTreeNodes = conditionTreeNode.getActionTreeNodes();
      if (actionTreeNodes != null) {
         for(ActionTreeNode actionTreeNode : (Iterable<ActionTreeNode>)(Iterable<?>)(actionTreeNodes)) {
            if (this.containsPacketReference(actionTreeNode.getActions(), longValue, text)) {
               return true;
            }
         }
      }

      List variableTreeNodes = conditionTreeNode.getVariableTreeNodes();
      if (variableTreeNodes != null) {
         for(VariableTreeNode variableTreeNode : (Iterable<VariableTreeNode>)(Iterable<?>)(variableTreeNodes)) {
            for(ConditionTreeNode conditionTreeNode2 : variableTreeNode.getConditionTreeNodes()) {
               boolean flag = this.containsPacketReferenceInTree(conditionTreeNode2, longValue, text);
               if (flag) {
                  return true;
               }
            }
         }
      }

      List conditionTreeNodes = conditionTreeNode.getConditionTreeNodes();
      if (conditionTreeNodes != null) {
         for(ConditionTreeNode conditionTreeNode3 : (Iterable<ConditionTreeNode>)(Iterable<?>)(conditionTreeNodes)) {
            boolean flag2 = this.containsPacketReferenceInTree(conditionTreeNode3, longValue, text);
            if (flag2) {
               return true;
            }
         }
      }

      return false;
   }

   public FileReference build(Object obj) {
      DecisionTree decisionTree = (DecisionTree)obj;
      FileReference fileReference = new FileReference();
      fileReference.setType(ResourceType.DecisionTree);
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);
      FileReference fileReference2 = this.buildLibraryReferences(decisionTree.getLibraries());
      if (fileReference2 != null) {
         items.add(fileReference2);
      }

      VariableTreeNode variableTreeNode = decisionTree.getVariableTreeNode();
      this.collectTreeNodeReferences((TreeNode)variableTreeNode, (List)items);
      return fileReference;
   }

   private void collectTreeNodeReferences(TreeNode treeNode, List items) {
      if (treeNode instanceof VariableTreeNode) {
         VariableTreeNode variableTreeNode2 = (VariableTreeNode)treeNode;

         for(ConditionTreeNode conditionTreeNode : variableTreeNode2.getConditionTreeNodes()) {
            this.collectTreeNodeReferences((TreeNode)conditionTreeNode, (List)items);
         }
      } else if (treeNode instanceof ConditionTreeNode) {
         ConditionTreeNode conditionTreeNode2 = (ConditionTreeNode)treeNode;
         List actionTreeNodes = conditionTreeNode2.getActionTreeNodes();
         if (actionTreeNodes != null) {
            for(ActionTreeNode actionTreeNode : (Iterable<ActionTreeNode>)(Iterable<?>)(actionTreeNodes)) {
               this.collectTreeNodeReferences((TreeNode)actionTreeNode, (List)items);
            }
         }

         List conditionTreeNodes = conditionTreeNode2.getConditionTreeNodes();
         if (conditionTreeNodes != null) {
            for(ConditionTreeNode conditionTreeNode3 : (Iterable<ConditionTreeNode>)(Iterable<?>)(conditionTreeNodes)) {
               this.collectTreeNodeReferences((TreeNode)conditionTreeNode3, (List)items);
            }
         }

         List variableTreeNodes = conditionTreeNode2.getVariableTreeNodes();
         if (variableTreeNodes != null) {
            for(VariableTreeNode variableTreeNode : (Iterable<VariableTreeNode>)(Iterable<?>)(variableTreeNodes)) {
               this.collectTreeNodeReferences((TreeNode)variableTreeNode, (List)items);
            }
         }
      } else if (treeNode instanceof ActionTreeNode) {
         ActionTreeNode actionTreeNode2 = (ActionTreeNode)treeNode;
         List actions = actionTreeNode2.getActions();
         List items2 = this.buildActionReferences(actions);
         if (items2 != null) {
            items.addAll(items2);
         }
      }

   }

   public boolean support(Object obj) {
      return obj instanceof DecisionTree;
   }
}
