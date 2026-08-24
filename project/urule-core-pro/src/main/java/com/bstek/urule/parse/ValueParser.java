package com.bstek.urule.parse;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.MathValue;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.PredefineValue;
import com.bstek.urule.model.rule.SignIValue;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.math.AbsoluteMathParser;
import com.bstek.urule.parse.math.DownRoundMathParser;
import com.bstek.urule.parse.math.ExtremumFunctionMathParser;
import com.bstek.urule.parse.math.FractionMathParser;
import com.bstek.urule.parse.math.LnMathParser;
import com.bstek.urule.parse.math.LogMathParser;
import com.bstek.urule.parse.math.MathParser;
import com.bstek.urule.parse.math.NRadicalMathParser;
import com.bstek.urule.parse.math.PiMathParser;
import com.bstek.urule.parse.math.PowerMathParser;
import com.bstek.urule.parse.math.RadicalMathParser;
import com.bstek.urule.parse.math.SigmaMathParser;
import com.bstek.urule.parse.math.TriangleFunctionMathParser;
import com.bstek.urule.parse.math.UpRoundMathParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class ValueParser extends AbstractParser<Value> {
   private List<MathParser> mathParsers = new ArrayList<>();
   private ComplexArithmeticParser complexArithmeticParser;

   public ValueParser() {
      this.mathParsers.add(new AbsoluteMathParser(this));
      this.mathParsers.add(new PowerMathParser(this));
      this.mathParsers.add(new RadicalMathParser(this));
      this.mathParsers.add(new NRadicalMathParser(this));
      this.mathParsers.add(new SigmaMathParser(this));
      this.mathParsers.add(new FractionMathParser(this));
      this.mathParsers.add(new PiMathParser(this));
      this.mathParsers.add(new LnMathParser(this));
      this.mathParsers.add(new LogMathParser(this));
      this.mathParsers.add(new TriangleFunctionMathParser(this));
      this.mathParsers.add(new ExtremumFunctionMathParser(this));
      this.mathParsers.add(new UpRoundMathParser(this));
      this.mathParsers.add(new DownRoundMathParser(this));
   }

   public Value parse(Element element) {
      if (this.complexArithmeticParser == null) {
         this.complexArithmeticParser = (ComplexArithmeticParser)Utils.getApplicationContext().getBean("urule.complexArithmeticParser");
      }

      AbstractValue abstractValue = null;
      ValueType valueType = ValueType.valueOf(element.attributeValue("type"));
      if (valueType.equals(ValueType.Input)) {
         SimpleValue simpleValue = new SimpleValue();
         String text = element.attributeValue("content");
         simpleValue.setContent(StringEscapeUtils.unescapeXml(text));
         abstractValue = simpleValue;
      } else if (valueType.equals(ValueType.Parameter)) {
         ParameterValue parameterValue = new ParameterValue();
         parameterValue.setUuid(element.attributeValue("uuid"));
         parameterValue.setVariableName(element.attributeValue("var"));
         parameterValue.setVariableLabel(element.attributeValue("var-label"));
         parameterValue.setKeyLabel(element.attributeValue("key-label"));
         parameterValue.setKeyName(element.attributeValue("key-name"));
         parameterValue.setKeyUuid(element.attributeValue("key-uuid"));
         parameterValue.setKeyCategoryUuid(element.attributeValue("key-category-uuid"));
         String text2 = element.attributeValue("datatype");
         if (StringUtils.isNotEmpty(text2) && !text2.equals("undefined")) {
            try {
               parameterValue.setDatatype(Datatype.valueOf(text2));
            } catch (Exception exception) {
            }
         }

         abstractValue = parameterValue;
      } else if (valueType.equals(ValueType.Variable)) {
         VariableValue variableValue = new VariableValue();
         variableValue.setUuid(element.attributeValue("uuid"));
         variableValue.setCategoryUuid(element.attributeValue("category-uuid"));
         String text3 = element.attributeValue("var");
         if (StringUtils.isNotEmpty(text3)) {
            variableValue.setVariableName(text3);
         }

         String text4 = element.attributeValue("var-label");
         if (StringUtils.isNotEmpty(text4)) {
            variableValue.setVariableLabel(text4);
         }

         String text5 = element.attributeValue("datatype");
         if (StringUtils.isNotEmpty(text5)) {
            variableValue.setDatatype(Datatype.valueOf(text5));
         }

         String text6 = element.attributeValue("var-category");
         if (StringUtils.isNotEmpty(text6)) {
            variableValue.setVariableCategory(text6);
         }

         abstractValue = variableValue;
      } else if (valueType.equals(ValueType.Predefine)) {
         PredefineValue predefineValue = new PredefineValue();
         predefineValue.setUuid(element.attributeValue("uuid"));
         predefineValue.setPropertyUuid(element.attributeValue("property-uuid"));
         abstractValue = predefineValue;
      } else if (valueType.equals(ValueType.VariableCategory)) {
         String text7 = element.attributeValue("var-category");
         VariableCategoryValue variableCategoryValue = new VariableCategoryValue(text7);
         variableCategoryValue.setUuid(element.attributeValue("category-uuid"));
         abstractValue = variableCategoryValue;
      } else if (valueType.equals(ValueType.Method)) {
         MethodValue methodValue = new MethodValue();
         methodValue.setUuid(element.attributeValue("uuid"));
         methodValue.setCategoryUuid(element.attributeValue("category-uuid"));
         String text8 = element.attributeValue("bean-name");
         methodValue.setBeanId(text8);
         String text9 = element.attributeValue("bean-label");
         methodValue.setBeanLabel(text9);
         String text10 = element.attributeValue("method-name");
         methodValue.setMethodName(text10);
         String text11 = element.attributeValue("method-label");
         methodValue.setMethodLabel(text11);
         List parameters = this.parseParameters(element, this);
         methodValue.setParameters(parameters);
         abstractValue = methodValue;
      } else if (valueType.equals(ValueType.CommonFunction)) {
         CommonFunctionValue commonFunctionValue = new CommonFunctionValue();
         commonFunctionValue.setName(element.attributeValue("function-name"));
         commonFunctionValue.setLabel(element.attributeValue("function-label"));

         for (Object objectValue : element.elements()) {
            if (objectValue instanceof Element) {
               Element element2 = (Element)objectValue;
               if (element2.getName().equals("function-parameter")) {
                  CommonFunctionParameter commonFunctionParameter = new CommonFunctionParameter();
                  commonFunctionParameter.setName(element2.attributeValue("name"));
                  commonFunctionParameter.setProperty(element2.attributeValue("property-name"));
                  commonFunctionParameter.setPropertyLabel(element2.attributeValue("property-name"));

                  for (Object objectValue2 : element2.elements()) {
                     if (objectValue2 instanceof Element) {
                        Element element3 = (Element)objectValue2;
                        if (element3.getName().equals("value")) {
                           commonFunctionParameter.setObjectParameter(this.parse(element3));
                        }
                     }
                  }

                  commonFunctionValue.setParameter(commonFunctionParameter);
               }
            }
         }

         abstractValue = commonFunctionValue;
      } else if (valueType.equals(ValueType.Math)) {
         MathValue mathValue = new MathValue();
         mathValue.setMathSign(this.resolveMathSign(element));
         abstractValue = mathValue;
      } else if (valueType.equals(ValueType.SignI)) {
         SignIValue signIValue = new SignIValue();
         abstractValue = signIValue;
      } else {
         ConstantValue constantValue = new ConstantValue();
         constantValue.setUuid(element.attributeValue("uuid"));
         constantValue.setCategoryUuid(element.attributeValue("category-uuid"));
         String text12 = element.attributeValue("const");
         constantValue.setConstantName(text12);
         String text13 = element.attributeValue("const-label");
         if (StringUtils.isNotEmpty(text13)) {
            constantValue.setConstantLabel(text13);
         }

         String text14 = element.attributeValue("const-category");
         if (StringUtils.isNotEmpty(text14)) {
            constantValue.setConstantCategory(text14);
         }

         String text15 = element.attributeValue("data-type");
         if (StringUtils.isNotBlank(text15)) {
            constantValue.setDatatype(Datatype.valueOf(text15));
         }

         abstractValue = constantValue;
      }

      for (Object objectValue3 : element.elements()) {
         if (objectValue3 != null && objectValue3 instanceof Element) {
            Element element4 = (Element)objectValue3;
            String name = element4.getName();
            if (this.complexArithmeticParser.support(name)) {
               abstractValue.setArithmetic(this.complexArithmeticParser.parse(element4));
               break;
            }
         }
      }

      return abstractValue;
   }

   private MathSign resolveMathSign(Element element) {
      for (Object objectValue : element.elements()) {
         if (objectValue instanceof Element) {
            Element element2 = (Element)objectValue;

            for (MathParser mathParser : this.mathParsers) {
               if (mathParser.support(element2.getName())) {
                  return mathParser.parse(element2);
               }
            }
         }
      }

      throw new RuleException("Unknow element [" + element.asXML() + "]");
   }

   @Override
   public boolean support(String name) {
      return name.equals("value");
   }
}
