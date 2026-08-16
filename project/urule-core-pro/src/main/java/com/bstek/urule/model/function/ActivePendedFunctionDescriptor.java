package com.bstek.urule.model.function;

import com.bstek.urule.runtime.WorkingMemory;

public class ActivePendedFunctionDescriptor implements FunctionDescriptor {
   private boolean enableActivePendedGroupAndExecute;

   @Override
   public Argument getArgument() {
      Argument var1 = new Argument();
      var1.setName("执行组名");
      var1.setEname("groupName");
      var1.setNeedProperty(false);
      return var1;
   }

   @Override
   public Object doFunction(Object var1, String var2, WorkingMemory var3) {
      if (var1 == null) {
         return null;
      }

      String var4 = var1.toString();
      if (this.enableActivePendedGroupAndExecute) {
         var3.activePendedGroupAndExecute(var4);
      } else {
         var3.activePendedGroup(var4);
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

   public void setEnableActivePendedGroupAndExecute(boolean var1) {
      this.enableActivePendedGroupAndExecute = var1;
   }

   @Override
   public boolean isDisabled() {
      return false;
   }
}
