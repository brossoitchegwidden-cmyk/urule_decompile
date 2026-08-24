package com.bstek.urule.model.function;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.WorkingMemory;
import com.bstek.urule.runtime.rete.ExecutionContext;

public class ActiveRuleFunctionDescriptor implements FunctionDescriptor {
   @Override
   public Argument getArgument() {
      Argument argument = new Argument();
      argument.setName("规则名");
      argument.setEname("ruleName");
      argument.setNeedProperty(false);
      return argument;
   }
   @Override
   public Object doFunction(Object object, String property, WorkingMemory workingMemory) {
      ExecutionContext context = (ExecutionContext)workingMemory.getContext();
      Rule currentRule = context.getCurrentRule();
      if (currentRule == null) {
         return null;
      }

      if (object == null) {
         return null;
      }

      String text = object.toString();
      workingMemory.activeRule(currentRule.getMutexGroup(), text);
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
