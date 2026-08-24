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
   private CrossRowParser crossRowParser;
   private CrossColumnParser crossColumnParser;
   private HeaderCellParser headerCellParser;
   private ConditionCrossCellParser conditionCrossCellParser;
   private ValueCrossCellParser valueCrossCellParser;
   private RulesRebuilder rulesRebuilder;
   private ResourceLibraryBuilder resourceLibraryBuilder;
   private PredefinesParser predefinesParser;

   public CrosstabDefinition parse(Element element) {
      CrosstabDefinition crosstabDefinition = new CrosstabDefinition();
      crosstabDefinition.setAssignTargetType(element.attributeValue("assign-target-type"));
      crosstabDefinition.setAssignVariableCategory(element.attributeValue("var-category"));
      crosstabDefinition.setAssignVariable(element.attributeValue("var"));
      crosstabDefinition.setAssignVariableLabel(element.attributeValue("var-label"));
      crosstabDefinition.setKeyLabel(element.attributeValue("key-label"));
      crosstabDefinition.setKeyName(element.attributeValue("key-name"));
      crosstabDefinition.setKeyCategoryUuid(element.attributeValue("key-category-uuid"));
      crosstabDefinition.setKeyUuid(element.attributeValue("key-uuid"));
      crosstabDefinition.setCategoryUuid(element.attributeValue("category-uuid"));
      crosstabDefinition.setUuid(element.attributeValue("uuid"));
      String text = element.attributeValue("datatype");
      if (StringUtils.isNotBlank(text)) {
         crosstabDefinition.setAssignDatatype(Datatype.valueOf(text));
      }

      String text2 = element.attributeValue("salience");
      if (StringUtils.isNotEmpty(text2)) {
         crosstabDefinition.setSalience(Integer.valueOf(text2));
      }

      String text3 = element.attributeValue("effective-date");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(text3)) {
         try {
            crosstabDefinition.setEffectiveDate(simpleDateFormat.parse(text3));
         } catch (ParseException parseException) {
            throw new RuleException(parseException);
         }
      }

      String text4 = element.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(text4)) {
         try {
            crosstabDefinition.setExpiresDate(simpleDateFormat.parse(text4));
         } catch (ParseException parseException2) {
            throw new RuleException(parseException2);
         }
      }

      String text5 = element.attributeValue("enabled");
      if (StringUtils.isNotEmpty(text5)) {
         crosstabDefinition.setEnabled(Boolean.valueOf(text5));
      }

      String text6 = element.attributeValue("debug");
      if (StringUtils.isNotEmpty(text6)) {
         crosstabDefinition.setDebug(Boolean.valueOf(text6));
      }

      ArrayList items = new ArrayList();
      ArrayList items2 = new ArrayList();
      ArrayList items3 = new ArrayList();
      crosstabDefinition.setCells(items);
      crosstabDefinition.setRows(items2);
      crosstabDefinition.setColumns(items3);

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.conditionCrossCellParser.support(name)) {
               items.add(this.conditionCrossCellParser.parse(element2));
            } else if (name.equals("quick-test-data")) {
               String textTrim = element2.getTextTrim();
               crosstabDefinition.setQuickTestData(textTrim);
            } else if (this.valueCrossCellParser.support(name)) {
               items.add(this.valueCrossCellParser.parse(element2));
            } else if (this.crossRowParser.support(name)) {
               items2.add(this.crossRowParser.parse(element2));
            } else if (this.crossColumnParser.support(name)) {
               items3.add(this.crossColumnParser.parse(element2));
            }

            Library library = this.parseLibrary(element2);
            if (library != null) {
               crosstabDefinition.addLibrary(library);
            } else if (this.predefinesParser.support(name)) {
               PredefineGroupDefinition predefineGroupDefinition = this.predefinesParser.parse(element2);
               crosstabDefinition.setPredefineGroup(predefineGroupDefinition);
            } else if (this.headerCellParser.support(name)) {
               crosstabDefinition.setHeaderCell(this.headerCellParser.parse(element2));
            } else if (name.equals("remark")) {
               crosstabDefinition.setRemark(element2.getText());
            }
         }
      }

      this.parseDefinition(crosstabDefinition);
      return crosstabDefinition;
   }

   private void parseDefinition(CrosstabDefinition crosstabDefinition) {
      List libraries = crosstabDefinition.getLibraries();
      List predefines = null;
      PredefineGroupDefinition predefineGroup = crosstabDefinition.getPredefineGroup();
      if (predefineGroup != null) {
         predefines = predefineGroup.getPredefines();
      }

      ResourceLibrary resourceLibrary = this.resourceLibraryBuilder.buildResourceLibrary(libraries, predefines);
      if (predefineGroup != null && predefineGroup.getPredefines() != null) {
         for (Predefine predefine : predefineGroup.getPredefines()) {
            Junction junction = predefine.getJunction();
            if (junction != null) {
               this.rulesRebuilder.rebuildCriterion(junction, resourceLibrary, false);
            }

            Value localValue = predefine.getValue();
            if (localValue != null) {
               this.rulesRebuilder.rebuildValue(localValue, resourceLibrary, false);
            }
         }
      }

      VariableData variableByUuid = resourceLibrary.getVariableByUuid(crosstabDefinition.getCategoryUuid(), crosstabDefinition.getUuid());
      if (variableByUuid != null) {
         if (crosstabDefinition.getKeyUuid() == null) {
            crosstabDefinition.setAssignVariableCategory(variableByUuid.getCategory().getName());
            crosstabDefinition.setAssignVariable(variableByUuid.getVariable().getName());
            crosstabDefinition.setAssignVariableLabel(variableByUuid.getVariable().getLabel());
            if (crosstabDefinition.getCategoryUuid() == null) {
               crosstabDefinition.setCategoryUuid(variableByUuid.getCategory().getUuid());
               crosstabDefinition.setUuid(variableByUuid.getVariable().getUuid());
            }
         } else {
            crosstabDefinition.setKeyLabel(variableByUuid.getVariable().getLabel());
            crosstabDefinition.setKeyName(variableByUuid.getVariable().getName());
            variableByUuid = resourceLibrary.getVariableByUuid(crosstabDefinition.getKeyCategoryUuid(), crosstabDefinition.getKeyUuid());
            crosstabDefinition.setAssignVariable(variableByUuid.getVariable().getName());
            crosstabDefinition.setAssignVariableLabel(variableByUuid.getVariable().getLabel());
         }
      }

      for (CrossRow crossRow : crosstabDefinition.getRows()) {
         if (crossRow instanceof BundleData) {
            BundleData bundleData = (BundleData)crossRow;
            String bundleDataType = bundleData.getBundleDataType();
            if (bundleDataType != null && bundleDataType.contentEquals("predefine")) {
               Predefine predefine2 = resourceLibrary.getPredefine(bundleData.getPredefineUuid());
               if (predefine2 != null) {
                  bundleData.setPredefineName(predefine2.getName());
                  String type = predefine2.getType();
                  if (Datatype.isType(type)) {
                     bundleData.setPredefineDatatype(Datatype.valueOf(type));
                  } else {
                     String predefinePropertyUuid = bundleData.getPredefinePropertyUuid();
                     if (predefinePropertyUuid != null) {
                        VariableData variableByUuid2 = resourceLibrary.getVariableByUuid(type, predefinePropertyUuid);
                        bundleData.setPredefineVariableCategory(variableByUuid2.getCategory().getName());
                        bundleData.setPredefinePropertyName(variableByUuid2.getVariable().getName());
                        bundleData.setPredefinePropertyLabel(variableByUuid2.getVariable().getLabel());
                        bundleData.setPredefineVariableCategoryUuid(variableByUuid2.getCategory().getUuid());
                     } else {
                        VariableCategory variableCategoryByUuid = resourceLibrary.getVariableCategoryByUuid(type);
                        bundleData.setPredefineVariableCategory(variableCategoryByUuid.getName());
                        bundleData.setPredefineVariableCategoryUuid(variableCategoryByUuid.getUuid());
                     }
                  }
               }
            } else {
               variableByUuid = resourceLibrary.getVariableByUuid(bundleData.getCategoryUuid(), bundleData.getUuid());
               if (variableByUuid == null) {
                  variableByUuid = resourceLibrary.getVariableByName(bundleData.getVariableCategory(), bundleData.getVariableName());
               }

               if (variableByUuid != null) {
                  if (bundleData.getKeyUuid() == null) {
                     bundleData.setVariableCategory(variableByUuid.getCategory().getName());
                     bundleData.setVariableLabel(variableByUuid.getVariable().getLabel());
                     bundleData.setVariableName(variableByUuid.getVariable().getName());
                     if (bundleData.getCategoryUuid() == null) {
                        bundleData.setCategoryUuid(variableByUuid.getCategory().getUuid());
                        bundleData.setUuid(variableByUuid.getVariable().getUuid());
                     }
                  } else {
                     bundleData.setKeyLabel(variableByUuid.getVariable().getLabel());
                     bundleData.setKeyName(variableByUuid.getVariable().getName());
                     variableByUuid = resourceLibrary.getVariableByUuid(bundleData.getKeyCategoryUuid(), bundleData.getKeyUuid());
                     bundleData.setVariableLabel(variableByUuid.getVariable().getLabel());
                     bundleData.setVariableName(variableByUuid.getVariable().getName());
                  }
               }
            }
         }
      }

      for (CrossColumn crossColumn : crosstabDefinition.getColumns()) {
         if (crossColumn instanceof BundleData) {
            BundleData bundleData2 = (BundleData)crossColumn;
            String bundleDataType2 = bundleData2.getBundleDataType();
            if (bundleDataType2 != null && bundleDataType2.contentEquals("predefine")) {
               Predefine predefine3 = resourceLibrary.getPredefine(bundleData2.getPredefineUuid());
               if (predefine3 != null) {
                  bundleData2.setPredefineName(predefine3.getName());
                  String type2 = predefine3.getType();
                  if (Datatype.isType(type2)) {
                     bundleData2.setPredefineDatatype(Datatype.valueOf(type2));
                  } else {
                     String predefinePropertyUuid2 = bundleData2.getPredefinePropertyUuid();
                     if (predefinePropertyUuid2 != null) {
                        VariableData variableByUuid3 = resourceLibrary.getVariableByUuid(type2, predefinePropertyUuid2);
                        bundleData2.setPredefineVariableCategory(variableByUuid3.getCategory().getName());
                        bundleData2.setPredefinePropertyName(variableByUuid3.getVariable().getName());
                        bundleData2.setPredefinePropertyLabel(variableByUuid3.getVariable().getLabel());
                        bundleData2.setPredefineVariableCategoryUuid(variableByUuid3.getCategory().getUuid());
                     } else {
                        VariableCategory variableCategoryByUuid2 = resourceLibrary.getVariableCategoryByUuid(type2);
                        bundleData2.setPredefineVariableCategory(variableCategoryByUuid2.getName());
                        bundleData2.setPredefineVariableCategoryUuid(variableCategoryByUuid2.getUuid());
                     }
                  }
               }
            } else {
               variableByUuid = resourceLibrary.getVariableByUuid(bundleData2.getCategoryUuid(), bundleData2.getUuid());
               if (variableByUuid == null) {
                  variableByUuid = resourceLibrary.getVariableByName(bundleData2.getVariableCategory(), bundleData2.getVariableName());
               }

               if (variableByUuid != null) {
                  if (bundleData2.getKeyUuid() == null) {
                     bundleData2.setVariableCategory(variableByUuid.getCategory().getName());
                     bundleData2.setVariableLabel(variableByUuid.getVariable().getLabel());
                     bundleData2.setVariableName(variableByUuid.getVariable().getName());
                     if (bundleData2.getCategoryUuid() == null) {
                        bundleData2.setCategoryUuid(variableByUuid.getCategory().getUuid());
                        bundleData2.setUuid(variableByUuid.getVariable().getUuid());
                     }
                  } else {
                     bundleData2.setKeyLabel(variableByUuid.getVariable().getLabel());
                     bundleData2.setKeyName(variableByUuid.getVariable().getName());
                     variableByUuid = resourceLibrary.getVariableByUuid(bundleData2.getKeyCategoryUuid(), bundleData2.getKeyUuid());
                     bundleData2.setVariableLabel(variableByUuid.getVariable().getLabel());
                     bundleData2.setVariableName(variableByUuid.getVariable().getName());
                  }
               }
            }
         }
      }

      for (CrossCell crossCell : crosstabDefinition.getCells()) {
         if (crossCell instanceof ConditionCrossCell) {
            ConditionCrossCell conditionCrossCell = (ConditionCrossCell)crossCell;
            Joint joint = conditionCrossCell.getJoint();
            this.parseDefinition(joint, resourceLibrary);
         } else if (crossCell instanceof ValueCrossCell) {
            ValueCrossCell valueCrossCell = (ValueCrossCell)crossCell;
            Value localValue2 = valueCrossCell.getValue();
            if (localValue2 != null) {
               this.rulesRebuilder.rebuildValue(localValue2, resourceLibrary, false);
            }
         }
      }
   }

   private void parseDefinition(Joint joint, ResourceLibrary resourceLibrary) {
      if (joint != null) {
         List conditions = joint.getConditions();
         if (conditions != null) {
            for (Condition condition : (Iterable<Condition>)(Iterable<?>)(conditions)) {
               Value localValue = condition.getValue();
               if (localValue != null) {
                  this.rulesRebuilder.rebuildValue(localValue, resourceLibrary, false);
               }
            }
         }

         List joints = joint.getJoints();
         if (joints != null) {
            for (Joint joint2 : (Iterable<Joint>)(Iterable<?>)(joints)) {
               this.parseDefinition(joint2, resourceLibrary);
            }
         }
      }
   }

   @Override
   public boolean support(String name) {
      return "crosstab".equals(name);
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }

   public void setResourceLibraryBuilder(ResourceLibraryBuilder resourceLibraryBuilder) {
      this.resourceLibraryBuilder = resourceLibraryBuilder;
   }

   public void setConditionCrossCellParser(ConditionCrossCellParser conditionCrossCellParser) {
      this.conditionCrossCellParser = conditionCrossCellParser;
   }

   public void setCrossColumnParser(CrossColumnParser crossColumnParser) {
      this.crossColumnParser = crossColumnParser;
   }

   public void setCrossRowParser(CrossRowParser crossRowParser) {
      this.crossRowParser = crossRowParser;
   }

   public void setHeaderCellParser(HeaderCellParser headerCellParser) {
      this.headerCellParser = headerCellParser;
   }

   public void setValueCrossCellParser(ValueCrossCellParser valueCrossCellParser) {
      this.valueCrossCellParser = valueCrossCellParser;
   }

   public void setPredefinesParser(PredefinesParser predefinesParser) {
      this.predefinesParser = predefinesParser;
   }
}
