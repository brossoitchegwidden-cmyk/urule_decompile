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
   protected Rule parseRule(JsonParser var1, JsonNode var2) {
      SimpleDateFormat var3 = new SimpleDateFormat(Configure.getDateFormat());

      try {
         JsonNode var4 = var2.get("rule");
         if (var4 == null) {
            var4 = var2;
         }

         Rule var5 = null;
         String var6 = JsonUtils.getJsonValue(var4, "scoringType");
         if (StringUtils.isNotBlank(var6)) {
            ScoringType var7 = ScoringType.valueOf(var6);
            ScoreRule var8 = new ScoreRule();
            var8.setScoringType(var7);
            this.buildScoreRule(var1, var4, var8);
            var5 = var8;
         } else {
            String var21 = JsonUtils.getJsonValue(var4, "loopRule");
            if (var21 != null) {
               boolean var23 = Boolean.valueOf(var21);
               if (var23) {
                  LoopRule var9 = new LoopRule();
                  this.buildLoopRule(var4, var9);
                  var5 = var9;
               } else {
                  var5 = new Rule();
               }
            } else {
               var5 = new Rule();
            }
         }

         String var22 = JsonUtils.getJsonValue(var4, "withElse");
         if (StringUtils.isNotBlank(var22)) {
            var5.setWithElse(Boolean.valueOf(var22));
         }

         var5.setRemark(JsonUtils.getJsonValue(var4, "remark"));
         var5.setMutexGroup(JsonUtils.getJsonValue(var4, "mutexGroup"));
         var5.setPendedGroup(JsonUtils.getJsonValue(var4, "pendedGroup"));
         String var24 = JsonUtils.getJsonValue(var4, "autoFocus");
         if (var24 != null) {
            var5.setAutoFocus(Boolean.valueOf(var24));
         }

         String var25 = JsonUtils.getJsonValue(var4, "loop");
         if (var25 != null) {
            var5.setLoop(Boolean.valueOf(var25));
         }

         String var10 = JsonUtils.getJsonValue(var4, "effectiveDate");
         if (var10 != null) {
            var5.setEffectiveDate(var3.parse(var10));
         }

         String var11 = JsonUtils.getJsonValue(var4, "enabled");
         if (var11 != null) {
            var5.setEnabled(Boolean.valueOf(var11));
         }

         String var12 = JsonUtils.getJsonValue(var4, "debug");
         if (var12 != null) {
            var5.setDebug(Boolean.valueOf(var12));
         }

         String var13 = JsonUtils.getJsonValue(var4, "expiresDate");
         if (var13 != null) {
            var5.setExpiresDate(var3.parse(var13));
         }

         var5.setName(JsonUtils.getJsonValue(var4, "name"));
         var5.setFile(JsonUtils.getJsonValue(var4, "file"));
         String var14 = JsonUtils.getJsonValue(var4, "salience");
         if (var14 != null) {
            var5.setSalience(Integer.valueOf(var14));
         }

         Rhs var15 = new Rhs();
         var5.setRhs(var15);
         JsonNode var16 = var4.get("rhs");
         if (var16 != null) {
            var15.setActions(this.parseActions(var16));
         }

         JsonNode var17 = var4.get("other");
         if (var17 != null) {
            Other var18 = new Other();
            var5.setOther(var18);
            var18.setActions(this.parseActions(var17));
         }

         if (var5.isWithElse()) {
            Utils.buildElseRule(var5);
         }

         return var5;
      } catch (ParseException var19) {
         throw new RuleException(var19);
      }
   }

   private void buildScoreRule(JsonParser var1, JsonNode var2, ScoreRule var3) {
      var3.setScoringBean(JsonUtils.getJsonValue(var2, "scoringBean"));
      AssignTargetType var4 = AssignTargetType.valueOf(JsonUtils.getJsonValue(var2, "assignTargetType"));
      var3.setAssignTargetType(var4);
      var3.setVariableCategory(JsonUtils.getJsonValue(var2, "variableCategory"));
      var3.setVariableName(JsonUtils.getJsonValue(var2, "variableName"));
      var3.setVariableLabel(JsonUtils.getJsonValue(var2, "variableLabel"));
      var3.setKeyLabel(JsonUtils.getJsonValue(var2, "keyLabel"));
      var3.setKeyName(JsonUtils.getJsonValue(var2, "keyName"));
      String var5 = JsonUtils.getJsonValue(var2, "datatype");
      if (StringUtils.isNotBlank(var5)) {
         var3.setDatatype(Datatype.valueOf(var5));
      }

      try {
         JsonNode var6 = var2.get("knowledgePackageWrapper");
         ObjectMapper var7 = (ObjectMapper)var1.getCodec();
         KnowledgePackageWrapper var8 = (KnowledgePackageWrapper)var7.convertValue(var6, KnowledgePackageWrapper.class);
         var8.buildDeserialize();
         var3.setKnowledgePackageWrapper(var8);
      } catch (Exception var9) {
         throw new RuleException(var9);
      }
   }

   private void buildLoopRule(JsonNode var1, LoopRule var2) {
      JsonNode var3 = var1.get("loopTarget");
      String var4 = JsonUtils.getJsonValue(var1, "loopTargetType");
      if (StringUtils.isNotBlank(var4)) {
         var2.setLoopTargetType(LoopTargetType.valueOf(var4));
      }

      if (var3 != null) {
         LoopTarget var5 = new LoopTarget();
         Value var6 = JsonUtils.parseValue(var3);
         var5.setValue(var6);
         var2.setLoopTarget(var5);
      }

      JsonNode var9 = var1.get("loopStart");
      if (var9 != null) {
         List var10 = this.parseActions(var9);
         LoopStart var7 = new LoopStart();
         var7.setActions(var10);
         var2.setLoopStart(var7);
      }

      JsonNode var11 = var1.get("loopEnd");
      if (var11 != null) {
         List var12 = this.parseActions(var11);
         LoopEnd var8 = new LoopEnd();
         var8.setActions(var12);
         var2.setLoopEnd(var8);
      }

      JsonNode var13 = var1.get("knowledgePackageWrapper");
      if (var13 != null) {
         KnowledgePackageWrapper var14 = JsonUtils.parseKnowledgePackageWrapper(var13.toString());
         var2.setKnowledgePackageWrapper(var14);
      }
   }

   private List<Action> parseActions(JsonNode var1) {
      ArrayList var2 = new ArrayList();
      JsonNode var3 = var1.get("actions");
      if (var3 == null) {
         return var2;
      }

      for (JsonNode var5 : var3) {
         ActionType var6 = ActionType.valueOf(JsonUtils.getJsonValue(var5, "actionType"));
         switch (var6) {
            case ConsolePrint:
               ConsolePrintAction var7 = new ConsolePrintAction();
               var7.setValue(JsonUtils.parseValue(var5));
               var7.setPriority(Integer.valueOf(JsonUtils.getJsonValue(var5, "priority")));
               var2.add(var7);
               break;
            case ExecuteMethod:
               ExecuteMethodAction var8 = new ExecuteMethodAction();
               var8.setBeanId(JsonUtils.getJsonValue(var5, "beanId"));
               var8.setBeanLabel(JsonUtils.getJsonValue(var5, "beanLabel"));
               var8.setMethodLabel(JsonUtils.getJsonValue(var5, "methodLabel"));
               var8.setPriority(Integer.valueOf(JsonUtils.getJsonValue(var5, "priority")));
               var8.setMethodName(JsonUtils.getJsonValue(var5, "methodName"));
               SpringBean var9 = ActionUtils.getBuiltinAction(var8.getBeanId());
               if (var9 != null) {
                  var8.setBeanELabel(var9.getEname());
               }

               var8.setParameters(JsonUtils.parseParameters(var5));
               JsonNode var10 = var5.get("invokeKnowledgePackage");
               if (var10 != null) {
                  String var11 = JsonUtils.getJsonValue(var10, "project");
                  String var20 = JsonUtils.getJsonValue(var10, "name");
                  String var24 = JsonUtils.getJsonValue(var10, "code");
                  long var27 = Long.valueOf(JsonUtils.getJsonValue(var10, "id"));
                  var8.setInvokeKnowledgePackage(new InvokeKnowledgePackage(var11, var20, var27, var24));
               }

               JsonNode var19 = var5.get("invokeFile");
               if (var19 != null) {
                  String var21 = JsonUtils.getJsonValue(var19, "path");
                  String var25 = JsonUtils.getJsonValue(var19, "version");
                  long var28 = Long.valueOf(JsonUtils.getJsonValue(var19, "id"));
                  InvokeFile var30 = new InvokeFile();
                  var30.setId(var28);
                  var30.setPath(var21);
                  var30.setVersion(var25);
                  var8.setInvokeFile(var30);
                  JsonNode var31 = var19.get("knowledgePackageWrapper");
                  if (var31 != null) {
                     KnowledgePackageWrapper var18 = JsonUtils.parseKnowledgePackageWrapper(var31.toString());
                     var30.setKnowledgePackageWrapper(var18);
                  }
               }

               var2.add(var8);
               break;
            case VariableAssign:
               String var12 = JsonUtils.getJsonValue(var5, "type");
               if (var12 != null && "predefine".contentEquals(var12)) {
                  PredefineAssignAction var23 = new PredefineAssignAction();
                  var23.setUuid(JsonUtils.getJsonValue(var5, "uuid"));
                  var23.setName(JsonUtils.getJsonValue(var5, "name"));
                  var23.setVariableCategory(JsonUtils.getJsonValue(var5, "variableCategory"));
                  String var26 = JsonUtils.getJsonValue(var5, "datatype");
                  if (var26 != null && Datatype.isType(var26)) {
                     var23.setDatatype(Datatype.valueOf(var26));
                  }

                  var23.setVariableCategoryUuid(JsonUtils.getJsonValue(var5, "variableCategoryUuid"));
                  var23.setPropertyName(JsonUtils.getJsonValue(var5, "propertyName"));
                  var23.setPropertyLabel(JsonUtils.getJsonValue(var5, "propertyLabel"));
                  String var29 = JsonUtils.getJsonValue(var5, "propertyDatatype");
                  if (var29 != null) {
                     var23.setPropertyDatatype(Datatype.valueOf(var29));
                  }

                  var23.setPropertyUuid(JsonUtils.getJsonValue(var5, "propertyUuid"));
                  var23.setPropertyUuid(JsonUtils.getJsonValue(var5, "propertyUuid"));
                  var23.setPriority(Integer.valueOf(JsonUtils.getJsonValue(var5, "priority")));
                  var23.setValue(JsonUtils.parseValue(var5));
                  var2.add(var23);
                  break;
               }

               VariableAssignAction var22 = new VariableAssignAction();
               if (var12 != null) {
                  var22.setType(LeftType.valueOf(var12));
               }

               var22.setDatatype(Datatype.valueOf(JsonUtils.getJsonValue(var5, "datatype")));
               var22.setVariableCategory(JsonUtils.getJsonValue(var5, "variableCategory"));
               var22.setCategoryUuid(JsonUtils.getJsonValue(var5, "categoryUuid"));
               var22.setVariableLabel(JsonUtils.getJsonValue(var5, "variableLabel"));
               var22.setVariableName(JsonUtils.getJsonValue(var5, "variableName"));
               var22.setUuid(JsonUtils.getJsonValue(var5, "uuid"));
               var22.setKeyLabel(JsonUtils.getJsonValue(var5, "keyLabel"));
               var22.setKeyName(JsonUtils.getJsonValue(var5, "keyName"));
               var22.setKeyUuid(JsonUtils.getJsonValue(var5, "keyUuid"));
               var22.setKeyCategoryUuid(JsonUtils.getJsonValue(var5, "keyCategoryUuid"));
               var22.setPriority(Integer.valueOf(JsonUtils.getJsonValue(var5, "priority")));
               var22.setValue(JsonUtils.parseValue(var5));
               var2.add(var22);
               break;
            case ExecuteCommonFunction:
               ExecuteCommonFunctionAction var13 = new ExecuteCommonFunctionAction();
               var13.setLabel(JsonUtils.getJsonValue(var5, "label"));
               var13.setName(JsonUtils.getJsonValue(var5, "name"));
               var13.setParameter(JsonUtils.parseCommonFunctionParameter(var5));
               var13.setPriority(Integer.valueOf(JsonUtils.getJsonValue(var5, "priority")));
               var2.add(var13);
               break;
            case Scoring:
               int var14 = Integer.valueOf(JsonUtils.getJsonValue(var5, "rowNumber"));
               String var15 = JsonUtils.getJsonValue(var5, "name");
               String var16 = JsonUtils.getJsonValue(var5, "weight");
               ScoringAction var17 = new ScoringAction(var14, var15, var16);
               var17.setValue(JsonUtils.parseValue(var5));
               var2.add(var17);
               break;
            case TemplateAction:
               throw new RuleException("Unsupport action type:" + ActionType.TemplateAction);
         }
      }

      return var2;
   }
}
