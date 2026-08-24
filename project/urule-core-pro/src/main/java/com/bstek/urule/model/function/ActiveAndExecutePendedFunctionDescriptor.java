package com.bstek.urule.model.function;

import com.bstek.urule.runtime.WorkingMemory;

public class ActiveAndExecutePendedFunctionDescriptor implements FunctionDescriptor {
   @Override
   public Argument getArgument() {
      Argument argument = new Argument();
      argument.setName("执行组名");
      argument.setEname("groupName");
      argument.setNeedProperty(false);
      return argument;
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      if (object == null) {
         return null;
      }

      String text = object.toString();
      workingMemory.activePendedGroupAndExecute(text);
      return null;
   }
   @Override
   public String getName() {
      return "ActivePended";
   }
   @Override
   public String getLabel() {
      return "激活执行组并执行";
   }

   @Override
   public boolean isDisabled() {
      return false;
   }
}
