package com.bstek.urule.runtime.execution;

import com.bstek.urule.Utils;
import com.bstek.urule.action.WorkingMemoryHolderAdapter;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.exception.RuleExecutionException;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.model.rule.PredefineGroup;
import com.bstek.urule.model.rule.PredefineValueType;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.loop.LoopObjectThreadLocal;
import com.bstek.urule.runtime.AbstractWorkingMemory;
import com.bstek.urule.runtime.DynamicSpringConfigLoaderImpl;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.agenda.AgendaFilter;
import com.bstek.urule.runtime.response.ExecutionResponseImpl;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.bstek.urule.runtime.rete.EvaluationContext;
import com.bstek.urule.runtime.rete.ReteInstance;
import com.bstek.urule.runtime.rete.ReteInstanceUnit;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Executes normal and reevaluation passes across rule and predefine Retes. */
public class RuleExecution extends FlowExecution {
   private boolean registered;
   private long licenseLimit;
   private List<String> processedMutexGroupIds = new ArrayList<>();
   private static String licenseExpiredMessage = "";

   public RuleExecution(KnowledgeSession knowledgeSession, Map<String, String> allVariableCateogoryMap, boolean reg, long limit) {
      super(knowledgeSession, allVariableCateogoryMap);
      this.licenseLimit = limit;
      this.registered = reg;
   }

   public RuleExecutionResponse fireRules(AgendaFilter filter, Map<String, Object> params, int max) {
      this.agenda.getEvaluationContext().reset();
      this.knowledgeSession.getLogManager().clean();
      Map runtimeParameters = this.factManager.buildRuntimeParameters(params);
      this.monitorManager.doMonitorInputData(runtimeParameters);

      try {
         WorkingMemoryHolderAdapter.set(this.knowledgeSession);
         RuleExecutionResponse response = this.executeAllRules(filter, max);
         this.monitorManager.setTotalDuration(response.getDuration());
         this.monitorManager.doMonitor(runtimeParameters);
         this.reset();
         return response;
      } finally {
         WorkingMemoryHolderAdapter.clean();
      }
   }

   public RuleExecutionResponse reevaluationRete(Object obj) {
      this.factManager.addToFactsMap(obj);
      ArrayList facts = new ArrayList();
      facts.add(obj);
      return this.reevaluateRules(facts);
   }

   private RuleExecutionResponse executeAllRules(AgendaFilter agendaFilter, int number) {
      if (DynamicSpringConfigLoaderImpl.getLimit() != this.licenseLimit) {
         throw new RuntimeException(new String(Base64.getDecoder().decode("RW52aXJvbm1lbnQgaXMgYnJva2Vu")));
      }

      if (this.registered && DynamicSpringConfigLoaderImpl.getAuthInfo() != null) {
         if (DynamicSpringConfigLoaderImpl.getLimit() >= 0L) {
            long time = new Date().getTime();
            if (time > DynamicSpringConfigLoaderImpl.getLimit()) {
               throw new RuleExecutionException(licenseExpiredMessage);
            }
         }
      } else {
         long time2 = new Date().getTime();
         if (time2 > DynamicSpringConfigLoaderImpl.getTrialExpired()) {
            throw new RuleExecutionException(licenseExpiredMessage);
         }
      }

      long longValue = System.currentTimeMillis();
      ExecutionResponseImpl executionResponseImpl = new ExecutionResponseImpl();

      for (PredefineExecutionUnit predefineExecutionUnit : this.predefineExecutionUnits) {
         if (predefineExecutionUnit.getGroup() != null) {
            this.executePredefineGroup(predefineExecutionUnit.getGroup(), agendaFilter, number);
         }
      }

      for (ReteInstance reteInstance : this.reteInstanceList) {
         this.executeRete(agendaFilter, number, reteInstance);
      }

      executionResponseImpl.setDuration(System.currentTimeMillis() - longValue);
      return executionResponseImpl;
   }

