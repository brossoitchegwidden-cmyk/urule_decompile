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

/**
 * Resolves rule-model values against the current rete context and fact map.
 */
public class ValueCompute {
   private static Logger logger = Logger.getGlobal();
   public static final String BEAN_ID = "urule.valueCompute";
   private static final String PLACEHOLDER_PREFIX = "${";
   private static final String PLACEHOLDER_SUFFIX = "}";

   public Object complexValueCompute(Value value, Context context, Map<String, Object> factMap) {
      ValueWrapper valueWrapper = this.computeValueWrapper(value, context, factMap);
      context.resetParentIsObjectInstanceMethod(false);
      return valueWrapper.getData() != null ? valueWrapper.getData() : valueWrapper.getOriginalValue();
   }

   public Object complexArithmeticCompute(Context context, Map<String, Object> factMap, ComplexArithmetic arithmetic, Object leftValue, String valueId) {
      ValueBuilder valueBuilder = new ValueBuilder();
      valueBuilder.addValue(new ValueWrapper(leftValue, valueId));

      while (arithmetic != null) {
         ArithmeticType type = arithmetic.getType();
         valueBuilder.addValue(type);
         AbstractValue abstractValue = (AbstractValue)arithmetic.getValue();
         if (abstractValue instanceof ParenValue) {
            ParenValue parenValue = (ParenValue)abstractValue;
            ValueWrapper valueWrapper = this.computeValueWrapper(parenValue.getValue(), context, factMap);
            valueBuilder.addValue(valueWrapper);
         } else {
            Object objectValue = this.resolveValue(abstractValue, context, factMap);
            if (objectValue == null) {
               objectValue = "null";
            }

            valueBuilder.addValue(new ValueWrapper(objectValue, abstractValue.getId()));
         }

         arithmetic = abstractValue.getArithmetic();
      }

      ValueWrapper result = valueBuilder.build();
      return result.getData() != null ? result.getData() : result.getOriginalValue();
   }

   private ValueWrapper computeValueWrapper(Value value, Context context, Map<String, Object> factMap) {
      Object objectValue = this.resolveValue(value, context, factMap);
      ComplexArithmetic arithmetic = value.getArithmetic();
      if (arithmetic == null) {
         return new ValueWrapper(objectValue, value.getValueId());
      }

      ValueBuilder valueBuilder = new ValueBuilder();
      valueBuilder.addValue(new ValueWrapper(objectValue, value.getValueId()));

      while (arithmetic != null) {
         ArithmeticType type = arithmetic.getType();
         valueBuilder.addValue(type);
         AbstractValue abstractValue = (AbstractValue)arithmetic.getValue();
         if (abstractValue instanceof ParenValue) {
            ParenValue parenValue = (ParenValue)abstractValue;
            ValueWrapper valueWrapper = this.computeValueWrapper(parenValue.getValue(), context, factMap);
            valueBuilder.addValue(valueWrapper);
         } else {
            Object arithmeticValue = this.resolveValue(abstractValue, context, factMap);
            if (arithmeticValue == null) {
               arithmeticValue = "null";
            }

            valueBuilder.addValue(new ValueWrapper(arithmeticValue, abstractValue.getValueId()));
         }

         arithmetic = abstractValue.getArithmetic();
      }

      return valueBuilder.build();
   }

