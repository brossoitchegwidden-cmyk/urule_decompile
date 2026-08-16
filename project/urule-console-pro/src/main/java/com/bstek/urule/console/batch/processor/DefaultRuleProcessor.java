package com.bstek.urule.console.batch.processor;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import java.util.HashMap;
import java.util.Map;

public class DefaultRuleProcessor implements RuleProcessor {
   public Map fireRules(BatchContext var1, GeneralEntity var2) throws ProcessorException {
      try {
         HashMap var3 = new HashMap();
         KnowledgePackage var4 = var1.getKnowledgePackage();
         KnowledgeSession var5 = KnowledgeSessionFactory.newKnowledgeSession(var1.getKnowledgePackage());
         Map var6 = var1.getBatch().getComplexPacketParams();
         var5.insert(var2);
         if (var4.getFlowMap().size() > 0) {
            String var7 = (String)var4.getFlowMap().keySet().iterator().next();
            var5.startProcess(var7, var6);
         } else {
            var5.fireRules(var6);
         }

         for(String var9 : (Iterable<String>)(Iterable<?>)(var1.getBatch().getOutParameterNameList())) {
            var3.put(var9, var5.getParameter(var9));
         }

         return var3;
      } catch (Exception var10) {
         throw new ProcessorException(var10.getMessage(), var10, var2);
      }
   }
}