   private String describePredefine(PredefineGroup predefineGroup, Predefine predefine) {
      return "规则文件【" + predefineGroup.getFilePath() + "】中的预定义对象【" + predefine.getName() + "】";
   }

   private void executePredefineGroup(PredefineGroup predefineGroup, AgendaFilter agendaFilter, int number) {
      if (predefineGroup != null) {
         AbstractWorkingMemory abstractWorkingMemory = (AbstractWorkingMemory)this.knowledgeSession;
         HashMap valuesByKey = new HashMap<>(this.factManager.getFactMap());
         Predefine predefine = predefineGroup.getPredefine();
         PredefineValueType valueType = predefine.getValueType();
         EvaluationContext evaluationContext = this.agenda.getEvaluationContext();
         Value localValue = predefine.getValue();
         if (valueType.equals(PredefineValueType.from)) {
            if (localValue == null) {
               throw new RuleException(this.describePredefine(predefineGroup, predefine) + "值未定义");
            }

            try {
               Object objectValue = evaluationContext.getValueCompute().complexValueCompute(localValue, evaluationContext, this.factManager.getFactMap());
               abstractWorkingMemory.setPredefineValue(predefine.getUuid(), objectValue);
               this.executePredefineKnowledgePackage(agendaFilter, number, predefineGroup);
            } catch (Exception exception) {
               if (exception.getCause() instanceof InvocationTargetException) {
                  InvocationTargetException cause = (InvocationTargetException)exception.getCause();
                  throw new RuleException(this.describePredefine(predefineGroup, predefine) + "赋值失败:" + cause.getTargetException().getMessage());
               }

               throw new RuleException(this.describePredefine(predefineGroup, predefine) + "赋值失败:" + exception.getMessage());
            }
         } else if (valueType.equals(PredefineValueType.in)) {
            if (localValue == null) {
               throw new RuleException(this.describePredefine(predefineGroup, predefine) + "值未定义");
            }

            for (Object objectValue2 : this.resolvePredefineValues(predefineGroup, predefine, localValue, evaluationContext)) {
               abstractWorkingMemory.setPredefineValue(predefine.getUuid(), objectValue2);
               LoopObjectThreadLocal.setLoopObject(objectValue2);
               this.factManager.insertLoopFact(objectValue2);
               if (predefine.withConditions()) {
                  String className = Utils.getClassName(objectValue2);
                  valuesByKey.put(className, objectValue2);
                  if (!predefine.evalCriterias(evaluationContext, valuesByKey)) {
                     continue;
                  }
               }

               this.executePredefineKnowledgePackage(agendaFilter, number, predefineGroup);
               LoopObjectThreadLocal.clean();
            }
         } else {
            this.executePredefineKnowledgePackage(agendaFilter, number, predefineGroup);
         }
      }
   }

   private void executePredefineKnowledgePackage(AgendaFilter agendaFilter, int number, PredefineGroup predefineGroup) {
      KnowledgePackageWrapper knowledgePackageWrapper = predefineGroup.getKnowledgePackageWrapper();
      if (knowledgePackageWrapper != null) {
         KnowledgePackage knowledgePackage = knowledgePackageWrapper.getKnowledgePackage();
         if (knowledgePackage.getAloneReteInstances().size() == 0) {
            ReteInstance reteInstance = knowledgePackage.loadReteInstance();
            this.executeRete(agendaFilter, number, reteInstance);
         } else {
            for (ReteInstance reteInstance2 : knowledgePackage.getAloneReteInstances()) {
               this.executeRete(agendaFilter, number, reteInstance2);
            }
         }
      }

      this.executePredefineGroup(predefineGroup.getNextGroup(), agendaFilter, number);
   }

   private void executeRete(AgendaFilter agendaFilter, int number, ReteInstance reteInstance) {
      List factList = this.factManager.getFactList();

      for (Object objectValue : factList) {
         this.agenda.doRete(reteInstance, objectValue, true);
      }

      this.agenda.doRete(reteInstance, "__*__", true);
      this.executeMutexGroups((Collection<Object>)factList, reteInstance);
      this.agenda.execute(reteInstance, agendaFilter, number);
      this.processedMutexGroupIds.clear();
   }

