package com.bstek.urule.model;

import com.bstek.urule.Configure;
import com.bstek.urule.Utils;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ActionType;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.InvokeFile;
import com.bstek.urule.action.InvokeKnowledgePackage;
import com.bstek.urule.action.PredefineAssignAction;
import com.bstek.urule.action.ScoringAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.rule.loop.LoopTargetType;
import com.bstek.urule.model.scorecard.AssignTargetType;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.model.scorecard.runtime.ScoreRule;
import com.bstek.urule.runtime.ActionUtils;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractJsonDeserializer<T> extends JsonDeserializer<T> {
   protected Rule parseRule(JsonParser jsonParser, JsonNode node) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());

      try {
         JsonNode jsonNode = node.get("rule");
         if (jsonNode == null) {
            jsonNode = node;
         }

         Rule rule = null;
         String jsonValue = JsonUtils.getJsonValue(jsonNode, "scoringType");
         if (StringUtils.isNotBlank(jsonValue)) {
            ScoringType scoringType = ScoringType.valueOf(jsonValue);
            ScoreRule scoreRule = new ScoreRule();
            scoreRule.setScoringType(scoringType);
            this.buildScoreRule(jsonParser, jsonNode, scoreRule);
            rule = scoreRule;
         } else {
            String jsonValue2 = JsonUtils.getJsonValue(jsonNode, "loopRule");
            if (jsonValue2 != null) {
               boolean flag = Boolean.valueOf(jsonValue2);
               if (flag) {
                  LoopRule loopRule = new LoopRule();
                  this.buildLoopRule(jsonNode, loopRule);
                  rule = loopRule;
               } else {
                  rule = new Rule();
               }
            } else {
               rule = new Rule();
            }
         }

         String jsonValue3 = JsonUtils.getJsonValue(jsonNode, "withElse");
         if (StringUtils.isNotBlank(jsonValue3)) {
            rule.setWithElse(Boolean.valueOf(jsonValue3));
         }

         rule.setRemark(JsonUtils.getJsonValue(jsonNode, "remark"));
         rule.setMutexGroup(JsonUtils.getJsonValue(jsonNode, "mutexGroup"));
         rule.setPendedGroup(JsonUtils.getJsonValue(jsonNode, "pendedGroup"));
         String jsonValue4 = JsonUtils.getJsonValue(jsonNode, "autoFocus");
         if (jsonValue4 != null) {
            rule.setAutoFocus(Boolean.valueOf(jsonValue4));
         }

         String jsonValue5 = JsonUtils.getJsonValue(jsonNode, "loop");
         if (jsonValue5 != null) {
            rule.setLoop(Boolean.valueOf(jsonValue5));
         }

         String jsonValue6 = JsonUtils.getJsonValue(jsonNode, "effectiveDate");
         if (jsonValue6 != null) {
            rule.setEffectiveDate(simpleDateFormat.parse(jsonValue6));
         }

         String jsonValue7 = JsonUtils.getJsonValue(jsonNode, "enabled");
         if (jsonValue7 != null) {
            rule.setEnabled(Boolean.valueOf(jsonValue7));
         }

         String jsonValue8 = JsonUtils.getJsonValue(jsonNode, "debug");
         if (jsonValue8 != null) {
            rule.setDebug(Boolean.valueOf(jsonValue8));
         }

         String jsonValue9 = JsonUtils.getJsonValue(jsonNode, "expiresDate");
         if (jsonValue9 != null) {
            rule.setExpiresDate(simpleDateFormat.parse(jsonValue9));
         }

         rule.setName(JsonUtils.getJsonValue(jsonNode, "name"));
         rule.setFile(JsonUtils.getJsonValue(jsonNode, "file"));
         String jsonValue10 = JsonUtils.getJsonValue(jsonNode, "salience");
         if (jsonValue10 != null) {
            rule.setSalience(Integer.valueOf(jsonValue10));
         }

         Rhs rhs = new Rhs();
         rule.setRhs(rhs);
         JsonNode rhs2 = jsonNode.get("rhs");
         if (rhs2 != null) {
            rhs.setActions(this.parseActions(rhs2));
         }

         JsonNode other2 = jsonNode.get("other");
         if (other2 != null) {
            Other other = new Other();
            rule.setOther(other);
            other.setActions(this.parseActions(other2));
         }

         if (rule.isWithElse()) {
            Utils.buildElseRule(rule);
         }

         return rule;
      } catch (ParseException parseException) {
         throw new RuleException(parseException);
      }
   }

   private void buildScoreRule(JsonParser jsonParser, JsonNode jsonNode, ScoreRule scoreRule) {
      scoreRule.setScoringBean(JsonUtils.getJsonValue(jsonNode, "scoringBean"));
      AssignTargetType assignTargetType = AssignTargetType.valueOf(JsonUtils.getJsonValue(jsonNode, "assignTargetType"));
      scoreRule.setAssignTargetType(assignTargetType);
      scoreRule.setVariableCategory(JsonUtils.getJsonValue(jsonNode, "variableCategory"));
      scoreRule.setVariableName(JsonUtils.getJsonValue(jsonNode, "variableName"));
      scoreRule.setVariableLabel(JsonUtils.getJsonValue(jsonNode, "variableLabel"));
      scoreRule.setKeyLabel(JsonUtils.getJsonValue(jsonNode, "keyLabel"));
      scoreRule.setKeyName(JsonUtils.getJsonValue(jsonNode, "keyName"));
      String jsonValue = JsonUtils.getJsonValue(jsonNode, "datatype");
      if (StringUtils.isNotBlank(jsonValue)) {
         scoreRule.setDatatype(Datatype.valueOf(jsonValue));
      }

      try {
         JsonNode knowledgePackageWrapper2 = jsonNode.get("knowledgePackageWrapper");
         ObjectMapper codec = (ObjectMapper)jsonParser.getCodec();
         KnowledgePackageWrapper knowledgePackageWrapper = (KnowledgePackageWrapper)codec.convertValue(knowledgePackageWrapper2, KnowledgePackageWrapper.class);
         knowledgePackageWrapper.buildDeserialize();
         scoreRule.setKnowledgePackageWrapper(knowledgePackageWrapper);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private void buildLoopRule(JsonNode jsonNode, LoopRule loopRule) {
      JsonNode loopTarget2 = jsonNode.get("loopTarget");
      String jsonValue = JsonUtils.getJsonValue(jsonNode, "loopTargetType");
      if (StringUtils.isNotBlank(jsonValue)) {
         loopRule.setLoopTargetType(LoopTargetType.valueOf(jsonValue));
      }

      if (loopTarget2 != null) {
         LoopTarget loopTarget = new LoopTarget();
         Value localValue = JsonUtils.parseValue(loopTarget2);
         loopTarget.setValue(localValue);
         loopRule.setLoopTarget(loopTarget);
      }

      JsonNode loopStart2 = jsonNode.get("loopStart");
      if (loopStart2 != null) {
         List actions = this.parseActions(loopStart2);
         LoopStart loopStart = new LoopStart();
         loopStart.setActions(actions);
         loopRule.setLoopStart(loopStart);
      }

      JsonNode loopEnd2 = jsonNode.get("loopEnd");
      if (loopEnd2 != null) {
         List actions2 = this.parseActions(loopEnd2);
         LoopEnd loopEnd = new LoopEnd();
         loopEnd.setActions(actions2);
         loopRule.setLoopEnd(loopEnd);
      }

      JsonNode knowledgePackageWrapper2 = jsonNode.get("knowledgePackageWrapper");
      if (knowledgePackageWrapper2 != null) {
         KnowledgePackageWrapper knowledgePackageWrapper = JsonUtils.parseKnowledgePackageWrapper(knowledgePackageWrapper2.toString());
         loopRule.setKnowledgePackageWrapper(knowledgePackageWrapper);
      }
   }

   private List<Action> parseActions(JsonNode jsonNode) {
      ArrayList actions = new ArrayList();
      JsonNode actions2 = jsonNode.get("actions");
      if (actions2 == null) {
         return actions;
      }

      for (JsonNode jsonNode2 : actions2) {
         ActionType actionType = ActionType.valueOf(JsonUtils.getJsonValue(jsonNode2, "actionType"));
         switch (actionType) {
            case ConsolePrint:
               ConsolePrintAction consolePrintAction = new ConsolePrintAction();
               consolePrintAction.setValue(JsonUtils.parseValue(jsonNode2));
               consolePrintAction.setPriority(Integer.valueOf(JsonUtils.getJsonValue(jsonNode2, "priority")));
               actions.add(consolePrintAction);
               break;
            case ExecuteMethod:
               ExecuteMethodAction executeMethodAction = new ExecuteMethodAction();
               executeMethodAction.setBeanId(JsonUtils.getJsonValue(jsonNode2, "beanId"));
               executeMethodAction.setBeanLabel(JsonUtils.getJsonValue(jsonNode2, "beanLabel"));
               executeMethodAction.setMethodLabel(JsonUtils.getJsonValue(jsonNode2, "methodLabel"));
               executeMethodAction.setPriority(Integer.valueOf(JsonUtils.getJsonValue(jsonNode2, "priority")));
               executeMethodAction.setMethodName(JsonUtils.getJsonValue(jsonNode2, "methodName"));
               SpringBean builtinAction = ActionUtils.getBuiltinAction(executeMethodAction.getBeanId());
               if (builtinAction != null) {
                  executeMethodAction.setBeanELabel(builtinAction.getEname());
               }

               executeMethodAction.setParameters(JsonUtils.parseParameters(jsonNode2));
               JsonNode invokeKnowledgePackage = jsonNode2.get("invokeKnowledgePackage");
               if (invokeKnowledgePackage != null) {
                  String jsonValue = JsonUtils.getJsonValue(invokeKnowledgePackage, "project");
                  String jsonValue2 = JsonUtils.getJsonValue(invokeKnowledgePackage, "name");
                  String jsonValue3 = JsonUtils.getJsonValue(invokeKnowledgePackage, "code");
                  long longValue = Long.valueOf(JsonUtils.getJsonValue(invokeKnowledgePackage, "id"));
                  executeMethodAction.setInvokeKnowledgePackage(new InvokeKnowledgePackage(jsonValue, jsonValue2, longValue, jsonValue3));
               }

               JsonNode invokeFile2 = jsonNode2.get("invokeFile");
               if (invokeFile2 != null) {
                  String jsonValue4 = JsonUtils.getJsonValue(invokeFile2, "path");
                  String jsonValue5 = JsonUtils.getJsonValue(invokeFile2, "version");
                  long longValue2 = Long.valueOf(JsonUtils.getJsonValue(invokeFile2, "id"));
                  InvokeFile invokeFile = new InvokeFile();
                  invokeFile.setId(longValue2);
                  invokeFile.setPath(jsonValue4);
                  invokeFile.setVersion(jsonValue5);
                  executeMethodAction.setInvokeFile(invokeFile);
                  JsonNode knowledgePackageWrapper2 = invokeFile2.get("knowledgePackageWrapper");
                  if (knowledgePackageWrapper2 != null) {
                     KnowledgePackageWrapper knowledgePackageWrapper = JsonUtils.parseKnowledgePackageWrapper(knowledgePackageWrapper2.toString());
                     invokeFile.setKnowledgePackageWrapper(knowledgePackageWrapper);
                  }
               }

               actions.add(executeMethodAction);
               break;
            case VariableAssign:
               String jsonValue6 = JsonUtils.getJsonValue(jsonNode2, "type");
               if (jsonValue6 != null && "predefine".contentEquals(jsonValue6)) {
                  PredefineAssignAction predefineAssignAction = new PredefineAssignAction();
                  predefineAssignAction.setUuid(JsonUtils.getJsonValue(jsonNode2, "uuid"));
                  predefineAssignAction.setName(JsonUtils.getJsonValue(jsonNode2, "name"));
                  predefineAssignAction.setVariableCategory(JsonUtils.getJsonValue(jsonNode2, "variableCategory"));
                  String jsonValue7 = JsonUtils.getJsonValue(jsonNode2, "datatype");
                  if (jsonValue7 != null && Datatype.isType(jsonValue7)) {
                     predefineAssignAction.setDatatype(Datatype.valueOf(jsonValue7));
                  }

                  predefineAssignAction.setVariableCategoryUuid(JsonUtils.getJsonValue(jsonNode2, "variableCategoryUuid"));
                  predefineAssignAction.setPropertyName(JsonUtils.getJsonValue(jsonNode2, "propertyName"));
                  predefineAssignAction.setPropertyLabel(JsonUtils.getJsonValue(jsonNode2, "propertyLabel"));
                  String jsonValue8 = JsonUtils.getJsonValue(jsonNode2, "propertyDatatype");
                  if (jsonValue8 != null) {
                     predefineAssignAction.setPropertyDatatype(Datatype.valueOf(jsonValue8));
                  }

                  predefineAssignAction.setPropertyUuid(JsonUtils.getJsonValue(jsonNode2, "propertyUuid"));
                  predefineAssignAction.setPropertyUuid(JsonUtils.getJsonValue(jsonNode2, "propertyUuid"));
                  predefineAssignAction.setPriority(Integer.valueOf(JsonUtils.getJsonValue(jsonNode2, "priority")));
                  predefineAssignAction.setValue(JsonUtils.parseValue(jsonNode2));
                  actions.add(predefineAssignAction);
                  break;
               }

               VariableAssignAction variableAssignAction = new VariableAssignAction();
               if (jsonValue6 != null) {
                  variableAssignAction.setType(LeftType.valueOf(jsonValue6));
               }

               variableAssignAction.setDatatype(Datatype.valueOf(JsonUtils.getJsonValue(jsonNode2, "datatype")));
               variableAssignAction.setVariableCategory(JsonUtils.getJsonValue(jsonNode2, "variableCategory"));
               variableAssignAction.setCategoryUuid(JsonUtils.getJsonValue(jsonNode2, "categoryUuid"));
               variableAssignAction.setVariableLabel(JsonUtils.getJsonValue(jsonNode2, "variableLabel"));
               variableAssignAction.setVariableName(JsonUtils.getJsonValue(jsonNode2, "variableName"));
               variableAssignAction.setUuid(JsonUtils.getJsonValue(jsonNode2, "uuid"));
               variableAssignAction.setKeyLabel(JsonUtils.getJsonValue(jsonNode2, "keyLabel"));
               variableAssignAction.setKeyName(JsonUtils.getJsonValue(jsonNode2, "keyName"));
               variableAssignAction.setKeyUuid(JsonUtils.getJsonValue(jsonNode2, "keyUuid"));
               variableAssignAction.setKeyCategoryUuid(JsonUtils.getJsonValue(jsonNode2, "keyCategoryUuid"));
               variableAssignAction.setPriority(Integer.valueOf(JsonUtils.getJsonValue(jsonNode2, "priority")));
               variableAssignAction.setValue(JsonUtils.parseValue(jsonNode2));
               actions.add(variableAssignAction);
               break;
            case ExecuteCommonFunction:
               ExecuteCommonFunctionAction executeCommonFunctionAction = new ExecuteCommonFunctionAction();
               executeCommonFunctionAction.setLabel(JsonUtils.getJsonValue(jsonNode2, "label"));
               executeCommonFunctionAction.setName(JsonUtils.getJsonValue(jsonNode2, "name"));
               executeCommonFunctionAction.setParameter(JsonUtils.parseCommonFunctionParameter(jsonNode2));
               executeCommonFunctionAction.setPriority(Integer.valueOf(JsonUtils.getJsonValue(jsonNode2, "priority")));
               actions.add(executeCommonFunctionAction);
               break;
            case Scoring:
               int number = Integer.valueOf(JsonUtils.getJsonValue(jsonNode2, "rowNumber"));
               String jsonValue9 = JsonUtils.getJsonValue(jsonNode2, "name");
               String jsonValue10 = JsonUtils.getJsonValue(jsonNode2, "weight");
               ScoringAction scoringAction = new ScoringAction(number, jsonValue9, jsonValue10);
               scoringAction.setValue(JsonUtils.parseValue(jsonNode2));
               actions.add(scoringAction);
               break;
            case TemplateAction:
               throw new RuleException("Unsupport action type:" + ActionType.TemplateAction);
         }
      }

      return actions;
   }
}
