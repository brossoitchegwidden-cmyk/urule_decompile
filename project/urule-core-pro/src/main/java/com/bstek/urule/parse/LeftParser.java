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

public class LeftParser extends AbstractParser<Left> {
   private ComplexArithmeticParser a;
   private SimpleArithmeticParser b;
   private ValueParser c;
   private JunctionParser d;

   public Left parse(Element var1) {
      Left var2 = new Left();
      String var3 = var1.attributeValue("type");
      if (StringUtils.isNotEmpty(var3)) {
         var2.setType(LeftType.valueOf(var3));
      } else {
         var2.setType(LeftType.variable);
      }

      switch (var2.getType()) {
         case variable:
            var2.setLeftPart(this.g(var1));
            break;
         case function:
            var2.setLeftPart(this.f(var1));
            break;
         case method:
            var2.setLeftPart(this.e(var1));
            break;
         case parameter:
            var2.setLeftPart(this.g(var1));
            break;
         case predefine:
            var2.setLeftPart(this.h(var1));
            break;
         case commonfunction:
            var2.setLeftPart(this.d(var1));
            break;
         case operatecollection:
            var2.setLeftPart(this.a(var1));
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

      for (Object var5 : var1.elements()) {
         if (var5 != null && var5 instanceof Element) {
            Element var6 = (Element)var5;
            String var7 = var6.getName();
            if (this.a.support(var7)) {
               var2.setArithmetic(this.a.parse(var6));
            } else if (this.b.support(var7)) {
               SimpleArithmetic var8 = this.b.parse(var6);
               var2.setArithmetic(this.a(var8));
            }
         }
      }

      return var2;
   }

   private AccumulateLeftPart a(Element var1) {
      for (Object var3 : var1.elements()) {
         if (var3 != null && var3 instanceof Element) {
            Element var4 = (Element)var3;
            if (var4.getName().equals("accumulate")) {
               return this.b(var4);
            }
         }
      }

      return null;
   }

   private AccumulateLeftPart b(Element var1) {
      AccumulateLeftPart var2 = new AccumulateLeftPart();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      var2.setCalculateItems(var4);
      var2.setConditionItems(var3);
      String var5 = var1.attributeValue("target-type");
      LoopTargetType var6 = LoopTargetType.valueOf(var5);
      var2.setLoopTargetType(var6);

      for (Object var8 : var1.elements()) {
         if (var8 != null && var8 instanceof Element) {
            Element var9 = (Element)var8;
            if (var9.getName().equals("value")) {
               Value var15 = this.c.parse(var9);
               LoopTarget var16 = new LoopTarget();
               var16.setValue(var15);
               var2.setLoopTarget(var16);
            } else if (var9.getName().equals("condition")) {
               ConditionItem var14 = new ConditionItem();
               var3.add(var14);
               var14.setLeft(var9.attributeValue("left"));
               var14.setOp(Op.valueOf(var9.attributeValue("op")));
               var14.setValue(this.c(var9));
            } else if (var9.getName().equals("calculate")) {
               CalculateItem var13 = new CalculateItem();
               var4.add(var13);
               var13.setType(CalculateType.valueOf(var9.attributeValue("type")));
               String var11 = var9.attributeValue("enable-assignment");
               var13.setEnableAssignment(Boolean.valueOf(var11));
               if (var13.isEnableAssignment()) {
                  var13.setAssignTargetType(var9.attributeValue("assign-target-type"));
                  var13.setAssignCategoryUuid(var9.attributeValue("category-uuid"));
                  var13.setAssignVariableCategory(var9.attributeValue("var-category"));
                  var13.setAssignVariableUuid(var9.attributeValue("uuid"));
                  var13.setAssignVariable(var9.attributeValue("var"));
                  var13.setAssignVariableLabel(var9.attributeValue("var-label"));
                  var13.setKeyLabel(var9.attributeValue("key-label"));
                  var13.setKeyName(var9.attributeValue("key-name"));
                  var13.setKeyUuid(var9.attributeValue("key-uuid"));
                  var13.setKeyCategoryUuid(var9.attributeValue("key-category-uuid"));
                  String var12 = var9.attributeValue("datatype");
                  if (StringUtils.isNotBlank(var12)) {
                     var13.setAssignDatatype(Datatype.valueOf(var12));
                  }
               }

               var13.setValue(this.c(var9));
            } else if (var9.getName().equals("and") || var9.getName().equals("or")) {
               Junction var10 = (Junction)this.d.parse(var9);
               var2.setJunction(var10);
            }
         }
      }

      return var2;
   }

   private Value c(Element var1) {
      for (Object var3 : var1.elements()) {
         if (var3 != null && var3 instanceof Element) {
            Element var4 = (Element)var3;
            if (var4.getName().equals("value")) {
               return this.c.parse(var4);
            }
         }
      }

      return null;
   }

   private ComplexArithmetic a(SimpleArithmetic var1) {
      if (var1 == null) {
         return null;
      }

      ComplexArithmetic var2 = new ComplexArithmetic();
      var2.setType(var1.getType());
      SimpleValue var3 = new SimpleValue();
      var2.setValue(var3);
      SimpleArithmeticValue var4 = var1.getValue();
      var3.setContent(var4.getContent());
      SimpleArithmetic var5 = var4.getArithmetic();
      var3.setArithmetic(this.a(var5));
      return var2;
   }

   private CommonFunctionLeftPart d(Element var1) {
      CommonFunctionLeftPart var2 = new CommonFunctionLeftPart();
      var2.setName(var1.attributeValue("function-name"));
      var2.setLabel(var1.attributeValue("function-label"));

      for (Object var4 : var1.elements()) {
         if (var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("function-parameter")) {
               CommonFunctionParameter var6 = new CommonFunctionParameter();
               var6.setName(var5.attributeValue("name"));
               var6.setProperty(var5.attributeValue("property-name"));
               var6.setPropertyLabel(var5.attributeValue("property-label"));

               for (Object var8 : var5.elements()) {
                  if (var8 instanceof Element) {
                     Element var9 = (Element)var8;
                     if (var9.getName().equals("value")) {
                        var6.setObjectParameter(this.c.parse(var9));
                     }
                  }
               }

               var2.setParameter(var6);
            }
         }
      }

      return var2;
   }

