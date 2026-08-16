package com.bstek.urule.runtime.rete;

import com.bstek.urule.PropertyConfigurer;
import com.bstek.urule.Utils;
import com.bstek.urule.action.ActionValue;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.MathValue;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.ObjectValue;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.PredefineValue;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.runtime.AbstractWorkingMemory;
import com.bstek.urule.runtime.ActionUtils;
import com.bstek.urule.runtime.expr.ValueBuilder;
import com.bstek.urule.runtime.expr.ValueWrapper;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

public class ValueCompute {
   private static Logger a = Logger.getGlobal();
   public static final String BEAN_ID = "urule.valueCompute";
   private static final String b = "${";
   private static final String c = "}";

   public Object complexValueCompute(Value var1, Context var2, Map<String, Object> var3) {
      ValueWrapper var4 = this.a(var1, var2, var3);
      var2.resetParentIsObjectInstanceMethod(false);
      return var4.getData() != null ? var4.getData() : var4.getOriginalValue();
   }

   public Object complexArithmeticCompute(Context var1, Map<String, Object> var2, ComplexArithmetic var3, Object var4, String var5) {
      ValueBuilder var6 = new ValueBuilder();
      var6.addValue(new ValueWrapper(var4, var5));

      while (var3 != null) {
         ArithmeticType var7 = var3.getType();
         var6.addValue(var7);
         AbstractValue var8 = (AbstractValue)var3.getValue();
         if (var8 instanceof ParenValue) {
            ParenValue var9 = (ParenValue)var8;
            ValueWrapper var10 = this.a(var9.getValue(), var1, var2);
            var6.addValue(var10);
         } else {
            Object var12 = this.b(var8, var1, var2);
            if (var12 == null) {
               var12 = "null";
            }

            var6.addValue(new ValueWrapper(var12, var8.getId()));
         }

         var3 = var8.getArithmetic();
      }

      ValueWrapper var11 = var6.build();
      return var11.getData() != null ? var11.getData() : var11.getOriginalValue();
   }

   private ValueWrapper a(Value var1, Context var2, Map<String, Object> var3) {
      Object var4 = this.b(var1, var2, var3);
      ComplexArithmetic var5 = var1.getArithmetic();
      if (var5 == null) {
         return new ValueWrapper(var4, var1.getValueId());
      }

      ValueBuilder var6 = new ValueBuilder();
      var6.addValue(new ValueWrapper(var4, var1.getValueId()));

      while (var5 != null) {
         ArithmeticType var7 = var5.getType();
         var6.addValue(var7);
         AbstractValue var8 = (AbstractValue)var5.getValue();
         if (var8 instanceof ParenValue) {
            ParenValue var9 = (ParenValue)var8;
            ValueWrapper var10 = this.a(var9.getValue(), var2, var3);
            var6.addValue(var10);
         } else {
            Object var11 = this.b(var8, var2, var3);
            if (var11 == null) {
               var11 = "null";
            }

            var6.addValue(new ValueWrapper(var11, var8.getValueId()));
         }

         var5 = var8.getArithmetic();
      }

      return var6.build();
   }

