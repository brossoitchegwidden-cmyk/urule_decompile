package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;
import java.math.BigDecimal;
import java.util.Collection;

public class MaxValueFunctionDescriptor implements FunctionDescriptor {
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
      return "求最大值";
   }

   @Override
   public String getName() {
      return "MaxValue";
   }

   @Override
   public Object doFunction(Object var1, String var2, WorkingMemory var3) {
      Collection var4 = null;
      if (var1 instanceof Collection) {
         var4 = (Collection)var1;
         BigDecimal var5 = null;

         for (Object var7 : var4) {
            Object var8 = Utils.getObjectProperty(var7, var2);
            BigDecimal var9 = Utils.toBigDecimal(var8);
            if (var5 == null) {
               var5 = var9;
            } else {
               int var10 = var9.compareTo(var5);
               if (var10 == 1) {
                  var5 = var9;
               }
            }
         }

         return var5;
      } else {
         throw new RuleException("Function[max value] parameter must be java.util.Collection type.");
      }
   }

   @Override
   public Argument getArgument() {
      Argument var1 = new Argument();
      var1.setName("集合对象");
      var1.setEname("Collection");
      var1.setNeedProperty(true);
      return var1;
   }
}
