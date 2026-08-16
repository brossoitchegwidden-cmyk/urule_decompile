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

   public boolean exist(Object var1, Long var2) {
      DecisionTree var3 = (DecisionTree)var1;
      List var4 = var3.getLibraries();
      if (var4 != null) {
         for(Library var6 : (Iterable<Library>)(Iterable<?>)(var4)) {
            if (var6.getId() == var2) {
               return true;
            }
         }
      }

      VariableTreeNode var10 = var3.getVariableTreeNode();
      List var11 = var10.getConditionTreeNodes();
      if (var11 != null) {
         for(ConditionTreeNode var8 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var11)) {
            boolean var9 = this.a(var8, var2);
            if (var9) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean existPacket(Object var1, Long var2, String var3) {
      DecisionTree var4 = (DecisionTree)var1;
      VariableTreeNode var5 = var4.getVariableTreeNode();
      List var6 = var5.getConditionTreeNodes();
      if (var6 != null) {
         for(ConditionTreeNode var8 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var6)) {
            boolean var9 = this.a(var8, var2, var3);
            if (var9) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean a(ConditionTreeNode var1, Long var2) {
      List var3 = var1.getActionTreeNodes();
      if (var3 != null) {
         for(ActionTreeNode var5 : (Iterable<ActionTreeNode>)(Iterable<?>)(var3)) {
            if (this.a((List)var5.getActions(), (Long)var2)) {
               return true;
            }
         }
      }

      List var11 = var1.getVariableTreeNodes();
      if (var11 != null) {
         for(VariableTreeNode var6 : (Iterable<VariableTreeNode>)(Iterable<?>)(var11)) {
            for(ConditionTreeNode var9 : var6.getConditionTreeNodes()) {
               boolean var10 = this.a(var9, var2);
               if (var10) {
                  return true;
               }
            }
         }
      }

      List var13 = var1.getConditionTreeNodes();
      if (var13 != null) {
         for(ConditionTreeNode var15 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var13)) {
            boolean var16 = this.a(var15, var2);
            if (var16) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean a(ConditionTreeNode var1, Long var2, String var3) {
      List var4 = var1.getActionTreeNodes();
      if (var4 != null) {
         for(ActionTreeNode var6 : (Iterable<ActionTreeNode>)(Iterable<?>)(var4)) {
            if (this.a(var6.getActions(), var2, var3)) {
               return true;
            }
         }
      }

      List var12 = var1.getVariableTreeNodes();
      if (var12 != null) {
         for(VariableTreeNode var7 : (Iterable<VariableTreeNode>)(Iterable<?>)(var12)) {
            for(ConditionTreeNode var10 : var7.getConditionTreeNodes()) {
               boolean var11 = this.a(var10, var2, var3);
               if (var11) {
                  return true;
               }
            }
         }
      }

      List var14 = var1.getConditionTreeNodes();
      if (var14 != null) {
         for(ConditionTreeNode var16 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var14)) {
            boolean var17 = this.a(var16, var2, var3);
            if (var17) {
               return true;
            }
         }
      }

      return false;
   }

   public FileReference build(Object var1) {
      DecisionTree var2 = (DecisionTree)var1;
      FileReference var3 = new FileReference();
      var3.setType(ResourceType.DecisionTree);
      ArrayList var4 = new ArrayList();
      var3.setChildren(var4);
      FileReference var5 = this.b(var2.getLibraries());
      if (var5 != null) {
         var4.add(var5);
      }

      VariableTreeNode var6 = var2.getVariableTreeNode();
      this.a((TreeNode)var6, (List)var4);
      return var3;
   }

   private void a(TreeNode var1, List var2) {
      if (var1 instanceof VariableTreeNode) {
         VariableTreeNode var3 = (VariableTreeNode)var1;

         for(ConditionTreeNode var6 : var3.getConditionTreeNodes()) {
            this.a((TreeNode)var6, (List)var2);
         }
      } else if (var1 instanceof ConditionTreeNode) {
         ConditionTreeNode var9 = (ConditionTreeNode)var1;
         List var11 = var9.getActionTreeNodes();
         if (var11 != null) {
            for(ActionTreeNode var16 : (Iterable<ActionTreeNode>)(Iterable<?>)(var11)) {
               this.a((TreeNode)var16, (List)var2);
            }
         }

         List var14 = var9.getConditionTreeNodes();
         if (var14 != null) {
            for(ConditionTreeNode var7 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var14)) {
               this.a((TreeNode)var7, (List)var2);
            }
         }

         List var18 = var9.getVariableTreeNodes();
         if (var18 != null) {
            for(VariableTreeNode var8 : (Iterable<VariableTreeNode>)(Iterable<?>)(var18)) {
               this.a((TreeNode)var8, (List)var2);
            }
         }
      } else if (var1 instanceof ActionTreeNode) {
         ActionTreeNode var10 = (ActionTreeNode)var1;
         List var12 = var10.getActions();
         List var15 = this.a(var12);
         if (var15 != null) {
            var2.addAll(var15);
         }
      }

   }

   public boolean support(Object var1) {
      return var1 instanceof DecisionTree;
   }
}
