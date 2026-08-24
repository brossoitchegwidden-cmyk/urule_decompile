package com.bstek.urule.model.function.impl;

import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;

public class InsertFactFunctionDescriptor implements FunctionDescriptor {
   private boolean disabled = false;
   @Override
   public Argument getArgument() {
      Argument argument = new Argument();
      argument.setName("要插入的对象");
      argument.setEname("Object");
      return argument;
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      boolean doFunctionResult = workingMemory.insert(object);
      workingMemory.update(object);
      return doFunctionResult;
   }
   @Override
   public String getName() {
      return "InsertFact";
   }
   @Override
   public String getLabel() {
      return "插入对象到工作区";
   }

   @Override
   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }
}
