package com.bstek.urule.runtime.rete;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rete.RuleData;
import com.bstek.urule.runtime.WorkingMemory;
import com.bstek.urule.runtime.assertor.AssertorEvaluator;
import com.bstek.urule.runtime.log.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

public class ContextImpl extends ExecutionContextImpl implements Context, ExecutionContext {
   private ValueCompute valueCompute;
   private WorkingMemory workingMemory;
   private List<RuleData> ruleDataList;
   private AssertorEvaluator assertorEvaluator;
   private ApplicationContext applicationContext;
   private Map<String, String> variableCategoryMap;
   private boolean parentIsObjectInstanceMethod;
   private StringBuilder tipMsgBuilder = new StringBuilder();

   public ContextImpl(WorkingMemory workingMemory, Map<String, String> variableCategoryMap) {
      this.workingMemory = workingMemory;
      this.applicationContext = Utils.getApplicationContext();
      this.assertorEvaluator = (AssertorEvaluator)this.applicationContext.getBean("urule.assertorEvaluator");
      this.variableCategoryMap = variableCategoryMap;
      this.valueCompute = (ValueCompute)this.applicationContext.getBean("urule.valueCompute");
      this.ruleDataList = workingMemory.getLogManager().getRuleData();
   }

   @Override
   public void addTipMsg(String msg) {
      if (this.tipMsgBuilder.length() > 0) {
         this.tipMsgBuilder.append(">>");
      }

      this.tipMsgBuilder.append(msg);
   }

   @Override
   public void cleanTipMsg() {
      this.tipMsgBuilder.delete(0, this.tipMsgBuilder.length());
   }

   @Override
   public String getTipMsg() {
      return this.tipMsgBuilder.length() > 0 ? this.tipMsgBuilder.toString() : null;
   }

   @Override
   public AssertorEvaluator getAssertorEvaluator() {
      return this.assertorEvaluator;
   }

   @Override
   public Logger getLogger() {
      return this.getWorkingMemory().getLogManager().getLogger();
   }

   @Override
   public String getVariableCategoryClass(String variableCategory) {
      String variableCategoryClass = this.variableCategoryMap.get(variableCategory);
      if (StringUtils.isEmpty(variableCategoryClass)) {
         variableCategoryClass = HashMap.class.getName();
      }

      return variableCategoryClass;
   }

   @Override
   public synchronized void addRuleData(List<RuleData> ruleData) {
      this.ruleDataList.addAll(ruleData);
   }

   @Override
   public ValueCompute getValueCompute() {
      return this.valueCompute;
   }

   @Override
   public WorkingMemory getWorkingMemory() {
      return this.workingMemory;
   }

   @Override
   public ApplicationContext getApplicationContext() {
      return this.applicationContext;
   }

   @Override
   public void resetParentIsObjectInstanceMethod(boolean parentIsObjectInstanceMethod) {
      this.parentIsObjectInstanceMethod = parentIsObjectInstanceMethod;
   }

   @Override
   public boolean parentIsObjectInstanceMethod() {
      return this.parentIsObjectInstanceMethod;
   }
}
