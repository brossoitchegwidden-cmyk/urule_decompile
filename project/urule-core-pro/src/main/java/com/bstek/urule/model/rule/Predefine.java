package com.bstek.urule.model.rule;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rete.JunctionJsonDeserializer;
import com.bstek.urule.model.rete.ValueJsonDeserializer;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.EvaluateResponse;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.runtime.rete.EvaluationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

public class Predefine {
   private String uuid;
   private String name;
   private String type;
   private PredefineValueType valueType;
   @JsonDeserialize(using = ValueJsonDeserializer.class)
   private Value value;
   @JsonDeserialize(using = JunctionJsonDeserializer.class)
   private Junction junction;

   public boolean evalCriterias(EvaluationContext context, Map<String, Object> factMap) {
      return this.junction != null && this.junction.getCriterions().size() != 0 ? this.evalCriterion(this.junction, context, factMap) : true;
   }

   private boolean evalCriterion(Criterion criterion, EvaluationContext evaluationContext, Map<String, Object> valuesByKey) {
      if (criterion instanceof Criteria) {
         Criteria criteria = (Criteria)criterion;
         EvaluateResponse evaluateResponse = criteria.evaluate(evaluationContext, valuesByKey);
         return evaluateResponse.getResult();
      }

      if (criterion instanceof Junction) {
         Junction junction = (Junction)criterion;
         boolean flag = true;
         if (junction.getJunctionType().contentEquals("and")) {
            flag = false;
         }

         for (Criterion criterion2 : this.junction.getCriterions()) {
            boolean flag2 = this.evalCriterion(criterion2, evaluationContext, valuesByKey);
            if (flag) {
               if (flag2) {
                  return true;
               }
            } else if (!flag2) {
               return false;
            }
         }

         return !flag;
      } else {
         throw new RuleException("Unsupport Criterion [" + criterion.getClass().getName() + "] In Predefine Conditions");
      }
   }

   public boolean withConditions() {
      return this.junction != null && this.junction.getCriterions().size() != 0;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public PredefineValueType getValueType() {
      return this.valueType;
   }

   public void setValueType(PredefineValueType valueType) {
      this.valueType = valueType;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   public Junction getJunction() {
      return this.junction;
   }

   public void setJunction(Junction junction) {
      this.junction = junction;
   }
}
