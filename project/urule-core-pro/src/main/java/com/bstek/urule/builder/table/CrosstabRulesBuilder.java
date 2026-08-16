package com.bstek.urule.builder.table;

import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.model.crosstab.ConditionCrossCell;
import com.bstek.urule.model.crosstab.CrossCell;
import com.bstek.urule.model.crosstab.CrossColumn;
import com.bstek.urule.model.crosstab.CrossRow;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.model.crosstab.LeftColumn;
import com.bstek.urule.model.crosstab.TopRow;
import com.bstek.urule.model.crosstab.ValueCrossCell;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.model.rule.lhs.PredefineLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.model.table.JointType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class CrosstabRulesBuilder {
   public List<Rule> buildRules(CrosstabDefinition var1, String var2) {
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      Map var6 = this.a(var1.getCells());
      List var7 = var1.getRows();
      int var8 = var7.size();
      List var9 = var1.getColumns();
      ArrayList var10 = new ArrayList();
      int var11 = 0;

      for (int var12 = 0; var12 < var8; var12++) {
         CrossRow var13 = (CrossRow)var7.get(var12);
         if (var13 instanceof TopRow) {
            var11++;
         }

         for (int var14 = 0; var14 < var9.size(); var14++) {
            Object var15 = null;
            CrossCell var16 = (CrossCell)var6.get(var12 + 1 + "," + (var14 + 1));
            if (var16 != null) {
               if (var13 instanceof TopRow) {
                  TopRow var17 = (TopRow)var13;
                  var15 = this.a(var16, var4, var17);
               } else {
                  CrossColumn var30 = (CrossColumn)var9.get(var14);
                  var15 = this.a(var16, var5, var30);
                  if (var16 instanceof ValueCrossCell) {
                     var10.add(var15);
                  }
               }
            }
         }
      }

      for (CellRange var24 : (Iterable<CellRange>)(Iterable<?>)(var10)) {
         CrossCell var25 = var24.getCell();
         Rule var28 = new Rule();
         var28.setFile(var2);
         var28.setDebug(var1.getDebug());
         var28.setSalience(var1.getSalience());
         var28.setExpiresDate(var1.getExpiresDate());
         var28.setEffectiveDate(var1.getEffectiveDate());
         var28.setEnabled(var1.getEnabled());
         var28.setName("rule(" + var25.getRow() + "," + var25.getCol() + ")");
         Lhs var29 = new Lhs();
         And var31 = new And();
         var28.setLhs(var29);
         Rhs var18 = new Rhs();
         var28.setRhs(var18);
         var3.add(var28);
         ValueCrossCell var19 = (ValueCrossCell)var25;
         Value var20 = var19.getValue();
         VariableAssignAction var21 = new VariableAssignAction();
         var21.setValue(var20);
         var21.setDatatype(var1.getAssignDatatype());
         var21.setVariableName(var1.getAssignVariable());
         var21.setVariableLabel(var1.getAssignVariableLabel());
         var21.setVariableCategory(var1.getAssignVariableCategory());
         var21.setKeyLabel(var1.getKeyLabel());
         var21.setKeyName(var1.getKeyName());
         var21.setCategoryUuid(var1.getCategoryUuid());
         var21.setUuid(var1.getUuid());
         var18.addAction(var21);
         this.a(var31, var24);
         if (var31.getCriterions() != null) {
            var29.setCriterion(var31);
         }

         CellRange var22 = this.a(var4, var24.getCell().getCol(), var11);
         var24.setParentRange(var22);
         this.a(var31, var24);
      }

      return var3;
   }

   private CellRange a(List<CellRange> var1, int var2, int var3) {
      CellRange var4 = null;

      for (CellRange var6 : var1) {
         if (var6.getStart() == var2 && var6.getEnd() == var2 && var6.getCell().getRow() == var3) {
            var4 = var6;
            break;
         }

         var4 = this.a(var6.getChildren(), var2, var3);
         if (var4 != null) {
            break;
         }
      }

      return var4;
   }

   private void a(And var1, CellRange var2) {
      CellRange var3 = var2.getParentRange();
      if (var3 != null) {
         ConditionCrossCell var4 = (ConditionCrossCell)var3.getCell();
         Criterion var5 = this.buildCriterion(var4, var3);
         if (var5 != null) {
            var1.addCriterion(var5);
         }

         this.a(var1, var3);
      }
   }

   public Criterion buildCriterion(ConditionCrossCell var1, CellRange var2) {
      Joint var3 = var1.getJoint();
      if (var3 == null) {
         return null;
      }

      List var4 = var3.getConditions();
      List var5 = var3.getJoints();
      if (var4 != null && var4.size() != 0 || var5 != null && var5.size() != 0) {
         Junction var6 = null;
         if (var4.size() == 1) {
            return this.a((Condition)var4.get(0), var2);
         }

         if (var3.getType().equals(JointType.and)) {
            var6 = new And();
         } else {
            var6 = new Or();
         }

         this.b(var4, var6, var2);
         this.a(var5, var6, var2);
         return var6;
      } else {
         return null;
      }
   }

   private void a(List<Joint> var1, Junction var2, CellRange var3) {
      if (var1 != null && var1.size() != 0) {
         for (Joint var5 : var1) {
            Junction var6 = var5.getJunction();
            List var7 = var5.getConditions();
            this.b(var7, var6, var3);
            List var8 = var5.getJoints();
            this.a(var8, var6, var3);
            var2.addCriterion(var6);
         }
      }
   }

   private void b(List<Condition> var1, Junction var2, CellRange var3) {
      if (var1 != null && var1.size() != 0) {
         for (Condition var5 : var1) {
            Criteria var6 = this.a(var5, var3);
            var2.addCriterion(var6);
         }
      }
   }

   private Criteria a(Condition var1, CellRange var2) {
      Criteria var3 = new Criteria();
      Left var4 = new Left();
      if (StringUtils.isNotBlank(var2.getPredefineUuid())) {
         PredefineLeftPart var5 = new PredefineLeftPart();
         var5.setUuid(var2.getPredefineUuid());
         var5.setName(var2.getPredefineName());
         var5.setVariableCategory(var2.getPredefineVariableCategory());
         var5.setVariableCategoryUuid(var2.getPredefineVariableCategoryUuid());
         var5.setPropertyUuid(var2.getPredefinePropertyUuid());
         var5.setPropertyName(var2.getPredefinePropertyName());
         var5.setPropertyLabel(var2.getPredefinePropertyLabel());
         var4.setLeftPart(var5);
         var4.setType(LeftType.predefine);
      } else {
         VariableLeftPart var6 = new VariableLeftPart();
         var6.setVariableCategory(var2.getVariableCategory());
         var6.setVariableName(var2.getVariableName());
         var6.setVariableLabel(var2.getVariableLabel());
         var6.setDatatype(var2.getDatatype());
         var6.setKeyLabel(var2.getKeyLabel());
         var6.setKeyName(var2.getKeyName());
         var6.setCategoryUuid(var2.getCategoryUuid());
         var6.setUuid(var2.getUuid());
         var4.setLeftPart(var6);
         var4.setType(LeftType.variable);
      }

      var3.setLeft(var4);
      var3.setOp(var1.getOp());
      var3.setValue(var1.getValue());
      return var3;
   }

   private CellRange a(CrossCell var1, List<CellRange> var2, TopRow var3) {
      int var4 = var1.getCol();
      int var5 = var1.getColspan();
      if (var5 > 0) {
         var5--;
      }

      int var6 = var4 + var5;
      CellRange var7 = new CellRange();
      var7.setStart(var4);
      var7.setEnd(var6);
      var7.setCell(var1);
      var7.setPredefineUuid(var3.getPredefineUuid());
      var7.setPredefineName(var3.getPredefineName());
      var7.setPredefineDatatype(var3.getPredefineDatatype());
      var7.setPredefineVariableCategory(var3.getPredefineVariableCategory());
      var7.setPredefineVariableCategoryUuid(var3.getPredefineVariableCategoryUuid());
      var7.setPredefinePropertyUuid(var3.getPredefinePropertyUuid());
      var7.setPredefinePropertyName(var3.getPredefinePropertyName());
      var7.setPredefinePropertyLabel(var3.getPredefinePropertyLabel());
      var7.setVariableCategory(var3.getVariableCategory());
      var7.setVariableName(var3.getVariableName());
      var7.setVariableLabel(var3.getVariableLabel());
      var7.setDatatype(var3.getDatatype());
      var7.setKeyLabel(var3.getKeyLabel());
      var7.setKeyName(var3.getKeyName());
      var7.setCategoryUuid(var3.getCategoryUuid());
      var7.setUuid(var3.getUuid());
      if (var4 == 1) {
         var2.add(var7);
      } else {
         CellRange var8 = this.a(var4, var6, var2);
         if (var8 != null) {
            var8.addChildRange(var7);
         } else {
            var2.add(var7);
         }
      }

      return var7;
   }

   private CellRange a(CrossCell var1, List<CellRange> var2, CrossColumn var3) {
      int var4 = var1.getRow();
      int var5 = var1.getRowspan();
      if (var5 > 0) {
         var5--;
      }

      int var6 = var4 + var5;
      CellRange var7 = new CellRange();
      var7.setStart(var4);
      var7.setEnd(var6);
      var7.setCell(var1);
      if (var3 instanceof LeftColumn) {
         LeftColumn var8 = (LeftColumn)var3;
         var7.setPredefineUuid(var8.getPredefineUuid());
         var7.setPredefineName(var8.getPredefineName());
         var7.setPredefineDatatype(var8.getPredefineDatatype());
         var7.setPredefineVariableCategory(var8.getPredefineVariableCategory());
         var7.setPredefineVariableCategoryUuid(var8.getPredefineVariableCategoryUuid());
         var7.setPredefinePropertyUuid(var8.getPredefinePropertyUuid());
         var7.setPredefinePropertyName(var8.getPredefinePropertyName());
         var7.setPredefinePropertyLabel(var8.getPredefinePropertyLabel());
         var7.setVariableCategory(var8.getVariableCategory());
         var7.setVariableName(var8.getVariableName());
         var7.setVariableLabel(var8.getVariableLabel());
         var7.setDatatype(var8.getDatatype());
         var7.setKeyLabel(var8.getKeyLabel());
         var7.setKeyName(var8.getKeyName());
         var7.setCategoryUuid(var8.getCategoryUuid());
         var7.setUuid(var8.getUuid());
      }

      if (var4 == 1) {
         var2.add(var7);
      } else {
         CellRange var9 = this.a(var4, var6, var2);
         if (var9 != null) {
            var9.addChildRange(var7);
         } else {
            var2.add(var7);
         }
      }

      return var7;
   }

   private CellRange a(int var1, int var2, List<CellRange> var3) {
      CellRange var4 = null;

      for (CellRange var6 : var3) {
         boolean var7 = false;
         if (!var6.isValueCell() && var6.getStart() <= var1 && var6.getEnd() >= var2) {
            var7 = true;
         }

         if (var7) {
            List var8 = var6.getChildren();
            if (var8.size() > 0) {
               var4 = this.a(var1, var2, var8);
            }

            if (var4 == null) {
               var4 = var6;
            }
            break;
         }
      }

      return var4;
   }

   private Map<String, CrossCell> a(List<CrossCell> var1) {
      HashMap var2 = new HashMap();

      for (CrossCell var4 : var1) {
         String var5 = var4.getRow() + "," + var4.getCol();
         var2.put(var5, var4);
      }

      return var2;
   }
}
