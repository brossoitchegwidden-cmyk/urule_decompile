package com.bstek.urule.runtime;

import com.bstek.urule.runtime.execution.RuleExecution;
import com.bstek.urule.runtime.log.LogManager;
import com.bstek.urule.runtime.rete.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorkingMemory implements WorkingMemory {
   protected LogManager logManager;
   protected FactManager factManager;
   protected RuleExecution ruleExecution;
   protected KnowledgeSession knowledgeSession;
   protected Map<String, Object> predefineValueMap = new HashMap<>();
   protected Map<String, Object> sessionValueMap = new HashMap<>();
   protected Map<String, KnowledgeSession> knowledgeSessionMap = new HashMap<>();

   public Object getPredefineValue(String predefineUuid) {
      return this.predefineValueMap.get(predefineUuid);
   }

   public void setPredefineValue(String predefineUuid, Object value) {
      this.predefineValueMap.put(predefineUuid, value);
   }

   public Map<String, Object> getPredefineValueMap() {
      return this.predefineValueMap;
   }

   @Override
   public Map<String, Object> getParameters() {
      return this.factManager.getParameters();
   }
   @Override
   public boolean insert(Object fact) {
      return this.factManager.insert(fact);
   }
   @Override
   public boolean update(Object obj) {
      this.ruleExecution.reevaluationRete(obj);
      return true;
   }
   @Override
   public Object getParameter(String key) {
      return this.factManager.getParameters().get(key);
   }

   @Override
   public Map<String, Object> getAllFactsMap() {
      return this.factManager.getFactMap();
   }

   @Override
   public List<Object> getFactList() {
      return this.factManager.getFactList();
   }
   @Override
   public KnowledgeSession getKnowledgeSession(String id) {
      return this.knowledgeSessionMap.get(id);
   }
   @Override
   public void putKnowledgeSession(String id, KnowledgeSession session) {
      if (this.knowledgeSessionMap.containsKey(id)) {
         this.knowledgeSessionMap.put(id, session);
      }
   }
   @Override
   public Object getSessionValue(String key) {
      return this.sessionValueMap.get(key);
   }
   @Override
   public void setSessionValue(String key, Object value) {
      this.sessionValueMap.put(key, value);
   }

   @Override
   public Map<String, Object> getSessionValueMap() {
      return this.sessionValueMap;
   }
   @Override
   public void activeRule(String mutexGroupName, String ruleName) {
      this.ruleExecution.getAgenda().activeMutexGroupRule(mutexGroupName, ruleName);
   }
   @Override
   public void activePendedGroup(String groupName) {
      this.ruleExecution.getAgenda().activePendedGroup(groupName);
   }
   @Override
   public void activePendedGroupAndExecute(String groupName) {
      this.ruleExecution.getAgenda().activePendedGroupAndExecute(groupName);
   }
   @Override
   public Context getContext() {
      return this.ruleExecution.getAgenda().getContext();
   }

   @Override
   public FactManager getFactManager() {
      return this.factManager;
   }

   @Override
   public LogManager getLogManager() {
      return this.logManager;
   }
}
