package com.bstek.urule.model.function.impl;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;

public class UpdateFactFunctionDescriptor implements FunctionDescriptor {
   private boolean disabled = false;
   @Override
   public Argument getArgument() {
      Argument argument = new Argument();
      argument.setName("要更新的对象");
      argument.setEname("Object");
      return argument;
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      if (object instanceof String) {
         String text = (String)object;
         if (!text.equals("参数") && !text.equals("parameter")) {
            throw new RuleException("Unsupport parameter[" + text + "].");
         } else {
            return workingMemory.update(workingMemory.getParameters());
         }
      } else {
         return workingMemory.update(object);
      }
   }
   @Override
   public String getName() {
      return "UpdateFact";
   }
   @Override
   public String getLabel() {
      return "更新工作区对象";
   }

   @Override
   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }
}
