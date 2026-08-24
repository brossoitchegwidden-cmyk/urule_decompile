package com.bstek.urule.parse;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.SimpleArithmetic;
import com.bstek.urule.model.rule.SimpleArithmeticValue;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.AccumulateLeftPart;
import com.bstek.urule.model.rule.lhs.CalculateItem;
import com.bstek.urule.model.rule.lhs.CalculateType;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.ConditionItem;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.PredefineLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.rule.loop.LoopTargetType;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/** Parses the left-hand expression of a rule condition. */
public class LeftParser extends AbstractParser<Left> {
   private ComplexArithmeticParser complexArithmeticParser;
   private SimpleArithmeticParser simpleArithmeticParser;
   private ValueParser valueParser;
   private JunctionParser junctionParser;

   public Left parse(Element element) {
      Left left = new Left();
      String text = element.attributeValue("type");
      if (StringUtils.isNotEmpty(text)) {
         left.setType(LeftType.valueOf(text));
      } else {
         left.setType(LeftType.variable);
      }

      switch (left.getType()) {
         case variable:
            left.setLeftPart(this.buildVariableLeftPart(element));
            break;
         case function:
            left.setLeftPart(this.buildFunctionLeftPart(element));
            break;
         case method:
            left.setLeftPart(this.buildMethodLeftPart(element));
            break;
         case parameter:
            left.setLeftPart(this.buildVariableLeftPart(element));
            break;
         case predefine:
            left.setLeftPart(this.buildPredefineLeftPart(element));
            break;
         case commonfunction:
            left.setLeftPart(this.buildCommonFunctionLeftPart(element));
            break;
         case operatecollection:
            left.setLeftPart(this.findAccumulateLeftPart(element));
            break;
         case all:
            throw new RuleException("Not support all type.");
         case exist:
            throw new RuleException("Not support exist type.");
         case collect:
            throw new RuleException("Not support collect type.");
         case eval:
            throw new RuleException("Not support eval type.");
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.complexArithmeticParser.support(name)) {
               left.setArithmetic(this.complexArithmeticParser.parse(element2));
            } else if (this.simpleArithmeticParser.support(name)) {
               SimpleArithmetic simpleArithmetic = this.simpleArithmeticParser.parse(element2);
               left.setArithmetic(this.convertArithmetic(simpleArithmetic));
            }
         }
      }

      return left;
   }

   private AccumulateLeftPart findAccumulateLeftPart(Element element) {
      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("accumulate")) {
               return this.buildAccumulateLeftPart(element2);
            }
         }
      }

      return null;
   }

   private AccumulateLeftPart buildAccumulateLeftPart(Element element) {
      AccumulateLeftPart accumulateLeftPart = new AccumulateLeftPart();
      ArrayList items = new ArrayList();
      ArrayList items2 = new ArrayList();
      accumulateLeftPart.setCalculateItems(items2);
      accumulateLeftPart.setConditionItems(items);
      String text = element.attributeValue("target-type");
      LoopTargetType loopTargetType = LoopTargetType.valueOf(text);
      accumulateLeftPart.setLoopTargetType(loopTargetType);

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("value")) {
               Value localValue = this.valueParser.parse(element2);
               LoopTarget loopTarget = new LoopTarget();
               loopTarget.setValue(localValue);
               accumulateLeftPart.setLoopTarget(loopTarget);
            } else if (element2.getName().equals("condition")) {
               ConditionItem conditionItem = new ConditionItem();
               items.add(conditionItem);
               conditionItem.setLeft(element2.attributeValue("left"));
               conditionItem.setOp(Op.valueOf(element2.attributeValue("op")));
               conditionItem.setValue(this.parseNestedValue(element2));
            } else if (element2.getName().equals("calculate")) {
               CalculateItem calculateItem = new CalculateItem();
               items2.add(calculateItem);
               calculateItem.setType(CalculateType.valueOf(element2.attributeValue("type")));
               String text2 = element2.attributeValue("enable-assignment");
               calculateItem.setEnableAssignment(Boolean.valueOf(text2));
               if (calculateItem.isEnableAssignment()) {
                  calculateItem.setAssignTargetType(element2.attributeValue("assign-target-type"));
                  calculateItem.setAssignCategoryUuid(element2.attributeValue("category-uuid"));
                  calculateItem.setAssignVariableCategory(element2.attributeValue("var-category"));
                  calculateItem.setAssignVariableUuid(element2.attributeValue("uuid"));
                  calculateItem.setAssignVariable(element2.attributeValue("var"));
                  calculateItem.setAssignVariableLabel(element2.attributeValue("var-label"));
                  calculateItem.setKeyLabel(element2.attributeValue("key-label"));
                  calculateItem.setKeyName(element2.attributeValue("key-name"));
                  calculateItem.setKeyUuid(element2.attributeValue("key-uuid"));
                  calculateItem.setKeyCategoryUuid(element2.attributeValue("key-category-uuid"));
                  String text3 = element2.attributeValue("datatype");
                  if (StringUtils.isNotBlank(text3)) {
                     calculateItem.setAssignDatatype(Datatype.valueOf(text3));
                  }
               }

               calculateItem.setValue(this.parseNestedValue(element2));
            } else if (element2.getName().equals("and") || element2.getName().equals("or")) {
               Junction junction = (Junction)this.junctionParser.parse(element2);
               accumulateLeftPart.setJunction(junction);
            }
         }
      }

      return accumulateLeftPart;
   }

   private Value parseNestedValue(Element element) {
      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("value")) {
               return this.valueParser.parse(element2);
            }
         }
      }

      return null;
   }

   private ComplexArithmetic convertArithmetic(SimpleArithmetic simpleArithmetic) {
      if (simpleArithmetic == null) {
         return null;
      }

      ComplexArithmetic complexArithmetic = new ComplexArithmetic();
      complexArithmetic.setType(simpleArithmetic.getType());
      SimpleValue simpleValue = new SimpleValue();
      complexArithmetic.setValue(simpleValue);
      SimpleArithmeticValue simpleArithmeticValue = simpleArithmetic.getValue();
      simpleValue.setContent(simpleArithmeticValue.getContent());
      SimpleArithmetic arithmetic = simpleArithmeticValue.getArithmetic();
      simpleValue.setArithmetic(this.convertArithmetic(arithmetic));
      return complexArithmetic;
   }

   private CommonFunctionLeftPart buildCommonFunctionLeftPart(Element element) {
      CommonFunctionLeftPart commonFunctionLeftPart = new CommonFunctionLeftPart();
      commonFunctionLeftPart.setName(element.attributeValue("function-name"));
      commonFunctionLeftPart.setLabel(element.attributeValue("function-label"));

      for (Object objectValue : element.elements()) {
         if (objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("function-parameter")) {
               CommonFunctionParameter commonFunctionParameter = new CommonFunctionParameter();
               commonFunctionParameter.setName(element2.attributeValue("name"));
               commonFunctionParameter.setProperty(element2.attributeValue("property-name"));
               commonFunctionParameter.setPropertyLabel(element2.attributeValue("property-label"));

               for (Object objectValue2 : element2.elements()) {
                  if (objectValue2 instanceof Element) {
                     Element element3 = (Element)objectValue2;
                     if (element3.getName().equals("value")) {
                        commonFunctionParameter.setObjectParameter(this.valueParser.parse(element3));
                     }
                  }
               }

               commonFunctionLeftPart.setParameter(commonFunctionParameter);
            }
         }
      }

      return commonFunctionLeftPart;
   }

   private MethodLeftPart buildMethodLeftPart(Element element) {
      MethodLeftPart methodLeftPart = new MethodLeftPart();
      methodLeftPart.setBeanId(element.attributeValue("bean-name"));
      methodLeftPart.setBeanLabel(element.attributeValue("bean-label"));
      methodLeftPart.setUuid(element.attributeValue("uuid"));
      methodLeftPart.setCategoryUuid(element.attributeValue("category-uuid"));
      methodLeftPart.setMethodLabel(element.attributeValue("method-label"));
      methodLeftPart.setMethodName(element.attributeValue("method-name"));
      methodLeftPart.setParameters(this.parseParameters(element, this.valueParser));
      return methodLeftPart;
   }

   private FunctionLeftPart buildFunctionLeftPart(Element element) {
      FunctionLeftPart functionLeftPart = new FunctionLeftPart();
      functionLeftPart.setName(element.attributeValue("name"));
      functionLeftPart.setParameters(this.parseParameters(element, this.valueParser));
      return functionLeftPart;
   }

   private VariableLeftPart buildVariableLeftPart(Element element) {
      VariableLeftPart variableLeftPart = new VariableLeftPart();
      variableLeftPart.setCategoryUuid(element.attributeValue("category-uuid"));
      variableLeftPart.setUuid(element.attributeValue("uuid"));
      variableLeftPart.setVariableName(element.attributeValue("var"));
      variableLeftPart.setVariableLabel(element.attributeValue("var-label"));
      String text = element.attributeValue("var-category");
      if (StringUtils.isNotEmpty(text)) {
         variableLeftPart.setVariableCategory(text);
      }

      String text2 = element.attributeValue("datatype");
      if (StringUtils.isNotEmpty(text2)) {
         try {
            variableLeftPart.setDatatype(Datatype.valueOf(text2));
         } catch (Exception exception) {
         }
      }

      variableLeftPart.setKeyName(element.attributeValue("key-name"));
      variableLeftPart.setKeyLabel(element.attributeValue("key-label"));
      variableLeftPart.setKeyCategoryUuid(element.attributeValue("key-category-uuid"));
      variableLeftPart.setKeyUuid(element.attributeValue("key-uuid"));
      return variableLeftPart;
   }

   private PredefineLeftPart buildPredefineLeftPart(Element element) {
      PredefineLeftPart predefineLeftPart = new PredefineLeftPart();
      predefineLeftPart.setUuid(element.attributeValue("uuid"));
      predefineLeftPart.setPropertyUuid(element.attributeValue("property-uuid"));
      return predefineLeftPart;
   }

   public void setJunctionParser(JunctionParser junctionParser) {
      this.junctionParser = junctionParser;
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }

   public void setComplexArithmeticParser(ComplexArithmeticParser complexArithmeticParser) {
      this.complexArithmeticParser = complexArithmeticParser;
   }

   public void setSimpleArithmeticParser(SimpleArithmeticParser simpleArithmeticParser) {
      this.simpleArithmeticParser = simpleArithmeticParser;
   }

   @Override
   public boolean support(String name) {
      return name.equals("left");
   }
}
