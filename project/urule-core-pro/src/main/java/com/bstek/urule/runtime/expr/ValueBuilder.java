package com.bstek.urule.runtime.expr;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.ArithmeticType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

/**
 * Builds and evaluates a value expression from operands and arithmetic operators.
 */
public class ValueBuilder {
   private Stack<Object> stack = new Stack<>();

   public ValueWrapper build() {
      ValueWrapper valueWrapper = (ValueWrapper)this.stack.firstElement();
      this.stack.removeElementAt(0);

      while (!this.stack.isEmpty()) {
         ArithmeticType arithmeticType = (ArithmeticType)this.stack.firstElement();
         this.stack.removeElementAt(0);
         if (this.stack.isEmpty()) {
            throw new RuleException("表达式不合法！");
         }

         ValueWrapper rightValue = (ValueWrapper)this.stack.firstElement();
         this.stack.removeElementAt(0);
         valueWrapper = this.compute(valueWrapper, rightValue, arithmeticType);
      }

      return valueWrapper;
   }

   private ValueWrapper compute(ValueWrapper leftValue, ValueWrapper rightValue, ArithmeticType arithmeticType) {
      BigDecimal leftDecimal = leftValue.getBigDecimalValue();
      BigDecimal rightDecimal = rightValue.getBigDecimalValue();
      if (!arithmeticType.equals(ArithmeticType.Add) || leftDecimal != null && rightDecimal != null) {
         if (!arithmeticType.equals(ArithmeticType.Eq) || leftDecimal != null && rightDecimal != null) {
            if (!arithmeticType.equals(ArithmeticType.NotEq) || leftDecimal != null && rightDecimal != null) {
               if (leftDecimal == null) {
                  String valueId = leftValue.getValueId();
                  if (valueId != null) {
                     throw new RuleException("表达式 [" + valueId + "]的值[" + leftValue.getOriginalValue() + "] 不能转换为数字!");
                  } else {
                     throw new RuleException("表达式 [" + leftValue.getOriginalValue() + "] 不能转换为数字!");
                  }
               } else if (rightDecimal == null) {
                  String valueId = rightValue.getValueId();
                  if (valueId != null) {
                     throw new RuleException("表达式 [" + valueId + "]的值[" + rightValue.getOriginalValue() + "] 不能转换为数字!");
                  } else {
                     throw new RuleException("表达式 [" + rightValue.getOriginalValue() + "] 不能转换为数字!");
                  }
               } else {
                  switch (arithmeticType) {
                     case Add:
                        BigDecimal result = leftDecimal.add(rightDecimal);
                        return new ValueWrapper(result, result, null);
                     case Div:
                        result = leftDecimal.divide(rightDecimal, 32, RoundingMode.HALF_UP);
                        return new ValueWrapper(result, result, null);
                     case Mod:
                        result = leftDecimal.divideAndRemainder(rightDecimal)[1];
                        return new ValueWrapper(result, result, null);
                     case Mul:
                        result = leftDecimal.multiply(rightDecimal);
                        return new ValueWrapper(result, result, null);
                     case Sub:
                        result = leftDecimal.subtract(rightDecimal);
                        return new ValueWrapper(result, result, null);
                     case Eq:
                        boolean matches = this.equalsValue(leftDecimal, rightDecimal);
                        return new ValueWrapper(matches, null);
                     case NotEq:
                        matches = this.equalsValue(leftDecimal, rightDecimal);
                        return new ValueWrapper(!matches, null);
                     case Gt:
                        matches = this.greaterThan(leftDecimal, rightDecimal);
                        return new ValueWrapper(matches, null);
                     case Gte:
                        matches = this.greaterThanOrEqual(leftDecimal, rightDecimal);
                        return new ValueWrapper(matches, null);
                     case Lt:
                        matches = this.lessThan(leftDecimal, rightDecimal);
                        return new ValueWrapper(matches, null);
                     case Lte:
                        matches = this.lessThanOrEqual(leftDecimal, rightDecimal);
                        return new ValueWrapper(matches, null);
                     default:
                        throw new RuleException("Unknow operator " + arithmeticType);
                  }
               }
            } else {
               boolean matches = leftValue.originalValueToString().equals(rightValue.originalValueToString());
               return new ValueWrapper(!matches, null);
            }
         } else {
            boolean matches = leftValue.originalValueToString().equals(rightValue.originalValueToString());
            return new ValueWrapper(matches, null);
         }
      } else {
         String text = leftValue.originalValueToString() + rightValue.originalValueToString();
         return new ValueWrapper(text, null);
      }
   }

