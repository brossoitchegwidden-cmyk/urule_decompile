package com.bstek.urule.builder;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.decisiontree.ActionTreeNode;
import com.bstek.urule.model.decisiontree.ConditionTreeNode;
import com.bstek.urule.model.decisiontree.DecisionTree;
import com.bstek.urule.model.decisiontree.TreeNode;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Lhs;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DecisionTreeRulesBuilder {
   public RuleSet buildRules(DecisionTree tree, String path) throws IOException {
      RuleSet ruleSet = new RuleSet();
      List libraries = tree.getLibraries();
      if (libraries != null) {
         for (Library library : (Iterable<Library>)(Iterable<?>)(libraries)) {
            ruleSet.addLibrary(library);
         }
      }

      ArrayList items = new ArrayList();
      items.add(tree.getVariableTreeNode());
      ArrayList items2 = new ArrayList();
      this.fetchActionTreeNodes(items, items2);
      ArrayList items3 = new ArrayList();

      for (ActionTreeNode actionTreeNode : (Iterable<ActionTreeNode>)(Iterable<?>)(items2)) {
         Rule rule = new Rule();
         rule.setFile(path);
         rule.setDebug(tree.getDebug());
         rule.setEnabled(tree.getEnabled());
         rule.setEffectiveDate(tree.getEffectiveDate());
         rule.setExpiresDate(tree.getExpiresDate());
         rule.setSalience(tree.getSalience());
         items3.add(rule);
         rule.setName("tree-rule");
         Rhs rhs = new Rhs();
         rhs.setActions(actionTreeNode.getActions());
         rule.setRhs(rhs);
         Lhs lhs = new Lhs();
         rule.setLhs(lhs);
         And and = new And();
         lhs.setCriterion(and);
         ConditionTreeNode parentNode = (ConditionTreeNode)actionTreeNode.getParentNode();
         this.appendPathCriteria(and, parentNode);
      }

      ruleSet.setRules(items3);
      return ruleSet;
   }

   private void appendPathCriteria(And and, ConditionTreeNode conditionTreeNode) {
      if (conditionTreeNode != null) {
         ArrayList items = new ArrayList();
         items.add(conditionTreeNode);
         VariableTreeNode variableTreeNode = null;
         TreeNode parentNode = conditionTreeNode.getParentNode();

         while (parentNode != null) {
            if (parentNode instanceof VariableTreeNode) {
               variableTreeNode = (VariableTreeNode)parentNode;
               this.appendPathCriteria(and, (ConditionTreeNode)parentNode.getParentNode());
               break;
            }

            if (parentNode instanceof ConditionTreeNode) {
               ConditionTreeNode conditionTreeNode2 = (ConditionTreeNode)parentNode;
               items.add(conditionTreeNode2);
               parentNode = conditionTreeNode2.getParentNode();
            }
         }

         if (variableTreeNode == null) {
            throw new RuleException("Decision tree is invalid.");
         }

         for (ConditionTreeNode conditionTreeNode3 : (Iterable<ConditionTreeNode>)(Iterable<?>)(items)) {
            and.addCriterion(this.buildCriteria(conditionTreeNode3, variableTreeNode));
         }
      }
   }

   private Criteria buildCriteria(ConditionTreeNode conditionTreeNode, VariableTreeNode variableTreeNode) {
      Criteria criteria = new Criteria();
      criteria.setLeft(variableTreeNode.getLeft());
      criteria.setOp(conditionTreeNode.getOp());
      criteria.setValue(conditionTreeNode.getValue());
      return criteria;
   }

   public void fetchActionTreeNodes(List<? extends TreeNode> nodes, List<ActionTreeNode> list) {
      for (TreeNode treeNode : nodes) {
         if (treeNode instanceof ActionTreeNode) {
            list.add((ActionTreeNode)treeNode);
         } else if (treeNode instanceof VariableTreeNode) {
            VariableTreeNode variableTreeNode = (VariableTreeNode)treeNode;
            List conditionTreeNodes = variableTreeNode.getConditionTreeNodes();
            if (conditionTreeNodes != null) {
               this.fetchActionTreeNodes(conditionTreeNodes, list);
            }
         } else if (treeNode instanceof ConditionTreeNode) {
            ConditionTreeNode conditionTreeNode = (ConditionTreeNode)treeNode;
            List actionTreeNodes = conditionTreeNode.getActionTreeNodes();
            if (actionTreeNodes != null) {
               this.fetchActionTreeNodes(actionTreeNodes, list);
            }

            List conditionTreeNodes2 = conditionTreeNode.getConditionTreeNodes();
            if (conditionTreeNodes2 != null) {
               this.fetchActionTreeNodes(conditionTreeNodes2, list);
            }

            List variableTreeNodes = conditionTreeNode.getVariableTreeNodes();
            if (variableTreeNodes != null) {
               this.fetchActionTreeNodes(variableTreeNodes, list);
            }
         }
      }
   }
}
