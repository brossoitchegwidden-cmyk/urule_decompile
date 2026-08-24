package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.Utils;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.EvaluationContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;

public class CommonFunctionLeftPart implements LeftPart {
   @JsonIgnore
   private String id;
   private String name;
   private String label;
   private CommonFunctionParameter parameter;

   public Object evaluate(EvaluationContext context, Map<String, Object> factMap) {
      FunctionDescriptor functionDescriptor = Utils.findFunctionDescriptor(this.name);
      Value objectParameter = this.parameter.getObjectParameter();
      Object objectValue = context.getValueCompute().complexValueCompute(objectParameter, context, factMap);
      Argument argument = functionDescriptor.getArgument();
      String property = null;
      if (argument.isNeedProperty()) {
         property = this.parameter.getProperty();
      }

      return functionDescriptor.doFunction(objectValue, property, context.getWorkingMemory());
   }

   @Override
   public String getId() {
      if (this.id == null) {
         this.id = this.label + "(" + this.parameter.getId() + ")";
      }

      return this.id;
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
