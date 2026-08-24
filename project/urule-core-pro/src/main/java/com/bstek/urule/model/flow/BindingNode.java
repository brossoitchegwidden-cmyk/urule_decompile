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

   public BindingNode(String name) {
      super(name);
   }

   protected KnowledgeSession executeKnowledgePackage(FlowContext context, ProcessInstance instance) {
      KnowledgeSession workingMemory = (KnowledgeSession)context.getWorkingMemory();
      KnowledgePackage knowledgePackage = this.loadKnowledgePackage(context);
      KnowledgeSession knowledgeSession = null;
      if (this instanceof RulePackageNode) {
         knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage, workingMemory);
      } else {
         knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(this.knowledgePackageWrapper, context, workingMemory);
      }

      if (knowledgePackage.getFlowMap() != null && knowledgePackage.getFlowMap().size() != 0) {
         String id = knowledgePackage.getFlowMap().values().iterator().next().getId();
         FlowExecutionResponse flowExecutionResponse = knowledgeSession.startProcess(id, context.getVariables());
         ((ExecutionResponseImpl)context.getResponse()).addFlowExecutionResponse(flowExecutionResponse);
      } else {
         RuleExecutionResponse ruleExecutionResponse = knowledgeSession.fireRules(context.getVariables());
         ((ExecutionResponseImpl)context.getResponse()).addRuleExecutionResponse(ruleExecutionResponse);
      }

      context.addRuleData(knowledgeSession.getLogManager().getRuleData());
      synchronized (context) {
         Map parameters = knowledgeSession.getParameters();
         Map variables = context.getVariables();

         for (String text : (Iterable<String>)(Iterable<?>)(parameters.keySet())) {
            if (!text.equals("return_to__")) {
               Object objectValue = parameters.get(text);
               variables.put(text, objectValue);
            }
         }

         return knowledgeSession;
      }
   }

   private KnowledgePackage loadKnowledgePackage(FlowContext flowContext) {
      if (this instanceof RulePackageNode) {
         RulePackageNode rulePackageNode = (RulePackageNode)this;
         KnowledgeService knowledgeService = (KnowledgeService)flowContext.getApplicationContext().getBean("urule.knowledgeService");
         String code = rulePackageNode.getCode();
         if (StringUtils.isNotBlank(code)) {
            try {
               return knowledgeService.getKnowledge(code);
            } catch (Exception exception) {
               throw new RuleException(exception);
            }
         } else {
            try {
               return knowledgeService.getKnowledge(rulePackageNode.getPackageId());
            } catch (Exception exception2) {
               throw new RuleException(exception2);
            }
         }
      } else {
         return this.knowledgePackageWrapper.getKnowledgePackage();
      }
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
      this.knowledgePackageWrapper = knowledgePackageWrapper;
   }
}
