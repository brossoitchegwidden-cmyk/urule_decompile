package com.bstek.urule.model.function.impl;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;
import java.math.BigDecimal;
import java.util.Collection;

public class MaxFunctionDescriptor implements FunctionDescriptor {
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
      return "求最大值对象";
   }

   @Override
   public String getName() {
      return "Max";
   }

   @Override
   public Object doFunction(Object var1, String var2, WorkingMemory var3) {
      Collection var4 = null;
      if (var1 instanceof Collection) {
         var4 = (Collection)var1;
         BigDecimal var5 = null;
         Object var6 = null;

         for (Object var8 : var4) {
            Object var9 = Utils.getObjectProperty(var8, var2);
            BigDecimal var10 = Utils.toBigDecimal(var9);
            if (var5 == null) {
               var5 = var10;
               var6 = var8;
            } else {
               int var11 = var10.compareTo(var5);
               if (var11 == 1) {
                  var5 = var10;
                  var6 = var8;
               }
            }
         }

         return var6;
      } else {
         throw new RuleException("Function[max] parameter must be java.util.Collection type.");
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
