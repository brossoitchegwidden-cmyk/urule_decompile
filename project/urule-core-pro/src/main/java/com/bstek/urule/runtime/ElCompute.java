package com.bstek.urule.runtime;

import com.bstek.urule.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

/**
 * Evaluates the lightweight arithmetic/string expression syntax used at runtime.
 */
public class ElCompute {
   private Stack<Object> operandStack = new Stack<>();
   private Stack<Character> operatorStack = new Stack<>();

   public Object doCompute(String expr) {
      this.parseExpression(expr);
      return this.operandStack.pop();
   }

   private void parseExpression(String expression) {
      StringBuilder token = new StringBuilder();
      byte quoteState = 32;
      char previousChar = ' ';

      for (int index = 0; index < expression.length(); index++) {
         char currentChar = expression.charAt(index);
         if (previousChar == '\\') {
            token.append(currentChar);
            previousChar = currentChar;
         } else if (quoteState == 34) {
            if (currentChar == '"') {
               quoteState = 32;
               this.operandStack.push(token.toString());
               token.setLength(0);
            } else {
               token.append(currentChar);
            }

            previousChar = currentChar;
         } else {
            switch (currentChar) {
               case ' ':
                  if (quoteState == 34) {
                     token.append(currentChar);
                  }
                  break;
               case '!':
               case '#':
               case '$':
               case '&':
               case '\'':
               case ',':
               case '.':
               default:
                  token.append(currentChar);
                  break;
               case '"':
                  if (quoteState == 34) {
                     quoteState = 32;
                     this.operandStack.push(token.toString());
                     token.setLength(0);
                  } else {
                     quoteState = 34;
                  }
                  break;
               case '%':
                  this.handleOperator(token, currentChar, previousChar);
                  break;
               case '(':
                  this.operatorStack.push(currentChar);
                  break;
               case ')':
                  this.pushToken(token);
                  this.evaluatePendingOperators(1);
                  break;
               case '*':
                  this.handleOperator(token, currentChar, previousChar);
                  break;
               case '+':
                  this.handleOperator(token, currentChar, previousChar);
                  break;
               case '-':
                  this.handleOperator(token, currentChar, previousChar);
                  break;
               case '/':
                  this.handleOperator(token, currentChar, previousChar);
            }

            previousChar = currentChar;
         }
      }

      if (token.length() > 0) {
         this.pushToken(token);
      }

      this.evaluatePendingOperators(0);
   }

   private void handleOperator(StringBuilder token, char operator, char previousChar) {
      if (token.length() == 0 && previousChar != ')' && previousChar != '"') {
         token.append(operator);
      } else {
         this.pushToken(token);
         if (operator != '+' && operator != '-') {
            this.evaluatePendingOperators(2);
         } else {
            this.evaluatePendingOperators(0);
         }

         this.operatorStack.push(operator);
      }
   }

   private void evaluatePendingOperators(int precedenceMode) {
      if (!this.operatorStack.empty()) {
         char pendingOperator = this.operatorStack.peek();
         if (pendingOperator == '(') {
            this.operatorStack.pop();
         } else {
            if (precedenceMode != 0 && precedenceMode != 1) {
               if (precedenceMode == 2) {
                  while (pendingOperator == '*' || pendingOperator == '/' || pendingOperator == '%') {
                     Object rightValue = this.operandStack.pop();
                     Object leftValue = this.operandStack.pop();
                     char operator = this.operatorStack.pop();
                     Object result = this.compute(leftValue, operator, rightValue);
                     this.operandStack.push(result);
                     if (this.operatorStack.isEmpty()) {
                        break;
                     }

                     pendingOperator = this.operatorStack.peek();
                     if (pendingOperator == '(') {
                        break;
                     }
                  }
               }
            } else {
               char operator = this.operatorStack.pop();

               do {
                  Object rightValue = null;
                  if (this.operandStack.isEmpty()) {
                     rightValue = "";
                  } else {
                     rightValue = this.operandStack.pop();
                  }

                  Object leftValue = null;
                  if (this.operandStack.isEmpty()) {
                     leftValue = "";
                  } else {
                     leftValue = this.operandStack.pop();
                  }

                  Object result = this.compute(leftValue, operator, rightValue);
                  this.operandStack.push(result);
                  if (this.operatorStack.isEmpty()) {
                     break;
                  }

                  operator = this.operatorStack.pop();
               } while (operator != '(');
            }
         }
      }
   }

