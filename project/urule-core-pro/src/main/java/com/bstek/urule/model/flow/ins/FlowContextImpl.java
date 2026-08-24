package com.bstek.urule.model.flow.ins;

import com.bstek.urule.runtime.WorkingMemory;
import com.bstek.urule.runtime.response.ExecutionResponseImpl;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.rete.ContextImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowContextImpl extends ContextImpl implements FlowContext {
   private Map<String, Object> variableMap;
   private Map<String, InstanceData> instanceDataMap = new HashMap<>();
   private List<FlowInstance> flowInstances = new ArrayList<>();
   private ExecutionResponseImpl response = new ExecutionResponseImpl();

   public FlowContextImpl(WorkingMemory workingMemory, Map<String, String> variableCategoryMap) {
      super(workingMemory, variableCategoryMap);
   }

   @Override
   public FlowExecutionResponse getResponse() {
      return this.response;
   }

   public Map<String, InstanceData> getInstanceDataMap() {
      return this.instanceDataMap;
   }

   @Override
   public Map<String, Object> getVariables() {
      return this.variableMap;
   }

   @Override
   public Object getVariable(String key) {
      return this.variableMap.get(key);
   }

   @Override
   public void removeVariable(String key) {
      this.variableMap.remove(key);
   }

   @Override
   public void addVariable(String key, Object object) {
      this.variableMap.put(key, object);
   }

   public void setVariableMap(Map<String, Object> variableMap) {
      this.variableMap = variableMap;
   }

   @Override
   public void addFlowInstance(FlowInstance instance) {
      this.flowInstances.add(instance);
   }

   @Override
   public List<FlowInstance> getFlowInstances() {
      return this.flowInstances;
   }
}
