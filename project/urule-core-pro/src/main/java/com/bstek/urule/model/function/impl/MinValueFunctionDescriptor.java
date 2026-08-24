package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;
import java.math.BigDecimal;
import java.util.Collection;

public class MinValueFunctionDescriptor implements FunctionDescriptor {
   private boolean disabled = false;

   @Override
   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }
   @Override
   public String getLabel() {
      return "求最小值";
   }
   @Override
   public String getName() {
      return "MinValue";
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      Collection items = null;
      if (object instanceof Collection) {
         items = (Collection)object;
         BigDecimal doFunctionResult = null;

         for (Object objectValue : items) {
            Object objectProperty = Utils.getObjectProperty(objectValue, property);
            BigDecimal decimalValue = Utils.toBigDecimal(objectProperty);
            if (doFunctionResult == null) {
               doFunctionResult = decimalValue;
            } else {
               int number = decimalValue.compareTo(doFunctionResult);
               if (number == -1) {
                  doFunctionResult = decimalValue;
               }
            }
         }

         return doFunctionResult;
      } else {
         throw new RuleException("Function[min value] parameter must be java.util.Collection type.");
      }
   }
   @Override
   public Argument getArgument() {
      Argument argument = new Argument();
      argument.setName("集合对象");
      argument.setEname("Collection");
      argument.setNeedProperty(true);
      return argument;
   }
}
