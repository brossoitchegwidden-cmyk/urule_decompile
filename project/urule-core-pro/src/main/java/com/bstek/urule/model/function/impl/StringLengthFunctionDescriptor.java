package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;

public class StringLengthFunctionDescriptor implements FunctionDescriptor {
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
      if (objectProperty == null) {
         return 0;
      } else if (!(objectProperty instanceof String)) {
         throw new RuleException("Function[StringLength] parameter value must be String.");
      } else {
         return objectProperty.toString().length();
      }
   }
   @Override
   public String getName() {
      return "StringLength";
   }
   @Override
   public String getLabel() {
      return "计算字符长度";
   }

   @Override
   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }
}