   private RuleExecutionResponse reevaluateRules(List<Object> objects) {
      long longValue = System.currentTimeMillis();
      ExecutionResponseImpl executionResponseImpl = new ExecutionResponseImpl();

      for (PredefineExecutionUnit predefineExecutionUnit : this.predefineExecutionUnits) {
         if (predefineExecutionUnit.getGroup() != null) {
            this.reevaluatePredefineGroup(predefineExecutionUnit.getGroup(), objects);
         }
      }

      for (ReteInstance reteInstance : this.reteInstanceList) {
         this.reevaluateRete(objects, reteInstance);
      }

      executionResponseImpl.setDuration(System.currentTimeMillis() - longValue);
      return executionResponseImpl;
   }

   private void reevaluatePredefineGroup(PredefineGroup predefineGroup, List<Object> objects) {
      if (predefineGroup != null) {
         AbstractWorkingMemory abstractWorkingMemory = (AbstractWorkingMemory)this.knowledgeSession;
         HashMap valuesByKey = new HashMap<>(this.factManager.getFactMap());
         Predefine predefine = predefineGroup.getPredefine();
         PredefineValueType valueType = predefine.getValueType();
         EvaluationContext evaluationContext = this.agenda.getEvaluationContext();
         Value localValue = predefine.getValue();
         if (valueType.equals(PredefineValueType.from)) {
            if (localValue == null) {
               throw new RuleException(this.describePredefine(predefineGroup, predefine) + "值未定义");
            }

            Object objectValue = evaluationContext.getValueCompute().complexValueCompute(localValue, evaluationContext, this.factManager.getFactMap());
            abstractWorkingMemory.setPredefineValue(predefine.getUuid(), objectValue);
            this.reevaluatePredefineKnowledgePackage(objects, predefineGroup);
         } else if (valueType.equals(PredefineValueType.in)) {
            if (localValue == null) {
               throw new RuleException(this.describePredefine(predefineGroup, predefine) + "值未定义");
            }

            for (Object objectValue2 : this.resolvePredefineValues(predefineGroup, predefine, localValue, evaluationContext)) {
               abstractWorkingMemory.setPredefineValue(predefine.getUuid(), objectValue2);
               LoopObjectThreadLocal.setLoopObject(objectValue2);
               this.factManager.insertLoopFact(objectValue2);
               if (predefine.withConditions()) {
                  String className = Utils.getClassName(objectValue2);
                  valuesByKey.put(className, objectValue2);
                  if (!predefine.evalCriterias(evaluationContext, valuesByKey)) {
                     continue;
                  }
               }

               this.reevaluatePredefineKnowledgePackage(objects, predefineGroup);
               LoopObjectThreadLocal.clean();
            }
         } else {
            this.reevaluatePredefineKnowledgePackage(objects, predefineGroup);
         }
      }
   }

   private void reevaluatePredefineKnowledgePackage(List<Object> objects, PredefineGroup predefineGroup) {
      KnowledgePackageWrapper knowledgePackageWrapper = predefineGroup.getKnowledgePackageWrapper();
      if (knowledgePackageWrapper != null) {
         KnowledgePackage knowledgePackage = knowledgePackageWrapper.getKnowledgePackage();
         if (knowledgePackage.getAloneReteInstances().size() == 0) {
            ReteInstance reteInstance = knowledgePackage.loadReteInstance();
            this.reevaluateRete(objects, reteInstance);
         } else {
            for (ReteInstance reteInstance2 : knowledgePackage.getAloneReteInstances()) {
               this.reevaluateRete(objects, reteInstance2);
            }
         }
      }

      this.reevaluatePredefineGroup(predefineGroup.getNextGroup(), objects);
   }

