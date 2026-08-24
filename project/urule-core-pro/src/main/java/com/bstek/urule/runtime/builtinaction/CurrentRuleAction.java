package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.action.WorkingMemoryHolder;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.runtime.WorkingMemory;
import com.bstek.urule.runtime.rete.ExecutionContext;
import java.util.Set;

@ActionBean(name = "当前规则", ename = "CurrentRule")
public class CurrentRuleAction {
   @ActionMethod(name = "当前规则对象")
   @ActionMethodParameter(names = {})
   public Rule getCurrentRule() {
      WorkingMemory currentWorkingMemory = WorkingMemoryHolder.getCurrentWorkingMemory();
      ExecutionContext context = (ExecutionContext)currentWorkingMemory.getContext();
      return context.getCurrentRule();
   }

   @ActionMethod(name = "当前规则名")
   @ActionMethodParameter(names = {})
   public String getCurrentRuleName() {
      WorkingMemory currentWorkingMemory = WorkingMemoryHolder.getCurrentWorkingMemory();
      ExecutionContext context = (ExecutionContext)currentWorkingMemory.getContext();
      Rule currentRule = context.getCurrentRule();
      return currentRule == null ? null : currentRule.getName();
   }

   @ActionMethod(name = "当前规则匹配的条件")
   @ActionMethodParameter(names = {})
   public Set<Criteria> getCurrentRuleCriterias() {
      WorkingMemory currentWorkingMemory = WorkingMemoryHolder.getCurrentWorkingMemory();
      ExecutionContext context = (ExecutionContext)currentWorkingMemory.getContext();
      return context.getCurrentRuleCriterias();
   }
}
