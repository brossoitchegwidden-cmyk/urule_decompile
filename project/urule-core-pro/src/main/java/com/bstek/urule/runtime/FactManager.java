package com.bstek.urule.runtime;

import com.bstek.urule.Utils;
import com.bstek.urule.model.GeneralEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactManager {
   private FactManager parentFactManager;
   private List<Object> facts = new ArrayList<>();
   private Map<String, Object> factMap = new HashMap<>();
   private Map<String, Object> parameters = new HashMap<>();
   private Map<String, Object> initialParameters = new HashMap<>();
   private Map<String, Object> insertedParameters = new HashMap<>();
   private Map<String, List<Object>> factListMap = new HashMap<>();

   public FactManager(KnowledgeSession parentSession) {
      if (parentSession != null) {
         for (Object parentFact : parentSession.getFactList()) {
            if (!parentFact.getClass().getName().equals(HashMap.class.getName())) {
               this.facts.add(parentFact);
            }
         }

         this.factListMap.putAll(parentSession.getFactManager().factListMap);
         this.factMap.putAll(parentSession.getAllFactsMap());
         this.parentFactManager = parentSession.getFactManager();
      }
   }

   public void initKnowledgePackageParameters(KnowledgePackage knowledgePackage) {
      ParameterManager.getInstance().initKnowledgePackageParameters(knowledgePackage, this.initialParameters);
   }

   public Map<String, Object> buildRuntimeParameters(Map<String, Object> inputParameters) {
      this.parameters.clear();
      ParameterManager.getInstance().clearInitParameters(this.initialParameters);
      this.parameters.putAll(this.initialParameters);
      this.parameters.putAll(this.insertedParameters);
      if (inputParameters != null) {
         for (String parameterName : inputParameters.keySet()) {
            if (!parameterName.equals("_loop_rule_break_tag__")) {
               this.parameters.put(parameterName, inputParameters.get(parameterName));
            }
         }
      }

      this.addToFactsMap(this.parameters);
      return this.parameters;
   }

   public boolean insert(Object fact) {
      if (this.parentFactManager != null) {
         this.parentFactManager.insert(fact);
      }

      if (!(fact instanceof GeneralEntity) && fact instanceof Map) {
         Map factParameters = (Map)fact;

         for (Object parameterName : factParameters.keySet()) {
            if (parameterName != null) {
               this.insertedParameters.put(parameterName.toString(), factParameters.get(parameterName));
            }
         }

         return false;
      } else {
         this.addToFactsMap(fact);
         return true;
      }
   }

   public void insertLoopFact(Object fact) {
      if (!(fact instanceof GeneralEntity) && fact instanceof Map) {
         Map factParameters = (Map)fact;

         for (Object parameterName : factParameters.keySet()) {
            if (parameterName != null) {
               this.insertedParameters.put(parameterName.toString(), factParameters.get(parameterName));
            }
         }
      } else {
         this.addToFactsMap(fact);
      }
   }

   public void addToFactsMap(Object fact) {
      boolean alreadyInserted = false;

      for (Object insertedFact : this.facts) {
         if (insertedFact == fact) {
            alreadyInserted = true;
            break;
         }
      }

      if (!alreadyInserted) {
         this.facts.add(fact);
      }

      String className = Utils.getClassName(fact);
      this.addToFactListMap(fact, className);
      this.factMap.put(className, fact);
   }

   private void addToFactListMap(Object fact, String className) {
      List factsOfType = this.factListMap.get(className);
      if (factsOfType == null) {
         factsOfType = new ArrayList();
         this.factListMap.put(className, factsOfType);
      }

      factsOfType.add(fact);
   }

   public void clean() {
      this.factMap.clear();
      this.facts.clear();
      this.factListMap.clear();
      this.insertedParameters.clear();
   }

   public Map<String, Object> getParameters() {
      return this.parameters;
   }

   public Map<String, Object> getFactMap() {
      return this.factMap;
   }

   public List<Object> getFacts(String className) {
      return this.factListMap.get(className);
   }

   public Map<String, List<Object>> getFactListMap() {
      return this.factListMap;
   }

   public List<Object> getFactList() {
      ArrayList factList = new ArrayList(this.facts.size());
      factList.addAll(this.facts);
      return factList;
   }
}
