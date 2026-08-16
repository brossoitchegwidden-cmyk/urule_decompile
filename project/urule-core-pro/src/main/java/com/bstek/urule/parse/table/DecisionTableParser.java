package com.bstek.urule.parse.table;

import com.bstek.urule.Configure;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableData;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.parse.LibrariesParser;
import com.bstek.urule.parse.PredefinesParser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class DecisionTableParser extends LibrariesParser<DecisionTable> {
   private RowParser a;
   private ColumnParser b;
   private CellParser c;
   private RulesRebuilder d;
   private PredefinesParser e;

   public DecisionTable parse(Element var1) {
      DecisionTable var2 = new DecisionTable();
      String var3 = var1.attributeValue("salience");
      if (StringUtils.isNotEmpty(var3)) {
         var2.setSalience(Integer.valueOf(var3));
      }

      var2.setMutexGroup(var1.attributeValue("mutex-group"));
      String var4 = var1.attributeValue("effective-date");
      SimpleDateFormat var5 = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(var4)) {
         try {
            var2.setEffectiveDate(var5.parse(var4));
         } catch (ParseException var16) {
            throw new RuleException(var16);
         }
      }

      String var6 = var1.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(var6)) {
         try {
            var2.setExpiresDate(var5.parse(var6));
         } catch (ParseException var15) {
            throw new RuleException(var15);
         }
      }

      String var7 = var1.attributeValue("enabled");
      if (StringUtils.isNotEmpty(var7)) {
         var2.setEnabled(Boolean.valueOf(var7));
      }

      String var8 = var1.attributeValue("debug");
      if (StringUtils.isNotEmpty(var8)) {
         var2.setDebug(Boolean.valueOf(var8));
      }

      for (Object var10 : var1.elements()) {
         if (var10 != null && var10 instanceof Element) {
            Element var11 = (Element)var10;
            String var12 = var11.getName();
            if (this.a.support(var12)) {
               var2.addRow(this.a.parse(var11));
            } else if (var12.equals("quick-test-data")) {
               String var13 = var11.getTextTrim();
               var2.setQuickTestData(var13);
            } else if (this.b.support(var12)) {
               var2.addColumn(this.b.parse(var11));
            } else if (this.c.support(var12)) {
               var2.addCell(this.c.parse(var11));
            }

            Library var17 = this.a(var11);
            if (var17 != null) {
               var2.addLibrary(var17);
            } else if (this.e.support(var12)) {
               PredefineGroupDefinition var14 = this.e.parse(var11);
               var2.setPredefineGroup(var14);
            } else if (var12.equals("remark")) {
               var2.setRemark(var11.getText());
            }
         }
      }

      this.a(var2);
      return var2;
   }

   private void a(DecisionTable var1) {
      List var2 = var1.getLibraries();
      List var3 = null;
      PredefineGroupDefinition var4 = var1.getPredefineGroup();
      if (var4 != null) {
         var3 = var4.getPredefines();
      }

      ResourceLibrary var5 = this.d.getResourceLibraryBuilder().buildResourceLibrary(var2, var3);
      if (var4 != null && var4.getPredefines() != null) {
         for (Predefine var7 : var4.getPredefines()) {
            Junction var8 = var7.getJunction();
            if (var8 != null) {
               this.d.rebuildCriterion(var8, var5, false);
            }

            Value var9 = var7.getValue();
            if (var9 != null) {
               this.d.rebuildValue(var9, var5, false);
            }
         }
      }

      for (Cell var14 : var1.getCellMap().values()) {
         if (var14.getAction() != null) {
            this.d.rebuildAction(var14.getAction(), var5, false);
         } else if (var14.getValue() != null) {
            this.d.rebuildValue(var14.getValue(), var5, false);
         } else if (var14.getJoint() != null && var14.getJoint() != null && var14.getJoint().getJunction() != null) {
            List var16 = var14.getJoint().getConditions();
            if (var16 != null) {
               for (Condition var10 : (Iterable<Condition>)(Iterable<?>)(var16)) {
                  Value var11 = var10.getValue();
                  if (var11 != null) {
                     this.d.rebuildValue(var11, var5, false);
                  }
               }
            }
         }
      }

      for (Column var15 : var1.getColumns()) {
         if (var15.isPredefine()) {
            Predefine var17 = var5.getPredefine(var15.getUuid());
            if (var17 != null) {
               var15.setPredefineName(var17.getName());
               String var20 = var17.getType();
               if (Datatype.isType(var20)) {
                  var15.setPredefineDatatype(Datatype.valueOf(var20));
               } else {
                  String var22 = var15.getPredefinePropertyUuid();
                  if (var22 != null) {
                     VariableData var25 = var5.getVariableByUuid(var20, var22);
                     var15.setPredefineVariableCategory(var25.getCategory().getName());
                     var15.setPredefinePropertyName(var25.getVariable().getName());
                     var15.setPredefinePropertyDatatype(var25.getVariable().getType());
                     var15.setPredefinePropertyLabel(var25.getVariable().getLabel());
                     var15.setPredefineVariableCategoryUuid(var25.getCategory().getUuid());
                  } else {
                     VariableCategory var26 = var5.getVariableCategoryByUuid(var20);
                     var15.setPredefineVariableCategory(var26.getName());
                     var15.setPredefineVariableCategoryUuid(var26.getUuid());
                  }
               }
            }
         }

         String var18 = var15.getVariableCategory();
         String var21 = var15.getVariableName();
         if (!StringUtils.isBlank(var18) && !StringUtils.isBlank(var21)) {
            VariableData var23 = var5.getVariableByUuid(var15.getCategoryUuid(), var15.getUuid());
            if (var23 == null) {
               var23 = var5.getVariableByName(var15.getVariableCategory(), var15.getVariableName());
            }

            if (var23 == null) {
               throw new RuleException("决策表条件列头变量【" + var15.getVariableCategory() + "." + var15.getVariableLabel() + "】未在库文件中定义.");
            }

            if (var15.getKeyUuid() == null) {
               var15.setDatatype(var23.getVariable().getType());
               var15.setVariableName(var23.getVariable().getName());
               var15.setVariableLabel(var23.getVariable().getLabel());
               var15.setVariableCategory(var23.getCategory().getName());
               if (var15.getCategoryUuid() == null) {
                  var15.setCategoryUuid(var23.getCategory().getUuid());
                  var15.setUuid(var23.getVariable().getUuid());
               }
            } else {
               var15.setKeyLabel(var23.getVariable().getLabel());
               var15.setKeyName(var23.getVariable().getName());
               var23 = var5.getVariableByUuid(var15.getKeyCategoryUuid(), var15.getKeyUuid());
               if (var23 != null) {
                  var15.setVariableName(var23.getVariable().getName());
                  var15.setVariableLabel(var23.getVariable().getLabel());
                  var15.setDatatype(var23.getVariable().getType());
               }
            }
         }
      }

      Collections.sort(var1.getColumns(), new DecisionTableParser$1(this));
      Collections.sort(var1.getRows(), new DecisionTableParser$2(this));
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("decision-table");
   }

   public void setColumnParser(ColumnParser var1) {
      this.b = var1;
   }

   public void setRowParser(RowParser var1) {
      this.a = var1;
   }

   public void setCellParser(CellParser var1) {
      this.c = var1;
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.d = var1;
   }

   public void setPredefinesParser(PredefinesParser var1) {
      this.e = var1;
   }
}
