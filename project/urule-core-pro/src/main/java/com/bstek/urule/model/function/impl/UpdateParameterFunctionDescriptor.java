package com.bstek.urule.model.function.impl;

import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;

public class UpdateParameterFunctionDescriptor implements FunctionDescriptor {
   @Override
   public Argument getArgument() {
      return null;
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      return workingMemory.update(workingMemory.getParameters());
   }
   @Override
   public String getName() {
      return "UpdateParameter";
   }
   @Override
   public String getLabel() {
      return "更新参数";
   }

   @Override
   public boolean isDisabled() {
      return false;
   }
}