   private Object compute(Object leftValue, char operator, Object rightValue) {
      if (operator != '*' && operator != '/' && operator != '%' && operator != '-') {
         if (operator == '+') {
            String concatenatedValue = this.concatenateIfString(leftValue, rightValue);
            if (concatenatedValue != null) {
               return concatenatedValue;
            }

            BigDecimal leftDecimal = null;
            if (leftValue instanceof ElCompute$DataWrapper) {
               leftDecimal = ElCompute$DataWrapper.getDecimalValue((ElCompute$DataWrapper)leftValue);
            } else {
               leftDecimal = Utils.toBigDecimal(leftValue);
            }

            BigDecimal rightDecimal = null;
            if (rightValue instanceof ElCompute$DataWrapper) {
               rightDecimal = ElCompute$DataWrapper.getDecimalValue((ElCompute$DataWrapper)rightValue);
            } else {
               rightDecimal = Utils.toBigDecimal(rightValue);
            }

            return leftDecimal.add(rightDecimal);
         }
      } else {
         String concatenatedValue = this.concatenateIfString(leftValue, rightValue);
         if (concatenatedValue != null) {
            return concatenatedValue;
         }

         BigDecimal leftDecimal = null;
         if (leftValue instanceof ElCompute$DataWrapper) {
            leftDecimal = ElCompute$DataWrapper.getDecimalValue((ElCompute$DataWrapper)leftValue);
         } else {
            leftDecimal = Utils.toBigDecimal(leftValue);
         }

         BigDecimal rightDecimal = null;
         if (rightValue instanceof ElCompute$DataWrapper) {
            rightDecimal = ElCompute$DataWrapper.getDecimalValue((ElCompute$DataWrapper)rightValue);
         } else {
            rightDecimal = Utils.toBigDecimal(rightValue);
         }

         if (operator == '*') {
            return leftDecimal.multiply(rightDecimal);
         }

         if (operator == '/') {
            return leftDecimal.divide(rightDecimal, 10, RoundingMode.HALF_UP).stripTrailingZeros();
         }

         if (operator == '%') {
            return leftDecimal.divideAndRemainder(rightDecimal)[1];
         }

         if (operator == '-') {
            return leftDecimal.subtract(rightDecimal);
         }
      }

      throw new RuntimeException("Unkown operate " + operator + "");
   }

   private String concatenateIfString(Object leftValue, Object rightValue) {
      if (rightValue instanceof String) {
         if (leftValue instanceof ElCompute$DataWrapper) {
            ElCompute$DataWrapper dataWrapper = (ElCompute$DataWrapper)leftValue;
            return ElCompute$DataWrapper.getOriginalText(dataWrapper) + rightValue.toString();
         }

         if (leftValue instanceof BigDecimal) {
            BigDecimal decimalValue = (BigDecimal)leftValue;

            try {
               return decimalValue.toBigIntegerExact() + rightValue.toString();
            } catch (ArithmeticException arithmeticException) {
               return leftValue.toString() + rightValue.toString();
            }
         } else {
            return leftValue.toString() + rightValue.toString();
         }
      } else if (leftValue instanceof String) {
         if (rightValue instanceof ElCompute$DataWrapper) {
            ElCompute$DataWrapper dataWrapper = (ElCompute$DataWrapper)rightValue;
            return leftValue.toString() + ElCompute$DataWrapper.getOriginalText(dataWrapper);
         }

         if (rightValue instanceof BigDecimal) {
            BigDecimal decimalValue = (BigDecimal)rightValue;

            try {
               return leftValue.toString() + decimalValue.toBigIntegerExact();
            } catch (ArithmeticException arithmeticException) {
               return leftValue.toString() + rightValue.toString();
            }
         } else {
            return leftValue.toString() + rightValue.toString();
         }
      } else {
         return null;
      }
   }

   private void pushToken(StringBuilder token) {
      if (token.length() != 0) {
         String text = token.toString();
         token.setLength(0);

         try {
            BigDecimal decimalValue = Utils.toBigDecimal(text);
            this.operandStack.push(new ElCompute$DataWrapper(this, text, decimalValue));
         } catch (Exception exception) {
            this.operandStack.push(text);
         }
      }
   }
}
