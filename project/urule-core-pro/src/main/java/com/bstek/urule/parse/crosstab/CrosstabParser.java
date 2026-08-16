package com.bstek.urule.parse.crosstab;

import com.bstek.urule.Configure;
import com.bstek.urule.builder.ResourceLibraryBuilder;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.crosstab.BundleData;
import com.bstek.urule.model.crosstab.ConditionCrossCell;
import com.bstek.urule.model.crosstab.CrossCell;
import com.bstek.urule.model.crosstab.CrossColumn;
import com.bstek.urule.model.crosstab.CrossRow;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.model.crosstab.ValueCrossCell;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableData;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.parse.LibrariesParser;
import com.bstek.urule.parse.PredefinesParser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class CrosstabParser extends LibrariesParser<CrosstabDefinition> {
   private CrossRowParser a;
   private CrossColumnParser b;
   private HeaderCellParser c;
   private ConditionCrossCellParser d;
   private ValueCrossCellParser e;
   private RulesRebuilder f;
   private ResourceLibraryBuilder g;
   private PredefinesParser h;

   public CrosstabDefinition parse(Element var1) {
      CrosstabDefinition var2 = new CrosstabDefinition();
      var2.setAssignTargetType(var1.attributeValue("assign-target-type"));
      var2.setAssignVariableCategory(var1.attributeValue("var-category"));
      var2.setAssignVariable(var1.attributeValue("var"));
      var2.setAssignVariableLabel(var1.attributeValue("var-label"));
      var2.setKeyLabel(var1.attributeValue("key-label"));
      var2.setKeyName(var1.attributeValue("key-name"));
      var2.setKeyCategoryUuid(var1.attributeValue("key-category-uuid"));
      var2.setKeyUuid(var1.attributeValue("key-uuid"));
      var2.setCategoryUuid(var1.attributeValue("category-uuid"));
      var2.setUuid(var1.attributeValue("uuid"));
      String var3 = var1.attributeValue("datatype");
      if (StringUtils.isNotBlank(var3)) {
         var2.setAssignDatatype(Datatype.valueOf(var3));
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

      ArrayList var10 = new ArrayList();
      ArrayList var11 = new ArrayList();
      ArrayList var12 = new ArrayList();
      var2.setCells(var10);
      var2.setRows(var11);
      var2.setColumns(var12);

      for (Object var14 : var1.elements()) {
         if (var14 != null && var14 instanceof Element) {
            Element var15 = (Element)var14;
            String var16 = var15.getName();
            if (this.d.support(var16)) {
               var10.add(this.d.parse(var15));
            } else if (var16.equals("quick-test-data")) {
               String var17 = var15.getTextTrim();
               var2.setQuickTestData(var17);
            } else if (this.e.support(var16)) {
               var10.add(this.e.parse(var15));
            } else if (this.a.support(var16)) {
               var11.add(this.a.parse(var15));
            } else if (this.b.support(var16)) {
               var12.add(this.b.parse(var15));
            }

            Library var21 = this.a(var15);
            if (var21 != null) {
               var2.addLibrary(var21);
            } else if (this.h.support(var16)) {
               PredefineGroupDefinition var18 = this.h.parse(var15);
               var2.setPredefineGroup(var18);
            } else if (this.c.support(var16)) {
               var2.setHeaderCell(this.c.parse(var15));
            } else if (var16.equals("remark")) {
               var2.setRemark(var15.getText());
            }
         }
      }

      this.a(var2);
      return var2;
   }

   private void a(CrosstabDefinition var1) {
      List var2 = var1.getLibraries();
      List var3 = null;
      PredefineGroupDefinition var4 = var1.getPredefineGroup();
      if (var4 != null) {
         var3 = var4.getPredefines();
      }

      ResourceLibrary var5 = this.g.buildResourceLibrary(var2, var3);
      if (var4 != null && var4.getPredefines() != null) {
         for (Predefine var7 : var4.getPredefines()) {
            Junction var8 = var7.getJunction();
            if (var8 != null) {
               this.f.rebuildCriterion(var8, var5, false);
            }

            Value var9 = var7.getValue();
            if (var9 != null) {
               this.f.rebuildValue(var9, var5, false);
            }
         }
      }

      VariableData var17 = var5.getVariableByUuid(var1.getCategoryUuid(), var1.getUuid());
      if (var17 != null) {
         if (var1.getKeyUuid() == null) {
            var1.setAssignVariableCategory(var17.getCategory().getName());
            var1.setAssignVariable(var17.getVariable().getName());
            var1.setAssignVariableLabel(var17.getVariable().getLabel());
            if (var1.getCategoryUuid() == null) {
               var1.setCategoryUuid(var17.getCategory().getUuid());
               var1.setUuid(var17.getVariable().getUuid());
            }
         } else {
            var1.setKeyLabel(var17.getVariable().getLabel());
            var1.setKeyName(var17.getVariable().getName());
            var17 = var5.getVariableByUuid(var1.getKeyCategoryUuid(), var1.getKeyUuid());
            var1.setAssignVariable(var17.getVariable().getName());
            var1.setAssignVariableLabel(var17.getVariable().getLabel());
         }
      }

      for (CrossRow var26 : var1.getRows()) {
         if (var26 instanceof BundleData) {
            BundleData var10 = (BundleData)var26;
            String var11 = var10.getBundleDataType();
            if (var11 != null && var11.contentEquals("predefine")) {
               Predefine var12 = var5.getPredefine(var10.getPredefineUuid());
               if (var12 != null) {
                  var10.setPredefineName(var12.getName());
                  String var13 = var12.getType();
                  if (Datatype.isType(var13)) {
                     var10.setPredefineDatatype(Datatype.valueOf(var13));
                  } else {
                     String var14 = var10.getPredefinePropertyUuid();
                     if (var14 != null) {
                        VariableData var15 = var5.getVariableByUuid(var13, var14);
                        var10.setPredefineVariableCategory(var15.getCategory().getName());
                        var10.setPredefinePropertyName(var15.getVariable().getName());
                        var10.setPredefinePropertyLabel(var15.getVariable().getLabel());
                        var10.setPredefineVariableCategoryUuid(var15.getCategory().getUuid());
                     } else {
                        VariableCategory var40 = var5.getVariableCategoryByUuid(var13);
                        var10.setPredefineVariableCategory(var40.getName());
                        var10.setPredefineVariableCategoryUuid(var40.getUuid());
                     }
                  }
               }
            } else {
               var17 = var5.getVariableByUuid(var10.getCategoryUuid(), var10.getUuid());
               if (var17 == null) {
                  var17 = var5.getVariableByName(var10.getVariableCategory(), var10.getVariableName());
               }

               if (var17 != null) {
                  if (var10.getKeyUuid() == null) {
                     var10.setVariableCategory(var17.getCategory().getName());
                     var10.setVariableLabel(var17.getVariable().getLabel());
                     var10.setVariableName(var17.getVariable().getName());
                     if (var10.getCategoryUuid() == null) {
                        var10.setCategoryUuid(var17.getCategory().getUuid());
                        var10.setUuid(var17.getVariable().getUuid());
                     }
                  } else {
                     var10.setKeyLabel(var17.getVariable().getLabel());
                     var10.setKeyName(var17.getVariable().getName());
                     var17 = var5.getVariableByUuid(var10.getKeyCategoryUuid(), var10.getKeyUuid());
                     var10.setVariableLabel(var17.getVariable().getLabel());
                     var10.setVariableName(var17.getVariable().getName());
                  }
               }
            }
         }
      }

      for (CrossColumn var29 : var1.getColumns()) {
         if (var29 instanceof BundleData) {
            BundleData var31 = (BundleData)var29;
            String var33 = var31.getBundleDataType();
            if (var33 != null && var33.contentEquals("predefine")) {
               Predefine var36 = var5.getPredefine(var31.getPredefineUuid());
               if (var36 != null) {
                  var31.setPredefineName(var36.getName());
                  String var39 = var36.getType();
                  if (Datatype.isType(var39)) {
                     var31.setPredefineDatatype(Datatype.valueOf(var39));
                  } else {
                     String var41 = var31.getPredefinePropertyUuid();
                     if (var41 != null) {
                        VariableData var16 = var5.getVariableByUuid(var39, var41);
                        var31.setPredefineVariableCategory(var16.getCategory().getName());
                        var31.setPredefinePropertyName(var16.getVariable().getName());
                        var31.setPredefinePropertyLabel(var16.getVariable().getLabel());
                        var31.setPredefineVariableCategoryUuid(var16.getCategory().getUuid());
                     } else {
                        VariableCategory var42 = var5.getVariableCategoryByUuid(var39);
                        var31.setPredefineVariableCategory(var42.getName());
                        var31.setPredefineVariableCategoryUuid(var42.getUuid());
                     }
                  }
               }
            } else {
               var17 = var5.getVariableByUuid(var31.getCategoryUuid(), var31.getUuid());
               if (var17 == null) {
                  var17 = var5.getVariableByName(var31.getVariableCategory(), var31.getVariableName());
               }

               if (var17 != null) {
                  if (var31.getKeyUuid() == null) {
                     var31.setVariableCategory(var17.getCategory().getName());
                     var31.setVariableLabel(var17.getVariable().getLabel());
                     var31.setVariableName(var17.getVariable().getName());
                     if (var31.getCategoryUuid() == null) {
                        var31.setCategoryUuid(var17.getCategory().getUuid());
                        var31.setUuid(var17.getVariable().getUuid());
                     }
                  } else {
                     var31.setKeyLabel(var17.getVariable().getLabel());
                     var31.setKeyName(var17.getVariable().getName());
                     var17 = var5.getVariableByUuid(var31.getKeyCategoryUuid(), var31.getKeyUuid());
                     var31.setVariableLabel(var17.getVariable().getLabel());
                     var31.setVariableName(var17.getVariable().getName());
                  }
               }
            }
         }
      }

      for (CrossCell var32 : var1.getCells()) {
         if (var32 instanceof ConditionCrossCell) {
            ConditionCrossCell var34 = (ConditionCrossCell)var32;
            Joint var37 = var34.getJoint();
            this.a(var37, var5);
         } else if (var32 instanceof ValueCrossCell) {
            ValueCrossCell var35 = (ValueCrossCell)var32;
            Value var38 = var35.getValue();
            if (var38 != null) {
               this.f.rebuildValue(var38, var5, false);
            }
         }
      }
   }

   private void a(Joint var1, ResourceLibrary var2) {
      if (var1 != null) {
         List var3 = var1.getConditions();
         if (var3 != null) {
            for (Condition var5 : (Iterable<Condition>)(Iterable<?>)(var3)) {
               Value var6 = var5.getValue();
               if (var6 != null) {
                  this.f.rebuildValue(var6, var2, false);
               }
            }
         }

         List var7 = var1.getJoints();
         if (var7 != null) {
            for (Joint var9 : (Iterable<Joint>)(Iterable<?>)(var7)) {
               this.a(var9, var2);
            }
         }
      }
   }

   @Override
   public boolean support(String var1) {
      return "crosstab".equals(var1);
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.f = var1;
   }

   public void setResourceLibraryBuilder(ResourceLibraryBuilder var1) {
      this.g = var1;
   }

   public void setConditionCrossCellParser(ConditionCrossCellParser var1) {
      this.d = var1;
   }

   public void setCrossColumnParser(CrossColumnParser var1) {
      this.b = var1;
   }

   public void setCrossRowParser(CrossRowParser var1) {
      this.a = var1;
   }

   public void setHeaderCellParser(HeaderCellParser var1) {
      this.c = var1;
   }

   public void setValueCrossCellParser(ValueCrossCellParser var1) {
      this.e = var1;
   }

   public void setPredefinesParser(PredefinesParser var1) {
      this.h = var1;
   }
}
