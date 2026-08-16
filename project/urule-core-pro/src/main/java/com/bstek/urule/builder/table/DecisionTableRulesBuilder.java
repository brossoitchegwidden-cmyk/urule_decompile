package com.bstek.urule.builder.table;

import com.bstek.urule.action.AbstractAction;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.PredefineAssignAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.ColumnType;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.model.table.Row;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DecisionTableRulesBuilder {
   private CellContentBuilder a;

   public List<Rule> buildRules(DecisionTable var1, String var2) {
      ArrayList var3 = new ArrayList();
      List var4 = var1.getRows();
      Integer var5 = var1.getSalience();
      int var6 = var4.size();
      List var7 = var1.getColumns();

      for (int var8 = 0; var8 < var4.size(); var8++) {
         Row var9 = (Row)var4.get(var8);
         Rule var10 = new Rule();
         var10.setFile(var2);
         if (var5 != null) {
            int var11 = var5 + (var6 - var8);
            var10.setSalience(var11);
         }

         var10.setMutexGroup(var1.getMutexGroup());
         var10.setDebug(var1.getDebug());
         var10.setSalience(var1.getSalience());
         var10.setExpiresDate(var1.getExpiresDate());
         var10.setEffectiveDate(var1.getEffectiveDate());
         var10.setEnabled(var1.getEnabled());
         var10.setName("r" + var9.getNum());
         Lhs var22 = new Lhs();
         And var12 = new And();
         var10.setLhs(var22);
         Rhs var13 = new Rhs();
         var10.setRhs(var13);
         var3.add(var10);
         Value var14 = null;

         for (Column var16 : (Iterable<Column>)(Iterable<?>)(var7)) {
            Cell var17 = this.a(var1, var9.getNum(), var16.getNum());
            ColumnType var18 = var16.getType();
            switch (var18) {
               case Criteria:
                  Criterion var19 = this.a.buildCriterion(var17, var16);
                  if (var19 != null) {
                     var12.addCriterion(var19);
                  }
                  break;
               case ConsolePrint:
                  var14 = var17.getValue();
                  if (var14 != null) {
                     ConsolePrintAction var27 = new ConsolePrintAction();
                     var27.setPriority(1000 - var16.getNum());
                     var27.setValue(var14);
                     var13.addAction(var27);
                  }
                  break;
               case Assignment:
                  var14 = var17.getValue();
                  if (var14 != null) {
                     if (var16.isPredefine()) {
                        PredefineAssignAction var25 = new PredefineAssignAction();
                        var25.setPriority(1000 - var16.getNum());
                        var25.setValue(var14);
                        var25.setDatatype(var16.getPredefineDatatype());
                        var25.setUuid(var16.getUuid());
                        var25.setName(var16.getPredefineName());
                        var25.setVariableCategoryUuid(var16.getPredefineVariableCategoryUuid());
                        var25.setVariableCategory(var16.getPredefineVariableCategory());
                        var25.setPropertyUuid(var16.getPredefinePropertyUuid());
                        var25.setPropertyName(var16.getPredefinePropertyName());
                        var25.setPropertyDatatype(var16.getPredefinePropertyDatatype());
                        var25.setPropertyLabel(var16.getPredefinePropertyLabel());
                        var13.addAction(var25);
                     } else {
                        VariableAssignAction var26 = new VariableAssignAction();
                        var26.setPriority(1000 - var16.getNum());
                        var26.setValue(var14);
                        var26.setDatatype(var16.getDatatype());
                        var26.setVariableName(var16.getVariableName());
                        var26.setCategoryUuid(var16.getCategoryUuid());
                        var26.setUuid(var16.getUuid());
                        var26.setVariableLabel(var16.getVariableLabel());
                        var26.setVariableCategory(var16.getVariableCategory());
                        var26.setKeyLabel(var16.getKeyLabel());
                        var26.setKeyName(var16.getKeyName());
                        var26.setKeyCategoryUuid(var16.getKeyCategoryUuid());
                        var26.setKeyUuid(var16.getKeyUuid());
                        var13.addAction(var26);
                     }
                  }
                  break;
               case ExecuteMethod:
                  Action var20 = var17.getAction();
                  if (var20 != null) {
                     AbstractAction var21 = (AbstractAction)var20;
                     var21.setPriority(1000 - var16.getNum());
                     var13.addAction(var21);
                  }
            }
         }

         if (var12.getCriterions() != null) {
            var22.setCriterion(var12);
         }
      }

      return var3;
   }

   private Cell a(DecisionTable var1, int var2, int var3) {
      Map var4 = var1.getCellMap();
      Cell var5 = null;

      for (int var6 = var2; var6 > -1; var6--) {
         String var7 = var1.buildCellKey(var6, var3);
         if (var4.containsKey(var7)) {
            var5 = (Cell)var4.get(var7);
            break;
         }
      }

      if (var5 == null) {
         throw new RuleException("Decision table cell[" + var2 + "," + var3 + "] not exist.");
      } else {
         return var5;
      }
   }

   public void setCellContentBuilder(CellContentBuilder var1) {
      this.a = var1;
   }
}