   private Object resolveValue(Value value, Context context, Map<String, Object> factMap) {
      AbstractWorkingMemory workingMemory = (AbstractWorkingMemory)context.getWorkingMemory();
      Object resolvedValue = null;
      ValueType valueType = value.getValueType();
      if (valueType.equals(ValueType.Input)) {
         resolvedValue = ((SimpleValue)value).getContent();
      } else if (valueType.equals(ValueType.Constant)) {
         ConstantValue constantValue = (ConstantValue)value;
         resolvedValue = this.resolvePropertyPlaceholder(constantValue.getConstantName());
         if (constantValue.getDatatype() != null) {
            resolvedValue = constantValue.getDatatype().convert(resolvedValue);
         }
      } else {
         if (valueType.equals(ValueType.VariableCategory)) {
            VariableCategoryValue variableCategoryValue = (VariableCategoryValue)value;
            String variableCategory = variableCategoryValue.getVariableCategory();
            String variableCategoryClass = context.getVariableCategoryClass(variableCategory);
            if (context.parentIsObjectInstanceMethod()) {
               return variableCategoryClass;
            }

            return this.findObject(variableCategoryClass, factMap, context);
         }

         if (valueType.equals(ValueType.Parameter)) {
            ParameterValue parameterValue = (ParameterValue)value;
            String parameterCategory = "参数";
            String parameterClass = context.getVariableCategoryClass(parameterCategory);
            Object parameterObject = this.findObject(parameterClass, factMap, context);
            if (parameterObject == null) {
               return null;
            }

            String variableName = parameterValue.getVariableName();
            String keyName = parameterValue.getKeyName();
            if (StringUtils.isNotBlank(keyName)) {
               Object objectProperty = Utils.getObjectProperty(parameterObject, keyName);
               if (objectProperty == null && variableName != null) {
                  throw new RuleException("参数中定义的对象[" + parameterValue.getKeyLabel() + "]不存在！");
               }

               resolvedValue = Utils.getObjectProperty(objectProperty, variableName);
            } else {
               resolvedValue = Utils.getObjectProperty(parameterObject, variableName);
            }
         } else if (valueType.equals(ValueType.Method)) {
            MethodValue methodValue = (MethodValue)value;
            ExecuteMethodAction executeMethodAction = new ExecuteMethodAction();
            executeMethodAction.setBeanId(methodValue.getBeanId());
            executeMethodAction.setBeanLabel(methodValue.getBeanLabel());
            executeMethodAction.setMethodName(methodValue.getMethodName());
            executeMethodAction.setMethodLabel(methodValue.getMethodLabel());
            SpringBean builtinAction = ActionUtils.getBuiltinAction(methodValue.getBeanId());
            if (builtinAction != null) {
               executeMethodAction.setBeanELabel(builtinAction.getEname());
            }

            executeMethodAction.setParameters(methodValue.getParameters());
            ActionValue actionValue = executeMethodAction.execute(context, factMap);
            if (actionValue != null) {
               resolvedValue = actionValue.getValue();
            } else {
               resolvedValue = null;
            }
         } else if (valueType.equals(ValueType.CommonFunction)) {
            CommonFunctionValue commonFunctionValue = (CommonFunctionValue)value;
            CommonFunctionParameter parameter = commonFunctionValue.getParameter();
            Value objectParameter = parameter.getObjectParameter();
            Object parameterValue = this.complexValueCompute(objectParameter, context, factMap);
            FunctionDescriptor functionDescriptor = Utils.findFunctionDescriptor(commonFunctionValue.getName());
            Argument argument = functionDescriptor.getArgument();
            String property = null;
            if (argument.isNeedProperty()) {
               property = parameter.getProperty();
            }

            resolvedValue = functionDescriptor.doFunction(parameterValue, property, context.getWorkingMemory());
         } else if (valueType.equals(ValueType.Paren)) {
            ParenValue parenValue = (ParenValue)value;
            resolvedValue = this.computeValueWrapper(parenValue.getValue(), context, factMap);
         } else if (valueType.equals(ValueType.Math)) {
            MathValue mathValue = (MathValue)value;
            resolvedValue = mathValue.getMathSign().calculate(context, factMap);
         } else {
            if (valueType.equals(ValueType.SignI)) {
               return context.getWorkingMemory().getParameter("__math_sigma_step_index_");
            }

            if (valueType.equals(ValueType.Object)) {
               ObjectValue objectValue = (ObjectValue)value;
               resolvedValue = objectValue.getObject();
            } else if (valueType.equals(ValueType.Predefine)) {
               PredefineValue predefineValue = (PredefineValue)value;
               Object predefineObject = workingMemory.getPredefineValue(predefineValue.getUuid());
               String propertyName = predefineValue.getPropertyName();
               if (StringUtils.isNotBlank(propertyName)) {
                  if (predefineObject == null) {
                     ValueCompute.logger.warning("预定义对象【" + predefineValue.getName() + "】值为null，无法从中获取属性【" + propertyName + "】的值");
                     return null;
                  }

                  resolvedValue = Utils.getObjectProperty(predefineObject, propertyName);
               } else {
                  resolvedValue = predefineObject;
               }
            } else {
               VariableValue variableValue = (VariableValue)value;
               String variableCategory = variableValue.getVariableCategory();
               String variableClass = context.getVariableCategoryClass(variableCategory);
               Object fact = this.findObject(variableClass, factMap, context);
               if (fact == null) {
                  ValueCompute.logger.warning("Object [" + variableCategory + "] not exist.");
                  return null;
               }

               String variableName = variableValue.getVariableName();
               resolvedValue = Utils.getObjectProperty(fact, variableName);
            }
         }
      }

      return resolvedValue;
   }

   private String resolvePropertyPlaceholder(String value) {
      if (value.startsWith(PLACEHOLDER_PREFIX) && value.endsWith(PLACEHOLDER_SUFFIX)) {
         String propertyName = value.substring(PLACEHOLDER_PREFIX.length(), value.length() - PLACEHOLDER_SUFFIX.length());
         return PropertyConfigurer.getProperty(propertyName);
      } else {
         return value;
      }
   }

   public Object findObject(String className, Map<String, Object> factMap, Context context) {
      if (factMap.containsKey(className)) {
         return factMap.get(className);
      }

      Map allFactsMap = context.getWorkingMemory().getAllFactsMap();
      return allFactsMap.get(className);
   }
}
