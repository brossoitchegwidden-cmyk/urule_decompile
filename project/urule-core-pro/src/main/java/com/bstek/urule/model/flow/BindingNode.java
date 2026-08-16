package com.bstek.urule.model.flow;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.ProcessInstance;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.response.ExecutionResponseImpl;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.bstek.urule.runtime.service.KnowledgeService;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public abstract class BindingNode extends FlowNode {
   private KnowledgePackageWrapper knowledgePackageWrapper;

   public BindingNode() {
   }

   public BindingNode(String var1) {
      super(var1);
   }

   protected KnowledgeSession executeKnowledgePackage(FlowContext var1, ProcessInstance var2) {
      KnowledgeSession var3 = (KnowledgeSession)var1.getWorkingMemory();
      KnowledgePackage var4 = this.loadKnowledgePackage(var1);
      KnowledgeSession var5 = null;
      if (this instanceof RulePackageNode) {
         var5 = KnowledgeSessionFactory.newKnowledgeSession(var4, var3);
      } else {
         var5 = KnowledgeSessionFactory.newKnowledgeSession(this.knowledgePackageWrapper, var1, var3);
      }

      if (var4.getFlowMap() != null && var4.getFlowMap().size() != 0) {
         String var15 = var4.getFlowMap().values().iterator().next().getId();
         FlowExecutionResponse var7 = var5.startProcess(var15, var1.getVariables());
         ((ExecutionResponseImpl)var1.getResponse()).addFlowExecutionResponse(var7);
      } else {
         RuleExecutionResponse var6 = var5.fireRules(var1.getVariables());
         ((ExecutionResponseImpl)var1.getResponse()).addRuleExecutionResponse(var6);
      }

      var1.addRuleData(var5.getLogManager().getRuleData());
      synchronized (var1) {
         Map var16 = var5.getParameters();
         Map var8 = var1.getVariables();

         for (String var10 : (Iterable<String>)(Iterable<?>)(var16.keySet())) {
            if (!var10.equals("return_to__")) {
               Object var11 = var16.get(var10);
               var8.put(var10, var11);
            }
         }

         return var5;
      }
   }

   private KnowledgePackage loadKnowledgePackage(FlowContext var1) {
      if (this instanceof RulePackageNode) {
         RulePackageNode var2 = (RulePackageNode)this;
         KnowledgeService var3 = (KnowledgeService)var1.getApplicationContext().getBean("urule.knowledgeService");
         String var4 = var2.getCode();
         if (StringUtils.isNotBlank(var4)) {
            try {
               return var3.getKnowledge(var4);
            } catch (Exception var6) {
               throw new RuleException(var6);
            }
         } else {
            try {
               return var3.getKnowledge(var2.getPackageId());
            } catch (Exception var7) {
               throw new RuleException(var7);
            }
         }
      } else {
         return this.knowledgePackageWrapper.getKnowledgePackage();
      }
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper var1) {
      this.knowledgePackageWrapper = var1;
   }
}
