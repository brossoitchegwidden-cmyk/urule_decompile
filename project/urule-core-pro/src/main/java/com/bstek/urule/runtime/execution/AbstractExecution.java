package com.bstek.urule.runtime.execution;

import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.model.rule.PredefineGroup;
import com.bstek.urule.runtime.FactManager;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionImpl;
import com.bstek.urule.runtime.agenda.Agenda;
import com.bstek.urule.runtime.log.LogManager;
import com.bstek.urule.runtime.monitor.MonitorManager;
import com.bstek.urule.runtime.rete.ReteInstance;
import com.bstek.urule.runtime.rete.ReteInstanceUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared execution state and rete registration for rule execution strategies.
 */
public abstract class AbstractExecution {
   protected Agenda agenda;
   protected FactManager factManager;
   protected MonitorManager monitorManager;
   protected KnowledgeSession knowledgeSession;
   protected List<ReteInstance> reteInstanceList;
   protected List<PredefineExecutionUnit> predefineExecutionUnits;
   private Map<String, List<ReteInstanceUnit>> mutexReteInstancesByGroup = new HashMap<>();
   private Map<String, List<ReteInstanceUnit>> pendedReteInstancesByGroup = new HashMap<>();

   public AbstractExecution(KnowledgeSession knowledgeSession, Map<String, String> allVariableCateogoryMap) {
      this.knowledgeSession = knowledgeSession;
      this.factManager = knowledgeSession.getFactManager();
      this.agenda = new Agenda(knowledgeSession, allVariableCateogoryMap, this.pendedReteInstancesByGroup, this.mutexReteInstancesByGroup);
      this.monitorManager = new MonitorManager(knowledgeSession);
      this.initializeExecutionState(knowledgeSession);
   }

   private void initializeExecutionState(KnowledgeSession knowledgeSession) {
      KnowledgeSession parentSession = knowledgeSession.getParentSession();
      if (parentSession != null) {
         this.registerParentSessionRetes(parentSession);
      }

      this.reteInstanceList = knowledgeSession.getReteInstanceList();
      this.predefineExecutionUnits = ((KnowledgeSessionImpl)knowledgeSession).getPredefineExecutionUnits();

      for (PredefineExecutionUnit predefineExecutionUnit : this.predefineExecutionUnits) {
         PredefineGroup group = predefineExecutionUnit.getGroup();
         if (group != null) {
            this.registerPredefineGroupRetes(group);
         }
      }

      this.registerSessionRetes(this.reteInstanceList);
   }

   private void registerPredefineGroupRetes(PredefineGroup predefineGroup) {
      KnowledgePackageWrapper knowledgePackageWrapper = predefineGroup.getKnowledgePackageWrapper();
      if (knowledgePackageWrapper != null) {
         List<ReteInstance> reteInstances = new ArrayList<>();
         List<ReteInstance> standaloneReteInstances = knowledgePackageWrapper.getKnowledgePackage().getAloneReteInstances();
         reteInstances.addAll(standaloneReteInstances);
         ReteInstance reteInstance = knowledgePackageWrapper.getKnowledgePackage().loadReteInstance();
         if (reteInstance != null) {
            reteInstances.add(reteInstance);
         }

         this.registerSessionRetes(reteInstances);
      }

      if (predefineGroup.getNextGroup() != null) {
         this.registerPredefineGroupRetes(predefineGroup.getNextGroup());
      }
   }

   private void registerParentSessionRetes(KnowledgeSession knowledgeSession) {
      KnowledgeSession parentSession = knowledgeSession.getParentSession();
      if (parentSession != null) {
         this.registerParentSessionRetes(parentSession);
      }

      LogManager logManager = this.knowledgeSession.getLogManager();

      for (ReteInstance reteInstance : knowledgeSession.getReteInstanceList()) {
         logManager.addRuleData(reteInstance.getAllRuleData());
         Map<String, List<ReteInstanceUnit>> pendedGroupReteInstancesMap = reteInstance.getPendedGroupReteInstancesMap();
         if (pendedGroupReteInstancesMap != null) {
            this.pendedReteInstancesByGroup.putAll(pendedGroupReteInstancesMap);
         }
      }
   }

   private void registerSessionRetes(List<ReteInstance> reteInstances) {
      LogManager logManager = this.knowledgeSession.getLogManager();

      for (ReteInstance reteInstance : reteInstances) {
         logManager.addRuleData(reteInstance.getAllRuleData());
         Map<String, List<ReteInstanceUnit>> pendedGroupReteInstancesMap = reteInstance.getPendedGroupReteInstancesMap();
         if (pendedGroupReteInstancesMap != null) {
            this.pendedReteInstancesByGroup.putAll(pendedGroupReteInstancesMap);
         }

         Map<String, List<ReteInstanceUnit>> mutexGroupReteInstancesMap = reteInstance.getMutexGroupReteInstancesMap();
         if (mutexGroupReteInstancesMap != null) {
            this.mutexReteInstancesByGroup.putAll(mutexGroupReteInstancesMap);
         }
      }
   }

   public Agenda getAgenda() {
      return this.agenda;
   }

   protected void reset() {
      this.agenda.clean();
      this.factManager.clean();
   }
}
