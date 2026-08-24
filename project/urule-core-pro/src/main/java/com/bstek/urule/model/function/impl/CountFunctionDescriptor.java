package com.bstek.urule.model.function.impl;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;
import java.util.Collection;

public class CountFunctionDescriptor implements FunctionDescriptor {
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
      return "统计数量";
   }
   @Override
   public String getName() {
      return "Count";
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      Collection items = null;
      if (object instanceof Collection) {
         items = (Collection)object;
         return items.size();
      } else {
         throw new RuleException("Function[count] parameter must be java.util.Collection type.");
      }
   }
   @Override
   public Argument getArgument() {
      Argument argument = new Argument();
      argument.setName("集合对象");
      argument.setEname("Collection");
      return argument;
   }
}
