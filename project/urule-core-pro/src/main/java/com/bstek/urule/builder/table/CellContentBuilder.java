package com.bstek.urule.builder.table;

import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.model.rule.lhs.PredefineLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.scorecard.ComplexColumn;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.model.table.JointType;
import java.util.List;

public class CellContentBuilder {
   public Criterion buildCriterion(Cell var1, Column var2) {
      Joint var3 = var1.getJoint();
      if (var3 == null) {
         return null;
      }

      List var4 = var3.getConditions();
      List var5 = var3.getJoints();
      if (var4 != null && var4.size() != 0 || var5 != null && var5.size() != 0) {
         Junction var6 = null;
         if (var4.size() == 1) {
            return this.a(var2, (Condition)var4.get(0));
         }

         if (var3.getType().equals(JointType.and)) {
            var6 = new And();
         } else {
            var6 = new Or();
         }

         this.a(var4, var6, var2);
         this.a(var5, var2, var6);
         return var6;
      } else {
         return null;
      }
   }

   public Criterion buildCriterion(Cell var1, ComplexColumn var2) {
      Joint var3 = var1.getJoint();
      if (var3 == null) {
         return null;
      }

      List var4 = var3.getConditions();
      List var5 = var3.getJoints();
      if (var4 != null && var4.size() != 0 || var5 != null && var5.size() != 0) {
         Junction var6 = null;
         if (var4.size() == 1) {
            return this.a(var1, var2, (Condition)var4.get(0));
         }

         if (var3.getType().equals(JointType.and)) {
            var6 = new And();
         } else {
            var6 = new Or();
         }

         this.a(var1, var4, var6, var2);
         this.a(var1, var5, var2, var6);
         return var6;
      } else {
         return null;
      }
   }

   private void a(List<Joint> var1, Column var2, Junction var3) {
      if (var1 != null && var1.size() != 0) {
         for (Joint var5 : var1) {
            Junction var6 = var5.getJunction();
            List var7 = var5.getConditions();
            this.a(var7, var6, var2);
            List var8 = var5.getJoints();
            this.a(var8, var2, var6);
            var3.addCriterion(var6);
         }
      }
   }

   private void a(Cell var1, List<Joint> var2, ComplexColumn var3, Junction var4) {
      if (var2 != null && var2.size() != 0) {
         for (Joint var6 : var2) {
            Junction var7 = var6.getJunction();
            List var8 = var6.getConditions();
            this.a(var1, var8, var7, var3);
            List var9 = var6.getJoints();
            this.a(var1, var9, var3, var7);
            var4.addCriterion(var7);
         }
      }
   }

   private void a(List<Condition> var1, Junction var2, Column var3) {
      if (var1 != null && var1.size() != 0) {
         for (Condition var5 : var1) {
            Criteria var6 = this.a(var3, var5);
            var2.addCriterion(var6);
         }
      }
   }

   private void a(Cell var1, List<Condition> var2, Junction var3, ComplexColumn var4) {
      if (var2 != null && var2.size() != 0) {
         for (Condition var6 : var2) {
            Criteria var7 = this.a(var1, var4, var6);
            var3.addCriterion(var7);
         }
      }
   }

   private Criteria a(Column var1, Condition var2) {
      Criteria var3 = new Criteria();
      Left var4 = new Left();
      if (var1.isPredefine()) {
         PredefineLeftPart var5 = new PredefineLeftPart();
         var5.setDatatype(var1.getPredefineDatatype());
         var5.setUuid(var1.getUuid());
         var5.setName(var1.getPredefineName());
         var5.setVariableCategory(var1.getPredefineVariableCategory());
         var5.setVariableCategoryUuid(var1.getPredefineVariableCategoryUuid());
         var5.setPropertyUuid(var1.getPredefinePropertyUuid());
         var5.setPropertyName(var1.getPredefinePropertyName());
         var5.setPropertyLabel(var1.getPredefinePropertyLabel());
         var4.setLeftPart(var5);
         var4.setType(LeftType.predefine);
      } else {
         VariableLeftPart var6 = new VariableLeftPart();
         var6.setVariableCategory(var1.getVariableCategory());
         var6.setVariableName(var1.getVariableName());
         var6.setCategoryUuid(var1.getCategoryUuid());
         var6.setUuid(var1.getUuid());
         var6.setVariableLabel(var1.getVariableLabel());
         var6.setKeyLabel(var1.getKeyLabel());
         var6.setKeyName(var1.getKeyName());
         var6.setKeyUuid(var1.getKeyUuid());
         var6.setKeyCategoryUuid(var1.getKeyCategoryUuid());
         var6.setDatatype(var1.getDatatype());
         var4.setLeftPart(var6);
         var4.setType(LeftType.variable);
      }

      var3.setLeft(var4);
      var3.setOp(var2.getOp());
      var3.setValue(var2.getValue());
      return var3;
   }

   private Criteria a(Cell var1, ComplexColumn var2, Condition var3) {
      Criteria var4 = new Criteria();
      Left var5 = new Left();
      VariableLeftPart var6 = new VariableLeftPart();
      var6.setVariableCategory(var2.getVariableCategory());
      var6.setVariableLabel(var1.getVariableLabel());
      var6.setVariableName(var1.getVariableName());
      var6.setDatatype(var1.getDatatype());
      var6.setKeyLabel(var1.getKeyLabel());
      var6.setKeyUuid(var1.getKeyUuid());
      var6.setKeyCategoryUuid(var1.getKeyCategoryUuid());
      var6.setKeyName(var1.getKeyName());
      var6.setCategoryUuid(var2.getUuid());
      var6.setUuid(var1.getUuid());
      var5.setLeftPart(var6);
      var5.setType(LeftType.variable);
      var4.setLeft(var5);
      var4.setOp(var3.getOp());
      var4.setValue(var3.getValue());
      return var4;
   }
}
