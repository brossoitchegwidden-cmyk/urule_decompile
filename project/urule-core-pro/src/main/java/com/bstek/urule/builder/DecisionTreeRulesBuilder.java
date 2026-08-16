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
   public RuleSet buildRules(DecisionTree var1, String var2) throws IOException {
      RuleSet var3 = new RuleSet();
      List var4 = var1.getLibraries();
      if (var4 != null) {
         for (Library var6 : (Iterable<Library>)(Iterable<?>)(var4)) {
            var3.addLibrary(var6);
         }
      }

      ArrayList var15 = new ArrayList();
      var15.add(var1.getVariableTreeNode());
      ArrayList var16 = new ArrayList();
      this.fetchActionTreeNodes(var15, var16);
      ArrayList var7 = new ArrayList();

      for (ActionTreeNode var9 : (Iterable<ActionTreeNode>)(Iterable<?>)(var16)) {
         Rule var10 = new Rule();
         var10.setFile(var2);
         var10.setDebug(var1.getDebug());
         var10.setEnabled(var1.getEnabled());
         var10.setEffectiveDate(var1.getEffectiveDate());
         var10.setExpiresDate(var1.getExpiresDate());
         var10.setSalience(var1.getSalience());
         var7.add(var10);
         var10.setName("tree-rule");
         Rhs var11 = new Rhs();
         var11.setActions(var9.getActions());
         var10.setRhs(var11);
         Lhs var12 = new Lhs();
         var10.setLhs(var12);
         And var13 = new And();
         var12.setCriterion(var13);
         ConditionTreeNode var14 = (ConditionTreeNode)var9.getParentNode();
         this.a(var13, var14);
      }

      var3.setRules(var7);
      return var3;
   }

   private void a(And var1, ConditionTreeNode var2) {
      if (var2 != null) {
         ArrayList var3 = new ArrayList();
         var3.add(var2);
         VariableTreeNode var4 = null;
         TreeNode var5 = var2.getParentNode();

         while (var5 != null) {
            if (var5 instanceof VariableTreeNode) {
               var4 = (VariableTreeNode)var5;
               this.a(var1, (ConditionTreeNode)var5.getParentNode());
               break;
            }

            if (var5 instanceof ConditionTreeNode) {
               ConditionTreeNode var6 = (ConditionTreeNode)var5;
               var3.add(var6);
               var5 = var6.getParentNode();
            }
         }

         if (var4 == null) {
            throw new RuleException("Decision tree is invalid.");
         }

         for (ConditionTreeNode var7 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var3)) {
            var1.addCriterion(this.a(var7, var4));
         }
      }
   }

   private Criteria a(ConditionTreeNode var1, VariableTreeNode var2) {
      Criteria var3 = new Criteria();
      var3.setLeft(var2.getLeft());
      var3.setOp(var1.getOp());
      var3.setValue(var1.getValue());
      return var3;
   }

   public void fetchActionTreeNodes(List<? extends TreeNode> var1, List<ActionTreeNode> var2) {
      for (TreeNode var4 : var1) {
         if (var4 instanceof ActionTreeNode) {
            var2.add((ActionTreeNode)var4);
         } else if (var4 instanceof VariableTreeNode) {
            VariableTreeNode var5 = (VariableTreeNode)var4;
            List var6 = var5.getConditionTreeNodes();
            if (var6 != null) {
               this.fetchActionTreeNodes(var6, var2);
            }
         } else if (var4 instanceof ConditionTreeNode) {
            ConditionTreeNode var9 = (ConditionTreeNode)var4;
            List var10 = var9.getActionTreeNodes();
            if (var10 != null) {
               this.fetchActionTreeNodes(var10, var2);
            }

            List var7 = var9.getConditionTreeNodes();
            if (var7 != null) {
               this.fetchActionTreeNodes(var7, var2);
            }

            List var8 = var9.getVariableTreeNodes();
            if (var8 != null) {
               this.fetchActionTreeNodes(var8, var2);
            }
         }
      }
   }
}
