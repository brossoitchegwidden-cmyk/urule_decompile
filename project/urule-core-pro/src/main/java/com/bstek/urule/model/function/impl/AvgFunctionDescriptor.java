package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;
import java.math.BigDecimal;
import java.util.Collection;

public class AvgFunctionDescriptor implements FunctionDescriptor {
   private boolean disabled = false;

   @Override
   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }
   @Override
   public String getName() {
      return "Avg";
   }
   @Override
   public String getLabel() {
      return "求平均值";
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

         BigDecimal decimalValue3 = decimalValue == null ? new BigDecimal(0) : decimalValue.divide(new BigDecimal(items.size()), 12, 4);
         return decimalValue3.doubleValue();
      } else {
         throw new RuleException("Function[avg] parameter must be java.util.Collection type.");
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
