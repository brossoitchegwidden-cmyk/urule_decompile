package com.bstek.urule.runtime.execution;

import com.bstek.urule.action.WorkingMemoryHolderAdapter;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.flow.ins.FlowContextImpl;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import java.util.HashMap;
import java.util.Map;

public class FlowExecution extends AbstractExecution {
   private FlowContextImpl flowContextImpl;

   public FlowExecution(KnowledgeSession knowledgeSession, Map<String, String> allVariableCateogoryMap) {
      super(knowledgeSession, allVariableCateogoryMap);
      this.flowContextImpl = new FlowContextImpl(knowledgeSession, allVariableCateogoryMap);
   }

   public FlowExecutionResponse startProcess(String processId, Map<String, Object> parameters) {
      FlowDefinition flowDefinition = null;

      for (KnowledgePackage knowledgePackage : this.knowledgeSession.getKnowledgePackageList()) {
         Map flowMap = knowledgePackage.getFlowMap();
         if (flowMap != null && flowMap.containsKey(processId)) {
            flowDefinition = (FlowDefinition)flowMap.get(processId);
            break;
         }
      }

      if (flowDefinition == null) {
         throw new RuleException("Rule flow [" + processId + "] not exist.");
      }

      this.knowledgeSession.getLogManager().clean();
      Map runtimeParameters = this.factManager.buildRuntimeParameters(parameters);

      try {
         HashMap valuesByKey = new HashMap(runtimeParameters.size());

         for (String text : (Iterable<String>)(Iterable<?>)(runtimeParameters.keySet())) {
            Object objectValue = runtimeParameters.get(text);
            if (objectValue != null) {
               valuesByKey.put(text, objectValue);
            }
         }

         WorkingMemoryHolderAdapter.set(this.knowledgeSession);
         this.monitorManager.doMonitorInputData(valuesByKey);
         this.flowContextImpl.setVariableMap(valuesByKey);
         flowDefinition.newInstance(this.flowContextImpl);
         FlowExecutionResponse response = this.flowContextImpl.getResponse();
         this.monitorManager.setTotalDuration(response.getDuration());
         this.monitorManager.doMonitor(valuesByKey);
         runtimeParameters.putAll(valuesByKey);
         this.reset();
         return response;
      } finally {
         WorkingMemoryHolderAdapter.clean();
      }
   }
}
