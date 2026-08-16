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

   public static String getJsonValue(JsonNode var0, String var1) {
      return var0.get(var1) != null ? var0.get(var1).asText() : null;
   }

   public static ComplexArithmetic parseComplexArithmetic(JsonNode var0) {
      JsonNode var1 = var0.get("arithmetic");
      if (var1 == null) {
         return null;
      }

      ComplexArithmetic var2 = new ComplexArithmetic();
      var2.setType(ArithmeticType.valueOf(getJsonValue(var1, "type")));
      var2.setValue(parseValue(var1));
      return var2;
   }

   public static List<Parameter> parseParameters(JsonNode var0) {
      JsonNode var1 = var0.get("parameters");
      if (var1 == null) {
         return null;
      }

      Iterator var2 = var1.iterator();
      ArrayList var3 = new ArrayList();

      while (var2.hasNext()) {
         JsonNode var4 = (JsonNode)var2.next();
         Parameter var5 = new Parameter();
         var5.setName(getJsonValue(var4, "name"));
         String var6 = getJsonValue(var4, "type");
         if (var6 != null) {
            var5.setType(Datatype.valueOf(var6));
         }

         String var7 = getJsonValue(var4, "valueType");
         if (var7 != null) {
            var5.setValue(parseValue(var4));
         }

         var5.setValue(parseValue(var4));
         var3.add(var5);
      }

      return var3;
   }

   public static Value parseValueNode(JsonNode var0) {
      Value var1 = null;
      ValueType var2 = ValueType.valueOf(getJsonValue(var0, "valueType"));

      for (ValueDeserializer var4 : valueDeserializers) {
         if (var4.support(var2)) {
            var1 = var4.deserialize(var0);
            break;
         }
      }

      return var1;
   }

   public static KnowledgePackageWrapper parseKnowledgePackageWrapper(String var0) {
      try {
         ObjectMapper var1 = JsonMapper.builder().build();
         SimpleDateFormat var2 = new SimpleDateFormat(Configure.getDateFormat());
         var1.getDeserializationConfig().with(var2);
         var1.setDateFormat(var2);
         KnowledgePackageWrapper var3 = (KnowledgePackageWrapper)var1.readValue(var0, KnowledgePackageWrapper.class);
         var3.buildDeserialize();
         return var3;
      } catch (Exception var4) {
         throw new RuleException(var4);
      }
   }

   public static CommonFunctionParameter parseCommonFunctionParameter(JsonNode var0) {
      JsonNode var1 = var0.get("parameter");
      if (var1 == null) {
         return null;
      }

      CommonFunctionParameter var2 = new CommonFunctionParameter();
      var2.setName(getJsonValue(var1, "name"));
      var2.setProperty(getJsonValue(var1, "property"));
      var2.setPropertyLabel(getJsonValue(var1, "propertyLabel"));
      var2.setObjectParameter(parseValueNode(var1.get("objectParameter")));
      return var2;
   }

   public static Criteria parseCriteria(JsonNode var0) {
      Criteria var1 = new Criteria();
      JsonNode var2 = var0.get("op");
      if (var2 != null && var2.textValue() != null) {
         String var3 = var2.textValue();
         Op var4 = Op.valueOf(var3);
         var1.setOp(var4);
      }

      if (var0.has("file") && var0.get("file") != null) {
         String var22 = var0.get("file").textValue();
         var1.setFile(var22);
      }

      JsonNode var23 = var0.get("necessaryClassList");
      if (var23 != null && var23 instanceof ArrayNode) {
         ArrayNode var24 = (ArrayNode)var23;
         Iterator var5 = var24.elements();

         while (var5.hasNext()) {
            String var6 = ((JsonNode)var5.next()).asText();
            var1.addNecessaryClass(var6);
         }
      }

      JsonNode var25 = var0.get("left");
      Left var26 = new Left();
      var1.setLeft(var26);
      String var27 = getJsonValue(var25, "type");
      JsonNode var7 = var25.get("leftPart");
      var26.setType(LeftType.valueOf(var27));
      switch (var26.getType()) {
         case function:
            FunctionLeftPart var8 = new FunctionLeftPart();
            var8.setName(getJsonValue(var7, "name"));
            var8.setParameters(parseParameters(var7));
            var26.setLeftPart(var8);
            break;
         case method:
            MethodLeftPart var9 = new MethodLeftPart();
            var9.setBeanId(getJsonValue(var7, "beanId"));
            var9.setBeanLabel(getJsonValue(var7, "beanLabel"));
            var9.setUuid(getJsonValue(var7, "uuid"));
            var9.setCategoryUuid(getJsonValue(var7, "categoryUuid"));
            var9.setMethodLabel(getJsonValue(var7, "methodLabel"));
            var9.setMethodName(getJsonValue(var7, "methodName"));
            var9.setParameters(parseParameters(var7));
            var26.setLeftPart(var9);
            break;
         case commonfunction:
            CommonFunctionLeftPart var10 = new CommonFunctionLeftPart();
            var10.setLabel(getJsonValue(var7, "label"));
            var10.setName(getJsonValue(var7, "name"));
            var10.setParameter(parseCommonFunctionParameter(var7));
            var26.setLeftPart(var10);
            break;
         case operatecollection:
            AccumulateLeftPart var11 = new AccumulateLeftPart();
            String var12 = getJsonValue(var7, "loopTargetType");
            var11.setLoopTargetType(LoopTargetType.valueOf(var12));
            var11.setJunction(parseJunction(var7));
            JsonNode var13 = var7.get("loopTarget");
            LoopTarget var14 = new LoopTarget();
            var14.setValue(parseValue(var13));
            var11.setLoopTarget(var14);
            JsonNode var15 = var7.get("calculateItems");
            if (var15 != null) {
               ArrayList var16 = new ArrayList();

               for (JsonNode var32 : var15) {
                  CalculateItem var34 = new CalculateItem();
                  var34.setType(CalculateType.valueOf(getJsonValue(var32, "type")));
                  Value var36 = parseValue(var32);
                  var34.setValue(var36);
                  boolean var21 = Boolean.valueOf(getJsonValue(var32, "enableAssignment"));
                  var34.setEnableAssignment(var21);
                  if (var21) {
                     var34.setAssignDatatype(Datatype.valueOf(getJsonValue(var32, "assignDatatype")));
                     var34.setAssignTargetType(getJsonValue(var32, "assignTargetType"));
                     var34.setAssignCategoryUuid(getJsonValue(var32, "assignCategoryUuid"));
                     var34.setAssignVariableUuid(getJsonValue(var32, "assignVariableUuid"));
                     var34.setAssignVariable(getJsonValue(var32, "assignVariable"));
                     var34.setAssignVariableCategory(getJsonValue(var32, "assignVariableCategory"));
                     var34.setAssignVariableLabel(getJsonValue(var32, "assignVariableLabel"));
                     var34.setKeyLabel(getJsonValue(var32, "keyLabel"));
                     var34.setKeyName(getJsonValue(var32, "keyName"));
                     var34.setType(CalculateType.valueOf(getJsonValue(var32, "type")));
                  }

                  var16.add(var34);
               }

               var11.setCalculateItems(var16);
            }

            JsonNode var29 = var7.get("conditionItems");
            if (var29 != null) {
               ArrayList var31 = new ArrayList();

               for (JsonNode var35 : var29) {
                  ConditionItem var37 = new ConditionItem();
                  var37.setLeft(getJsonValue(var35, "left"));
                  var37.setOp(Op.valueOf(getJsonValue(var35, "op")));
                  var37.setValue(parseValue(var35));
                  var31.add(var37);
               }

               var11.setConditionItems(var31);
            }

            var26.setLeftPart(var11);
            break;
         case predefine:
            PredefineLeftPart var17 = new PredefineLeftPart();
            var17.setName(getJsonValue(var7, "name"));
            String var18 = getJsonValue(var7, "datatype");
            if (var18 != null) {
               var17.setDatatype(Datatype.valueOf(var18));
            }

            var17.setUuid(getJsonValue(var7, "uuid"));
            var17.setPropertyUuid(getJsonValue(var7, "propertyUuid"));
            var17.setPropertyName(getJsonValue(var7, "propertyName"));
            var17.setPropertyLabel(getJsonValue(var7, "propertyLabel"));
            var17.setVariableCategoryUuid(getJsonValue(var7, "variableCategoryUuid"));
            var17.setVariableCategory(getJsonValue(var7, "variableCategory"));
            var26.setLeftPart(var17);
            break;
         default:
            VariableLeftPart var19 = new VariableLeftPart();
            var19.setVariableCategory(getJsonValue(var7, "variableCategory"));
            var19.setVariableLabel(getJsonValue(var7, "variableLabel"));
            var19.setVariableName(getJsonValue(var7, "variableName"));
            var19.setCategoryUuid(getJsonValue(var7, "categoryUuid"));
            var19.setUuid(getJsonValue(var7, "uuid"));
            var19.setKeyLabel(getJsonValue(var7, "keyLabel"));
            var19.setKeyName(getJsonValue(var7, "keyName"));
            String var20 = getJsonValue(var7, "datatype");
            if (StringUtils.isNotBlank(var20)) {
               var19.setDatatype(Datatype.valueOf(var20));
            }

            var26.setLeftPart(var19);
      }

      var26.setArithmetic(parseComplexArithmetic(var25));
      Value var28 = parseValue(var0);
      if (var28 != null) {
         var1.setValue(var28);
      }

      return var1;
   }

   public static Junction parseJunction(JsonNode var0) {
      String var1 = getJsonValue(var0, "junctionType");
      if (var1 != null) {
         return doParseJunction(var0);
      }

      JsonNode var2 = var0.get("junction");
      return var2 == null ? null : doParseJunction(var2);
   }

   private static Junction doParseJunction(JsonNode var0) {
      String var1 = getJsonValue(var0, "junctionType");
      Junction var2 = null;
      if (var1.equals("and")) {
         var2 = new And();
      } else if (var1.equals("or")) {
         var2 = new Or();
      } else if (var1.equals("met")) {
         Met var3 = new Met();
         var3.setMet(Integer.parseInt(getJsonValue(var0, "met")));
         String var4 = getJsonValue(var0, "only");
         if (var4 != null) {
            var3.setOnly(Boolean.parseBoolean(var4));
         }

         var2 = var3;
      }

      JsonNode var5 = var0.get("criterions");
      if (var5 != null) {
         List var6 = parseCriterions(var5);
         var2.setCriterions(var6);
      }

      return var2;
   }

   public static List<Criterion> parseCriterions(JsonNode var0) {
      Iterator var1 = var0.iterator();
      ArrayList var2 = new ArrayList();

      while (var1.hasNext()) {
         JsonNode var3 = (JsonNode)var1.next();
         String var4 = getJsonValue(var3, "junctionType");
         if (var4 != null) {
            var2.add(doParseJunction(var3));
         } else {
            Criteria var5 = parseCriteria(var3);
            var2.add(var5);
         }
      }

      return var2;
   }

   public static Value parseValue(JsonNode var0) {
      JsonNode var1 = var0.get("value");
      return var1 == null ? null : parseValueNode(var1);
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
