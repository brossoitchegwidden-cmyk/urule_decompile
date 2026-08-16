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
   private List<MathParser> a = new ArrayList<>();
   private ComplexArithmeticParser b;

   public ValueParser() {
      this.a.add(new AbsoluteMathParser(this));
      this.a.add(new PowerMathParser(this));
      this.a.add(new RadicalMathParser(this));
      this.a.add(new NRadicalMathParser(this));
      this.a.add(new SigmaMathParser(this));
      this.a.add(new FractionMathParser(this));
      this.a.add(new PiMathParser(this));
      this.a.add(new LnMathParser(this));
      this.a.add(new LogMathParser(this));
      this.a.add(new TriangleFunctionMathParser(this));
      this.a.add(new ExtremumFunctionMathParser(this));
      this.a.add(new UpRoundMathParser(this));
      this.a.add(new DownRoundMathParser(this));
   }

   public Value parse(Element var1) {
      if (this.b == null) {
         this.b = (ComplexArithmeticParser)Utils.getApplicationContext().getBean("urule.complexArithmeticParser");
      }

      AbstractValue var2 = null;
      ValueType var3 = ValueType.valueOf(var1.attributeValue("type"));
      if (var3.equals(ValueType.Input)) {
         SimpleValue var4 = new SimpleValue();
         String var5 = var1.attributeValue("content");
         var4.setContent(StringEscapeUtils.unescapeXml(var5));
         var2 = var4;
      } else if (var3.equals(ValueType.Parameter)) {
         ParameterValue var14 = new ParameterValue();
         var14.setUuid(var1.attributeValue("uuid"));
         var14.setVariableName(var1.attributeValue("var"));
         var14.setVariableLabel(var1.attributeValue("var-label"));
         var14.setKeyLabel(var1.attributeValue("key-label"));
         var14.setKeyName(var1.attributeValue("key-name"));
         var14.setKeyUuid(var1.attributeValue("key-uuid"));
         var14.setKeyCategoryUuid(var1.attributeValue("key-category-uuid"));
         String var24 = var1.attributeValue("datatype");
         if (StringUtils.isNotEmpty(var24) && !var24.equals("undefined")) {
            try {
               var14.setDatatype(Datatype.valueOf(var24));
            } catch (Exception var12) {
            }
         }

         var2 = var14;
      } else if (var3.equals(ValueType.Variable)) {
         VariableValue var15 = new VariableValue();
         var15.setUuid(var1.attributeValue("uuid"));
         var15.setCategoryUuid(var1.attributeValue("category-uuid"));
         String var25 = var1.attributeValue("var");
         if (StringUtils.isNotEmpty(var25)) {
            var15.setVariableName(var25);
         }

         String var6 = var1.attributeValue("var-label");
         if (StringUtils.isNotEmpty(var6)) {
            var15.setVariableLabel(var6);
         }

         String var7 = var1.attributeValue("datatype");
         if (StringUtils.isNotEmpty(var7)) {
            var15.setDatatype(Datatype.valueOf(var7));
         }

         String var8 = var1.attributeValue("var-category");
         if (StringUtils.isNotEmpty(var8)) {
            var15.setVariableCategory(var8);
         }

         var2 = var15;
      } else if (var3.equals(ValueType.Predefine)) {
         PredefineValue var16 = new PredefineValue();
         var16.setUuid(var1.attributeValue("uuid"));
         var16.setPropertyUuid(var1.attributeValue("property-uuid"));
         var2 = var16;
      } else if (var3.equals(ValueType.VariableCategory)) {
         String var17 = var1.attributeValue("var-category");
         VariableCategoryValue var26 = new VariableCategoryValue(var17);
         var26.setUuid(var1.attributeValue("category-uuid"));
         var2 = var26;
      } else if (var3.equals(ValueType.Method)) {
         MethodValue var18 = new MethodValue();
         var18.setUuid(var1.attributeValue("uuid"));
         var18.setCategoryUuid(var1.attributeValue("category-uuid"));
         String var27 = var1.attributeValue("bean-name");
         var18.setBeanId(var27);
         String var31 = var1.attributeValue("bean-label");
         var18.setBeanLabel(var31);
         String var35 = var1.attributeValue("method-name");
         var18.setMethodName(var35);
         String var39 = var1.attributeValue("method-label");
         var18.setMethodLabel(var39);
         List var9 = this.a(var1, this);
         var18.setParameters(var9);
         var2 = var18;
      } else if (var3.equals(ValueType.CommonFunction)) {
         CommonFunctionValue var19 = new CommonFunctionValue();
         var19.setName(var1.attributeValue("function-name"));
         var19.setLabel(var1.attributeValue("function-label"));

         for (Object var32 : var1.elements()) {
            if (var32 instanceof Element) {
               Element var36 = (Element)var32;
               if (var36.getName().equals("function-parameter")) {
                  CommonFunctionParameter var40 = new CommonFunctionParameter();
                  var40.setName(var36.attributeValue("name"));
                  var40.setProperty(var36.attributeValue("property-name"));
                  var40.setPropertyLabel(var36.attributeValue("property-name"));

                  for (Object var10 : var36.elements()) {
                     if (var10 instanceof Element) {
                        Element var11 = (Element)var10;
                        if (var11.getName().equals("value")) {
                           var40.setObjectParameter(this.parse(var11));
                        }
                     }
                  }

                  var19.setParameter(var40);
               }
            }
         }

         var2 = var19;
      } else if (var3.equals(ValueType.Math)) {
         MathValue var20 = new MathValue();
         var20.setMathSign(this.a(var1));
         var2 = var20;
      } else if (var3.equals(ValueType.SignI)) {
         SignIValue var21 = new SignIValue();
         var2 = var21;
      } else {
         ConstantValue var22 = new ConstantValue();
         var22.setUuid(var1.attributeValue("uuid"));
         var22.setCategoryUuid(var1.attributeValue("category-uuid"));
         String var29 = var1.attributeValue("const");
         var22.setConstantName(var29);
         String var33 = var1.attributeValue("const-label");
         if (StringUtils.isNotEmpty(var33)) {
            var22.setConstantLabel(var33);
         }

         String var37 = var1.attributeValue("const-category");
         if (StringUtils.isNotEmpty(var37)) {
            var22.setConstantCategory(var37);
         }

         String var41 = var1.attributeValue("data-type");
         if (StringUtils.isNotBlank(var41)) {
            var22.setDatatype(Datatype.valueOf(var41));
         }

         var2 = var22;
      }

      for (Object var30 : var1.elements()) {
         if (var30 != null && var30 instanceof Element) {
            Element var34 = (Element)var30;
            String var38 = var34.getName();
            if (this.b.support(var38)) {
               var2.setArithmetic(this.b.parse(var34));
               break;
            }
         }
      }

      return var2;
   }

   private MathSign a(Element var1) {
      for (Object var3 : var1.elements()) {
         if (var3 instanceof Element) {
            Element var4 = (Element)var3;

            for (MathParser var6 : this.a) {
               if (var6.support(var4.getName())) {
                  return var6.parse(var4);
               }
            }
         }
      }

      throw new RuleException("Unknow element [" + var1.asXML() + "]");
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("value");
   }
}
