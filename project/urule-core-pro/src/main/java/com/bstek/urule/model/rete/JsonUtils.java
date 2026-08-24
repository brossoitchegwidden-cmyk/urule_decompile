package com.bstek.urule.model.rete;

import com.bstek.urule.Configure;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rete.jsondeserializer.CommonFunctionValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.ConstantValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.InputValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.MathValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.MethodValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.ParameterValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.ParenValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.PredefineValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.SingIValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.ValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.VariableCategoryValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.VariableValueDeserializer;
import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.lhs.AccumulateLeftPart;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.CalculateItem;
import com.bstek.urule.model.rule.lhs.CalculateType;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.ConditionItem;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.Met;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.model.rule.lhs.PredefineLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.rule.loop.LoopTargetType;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class JsonUtils {
   private static List<ValueDeserializer> valueDeserializers = new ArrayList<>();

   public static String getJsonValue(JsonNode node, String propName) {
      return node.get(propName) != null ? node.get(propName).asText() : null;
   }

   public static ComplexArithmetic parseComplexArithmetic(JsonNode node) {
      JsonNode jsonNode = node.get("arithmetic");
      if (jsonNode == null) {
         return null;
      }

      ComplexArithmetic complexArithmetic = new ComplexArithmetic();
      complexArithmetic.setType(ArithmeticType.valueOf(getJsonValue(jsonNode, "type")));
      complexArithmetic.setValue(parseValue(jsonNode));
      return complexArithmetic;
   }

   public static List<Parameter> parseParameters(JsonNode node) {
      JsonNode jsonNode = node.get("parameters");
      if (jsonNode == null) {
         return null;
      }

      Iterator iterator = jsonNode.iterator();
      ArrayList parameters = new ArrayList();

      while (iterator.hasNext()) {
         JsonNode jsonNode2 = (JsonNode)iterator.next();
         Parameter parameter = new Parameter();
         parameter.setName(getJsonValue(jsonNode2, "name"));
         String jsonValue = getJsonValue(jsonNode2, "type");
         if (jsonValue != null) {
            parameter.setType(Datatype.valueOf(jsonValue));
         }

         String jsonValue2 = getJsonValue(jsonNode2, "valueType");
         if (jsonValue2 != null) {
            parameter.setValue(parseValue(jsonNode2));
         }

         parameter.setValue(parseValue(jsonNode2));
         parameters.add(parameter);
      }

      return parameters;
   }

   public static Value parseValueNode(JsonNode valueNode) {
      Value valueNode2 = null;
      ValueType valueType = ValueType.valueOf(getJsonValue(valueNode, "valueType"));

      for (ValueDeserializer valueDeserializer : valueDeserializers) {
         if (valueDeserializer.support(valueType)) {
            valueNode2 = valueDeserializer.deserialize(valueNode);
            break;
         }
      }

      return valueNode2;
   }

   public static KnowledgePackageWrapper parseKnowledgePackageWrapper(String content) {
      try {
         ObjectMapper objectMapper = JsonMapper.builder().build();
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
         objectMapper.getDeserializationConfig().with(simpleDateFormat);
         objectMapper.setDateFormat(simpleDateFormat);
         KnowledgePackageWrapper knowledgePackageWrapper = (KnowledgePackageWrapper)objectMapper.readValue(content, KnowledgePackageWrapper.class);
         knowledgePackageWrapper.buildDeserialize();
         return knowledgePackageWrapper;
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public static CommonFunctionParameter parseCommonFunctionParameter(JsonNode node) {
      JsonNode jsonNode = node.get("parameter");
      if (jsonNode == null) {
         return null;
      }

      CommonFunctionParameter commonFunctionParameter = new CommonFunctionParameter();
      commonFunctionParameter.setName(getJsonValue(jsonNode, "name"));
      commonFunctionParameter.setProperty(getJsonValue(jsonNode, "property"));
      commonFunctionParameter.setPropertyLabel(getJsonValue(jsonNode, "propertyLabel"));
      commonFunctionParameter.setObjectParameter(parseValueNode(jsonNode.get("objectParameter")));
      return commonFunctionParameter;
   }

   public static Criteria parseCriteria(JsonNode jsonNode) {
      Criteria criteria = new Criteria();
      JsonNode op2 = jsonNode.get("op");
      if (op2 != null && op2.textValue() != null) {
         String text = op2.textValue();
         Op op = Op.valueOf(text);
         criteria.setOp(op);
      }

      if (jsonNode.has("file") && jsonNode.get("file") != null) {
         String text2 = jsonNode.get("file").textValue();
         criteria.setFile(text2);
      }

      JsonNode necessaryClassList = jsonNode.get("necessaryClassList");
      if (necessaryClassList != null && necessaryClassList instanceof ArrayNode) {
         ArrayNode arrayNode = (ArrayNode)necessaryClassList;
         Iterator iterator = arrayNode.elements();

         while (iterator.hasNext()) {
            String text3 = ((JsonNode)iterator.next()).asText();
            criteria.addNecessaryClass(text3);
         }
      }

      JsonNode left2 = jsonNode.get("left");
      Left left = new Left();
      criteria.setLeft(left);
      String jsonValue = getJsonValue(left2, "type");
      JsonNode leftPart = left2.get("leftPart");
      left.setType(LeftType.valueOf(jsonValue));
      switch (left.getType()) {
         case function:
            FunctionLeftPart functionLeftPart = new FunctionLeftPart();
            functionLeftPart.setName(getJsonValue(leftPart, "name"));
            functionLeftPart.setParameters(parseParameters(leftPart));
            left.setLeftPart(functionLeftPart);
            break;
         case method:
            MethodLeftPart methodLeftPart = new MethodLeftPart();
            methodLeftPart.setBeanId(getJsonValue(leftPart, "beanId"));
            methodLeftPart.setBeanLabel(getJsonValue(leftPart, "beanLabel"));
            methodLeftPart.setUuid(getJsonValue(leftPart, "uuid"));
            methodLeftPart.setCategoryUuid(getJsonValue(leftPart, "categoryUuid"));
            methodLeftPart.setMethodLabel(getJsonValue(leftPart, "methodLabel"));
            methodLeftPart.setMethodName(getJsonValue(leftPart, "methodName"));
            methodLeftPart.setParameters(parseParameters(leftPart));
            left.setLeftPart(methodLeftPart);
            break;
         case commonfunction:
            CommonFunctionLeftPart commonFunctionLeftPart = new CommonFunctionLeftPart();
            commonFunctionLeftPart.setLabel(getJsonValue(leftPart, "label"));
            commonFunctionLeftPart.setName(getJsonValue(leftPart, "name"));
            commonFunctionLeftPart.setParameter(parseCommonFunctionParameter(leftPart));
            left.setLeftPart(commonFunctionLeftPart);
            break;
         case operatecollection:
            AccumulateLeftPart accumulateLeftPart = new AccumulateLeftPart();
            String jsonValue2 = getJsonValue(leftPart, "loopTargetType");
            accumulateLeftPart.setLoopTargetType(LoopTargetType.valueOf(jsonValue2));
            accumulateLeftPart.setJunction(parseJunction(leftPart));
            JsonNode loopTarget2 = leftPart.get("loopTarget");
            LoopTarget loopTarget = new LoopTarget();
            loopTarget.setValue(parseValue(loopTarget2));
            accumulateLeftPart.setLoopTarget(loopTarget);
            JsonNode calculateItems = leftPart.get("calculateItems");
            if (calculateItems != null) {
               ArrayList items = new ArrayList();

               for (JsonNode jsonNode2 : calculateItems) {
                  CalculateItem calculateItem = new CalculateItem();
                  calculateItem.setType(CalculateType.valueOf(getJsonValue(jsonNode2, "type")));
                  Value localValue = parseValue(jsonNode2);
                  calculateItem.setValue(localValue);
                  boolean flag = Boolean.valueOf(getJsonValue(jsonNode2, "enableAssignment"));
                  calculateItem.setEnableAssignment(flag);
                  if (flag) {
                     calculateItem.setAssignDatatype(Datatype.valueOf(getJsonValue(jsonNode2, "assignDatatype")));
                     calculateItem.setAssignTargetType(getJsonValue(jsonNode2, "assignTargetType"));
                     calculateItem.setAssignCategoryUuid(getJsonValue(jsonNode2, "assignCategoryUuid"));
                     calculateItem.setAssignVariableUuid(getJsonValue(jsonNode2, "assignVariableUuid"));
                     calculateItem.setAssignVariable(getJsonValue(jsonNode2, "assignVariable"));
                     calculateItem.setAssignVariableCategory(getJsonValue(jsonNode2, "assignVariableCategory"));
                     calculateItem.setAssignVariableLabel(getJsonValue(jsonNode2, "assignVariableLabel"));
                     calculateItem.setKeyLabel(getJsonValue(jsonNode2, "keyLabel"));
                     calculateItem.setKeyName(getJsonValue(jsonNode2, "keyName"));
                     calculateItem.setType(CalculateType.valueOf(getJsonValue(jsonNode2, "type")));
                  }

                  items.add(calculateItem);
               }

               accumulateLeftPart.setCalculateItems(items);
            }

            JsonNode conditionItems = leftPart.get("conditionItems");
            if (conditionItems != null) {
               ArrayList items2 = new ArrayList();

               for (JsonNode jsonNode3 : conditionItems) {
                  ConditionItem conditionItem = new ConditionItem();
                  conditionItem.setLeft(getJsonValue(jsonNode3, "left"));
                  conditionItem.setOp(Op.valueOf(getJsonValue(jsonNode3, "op")));
                  conditionItem.setValue(parseValue(jsonNode3));
                  items2.add(conditionItem);
               }

               accumulateLeftPart.setConditionItems(items2);
            }

            left.setLeftPart(accumulateLeftPart);
            break;
         case predefine:
            PredefineLeftPart predefineLeftPart = new PredefineLeftPart();
            predefineLeftPart.setName(getJsonValue(leftPart, "name"));
            String jsonValue3 = getJsonValue(leftPart, "datatype");
            if (jsonValue3 != null) {
               predefineLeftPart.setDatatype(Datatype.valueOf(jsonValue3));
            }

            predefineLeftPart.setUuid(getJsonValue(leftPart, "uuid"));
            predefineLeftPart.setPropertyUuid(getJsonValue(leftPart, "propertyUuid"));
            predefineLeftPart.setPropertyName(getJsonValue(leftPart, "propertyName"));
            predefineLeftPart.setPropertyLabel(getJsonValue(leftPart, "propertyLabel"));
            predefineLeftPart.setVariableCategoryUuid(getJsonValue(leftPart, "variableCategoryUuid"));
            predefineLeftPart.setVariableCategory(getJsonValue(leftPart, "variableCategory"));
            left.setLeftPart(predefineLeftPart);
            break;
         default:
            VariableLeftPart variableLeftPart = new VariableLeftPart();
            variableLeftPart.setVariableCategory(getJsonValue(leftPart, "variableCategory"));
            variableLeftPart.setVariableLabel(getJsonValue(leftPart, "variableLabel"));
            variableLeftPart.setVariableName(getJsonValue(leftPart, "variableName"));
            variableLeftPart.setCategoryUuid(getJsonValue(leftPart, "categoryUuid"));
            variableLeftPart.setUuid(getJsonValue(leftPart, "uuid"));
            variableLeftPart.setKeyLabel(getJsonValue(leftPart, "keyLabel"));
            variableLeftPart.setKeyName(getJsonValue(leftPart, "keyName"));
            String jsonValue4 = getJsonValue(leftPart, "datatype");
            if (StringUtils.isNotBlank(jsonValue4)) {
               variableLeftPart.setDatatype(Datatype.valueOf(jsonValue4));
            }

            left.setLeftPart(variableLeftPart);
      }

      left.setArithmetic(parseComplexArithmetic(left2));
      Value localValue2 = parseValue(jsonNode);
      if (localValue2 != null) {
         criteria.setValue(localValue2);
      }

      return criteria;
   }

   public static Junction parseJunction(JsonNode junctionNode) {
      String jsonValue = getJsonValue(junctionNode, "junctionType");
      if (jsonValue != null) {
         return doParseJunction(junctionNode);
      }

      JsonNode jsonNode = junctionNode.get("junction");
      return jsonNode == null ? null : doParseJunction(jsonNode);
   }

   private static Junction doParseJunction(JsonNode jsonNode) {
      String jsonValue = getJsonValue(jsonNode, "junctionType");
      Junction junction = null;
      if (jsonValue.equals("and")) {
         junction = new And();
      } else if (jsonValue.equals("or")) {
         junction = new Or();
      } else if (jsonValue.equals("met")) {
         Met met = new Met();
         met.setMet(Integer.parseInt(getJsonValue(jsonNode, "met")));
         String jsonValue2 = getJsonValue(jsonNode, "only");
         if (jsonValue2 != null) {
            met.setOnly(Boolean.parseBoolean(jsonValue2));
         }

         junction = met;
      }

      JsonNode criterions2 = jsonNode.get("criterions");
      if (criterions2 != null) {
         List criterions = parseCriterions(criterions2);
         junction.setCriterions(criterions);
      }

      return junction;
   }

   public static List<Criterion> parseCriterions(JsonNode criterionsNode) {
      Iterator iterator = criterionsNode.iterator();
      ArrayList criterions = new ArrayList();

      while (iterator.hasNext()) {
         JsonNode jsonNode = (JsonNode)iterator.next();
         String jsonValue = getJsonValue(jsonNode, "junctionType");
         if (jsonValue != null) {
            criterions.add(doParseJunction(jsonNode));
         } else {
            Criteria criteria = parseCriteria(jsonNode);
            criterions.add(criteria);
         }
      }

      return criterions;
   }

   public static Value parseValue(JsonNode node) {
      JsonNode jsonNode = node.get("value");
      return jsonNode == null ? null : parseValueNode(jsonNode);
   }

   public static List<ValueDeserializer> getValueDeserializers() {
      return valueDeserializers;
   }

   static {
      valueDeserializers.add(new ConstantValueDeserializer());
      valueDeserializers.add(new InputValueDeserializer());
      valueDeserializers.add(new ParameterValueDeserializer());
      valueDeserializers.add(new MethodValueDeserializer());
      valueDeserializers.add(new VariableCategoryValueDeserializer());
      valueDeserializers.add(new VariableValueDeserializer());
      valueDeserializers.add(new CommonFunctionValueDeserializer());
      valueDeserializers.add(new ParenValueDeserializer());
      valueDeserializers.add(new MathValueDeserializer());
      valueDeserializers.add(new SingIValueDeserializer());
      valueDeserializers.add(new PredefineValueDeserializer());
   }
}
