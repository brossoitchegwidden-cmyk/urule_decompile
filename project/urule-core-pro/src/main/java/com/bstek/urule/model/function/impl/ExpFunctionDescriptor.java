package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;
import java.math.BigDecimal;

public class ExpFunctionDescriptor implements FunctionDescriptor {
   private boolean disabled = false;
   @Override
   public Argument getArgument() {
      Argument argument = new Argument();
      argument.setName("对象");
      argument.setEname("Object");
      argument.setNeedProperty(true);
      return argument;
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      Object objectProperty = Utils.getObjectProperty(object, property);
      BigDecimal decimalValue = Utils.toBigDecimal(objectProperty);
      return Math.exp(decimalValue.doubleValue());
   }
   @Override
   public String getName() {
      return "Exp";
   }
   @Override
   public String getLabel() {
      return "求e为底指数";
   }

   @Override
   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }
}