   private boolean greaterThanOrEqual(Object leftValue, Object rightValue) {
      if (leftValue == null && rightValue == null) {
         return true;
      }

      if (leftValue == null && rightValue != null) {
         return false;
      }

      if (leftValue != null && rightValue == null) {
         return false;
      }

      BigDecimal leftDecimal = Utils.toBigDecimal(leftValue);
      BigDecimal rightDecimal = Utils.toBigDecimal(rightValue);
      return leftDecimal.compareTo(rightDecimal) > -1;
   }

   private boolean greaterThan(Object leftValue, Object rightValue) {
      if (leftValue == null && rightValue == null) {
         return true;
      }

      if (leftValue == null && rightValue != null) {
         return false;
      }

      if (leftValue != null && rightValue == null) {
         return false;
      }

      BigDecimal leftDecimal = Utils.toBigDecimal(leftValue);
      BigDecimal rightDecimal = Utils.toBigDecimal(rightValue);
      return leftDecimal.compareTo(rightDecimal) == 1;
   }

   private boolean lessThanOrEqual(Object leftValue, Object rightValue) {
      if (leftValue == null && rightValue == null) {
         return true;
      }

      if (leftValue == null && rightValue != null) {
         return false;
      }

      if (leftValue != null && rightValue == null) {
         return false;
      }

      BigDecimal leftDecimal = Utils.toBigDecimal(leftValue);
      BigDecimal rightDecimal = Utils.toBigDecimal(rightValue);
      return leftDecimal.compareTo(rightDecimal) < 1;
   }

   private boolean lessThan(Object leftValue, Object rightValue) {
      if (leftValue == null && rightValue == null) {
         return true;
      }

      if (leftValue == null && rightValue != null) {
         return false;
      }

      if (leftValue != null && rightValue == null) {
         return false;
      }

      BigDecimal leftDecimal = Utils.toBigDecimal(leftValue);
      BigDecimal rightDecimal = Utils.toBigDecimal(rightValue);
      return leftDecimal.compareTo(rightDecimal) == -1;
   }

   private boolean equalsValue(Object leftValue, Object rightValue) {
      if (leftValue == null && rightValue == null) {
         return true;
      }

      if (leftValue == null && rightValue != null) {
         return false;
      }

      if (leftValue != null && rightValue == null) {
         return false;
      }

      try {
         BigDecimal leftDecimal = Utils.toBigDecimal(leftValue);
         BigDecimal rightDecimal = Utils.toBigDecimal(rightValue);
         return leftDecimal.compareTo(rightDecimal) == 0;
      } catch (Exception exception) {
         return leftValue.toString().equals(rightValue.toString());
      }
   }

   public void addValue(Object obj) {
      if (!this.stack.isEmpty()) {
         Object objectValue = this.stack.peek();
         if (objectValue instanceof ValueWrapper) {
            if (obj instanceof ValueWrapper) {
               throw new RuleException("表达式不合法！");
            }
         } else {
            if (obj instanceof ArithmeticType) {
               throw new RuleException("表达式不合法！");
            }

            ArithmeticType arithmeticType = (ArithmeticType)objectValue;
            if (!arithmeticType.equals(ArithmeticType.Add) && !arithmeticType.equals(ArithmeticType.Sub)) {
               this.stack.pop();
               ValueWrapper valueWrapper = (ValueWrapper)this.stack.pop();
               ValueWrapper rightValue = (ValueWrapper)obj;
               obj = this.compute(valueWrapper, rightValue, arithmeticType);
            } else if (this.stack.size() > 2) {
            }
         }
      } else if (obj instanceof ArithmeticType) {
         throw new RuleException("表达式不合法！");
      }

      this.stack.push(obj);
   }
}