   private Object b(Value var1, Context var2, Map<String, Object> var3) {
      AbstractWorkingMemory var4 = (AbstractWorkingMemory)var2.getWorkingMemory();
      Object var5 = null;
      ValueType var6 = var1.getValueType();
      if (var6.equals(ValueType.Input)) {
         var5 = ((SimpleValue)var1).getContent();
      } else if (var6.equals(ValueType.Constant)) {
         ConstantValue var7 = (ConstantValue)var1;
         var5 = this.a(var7.getConstantName());
         if (var7.getDatatype() != null) {
            var5 = var7.getDatatype().convert(var5);
         }
      } else {
         if (var6.equals(ValueType.VariableCategory)) {
            VariableCategoryValue var23 = (VariableCategoryValue)var1;
            String var28 = var23.getVariableCategory();
            String var33 = var2.getVariableCategoryClass(var28);
            if (var2.parentIsObjectInstanceMethod()) {
               return var33;
            }

            return this.findObject(var33, var3, var2);
         }

         if (var6.equals(ValueType.Parameter)) {
            ParameterValue var15 = (ParameterValue)var1;
            String var8 = "参数";
            String var9 = var2.getVariableCategoryClass(var8);
            Object var10 = this.findObject(var9, var3, var2);
            if (var10 == null) {
               return null;
            }

            String var11 = var15.getVariableName();
            String var12 = var15.getKeyName();
            if (StringUtils.isNotBlank(var12)) {
               Object var13 = Utils.getObjectProperty(var10, var12);
               if (var13 == null && var11 != null) {
                  throw new RuleException("参数中定义的对象[" + var15.getKeyLabel() + "]不存在！");
               }

               var5 = Utils.getObjectProperty(var13, var11);
            } else {
               var5 = Utils.getObjectProperty(var10, var11);
            }
         } else if (var6.equals(ValueType.Method)) {
            MethodValue var16 = (MethodValue)var1;
            ExecuteMethodAction var24 = new ExecuteMethodAction();
            var24.setBeanId(var16.getBeanId());
            var24.setBeanLabel(var16.getBeanLabel());
            var24.setMethodName(var16.getMethodName());
            var24.setMethodLabel(var16.getMethodLabel());
            SpringBean var29 = ActionUtils.getBuiltinAction(var16.getBeanId());
            if (var29 != null) {
               var24.setBeanELabel(var29.getEname());
            }

            var24.setParameters(var16.getParameters());
            ActionValue var34 = var24.execute(var2, var3);
            if (var34 != null) {
               var5 = var34.getValue();
            } else {
               var5 = null;
            }
         } else if (var6.equals(ValueType.CommonFunction)) {
            CommonFunctionValue var17 = (CommonFunctionValue)var1;
            CommonFunctionParameter var25 = var17.getParameter();
            Value var30 = var25.getObjectParameter();
            Object var35 = this.complexValueCompute(var30, var2, var3);
            FunctionDescriptor var37 = Utils.findFunctionDescriptor(var17.getName());
            Argument var39 = var37.getArgument();
            String var40 = null;
            if (var39.isNeedProperty()) {
               var40 = var25.getProperty();
            }

            var5 = var37.doFunction(var35, var40, var2.getWorkingMemory());
         } else if (var6.equals(ValueType.Paren)) {
            ParenValue var18 = (ParenValue)var1;
            var5 = this.a(var18.getValue(), var2, var3);
         } else if (var6.equals(ValueType.Math)) {
            MathValue var19 = (MathValue)var1;
            var5 = var19.getMathSign().calculate(var2, var3);
         } else {
            if (var6.equals(ValueType.SignI)) {
               return var2.getWorkingMemory().getParameter("__math_sigma_step_index_");
            }

            if (var6.equals(ValueType.Object)) {
               ObjectValue var20 = (ObjectValue)var1;
               var5 = var20.getObject();
            } else if (var6.equals(ValueType.Predefine)) {
               PredefineValue var21 = (PredefineValue)var1;
               Object var26 = var4.getPredefineValue(var21.getUuid());
               String var31 = var21.getPropertyName();
               if (StringUtils.isNotBlank(var31)) {
                  if (var26 == null) {
                     a.warning("预定义对象【" + var21.getName() + "】值为null，无法从中获取属性【" + var31 + "】的值");
                     return null;
                  }

                  var5 = Utils.getObjectProperty(var26, var31);
               } else {
                  var5 = var26;
               }
            } else {
               VariableValue var22 = (VariableValue)var1;
               String var27 = var22.getVariableCategory();
               String var32 = var2.getVariableCategoryClass(var27);
               Object var36 = this.findObject(var32, var3, var2);
               if (var36 == null) {
                  a.warning("Object [" + var27 + "] not exist.");
                  return null;
               }

               String var38 = var22.getVariableName();
               var5 = Utils.getObjectProperty(var36, var38);
            }
         }
      }

      return var5;
   }

   private String a(String var1) {
      if (var1.startsWith("${") && var1.endsWith("}")) {
         String var2 = var1.substring(2, var1.length() - 1);
         return PropertyConfigurer.getProperty(var2);
      } else {
         return var1;
      }
   }

   public Object findObject(String var1, Map<String, Object> var2, Context var3) {
      if (var2.containsKey(var1)) {
         return var2.get(var1);
      }

      Map var4 = var3.getWorkingMemory().getAllFactsMap();
      return var4.get(var1);
   }
}
