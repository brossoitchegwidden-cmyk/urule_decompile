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
import com.bstek.urule.model.scorecard.CardCell;
import com.bstek.urule.model.scorecard.CellType;
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.parse.LibrariesParser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class ScorecardParser extends LibrariesParser<ScorecardDefinition> {
   private CardCellParser a;
   private AttributeRowParser b = new AttributeRowParser();
   private CustomColParser c = new CustomColParser();
   private RulesRebuilder d;

   public ScorecardDefinition parse(Element var1) {
      ScorecardDefinition var2 = new ScorecardDefinition();
      var2.setName(var1.attributeValue("name"));
      var2.setScoringType(ScoringType.valueOf(var1.attributeValue("scoring-type")));
      var2.setAssignTargetType(AssignTargetType.valueOf(var1.attributeValue("assign-target-type")));
      var2.setVariableCategory(var1.attributeValue("var-category"));
      var2.setVariableName(var1.attributeValue("var"));
      var2.setVariableLabel(var1.attributeValue("var-label"));
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setCategoryUuid(var1.attributeValue("category-uuid"));
      var2.setKeyUuid(var1.attributeValue("key-uuid"));
      var2.setKeyCategoryUuid(var1.attributeValue("key-category-uuid"));
      String var3 = var1.attributeValue("datatype");
      if (StringUtils.isNotBlank(var3)) {
         var2.setDatatype(Datatype.valueOf(var3));
      }

      var2.setKeyLabel(var1.attributeValue("key-label"));
      var2.setKeyName(var1.attributeValue("key-name"));
      var2.setScoringBean(var1.attributeValue("custom-scoring-bean"));
      String var4 = var1.attributeValue("salience");
      if (StringUtils.isNotEmpty(var4)) {
         var2.setSalience(Integer.valueOf(var4));
      }

      String var5 = var1.attributeValue("effective-date");
      SimpleDateFormat var6 = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(var5)) {
         try {
            var2.setEffectiveDate(var6.parse(var5));
         } catch (ParseException var20) {
            throw new RuleException(var20);
         }
      }

      String var7 = var1.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(var7)) {
         try {
            var2.setExpiresDate(var6.parse(var7));
         } catch (ParseException var19) {
            throw new RuleException(var19);
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

      var2.setAttributeColWidth(var1.attributeValue("attr-col-width"));
      var2.setAttributeColName(var1.attributeValue("attr-col-name"));
      var2.setAttributeColVariableCategory(var1.attributeValue("attr-col-category"));
      var2.setAttributeColVariableCategoryUuid(var1.attributeValue("attr-col-category-uuid"));
      var2.setConditionColName(var1.attributeValue("condition-col-name"));
      var2.setConditionColWidth(var1.attributeValue("condition-col-width"));
      var2.setScoreColName(var1.attributeValue("score-col-name"));
      var2.setScoreColWidth(var1.attributeValue("score-col-width"));
      String var10 = var1.attributeValue("weight-support");
      if (StringUtils.isNotBlank(var10)) {
         var2.setWeightSupport(Boolean.valueOf(var10));
      }

      ArrayList var11 = new ArrayList();
      ArrayList var12 = new ArrayList();
      ArrayList var13 = new ArrayList();
      var2.setCells(var11);
      var2.setRows(var12);
      var2.setCustomCols(var13);

      for (Object var15 : var1.elements()) {
         if (var15 != null && var15 instanceof Element) {
            Element var16 = (Element)var15;
            String var17 = var16.getName();
            if (this.a.support(var17)) {
               var11.add(this.a.parse(var16));
            } else if (this.b.support(var17)) {
               var12.add(this.b.parse(var16));
            } else if (this.c.support(var17)) {
               var13.add(this.c.parse(var16));
            } else if (var17.equals("quick-test-data")) {
               String var18 = var16.getTextTrim();
               var2.setQuickTestData(var18);
            }

            Library var21 = this.a(var16);
            if (var21 != null) {
               var2.addLibrary(var21);
            } else if (var17.equals("remark")) {
               var2.setRemark(var16.getText());
            }
         }
      }

      this.a(var2);
      return var2;
   }

   private void a(ScorecardDefinition var1) {
      List var2 = var1.getLibraries();
      if (var2 != null) {
         ResourceLibrary var3 = this.d.getResourceLibraryBuilder().buildResourceLibrary(var2, var1.getPredefines());
         VariableCategory var4 = var3.getVariableCategoryByUuid(var1.getAttributeColVariableCategoryUuid());
         if (var4 == null) {
            var4 = var3.getVariableCategoryByCategoryName(var1.getAttributeColVariableCategory());
         }

         var1.setAttributeColVariableCategory(var4.getName());
         if (var1.getAssignTargetType() == AssignTargetType.parameter && StringUtils.isBlank(var1.getKeyUuid())) {
            Variable var5 = var3.getParameterByUuid(var1.getKeyUuid(), var1.getKeyName(), var1.getKeyLabel());
            if (var5 != null && var5.getType() == Datatype.Object) {
               var1.setKeyUuid(var5.getUuid());
            }
         }

         if (!var1.getAssignTargetType().equals(AssignTargetType.none)) {
            if (var1.getKeyCategoryUuid() != null) {
               VariableData var12 = var3.getVariableByUuid(var1.getKeyCategoryUuid(), var1.getKeyUuid());
               var1.setVariableLabel(var12.getVariable().getLabel());
               var1.setDatatype(var12.getVariable().getType());
               var1.setVariableName(var12.getVariable().getName());
               var12 = var3.getVariableByUuid(var1.getCategoryUuid(), var1.getUuid());
               var1.setKeyName(var12.getVariable().getName());
               var1.setKeyLabel(var12.getVariable().getLabel());
            } else {
               VariableData var14 = null;
               if ("参数".equals(var1.getCategoryUuid())) {
                  Variable var6 = var3.getParameterByUuid(var1.getKeyUuid(), var1.getKeyName(), var1.getKeyLabel());
                  if (var6 == null) {
                     var6 = var3.getParameterByUuid(var1.getKeyUuid(), var1.getVariableName(), var1.getVariableLabel());
                  }

                  if (var6 != null && var6.getType() == Datatype.Object) {
                     var14 = var3.getVariableByUuid(var6.getDataType(), var1.getUuid());
                     if (StringUtils.isBlank(var1.getKeyUuid())) {
                        var1.setKeyUuid(var6.getUuid());
                     }
                  }

                  if (var14 == null) {
                     var14 = var3.getVariableByName(var1.getCategoryUuid(), var1.getVariableName());
                  }
               } else {
                  var14 = var3.getVariableByUuid(var1.getCategoryUuid(), var1.getUuid());
               }

               var1.setVariableLabel(var14.getVariable().getLabel());
               var1.setDatatype(var14.getVariable().getType());
               var1.setVariableCategory(var14.getCategory().getName());
               var1.setVariableName(var14.getVariable().getName());
            }
         }

         List var15 = var1.getCells();
         if (var15 != null) {
            for (CardCell var7 : (Iterable<CardCell>)(Iterable<?>)(var15)) {
               Joint var8 = var7.getJoint();
               if (var8 != null && var8.getConditions() != null) {
                  for (Condition var10 : var8.getConditions()) {
                     if (var10 != null) {
                        Value var11 = var10.getValue();
                        if (var11 != null) {
                           this.d.rebuildValue(var11, var3, false);
                        }
                     }
                  }
               }

               Value var17 = var7.getValue();
               if (var17 != null) {
                  this.d.rebuildValue(var17, var3, false);
               }

               if (var7.getType().equals(CellType.attribute)) {
                  VariableData var18 = null;
                  if ("参数".equals(var4.getUuid())) {
                     Variable var19 = var3.getParameterByUuid(var7.getKeyUuid(), var7.getKeyName(), var7.getKeyLabel());
                     if (var19 != null && var19.getType() == Datatype.Object) {
                        var18 = var3.getVariableByUuid(var19.getDataType(), var7.getUuid());
                        if (StringUtils.isBlank(var7.getKeyUuid())) {
                           var7.setKeyUuid(var19.getUuid());
                        }
                     }

                     if (var18 == null) {
                        var18 = var3.getVariableByName(var4.getUuid(), var7.getVariableName());
                     }
                  } else {
                     var18 = var3.getVariableByUuid(var4.getUuid(), var7.getUuid());
                  }

                  if (var18 != null) {
                     var7.setDatatype(var18.getVariable().getType());
                     var7.setVariableLabel(var18.getVariable().getLabel());
                     var7.setVariableName(var18.getVariable().getName());
                  }
               }
            }
         }
      }
   }

   public void setCardCellParser(CardCellParser var1) {
      this.a = var1;
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.d = var1;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("scorecard");
   }
}
