package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.rule.loop.LoopTargetType;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.EvaluationContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AccumulateLeftPart implements LeftPart {
   private LoopTargetType loopTargetType;
   private LoopTarget loopTarget;
   private List<ConditionItem> conditionItems;
   private List<CalculateItem> calculateItems;
   private Junction junction;
   private String id;
   private static final Logger log = Logger.getLogger(AccumulateLeftPart.class.getName());

   public EvaluateResponse evaluate(EvaluationContext context, Map<String, Object> factMap) {
      List loopTarget = this.buildLoopTarget(context, factMap);
      if (loopTarget == null) {
         log.warning("AccumulateLeftPart collection value is null,return false.");
         EvaluateResponse evaluateResponse = new EvaluateResponse();
         evaluateResponse.setResult(false);
         return evaluateResponse;
      }

      HashMap valuesByKey = new HashMap();
      valuesByKey.putAll(factMap);
      HashMap valuesByKey2 = new HashMap();
      boolean flag = false;

      for (Object objectValue : loopTarget) {
         String className = Utils.getClassName(objectValue);
         valuesByKey.put(className, objectValue);
         boolean flag2 = this.evalCondition(context, valuesByKey);
         if (flag2) {
            flag = true;

            for (CalculateItem calculateItem : this.calculateItems) {
               calculateItem.calculate(context, valuesByKey, objectValue, valuesByKey2);
            }
         }
      }

      if (!flag) {
         for (CalculateItem calculateItem2 : this.calculateItems) {
            calculateItem2.init(valuesByKey2);
         }
      }

      for (CalculateData calculateData : (Iterable<CalculateData>)(Iterable<?>)(valuesByKey2.values())) {
         calculateData.buildValue(context, factMap);
      }

      EvaluateResponse evaluateResult = null;

      for (ConditionItem conditionItem : this.conditionItems) {
         EvaluateResponse evaluateResponse2 = conditionItem.eval(valuesByKey2, context, factMap);
         if (!evaluateResponse2.getResult()) {
            evaluateResult = evaluateResponse2;
            break;
         }
      }

      if (evaluateResult == null) {
         evaluateResult = new EvaluateResponse();
         evaluateResult.setResult(true);
      }

      return evaluateResult;
   }

   private boolean evalCondition(EvaluationContext evaluationContext, Map<String, Object> valuesByKey) {
      if (this.junction == null) {
         return true;
      }

      List criterions = this.junction.getCriterions();
      if (criterions != null && criterions.size() != 0) {
         boolean flag = true;
         if (this.junction instanceof Or) {
            flag = false;
         }

         boolean evalConditionResult = false;

         for (Criterion criterion : (Iterable<Criterion>)(Iterable<?>)(criterions)) {
            Criteria criteria = (Criteria)criterion;
            EvaluateResponse evaluateResponse = criteria.evaluate(evaluationContext, valuesByKey);
            if (flag) {
               if (!evaluateResponse.getResult()) {
                  evalConditionResult = false;
                  break;
               }

               evalConditionResult = true;
            } else {
               if (evaluateResponse.getResult()) {
                  evalConditionResult = true;
                  break;
               }

               evalConditionResult = false;
            }
         }

         return evalConditionResult;
      } else {
         return true;
      }
   }

   private List<Object> buildLoopTarget(Context context, Map<String, Object> valuesByKey) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.loopTarget.getValue(), context, valuesByKey);
      if (this.loopTargetType.equals(LoopTargetType.list)) {
         if (objectValue instanceof Collection) {
            ArrayList loopTarget = new ArrayList();
            Collection objectValue2 = (Collection)objectValue;
            loopTarget.addAll(objectValue2);
            return loopTarget;
         } else {
            return null;
         }
      } else {
         KnowledgeSession workingMemory = (KnowledgeSession)context.getWorkingMemory();
         List factList = workingMemory.getFactList();
         String className = Utils.getClassName(objectValue);
         ArrayList loopTarget2 = new ArrayList();

         for (Object objectValue3 : factList) {
            String className2 = Utils.getClassName(objectValue3);
            if (className2.equals(className)) {
               loopTarget2.add(objectValue3);
            }
         }

         return loopTarget2;
      }
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

   public List<ConditionItem> getConditionItems() {
      return this.conditionItems;
   }

   public void setConditionItems(List<ConditionItem> conditionItems) {
      this.conditionItems = conditionItems;
   }

   public List<CalculateItem> getCalculateItems() {
      return this.calculateItems;
   }

   public void setCalculateItems(List<CalculateItem> calculateItems) {
      this.calculateItems = calculateItems;
   }

   public Junction getJunction() {
      return this.junction;
   }

   public void setJunction(Junction junction) {
      this.junction = junction;
   }

   @Override
   public String getId() {
      if (this.id != null) {
         return this.id;
      }

      String text = LocaleHolder.isEnglish() ? "Accumulate" : "聚合";
      this.id = text + "," + this.loopTargetType.toString() + ":" + this.loopTarget.getValue().getId();
      int number = 0;

      for (ConditionItem conditionItem : this.conditionItems) {
         if (number == 0) {
            String text2 = LocaleHolder.isEnglish() ? "condition" : "条件";
            this.id = this.id + "；" + text2 + ":";
         } else {
            this.id = this.id + ",";
         }

         this.id = this.id + conditionItem.getLeft() + conditionItem.getOp() + conditionItem.getValue().getId();
         number++;
      }

      boolean flag = false;

      for (CalculateItem calculateItem : this.calculateItems) {
         if (!flag) {
            String text3 = LocaleHolder.isEnglish() ? "compute" : "计算";
            this.id = this.id + ";" + text3 + ":";
         } else {
            this.id = this.id + ",";
         }

         this.id = this.id + calculateItem.getType();
         if (calculateItem.getValue() != null) {
            this.id = this.id + "(" + calculateItem.getValue().getId() + ")";
         }

         if (calculateItem.isEnableAssignment()) {
            String text4 = LocaleHolder.isEnglish() ? "set" : "赋给";
            this.id = this.id + text4 + calculateItem.getAssignTargetType() + ":" + calculateItem.getAssignVariableCategory() + "." + calculateItem.getAssignVariableLabel();
            if (calculateItem.getKeyLabel() != null) {
               this.id = this.id + "." + calculateItem.getKeyLabel();
            }
         }
      }

      if (this.junction != null && this.junction.getCriterions() != null) {
         for (Criterion criterion : this.junction.getCriterions()) {
            if (criterion instanceof Criteria) {
               Criteria criteria = (Criteria)criterion;
               this.id = this.id + criteria.getId();
            }
         }
      }

      return this.id;
   }
}
