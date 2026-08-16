package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;
import java.math.BigDecimal;

public class AbsFunctionDescriptor implements FunctionDescriptor {
   private boolean disabled = false;

   @Override
   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean var1) {
      this.disabled = var1;
   }

   @Override
   public String getLabel() {
      return "求绝对值";
   }

   @Override
   public String getName() {
      return "Abs";
   }

   @Override
   public Object doFunction(Object var1, String var2, WorkingMemory var3) {
      Object var4 = Utils.getObjectProperty(var1, var2);
      BigDecimal var5 = Utils.toBigDecimal(var4);
      return Math.abs(var5.doubleValue());
   }

   @Override
   public Argument getArgument() {
      Argument var1 = new Argument();
      var1.setName("对象");
      var1.setEname("Object");
      var1.setNeedProperty(true);
      return var1;
   }
}
