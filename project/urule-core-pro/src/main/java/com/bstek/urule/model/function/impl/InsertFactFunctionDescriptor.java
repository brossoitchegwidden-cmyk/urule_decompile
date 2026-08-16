package com.bstek.urule.model.function.impl;

import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;

public class InsertFactFunctionDescriptor implements FunctionDescriptor {
   private boolean disabled = false;

   @Override
   public Argument getArgument() {
      Argument var1 = new Argument();
      var1.setName("要插入的对象");
      var1.setEname("Object");
      return var1;
   }

   @Override
   public Object doFunction(Object var1, String var2, WorkingMemory var3) {
      boolean var4 = var3.insert(var1);
      var3.update(var1);
      return var4;
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

   public void setDisabled(boolean var1) {
      this.disabled = var1;
   }
}
