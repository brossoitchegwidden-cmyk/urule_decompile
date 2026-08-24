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
   private CardCellParser cardCellParser;
   private AttributeRowParser attributeRowParser = new AttributeRowParser();
   private CustomColParser customColParser = new CustomColParser();
   private RulesRebuilder rulesRebuilder;

   public ScorecardDefinition parse(Element element) {
      ScorecardDefinition scorecardDefinition = new ScorecardDefinition();
      scorecardDefinition.setName(element.attributeValue("name"));
      scorecardDefinition.setScoringType(ScoringType.valueOf(element.attributeValue("scoring-type")));
      scorecardDefinition.setAssignTargetType(AssignTargetType.valueOf(element.attributeValue("assign-target-type")));
      scorecardDefinition.setVariableCategory(element.attributeValue("var-category"));
      scorecardDefinition.setVariableName(element.attributeValue("var"));
      scorecardDefinition.setVariableLabel(element.attributeValue("var-label"));
      scorecardDefinition.setUuid(element.attributeValue("uuid"));
      scorecardDefinition.setCategoryUuid(element.attributeValue("category-uuid"));
      scorecardDefinition.setKeyUuid(element.attributeValue("key-uuid"));
      scorecardDefinition.setKeyCategoryUuid(element.attributeValue("key-category-uuid"));
      String text = element.attributeValue("datatype");
      if (StringUtils.isNotBlank(text)) {
         scorecardDefinition.setDatatype(Datatype.valueOf(text));
      }

      scorecardDefinition.setKeyLabel(element.attributeValue("key-label"));
      scorecardDefinition.setKeyName(element.attributeValue("key-name"));
      scorecardDefinition.setScoringBean(element.attributeValue("custom-scoring-bean"));
      String text2 = element.attributeValue("salience");
      if (StringUtils.isNotEmpty(text2)) {
         scorecardDefinition.setSalience(Integer.valueOf(text2));
      }

      String text3 = element.attributeValue("effective-date");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(text3)) {
         try {
            scorecardDefinition.setEffectiveDate(simpleDateFormat.parse(text3));
         } catch (ParseException parseException) {
            throw new RuleException(parseException);
         }
      }

      String text4 = element.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(text4)) {
         try {
            scorecardDefinition.setExpiresDate(simpleDateFormat.parse(text4));
         } catch (ParseException parseException2) {
            throw new RuleException(parseException2);
         }
      }

      String text5 = element.attributeValue("enabled");
      if (StringUtils.isNotEmpty(text5)) {
         scorecardDefinition.setEnabled(Boolean.valueOf(text5));
      }

      String text6 = element.attributeValue("debug");
      if (StringUtils.isNotEmpty(text6)) {
         scorecardDefinition.setDebug(Boolean.valueOf(text6));
      }

      scorecardDefinition.setAttributeColWidth(element.attributeValue("attr-col-width"));
      scorecardDefinition.setAttributeColName(element.attributeValue("attr-col-name"));
      scorecardDefinition.setAttributeColVariableCategory(element.attributeValue("attr-col-category"));
      scorecardDefinition.setAttributeColVariableCategoryUuid(element.attributeValue("attr-col-category-uuid"));
      scorecardDefinition.setConditionColName(element.attributeValue("condition-col-name"));
      scorecardDefinition.setConditionColWidth(element.attributeValue("condition-col-width"));
      scorecardDefinition.setScoreColName(element.attributeValue("score-col-name"));
      scorecardDefinition.setScoreColWidth(element.attributeValue("score-col-width"));
      String text7 = element.attributeValue("weight-support");
      if (StringUtils.isNotBlank(text7)) {
         scorecardDefinition.setWeightSupport(Boolean.valueOf(text7));
      }

      ArrayList items = new ArrayList();
      ArrayList items2 = new ArrayList();
      ArrayList items3 = new ArrayList();
      scorecardDefinition.setCells(items);
      scorecardDefinition.setRows(items2);
      scorecardDefinition.setCustomCols(items3);

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.cardCellParser.support(name)) {
               items.add(this.cardCellParser.parse(element2));
            } else if (this.attributeRowParser.support(name)) {
               items2.add(this.attributeRowParser.parse(element2));
            } else if (this.customColParser.support(name)) {
               items3.add(this.customColParser.parse(element2));
            } else if (name.equals("quick-test-data")) {
               String textTrim = element2.getTextTrim();
               scorecardDefinition.setQuickTestData(textTrim);
            }

            Library library = this.parseLibrary(element2);
            if (library != null) {
               scorecardDefinition.addLibrary(library);
            } else if (name.equals("remark")) {
               scorecardDefinition.setRemark(element2.getText());
            }
         }
      }

      this.processScorecardDefinition(scorecardDefinition);
      return scorecardDefinition;
   }

   private void processScorecardDefinition(ScorecardDefinition scorecardDefinition) {
      List libraries = scorecardDefinition.getLibraries();
      if (libraries != null) {
         ResourceLibrary resourceLibrary = this.rulesRebuilder.getResourceLibraryBuilder().buildResourceLibrary(libraries, scorecardDefinition.getPredefines());
         VariableCategory variableCategoryByUuid = resourceLibrary.getVariableCategoryByUuid(scorecardDefinition.getAttributeColVariableCategoryUuid());
         if (variableCategoryByUuid == null) {
            variableCategoryByUuid = resourceLibrary.getVariableCategoryByCategoryName(scorecardDefinition.getAttributeColVariableCategory());
         }

         scorecardDefinition.setAttributeColVariableCategory(variableCategoryByUuid.getName());
         if (scorecardDefinition.getAssignTargetType() == AssignTargetType.parameter && StringUtils.isBlank(scorecardDefinition.getKeyUuid())) {
            Variable parameterByUuid = resourceLibrary.getParameterByUuid(scorecardDefinition.getKeyUuid(), scorecardDefinition.getKeyName(), scorecardDefinition.getKeyLabel());
            if (parameterByUuid != null && parameterByUuid.getType() == Datatype.Object) {
               scorecardDefinition.setKeyUuid(parameterByUuid.getUuid());
            }
         }

         if (!scorecardDefinition.getAssignTargetType().equals(AssignTargetType.none)) {
            if (scorecardDefinition.getKeyCategoryUuid() != null) {
               VariableData variableByUuid = resourceLibrary.getVariableByUuid(scorecardDefinition.getKeyCategoryUuid(), scorecardDefinition.getKeyUuid());
               scorecardDefinition.setVariableLabel(variableByUuid.getVariable().getLabel());
               scorecardDefinition.setDatatype(variableByUuid.getVariable().getType());
               scorecardDefinition.setVariableName(variableByUuid.getVariable().getName());
               variableByUuid = resourceLibrary.getVariableByUuid(scorecardDefinition.getCategoryUuid(), scorecardDefinition.getUuid());
               scorecardDefinition.setKeyName(variableByUuid.getVariable().getName());
               scorecardDefinition.setKeyLabel(variableByUuid.getVariable().getLabel());
            } else {
               VariableData variableData = null;
               if ("参数".equals(scorecardDefinition.getCategoryUuid())) {
                  Variable parameterByUuid2 = resourceLibrary.getParameterByUuid(scorecardDefinition.getKeyUuid(), scorecardDefinition.getKeyName(), scorecardDefinition.getKeyLabel());
                  if (parameterByUuid2 == null) {
                     parameterByUuid2 = resourceLibrary.getParameterByUuid(scorecardDefinition.getKeyUuid(), scorecardDefinition.getVariableName(), scorecardDefinition.getVariableLabel());
                  }

                  if (parameterByUuid2 != null && parameterByUuid2.getType() == Datatype.Object) {
                     variableData = resourceLibrary.getVariableByUuid(parameterByUuid2.getDataType(), scorecardDefinition.getUuid());
                     if (StringUtils.isBlank(scorecardDefinition.getKeyUuid())) {
                        scorecardDefinition.setKeyUuid(parameterByUuid2.getUuid());
                     }
                  }

                  if (variableData == null) {
                     variableData = resourceLibrary.getVariableByName(scorecardDefinition.getCategoryUuid(), scorecardDefinition.getVariableName());
                  }
               } else {
                  variableData = resourceLibrary.getVariableByUuid(scorecardDefinition.getCategoryUuid(), scorecardDefinition.getUuid());
               }

               scorecardDefinition.setVariableLabel(variableData.getVariable().getLabel());
               scorecardDefinition.setDatatype(variableData.getVariable().getType());
               scorecardDefinition.setVariableCategory(variableData.getCategory().getName());
               scorecardDefinition.setVariableName(variableData.getVariable().getName());
            }
         }

         List cells = scorecardDefinition.getCells();
         if (cells != null) {
            for (CardCell cardCell : (Iterable<CardCell>)(Iterable<?>)(cells)) {
               Joint joint = cardCell.getJoint();
               if (joint != null && joint.getConditions() != null) {
                  for (Condition condition : joint.getConditions()) {
                     if (condition != null) {
                        Value localValue = condition.getValue();
                        if (localValue != null) {
                           this.rulesRebuilder.rebuildValue(localValue, resourceLibrary, false);
                        }
                     }
                  }
               }

               Value localValue2 = cardCell.getValue();
               if (localValue2 != null) {
                  this.rulesRebuilder.rebuildValue(localValue2, resourceLibrary, false);
               }

               if (cardCell.getType().equals(CellType.attribute)) {
                  VariableData variableData2 = null;
                  if ("参数".equals(variableCategoryByUuid.getUuid())) {
                     Variable parameterByUuid3 = resourceLibrary.getParameterByUuid(cardCell.getKeyUuid(), cardCell.getKeyName(), cardCell.getKeyLabel());
                     if (parameterByUuid3 != null && parameterByUuid3.getType() == Datatype.Object) {
                        variableData2 = resourceLibrary.getVariableByUuid(parameterByUuid3.getDataType(), cardCell.getUuid());
                        if (StringUtils.isBlank(cardCell.getKeyUuid())) {
                           cardCell.setKeyUuid(parameterByUuid3.getUuid());
                        }
                     }

                     if (variableData2 == null) {
                        variableData2 = resourceLibrary.getVariableByName(variableCategoryByUuid.getUuid(), cardCell.getVariableName());
                     }
                  } else {
                     variableData2 = resourceLibrary.getVariableByUuid(variableCategoryByUuid.getUuid(), cardCell.getUuid());
                  }

                  if (variableData2 != null) {
                     cardCell.setDatatype(variableData2.getVariable().getType());
                     cardCell.setVariableLabel(variableData2.getVariable().getLabel());
                     cardCell.setVariableName(variableData2.getVariable().getName());
                  }
               }
            }
         }
      }
   }

   public void setCardCellParser(CardCellParser cardCellParser) {
      this.cardCellParser = cardCellParser;
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }

   @Override
   public boolean support(String name) {
      return name.equals("scorecard");
   }
}
