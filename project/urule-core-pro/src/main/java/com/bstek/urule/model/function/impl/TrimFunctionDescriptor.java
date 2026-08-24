package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;

public class TrimFunctionDescriptor implements FunctionDescriptor {
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
      return "字符去空格";
   }
   @Override
   public String getName() {
      return "Trim";
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      Object objectProperty = Utils.getObjectProperty(object, property);
      return objectProperty == null ? "null" : objectProperty.toString().trim();
   }
   @Override
   public Argument getArgument() {
      Argument argument = new Argument();
      argument.setName("对象");
      argument.setEname("Object");
      argument.setNeedProperty(true);
      return argument;
   }
}
