package com.bstek.urule.console.batch.processor;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import java.util.HashMap;
import java.util.Map;

public class DefaultRuleProcessor implements RuleProcessor {
   public Map fireRules(BatchContext batchContext, GeneralEntity data) throws ProcessorException {
      try {
         HashMap fireRulesResult = new HashMap();
         KnowledgePackage knowledgePackage = batchContext.getKnowledgePackage();
         KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(batchContext.getKnowledgePackage());
         Map complexPacketParams = batchContext.getBatch().getComplexPacketParams();
         knowledgeSession.insert(data);
         if (knowledgePackage.getFlowMap().size() > 0) {
            String text = (String)knowledgePackage.getFlowMap().keySet().iterator().next();
            knowledgeSession.startProcess(text, complexPacketParams);
         } else {
            knowledgeSession.fireRules(complexPacketParams);
         }

         for(String text2 : (Iterable<String>)(Iterable<?>)(batchContext.getBatch().getOutParameterNameList())) {
            fireRulesResult.put(text2, knowledgeSession.getParameter(text2));
         }

         return fireRulesResult;
      } catch (Exception exception) {
         throw new ProcessorException(exception.getMessage(), exception, data);
      }
   }
}
