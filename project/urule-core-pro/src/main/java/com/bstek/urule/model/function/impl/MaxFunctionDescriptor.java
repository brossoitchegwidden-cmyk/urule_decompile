package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;
import java.math.BigDecimal;
import java.util.Collection;

public class MaxFunctionDescriptor implements FunctionDescriptor {
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
      return "求最大值对象";
   }
   @Override
   public String getName() {
      return "Max";
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      Collection items = null;
      if (object instanceof Collection) {
         items = (Collection)object;
         BigDecimal decimalValue = null;
         Object doFunctionResult = null;

         for (Object objectValue : items) {
            Object objectProperty = Utils.getObjectProperty(objectValue, property);
            BigDecimal decimalValue2 = Utils.toBigDecimal(objectProperty);
            if (decimalValue == null) {
               decimalValue = decimalValue2;
               doFunctionResult = objectValue;
            } else {
               int number = decimalValue2.compareTo(decimalValue);
               if (number == 1) {
                  decimalValue = decimalValue2;
                  doFunctionResult = objectValue;
               }
            }
         }

         return doFunctionResult;
      } else {
         throw new RuleException("Function[max] parameter must be java.util.Collection type.");
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