   private Collection<?> resolvePredefineValues(PredefineGroup predefineGroup, Predefine predefine, Value localValue, EvaluationContext evaluationContext) {
      if (localValue.getArithmetic() == null && localValue instanceof VariableCategoryValue) {
         VariableCategoryValue variableCategoryValue = (VariableCategoryValue)localValue;
         String variableCategory = variableCategoryValue.getVariableCategory();
         String variableCategoryClass = evaluationContext.getVariableCategoryClass(variableCategory);
         List facts = this.factManager.getFacts(variableCategoryClass);
         if (facts == null) {
            throw new RuleException("当前工作区中不存在【" + variableCategoryClass + "】类型的对象");
         } else {
            return facts;
         }
      } else {
         Object objectValue = evaluationContext.getValueCompute().complexValueCompute(localValue, evaluationContext, this.factManager.getFactMap());
         if (objectValue instanceof Collection) {
            return (Collection<?>)objectValue;
         }

         if (null == objectValue) {
         throw new RuleException(this.describePredefine(predefineGroup, predefine) + "值未定义");
         }

         ArrayList items = new ArrayList();
         items.add(objectValue);
         return items;
      }
   }

   private void reevaluateRete(List<Object> objects, ReteInstance reteInstance) {
      for (Object objectValue : objects) {
         this.agenda.doRete(reteInstance, objectValue, false);
      }

      this.agenda.doRete(reteInstance, "__*__", false);
      this.executeMutexGroups((Collection<Object>)objects, reteInstance);
      this.agenda.reEvaluationExecute(reteInstance);
   }

   private void executeMutexGroups(Collection<Object> objects, ReteInstance reteInstance) {
      Map mutexGroupReteInstancesMap = reteInstance.getMutexGroupReteInstancesMap();
      if (mutexGroupReteInstancesMap != null) {
         Collection items = null;

         for (String text : (Iterable<String>)(Iterable<?>)(mutexGroupReteInstancesMap.keySet())) {
            String text2 = reteInstance.getId() + text;
            if (!this.processedMutexGroupIds.contains(text2)) {
               this.agenda.getContext().cleanTipMsg();
               this.agenda.getContext().addTipMsg("执行互斥组:" + text + "");
               List items2 = (List)mutexGroupReteInstancesMap.get(text);
               Date date = new Date();

               for (ReteInstanceUnit reteInstanceUnit : (Iterable<ReteInstanceUnit>)(Iterable<?>)(items2)) {
                  Date effectiveDate = reteInstanceUnit.getEffectiveDate();
                  if (effectiveDate == null || effectiveDate.compareTo(date) <= 0) {
                     Date expiresDate = reteInstanceUnit.getExpiresDate();
                     if (expiresDate == null || expiresDate.compareTo(date) >= 0) {
                        ReteInstance reteInstance2 = reteInstanceUnit.getReteInstance();

                        for (Object objectValue : objects) {
                           items = this.agenda.doRete(reteInstance2, objectValue, true);
                           if (items != null && items.size() != 0) {
                              this.processedMutexGroupIds.add(text2);
                              break;
                           }
                        }

                        if (items != null && items.size() != 0) {
                           break;
                        }

                        items = this.agenda.doRete(reteInstance2, "__*__", true);
                        if (items != null) {
                           this.processedMutexGroupIds.add(text2);
                           break;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   protected void reset() {
      super.reset();
      this.processedMutexGroupIds.clear();
   }

   static {
      try {
         licenseExpiredMessage = URLDecoder.decode(
            "%E5%BD%93%E5%89%8D%E4%BD%BF%E7%94%A8%E7%9A%84%E6%98%AFURule%20Pro%E8%AF%95%E7%94%A8%E7%89%88%E5%B7%B2%E8%BF%87%E6%9C%9F%EF%BC%8C%E8%AF%B7%E9%87%87%E8%B4%AD%E6%AD%A3%E5%BC%8F%E7%89%88%E6%9C%AC%EF%BC%81(The%20trial%20period%20has%20expired%2Cplease%20purchase%20the%20official%20version!)",
            "UTF-8"
         );
      } catch (UnsupportedEncodingException exception) {
         throw new IllegalStateException("UTF-8 encoding is unavailable", exception);
      }
   }
}
