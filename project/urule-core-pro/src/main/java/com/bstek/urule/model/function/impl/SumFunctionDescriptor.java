package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;
import java.math.BigDecimal;
import java.util.Collection;

public class SumFunctionDescriptor implements FunctionDescriptor {
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
      return "求和";
   }
   @Override
   public String getName() {
      return "Sum";
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      Collection items = null;
      if (object instanceof Collection) {
         items = (Collection)object;
         BigDecimal decimalValue = null;

         for (Object objectValue : items) {
            Object objectProperty = Utils.getObjectProperty(objectValue, property);
            BigDecimal decimalValue2 = Utils.toBigDecimal(objectProperty);
            if (decimalValue == null) {
               decimalValue = decimalValue2;
            } else {
               decimalValue = decimalValue.add(decimalValue2);
            }
         }

         return decimalValue == null ? 0.0 : decimalValue.doubleValue();
      } else {
         throw new RuleException("Function[sum] parameter must be java.util.Collection type.");
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
