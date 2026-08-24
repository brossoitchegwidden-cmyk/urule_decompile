package com.bstek.urule.model.function;

import com.bstek.urule.runtime.WorkingMemory;

public class ActivePendedFunctionDescriptor implements FunctionDescriptor {
   private boolean enableActivePendedGroupAndExecute;
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
      if (this.enableActivePendedGroupAndExecute) {
         workingMemory.activePendedGroupAndExecute(text);
      } else {
         workingMemory.activePendedGroup(text);
      }

      return null;
   }
   @Override
   public String getName() {
      return "ActiveAgenda";
   }
   @Override
   public String getLabel() {
      return "激活执行组";
   }

   public void setEnableActivePendedGroupAndExecute(boolean enableActivePendedGroupAndExecute) {
      this.enableActivePendedGroupAndExecute = enableActivePendedGroupAndExecute;
   }

   @Override
   public boolean isDisabled() {
      return false;
   }
}
