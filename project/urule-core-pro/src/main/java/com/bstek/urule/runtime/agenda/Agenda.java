package com.bstek.urule.runtime.agenda;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.FactManager;
import com.bstek.urule.runtime.WorkingMemory;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ContextImpl;
import com.bstek.urule.runtime.rete.EvaluationContext;
import com.bstek.urule.runtime.rete.EvaluationContextImpl;
import com.bstek.urule.runtime.rete.FactTracker;
import com.bstek.urule.runtime.rete.MutexReteInstanceUnit;
import com.bstek.urule.runtime.rete.ReteInstance;
import com.bstek.urule.runtime.rete.ReteInstanceUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class Agenda {
   private ActivationRuleBox activationRuleBox;
   private Context context;
   private FactManager factManager;
   private EvaluationContextImpl evaluationContext;
   private Map<String, List<ReteInstanceUnit>> mutexReteInstancesMap;
   private Map<String, List<ReteInstanceUnit>> pendedReteInstancesMap;

   public Agenda(WorkingMemory workingMemory, Map<String, String> allVariableCateogoryMap, Map<String, List<ReteInstanceUnit>> pendedReteInstancesMap, Map<String, List<ReteInstanceUnit>> mutexReteInstancesMap) {
      this.factManager = workingMemory.getFactManager();
      this.pendedReteInstancesMap = pendedReteInstancesMap;
      this.mutexReteInstancesMap = mutexReteInstancesMap;
      this.context = new ContextImpl(workingMemory, allVariableCateogoryMap);
      this.activationRuleBox = new ActivationRuleBox(this.context);
      this.evaluationContext = new EvaluationContextImpl(workingMemory, allVariableCateogoryMap);
   }

   public Collection<FactTracker> doRete(ReteInstance reteInstance, Object fact, boolean prime) {
      Collection doReteResult = reteInstance.enter(this.evaluationContext, fact);
      this.addActivations(doReteResult, fact, prime);
      return doReteResult;
   }

   public void execute(ReteInstance reteInstance, AgendaFilter filter, int max) {
      this.activationRuleBox.execute(filter, max);
      this.activationRuleBox.clean();
      this.evaluationContext.resetActivitiesState();
   }

   public void reEvaluationExecute(ReteInstance reteInstance) {
      this.activationRuleBox.execute(null, Integer.MAX_VALUE);
   }

   public void activePendedGroupAndExecute(String groupName) {
      this.activatePendedGroup(groupName, true);
   }

   public void activePendedGroup(String groupName) {
      this.activatePendedGroup(groupName, false);
   }

   private void activatePendedGroup(String groupName, boolean executeImmediately) {
      if (!this.pendedReteInstancesMap.containsKey(groupName)) {
         throw new RuleException("执行组 [" + groupName + "] 不存在！");
      }

      this.context.cleanTipMsg();
      this.evaluationContext.resetActivitiesState();
      List factList = this.factManager.getFactList();

      for (ReteInstanceUnit reteInstanceUnit : this.pendedReteInstancesMap.get(groupName)) {
         if (this.isRuleActive(reteInstanceUnit)) {
            if (reteInstanceUnit instanceof MutexReteInstanceUnit) {
               MutexReteInstanceUnit mutexReteInstanceUnit = (MutexReteInstanceUnit)reteInstanceUnit;
               this.context.addTipMsg("执行执行组[" + groupName + "]下的互斥组:" + mutexReteInstanceUnit.getMutexGroupName() + "");
               this.executeMutexGroup(mutexReteInstanceUnit, factList);
            } else {
               ReteInstance reteInstance = reteInstanceUnit.getReteInstance();

               for (Object objectValue : factList) {
                  this.doRete(reteInstance, objectValue, true);
               }

               this.doRete(reteInstance, "__*__", true);
            }
         }
      }

      if (executeImmediately) {
         this.activationRuleBox.execute(null, Integer.MAX_VALUE);
      }
   }

   private void executeMutexGroup(MutexReteInstanceUnit mutexReteInstanceUnit, List<Object> objects) {
      this.evaluationContext.resetActivitiesState();

      for (ReteInstance reteInstance : mutexReteInstanceUnit.getReteInstances()) {
         Collection items = null;

         for (Object objectValue : objects) {
            items = this.doRete(reteInstance, objectValue, true);
            if (items != null && items.size() > 0) {
               break;
            }
         }

         if (items == null || items.size() == 0) {
            items = this.doRete(reteInstance, "__*__", true);
         }

         if (items != null && items.size() != 0) {
            break;
         }
      }
   }

   public void activeMutexGroupRule(String mutexGroupName, String ruleName) {
      if (StringUtils.isBlank(mutexGroupName)) {
         throw new RuleException("互斥组名不能为空，当前操作只能发生在互斥组内部！");
      }

      if (!this.mutexReteInstancesMap.containsKey(mutexGroupName)) {
         throw new RuleException("互斥组 [" + mutexGroupName + "] 不存在!");
      }

      this.evaluationContext.resetActivitiesState();
      List items = this.mutexReteInstancesMap.get(mutexGroupName);
      List factList = this.factManager.getFactList();

      for (ReteInstanceUnit reteInstanceUnit : (Iterable<ReteInstanceUnit>)(Iterable<?>)(items)) {
         String ruleName2 = reteInstanceUnit.getRuleName();
         if (ruleName2.equals(ruleName) && this.isRuleActive(reteInstanceUnit)) {
            ReteInstance reteInstance = reteInstanceUnit.getReteInstance();

            for (Object objectValue : factList) {
               this.doRete(reteInstance, objectValue, false);
            }

            this.doRete(reteInstance, "__*__", false);
            break;
         }
      }
   }

   private boolean isRuleActive(ReteInstanceUnit reteInstanceUnit) {
      Date date = new Date();
      Date effectiveDate = reteInstanceUnit.getEffectiveDate();
      if (effectiveDate != null && effectiveDate.compareTo(date) < 0) {
         return false;
      }

      Date expiresDate = reteInstanceUnit.getExpiresDate();
      return expiresDate == null || expiresDate.compareTo(date) <= 0;
   }

   private void addActivations(Collection<FactTracker> factTrackers, Object objectValue, boolean prime) {
      if (factTrackers != null) {
         for (FactTracker factTracker : factTrackers) {
            Activation activation = factTracker.getActivation();
            Rule rule = activation.getRule();
            if (objectValue.equals("__*__") && rule.isWithElse()) {
               this.activationRuleBox.addElseRule(activation);
            } else {
               this.activationRuleBox.add(activation, prime);
            }
         }
      }
   }

   public Context getContext() {
      return this.context;
   }

   public EvaluationContext getEvaluationContext() {
      return this.evaluationContext;
   }

   public void clean() {
      this.activationRuleBox.clean();
   }
}
