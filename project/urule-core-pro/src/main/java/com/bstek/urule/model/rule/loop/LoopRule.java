package com.bstek.urule.model.rule.loop;

import com.bstek.urule.action.Action;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.FactManager;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.rete.Context;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LoopRule extends Rule {
   private LoopStart loopStart;
   private LoopEnd loopEnd;
   private LoopTargetType loopTargetType = LoopTargetType.list;
   private LoopTarget loopTarget;
   private List<LoopRuleUnit> units;
   private KnowledgePackageWrapper knowledgePackageWrapper;
   private Logger log = Logger.getGlobal();

   public LoopRule() {
      this.setLoopRule(true);
   }

   public void execute(Context context, Map<String, Object> factMap) {
      Object loopTarget = this.buildLoopTarget(context, factMap);
      if (loopTarget == null) {
         this.log.warning("Loop rule [" + this.getName() + "] target value is null,cannot be executed.");
      } else {
         KnowledgeSession workingMemory = (KnowledgeSession)context.getWorkingMemory();
         Map parameters = workingMemory.getParameters();
         if (this.loopStart != null) {
            this.doActions(this.loopStart.getActions(), context, factMap, true);
         }

         boolean flag = true;
         KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(this.knowledgePackageWrapper, context, workingMemory);
         List factList = workingMemory.getFactList();
         if (loopTarget instanceof Collection) {
            for (Object objectValue : (Collection)loopTarget) {
               parameters = this.doLoop(knowledgeSession, parameters, objectValue, factList, flag);
               if (this.breakLoop(parameters)) {
                  break;
               }

               flag = false;
            }
         } else if (loopTarget instanceof Object[]) {
            Object[] loopTarget2 = (Object[])loopTarget;

            for (Object objectValue2 : loopTarget2) {
               parameters = this.doLoop(knowledgeSession, parameters, objectValue2, factList, flag);
               if (this.breakLoop(parameters)) {
                  break;
               }

               flag = false;
            }
         }

         workingMemory.getParameters().putAll(parameters);
         if (this.loopEnd != null) {
            this.doActions(this.loopEnd.getActions(), context, factMap, false);
         }
      }
   }

   private Map<String, Object> doLoop(KnowledgeSession knowledgeSession, Map<String, Object> valuesByKey, Object objectValue, List<Object> objects, boolean flag) {
      FactManager factManager = knowledgeSession.getFactManager();
      String className = this.getClass(objectValue);
      if (flag) {
         factManager.clean();
      }

      LoopObjectThreadLocal.clean();

      for (Object objectValue2 : objects) {
         String text = this.getClass(objectValue2);
         if (!text.equals(HashMap.class.getName()) && !text.equals(className)) {
            factManager.insertLoopFact(objectValue2);
         }
      }

      LoopObjectThreadLocal.setLoopObject(objectValue);
      factManager.insertLoopFact(objectValue);
      knowledgeSession.fireRules(valuesByKey);
      Map parameters = knowledgeSession.getParameters();
      valuesByKey = new HashMap();
      valuesByKey.putAll(parameters);
      LoopObjectThreadLocal.clean();
      return valuesByKey;
   }

   private boolean breakLoop(Map<String, Object> valuesByKey) {
      boolean breakLoopResult = false;
      if (valuesByKey.containsKey("_loop_rule_break_tag__")) {
         valuesByKey.remove("_loop_rule_break_tag__");
         breakLoopResult = true;
      }

      return breakLoopResult;
   }

   private void doActions(List<Action> actions, Context context, Map<String, Object> valuesByKey, boolean flag) {
      if (actions != null && actions.size() != 0) {
         if (flag) {
            context.addTipMsg("执行【" + this.getName() + "】开始前动作");
            context.getLogger().logMessage("==执行循环规则规则【" + this.getName() + "】的开始前动作==");
         } else {
            context.addTipMsg("执行【" + this.getName() + "】结束后动作");
            context.getLogger().logMessage("==执行循环规则规则【" + this.getName() + "】的结束后动作==");
         }

         for (Action action : actions) {
            if (this.getDebug() != null) {
               action.setDebug(this.getDebug());
            }

            action.execute(context, valuesByKey);
         }

         context.cleanTipMsg();
      }
   }

   private Object buildLoopTarget(Context context, Map<String, Object> valuesByKey) {
      Object loopTarget = context.getValueCompute().complexValueCompute(this.loopTarget.getValue(), context, valuesByKey);
      if (this.loopTargetType.equals(LoopTargetType.list)) {
         if (loopTarget != null && !(loopTarget instanceof Collection) && !(loopTarget instanceof Object[])) {
            throw new RuntimeException("循环对象必须是一个Collection类型的集合对象或一个数组对象，当前对象为：" + loopTarget + ".");
         } else {
            return loopTarget;
         }
      } else {
         KnowledgeSession workingMemory = (KnowledgeSession)context.getWorkingMemory();
         List factList = workingMemory.getFactList();
         String className = this.getClass(loopTarget);
         ArrayList loopTarget2 = new ArrayList();

         for (Object objectValue : factList) {
            String text = this.getClass(objectValue);
            if (text.equals(className)) {
               loopTarget2.add(objectValue);
            }
         }

         return loopTarget2;
      }
   }

   private String getClass(Object objectValue) {
      String text = null;
      if (objectValue instanceof GeneralEntity) {
         text = ((GeneralEntity)objectValue).getTargetClass();
      } else {
         text = objectValue.getClass().getName();
      }

      return text;
   }

   public List<LoopRuleUnit> getUnits() {
      return this.units;
   }

   public void setUnits(List<LoopRuleUnit> units) {
      this.units = units;
   }

   public LoopStart getLoopStart() {
      return this.loopStart;
   }

   public void setLoopStart(LoopStart loopStart) {
      this.loopStart = loopStart;
   }

   public LoopEnd getLoopEnd() {
      return this.loopEnd;
   }

   public void setLoopEnd(LoopEnd loopEnd) {
      this.loopEnd = loopEnd;
   }

   public LoopTargetType getLoopTargetType() {
      return this.loopTargetType;
   }

   public void setLoopTargetType(LoopTargetType loopTargetType) {
      this.loopTargetType = loopTargetType;
   }

   public LoopTarget getLoopTarget() {
      return this.loopTarget;
   }

   public void setLoopTarget(LoopTarget loopTarget) {
      this.loopTarget = loopTarget;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
      this.knowledgePackageWrapper = knowledgePackageWrapper;
   }
}
