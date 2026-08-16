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

   public boolean evalCriterias(EvaluationContext var1, Map<String, Object> var2) {
      return this.junction != null && this.junction.getCriterions().size() != 0 ? this.evalCriterion(this.junction, var1, var2) : true;
   }

   private boolean evalCriterion(Criterion var1, EvaluationContext var2, Map<String, Object> var3) {
      if (var1 instanceof Criteria) {
         Criteria var9 = (Criteria)var1;
         EvaluateResponse var10 = var9.evaluate(var2, var3);
         return var10.getResult();
      }

      if (var1 instanceof Junction) {
         Junction var4 = (Junction)var1;
         boolean var5 = true;
         if (var4.getJunctionType().contentEquals("and")) {
            var5 = false;
         }

         for (Criterion var7 : this.junction.getCriterions()) {
            boolean var8 = this.evalCriterion(var7, var2, var3);
            if (var5) {
               if (var8) {
                  return true;
               }
            } else if (!var8) {
               return false;
            }
         }

         return !var5;
      } else {
         throw new RuleException("Unsupport Criterion [" + var1.getClass().getName() + "] In Predefine Conditions");
      }
   }

   public boolean withConditions() {
      return this.junction != null && this.junction.getCriterions().size() != 0;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public PredefineValueType getValueType() {
      return this.valueType;
   }

   public void setValueType(PredefineValueType var1) {
      this.valueType = var1;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value var1) {
      this.value = var1;
   }

   public Junction getJunction() {
      return this.junction;
   }

   public void setJunction(Junction var1) {
      this.junction = var1;
   }
}
