package com.bstek.urule.runtime;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.exception.RuleExecutionException;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.runtime.agenda.AgendaFilter;
import com.bstek.urule.runtime.execution.RuleExecution;
import com.bstek.urule.runtime.log.LogManager;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.bstek.urule.runtime.rete.ReteInstance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeSessionImpl extends AbstractWorkingMemory implements KnowledgeSession {
   private List<ReteInstance> reteInstanceList = new ArrayList<>();
   private List<PredefineExecutionUnit> predefineExecutionUnits = new ArrayList<>();
   private List<KnowledgePackage> knowledgePackageList = new ArrayList<>();

   protected KnowledgeSessionImpl(KnowledgePackage knowledgePackage, boolean reg, long limit) {
      this(new KnowledgePackage[]{knowledgePackage}, null, reg, limit);
   }

   protected KnowledgeSessionImpl(KnowledgePackage knowledgePackage, KnowledgeSession parentSession, boolean reg, long limit) {
      this(new KnowledgePackage[]{knowledgePackage}, parentSession, reg, limit);
   }

   protected KnowledgeSessionImpl(KnowledgePackage[] knowledgePackages, KnowledgeSession parentSession, boolean reg, long limit) {
      this.logManager = new LogManager(parentSession);
      this.factManager = new FactManager(parentSession);
      HashMap valuesByKey = new HashMap();

      for (KnowledgePackage knowledgePackage : knowledgePackages) {
         this.registerKnowledgePackage(knowledgePackage);
         valuesByKey.putAll(knowledgePackage.getVariableCateogoryMap());
      }

      this.initFromParentSession(parentSession);
      this.ruleExecution = new RuleExecution(this, valuesByKey, reg, limit);
   }

   private void registerKnowledgePackage(KnowledgePackage knowledgePackage) {
      this.knowledgePackageList.add(knowledgePackage);
      if (knowledgePackage.getPredefineExecutionUnits() != null) {
         this.predefineExecutionUnits.addAll(knowledgePackage.getPredefineExecutionUnits());
      }

      this.reteInstanceList.addAll(knowledgePackage.getAloneReteInstances());
      this.reteInstanceList.add(knowledgePackage.loadReteInstance());
      this.factManager.initKnowledgePackageParameters(knowledgePackage);
   }

   @Override
   public void initFromParentSession(KnowledgeSession parentSession) {
      if (parentSession != null) {
         this.knowledgeSession = parentSession;
         this.knowledgeSessionMap = parentSession.getKnowledgeSessionMap();
         this.sessionValueMap.putAll(parentSession.getSessionValueMap());
         AbstractWorkingMemory abstractWorkingMemory = (AbstractWorkingMemory)parentSession;
         this.predefineValueMap = abstractWorkingMemory.getPredefineValueMap();
      }
   }
   @Override
   public boolean insert(Object fact) {
      boolean insertResult = super.insert(fact);
      if (fact instanceof GeneralEntity) {
         GeneralEntity generalEntity = (GeneralEntity)fact;
         Utils.assignVariableObjectDefaultValue(generalEntity, this);
      }

      return insertResult;
   }
   @Override
   public RuleExecutionResponse fireRules() {
      return this.fireRulesInternal(null, null, Integer.MAX_VALUE);
   }
   @Override
   public RuleExecutionResponse fireRules(int max) {
      return this.fireRulesInternal(null, null, max);
   }
   @Override
   public RuleExecutionResponse fireRules(AgendaFilter filter) {
      return this.fireRulesInternal(filter, null, Integer.MAX_VALUE);
   }
   @Override
   public RuleExecutionResponse fireRules(AgendaFilter filter, int max) {
      return this.fireRulesInternal(filter, null, max);
   }
   @Override
   public RuleExecutionResponse fireRules(Map<String, Object> parameters) {
      return this.fireRulesInternal(null, parameters, Integer.MAX_VALUE);
   }
   @Override
   public RuleExecutionResponse fireRules(Map<String, Object> parameters, AgendaFilter filter) {
      return this.fireRulesInternal(filter, parameters, Integer.MAX_VALUE);
   }
   @Override
   public RuleExecutionResponse fireRules(Map<String, Object> parameters, AgendaFilter filter, int max) {
      return this.fireRulesInternal(filter, parameters, max);
   }
   @Override
   public RuleExecutionResponse fireRules(Map<String, Object> parameters, int max) {
      return this.fireRulesInternal(null, parameters, max);
   }
   @Override
   public FlowExecutionResponse startProcess(String processId) {
      return this.startProcess(processId, null);
   }
   @Override
   public FlowExecutionResponse startProcess(String processId, Map<String, Object> parameters) {
      return this.ruleExecution.startProcess(processId, parameters);
   }

   private RuleExecutionResponse fireRulesInternal(AgendaFilter agendaFilter, Map<String, Object> valuesByKey, int number) {
      try {
         return this.ruleExecution.fireRules(agendaFilter, valuesByKey, number);
      } catch (RuleExecutionException ruleExecutionException) {
         throw new RuleException(ruleExecutionException.getMessage());
      }
   }

   @Override
   public List<KnowledgePackage> getKnowledgePackageList() {
      return this.knowledgePackageList;
   }
   @Override
   public void writeLogFile() throws IOException {
      this.logManager.writeLog();
   }

   @Override
   public Map<String, KnowledgeSession> getKnowledgeSessionMap() {
      return this.knowledgeSessionMap;
   }

   @Override
   public KnowledgeSession getParentSession() {
      return this.knowledgeSession;
   }

   @Override
   public List<ReteInstance> getReteInstanceList() {
      return this.reteInstanceList;
   }

   public List<PredefineExecutionUnit> getPredefineExecutionUnits() {
      return this.predefineExecutionUnits;
   }
}
