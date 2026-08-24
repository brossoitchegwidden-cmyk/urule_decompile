package com.bstek.urule.action;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.runtime.rete.Context;
import java.util.Map;

public class ExecuteCommonFunctionAction extends AbstractAction {
   private String name;
   private String label;
   private CommonFunctionParameter parameter;

   @Override
   public ActionValue execute(Context context, Map<String, Object> factMap) {
      FunctionDescriptor functionDescriptor = null;
      if (Utils.getFunctionDescriptorMap().containsKey(this.name)) {
         functionDescriptor = Utils.findFunctionDescriptor(this.name);
      } else if (Utils.getFunctionDescriptorLabelMap().containsKey(this.label)) {
         functionDescriptor = Utils.getFunctionDescriptorLabelMap().get(this.label);
      }

      if (functionDescriptor == null) {
         throw new RuleException("Function[" + this.name + "] not exist.");
      }

      String text = LocaleHolder.isEnglish() ? this.name : (this.label == null ? this.name : this.label);
      Value objectParameter = null;
      Object objectValue = null;
      if (this.parameter != null) {
         objectParameter = this.parameter.getObjectParameter();
         objectValue = context.getValueCompute().complexValueCompute(objectParameter, context, factMap);
      }

      String property = null;
      if (functionDescriptor.getArgument() != null && functionDescriptor.getArgument().isNeedProperty()) {
         property = this.parameter.getProperty();
      }

      if (this.debug) {
         context.getLogger().logExecuteFunction(text, objectValue);
      }

      Object objectValue2 = functionDescriptor.doFunction(objectValue, property, context.getWorkingMemory());
      return objectValue2 == null ? null : new ActionValueImpl(this.name, objectValue2);
   }

   @Override
   public ActionType getActionType() {
      return ActionType.ExecuteCommonFunction;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public CommonFunctionParameter getParameter() {
      return this.parameter;
   }

   public void setParameter(CommonFunctionParameter parameter) {
      this.parameter = parameter;
   }
}
