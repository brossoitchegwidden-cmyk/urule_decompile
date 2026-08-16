package com.bstek.urule.parse.scorecard;

import com.bstek.urule.Configure;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableData;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.scorecard.AssignTargetType;
import com.bstek.urule.model.scorecard.ComplexColumn;
import com.bstek.urule.model.scorecard.ComplexColumnType;
import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.parse.LibrariesParser;
import com.bstek.urule.parse.table.CellParser;
import com.bstek.urule.parse.table.RowParser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class ComplexScorecardParser extends LibrariesParser<ComplexScorecardDefinition> {
   private RowParser a;
   private ComplexColumnParser b;
   private CellParser c;
   private RulesRebuilder d;

   public ComplexScorecardDefinition parse(Element var1) {
      ComplexScorecardDefinition var2 = new ComplexScorecardDefinition();
      var2.setScoringType(ScoringType.valueOf(var1.attributeValue("scoring-type")));
      var2.setScoringBean(var1.attributeValue("custom-scoring-bean"));
      var2.setAssignTargetType(AssignTargetType.valueOf(var1.attributeValue("assign-target-type")));
      var2.setVariableCategory(var1.attributeValue("var-category"));
      var2.setVariableName(var1.attributeValue("var"));
      var2.setVariableLabel(var1.attributeValue("var-label"));
      var2.setKeyLabel(var1.attributeValue("key-label"));
      var2.setKeyName(var1.attributeValue("key-name"));
      var2.setKeyUuid(var1.attributeValue("key-uuid"));
      var2.setCategoryUuid(var1.attributeValue("category-uuid"));
      var2.setUuid(var1.attributeValue("uuid"));
      String var3 = var1.attributeValue("datatype");
      if (StringUtils.isNotBlank(var3)) {
         var2.setDatatype(Datatype.valueOf(var3));
      }

      String var4 = var1.attributeValue("salience");
      if (StringUtils.isNotEmpty(var4)) {
         var2.setSalience(Integer.valueOf(var4));
      }

      String var5 = var1.attributeValue("effective-date");
      SimpleDateFormat var6 = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(var5)) {
         try {
            var2.setEffectiveDate(var6.parse(var5));
         } catch (ParseException var17) {
            throw new RuleException(var17);
         }
      }

      String var7 = var1.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(var7)) {
         try {
            var2.setExpiresDate(var6.parse(var7));
         } catch (ParseException var16) {
            throw new RuleException(var16);
         }
      }

      String var8 = var1.attributeValue("enabled");
      if (StringUtils.isNotEmpty(var8)) {
         var2.setEnabled(Boolean.valueOf(var8));
      }

      String var9 = var1.attributeValue("debug");
      if (StringUtils.isNotEmpty(var9)) {
         var2.setDebug(Boolean.valueOf(var9));
      }

      for (Object var11 : var1.elements()) {
         if (var11 != null && var11 instanceof Element) {
            Element var12 = (Element)var11;
            String var13 = var12.getName();
            if (this.a.support(var13)) {
               var2.addRow(this.a.parse(var12));
            } else if (this.b.support(var13)) {
               var2.addColumn(this.b.parse(var12));
            } else if (this.c.support(var13)) {
               var2.addCell(this.c.parse(var12));
            }

            Library var14 = this.a(var12);
            if (var14 != null) {
               var2.addLibrary(var14);
            } else if (var13.equals("quick-test-data")) {
               String var15 = var12.getTextTrim();
               var2.setQuickTestData(var15);
            } else if (var13.equals("remark")) {
               var2.setRemark(var12.getText());
            }
         }
      }

      this.a(var2);
      return var2;
   }

   private void a(ComplexScorecardDefinition var1) {
      List var2 = var1.getLibraries();
      ResourceLibrary var3 = this.d.getResourceLibraryBuilder().buildResourceLibrary(var2, var1.getPredefines());
      if (var1.getAssignTargetType() == AssignTargetType.parameter && StringUtils.isBlank(var1.getKeyUuid())) {
         Variable var4 = var3.getParameterByUuid(var1.getKeyUuid(), var1.getKeyName(), var1.getKeyLabel());
         if (var4 != null && var4.getType() == Datatype.Object) {
            var1.setKeyUuid(var4.getUuid());
         }
      }

      List var11 = var1.getColumns();
      if (var11 != null) {
         for (ComplexColumn var6 : (Iterable<ComplexColumn>)(Iterable<?>)(var11)) {
            ComplexColumnType var7 = var6.getType();
            if (var7.equals(ComplexColumnType.Criteria)) {
               VariableCategory var8 = var3.getVariableCategoryByUuid(var6.getUuid());
               if (var8 != null) {
                  var6.setVariableCategory(var8.getName());
               }
            }
         }
      }

      for (Cell var13 : var1.getCellMap().values()) {
         if (var13.getAction() != null) {
            this.d.rebuildAction(var13.getAction(), var3, false);
         } else if (var13.getValue() != null) {
            this.d.rebuildValue(var13.getValue(), var3, false);
         } else if (var13.getJoint() != null && var13.getJoint() != null && var13.getJoint().getJunction() != null) {
            List var14 = var13.getJoint().getConditions();
            if (var14 != null) {
               for (Condition var9 : (Iterable<Condition>)(Iterable<?>)(var14)) {
                  Value var10 = var9.getValue();
                  if (var10 != null) {
                     this.d.rebuildValue(var10, var3, false);
                  }
               }
            }
         }

         if (var13.getUuid() != null) {
            String var15 = this.a(var11, var13.getCol());
            VariableData var17 = null;
            if ("参数".equals(var15)) {
               Variable var19 = var3.getParameterByUuid(var13.getKeyUuid(), var13.getKeyName(), var13.getKeyLabel());
               if (var19 != null && var19.getType() == Datatype.Object) {
                  var17 = var3.getVariableByUuid(var19.getDataType(), var13.getUuid());
                  if (StringUtils.isBlank(var13.getKeyUuid())) {
                     var13.setKeyUuid(var19.getUuid());
                  }
               }

               if (var17 == null) {
                  var17 = var3.getVariableByName(var15, var13.getVariableName());
               }

               if (var17 != null) {
                  var13.setDatatype(var17.getVariable().getType());
                  var13.setVariableLabel(var17.getVariable().getLabel());
                  var13.setVariableName(var17.getVariable().getName());
               }
            } else {
               var17 = var3.getVariableByUuid(var15, var13.getUuid());
               if (var17 != null) {
                  if (var13.getKeyCategoryUuid() != null) {
                     VariableData var20 = var3.getVariableByUuid(var13.getKeyCategoryUuid(), var13.getKeyUuid());
                     var13.setDatatype(var20.getVariable().getType());
                     var13.setVariableName(var20.getVariable().getName());
                     var13.setVariableLabel(var20.getVariable().getLabel());
                     var13.setKeyLabel(var17.getVariable().getLabel());
                     var13.setKeyName(var17.getVariable().getName());
                  } else {
                     var13.setDatatype(var17.getVariable().getType());
                     var13.setVariableName(var17.getVariable().getName());
                     var13.setVariableLabel(var17.getVariable().getLabel());
                  }
               }
            }
         }
      }

      Collections.sort(var1.getColumns(), new ComplexScorecardParser$1(this));
      Collections.sort(var1.getRows(), new ComplexScorecardParser$2(this));
   }

   private String a(List<ComplexColumn> var1, int var2) {
      if (var1 != null) {
         for (ComplexColumn var4 : var1) {
            if (var4.getNum() == var2) {
               return var4.getUuid();
            }
         }
      }

      return null;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("complex-scorecard");
   }

   public void setColumnParser(ComplexColumnParser var1) {
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
}
