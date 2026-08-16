package com.bstek.urule.model.function;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.WorkingMemory;
import com.bstek.urule.runtime.rete.ExecutionContext;

public class ActiveRuleFunctionDescriptor implements FunctionDescriptor {
   @Override
   public Argument getArgument() {
      Argument var1 = new Argument();
      var1.setName("规则名");
      var1.setEname("ruleName");
      var1.setNeedProperty(false);
      return var1;
   }

   @Override
   public Object doFunction(Object var1, String var2, WorkingMemory var3) {
      ExecutionContext var4 = (ExecutionContext)var3.getContext();
      Rule var5 = var4.getCurrentRule();
      if (var5 == null) {
         return null;
      }

      if (var1 == null) {
         return null;
      }

      String var6 = var1.toString();
      var3.activeRule(var5.getMutexGroup(), var6);
      return null;
   }

   @Override
   public String getName() {
      return "ActiveRule";
   }

   @Override
   public String getLabel() {
      return "激活当前互斥组规则";
   }

   @Override
   public boolean isDisabled() {
      return false;
   }
}
