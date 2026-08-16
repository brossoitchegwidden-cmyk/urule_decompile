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
   private FlowContextImpl g;

   public FlowExecution(KnowledgeSession var1, Map<String, String> var2) {
      super(var1, var2);
      this.g = new FlowContextImpl(var1, var2);
   }

   public FlowExecutionResponse startProcess(String var1, Map<String, Object> var2) {
      FlowDefinition var3 = null;

      for (KnowledgePackage var5 : this.d.getKnowledgePackageList()) {
         Map var6 = var5.getFlowMap();
         if (var6 != null && var6.containsKey(var1)) {
            var3 = (FlowDefinition)var6.get(var1);
            break;
         }
      }

      if (var3 == null) {
         throw new RuleException("Rule flow [" + var1 + "] not exist.");
      }

      this.d.getLogManager().clean();
      Map var12 = this.b.buildRuntimeParameters(var2);

      try {
         HashMap var13 = new HashMap(var12.size());

         for (String var7 : (Iterable<String>)(Iterable<?>)(var12.keySet())) {
            Object var8 = var12.get(var7);
            if (var8 != null) {
               var13.put(var7, var8);
            }
         }

         WorkingMemoryHolderAdapter.set(this.d);
         this.c.doMonitorInputData(var13);
         this.g.setVariableMap(var13);
         var3.newInstance(this.g);
         FlowExecutionResponse var15 = this.g.getResponse();
         this.c.setTotalDuration(var15.getDuration());
         this.c.doMonitor(var13);
         var12.putAll(var13);
         this.a();
         return var15;
      } finally {
         WorkingMemoryHolderAdapter.clean();
      }
   }
}