   private MethodLeftPart e(Element var1) {
      MethodLeftPart var2 = new MethodLeftPart();
      var2.setBeanId(var1.attributeValue("bean-name"));
      var2.setBeanLabel(var1.attributeValue("bean-label"));
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setCategoryUuid(var1.attributeValue("category-uuid"));
      var2.setMethodLabel(var1.attributeValue("method-label"));
      var2.setMethodName(var1.attributeValue("method-name"));
      var2.setParameters(this.a(var1, this.c));
      return var2;
   }

   private FunctionLeftPart f(Element var1) {
      FunctionLeftPart var2 = new FunctionLeftPart();
      var2.setName(var1.attributeValue("name"));
      var2.setParameters(this.a(var1, this.c));
      return var2;
   }

   private VariableLeftPart g(Element var1) {
      VariableLeftPart var2 = new VariableLeftPart();
      var2.setCategoryUuid(var1.attributeValue("category-uuid"));
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setVariableName(var1.attributeValue("var"));
      var2.setVariableLabel(var1.attributeValue("var-label"));
      String var3 = var1.attributeValue("var-category");
      if (StringUtils.isNotEmpty(var3)) {
         var2.setVariableCategory(var3);
      }

      String var4 = var1.attributeValue("datatype");
      if (StringUtils.isNotEmpty(var4)) {
         try {
            var2.setDatatype(Datatype.valueOf(var4));
         } catch (Exception var6) {
         }
      }

      var2.setKeyName(var1.attributeValue("key-name"));
      var2.setKeyLabel(var1.attributeValue("key-label"));
      var2.setKeyCategoryUuid(var1.attributeValue("key-category-uuid"));
      var2.setKeyUuid(var1.attributeValue("key-uuid"));
      return var2;
   }

   private PredefineLeftPart h(Element var1) {
      PredefineLeftPart var2 = new PredefineLeftPart();
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setPropertyUuid(var1.attributeValue("property-uuid"));
      return var2;
   }

   public void setJunctionParser(JunctionParser var1) {
      this.d = var1;
   }

   public void setValueParser(ValueParser var1) {
      this.c = var1;
   }

   public void setComplexArithmeticParser(ComplexArithmeticParser var1) {
      this.a = var1;
   }

   public void setSimpleArithmeticParser(SimpleArithmeticParser var1) {
      this.b = var1;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("left");
   }
}
