package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.Criteria;
import java.util.Map;
import java.util.Set;

public class ExecutionContextImpl implements ExecutionContext {
   private Rule currentRule;
   private Set<Criteria> currentRuleCriterias;
   private Map<String, Object> currentRuleFactMap;

   public void setCurrentRuleCriterias(Set<Criteria> var1) {
      this.currentRuleCriterias = var1;
   }

   @Override
   public Set<Criteria> getCurrentRuleCriterias() {
      return this.currentRuleCriterias;
   }

   public void setCurrentRuleFactMap(Map<String, Object> var1) {
      this.currentRuleFactMap = var1;
   }

   @Override
   public Map<String, Object> getCurrentRuleFactMap() {
      return this.currentRuleFactMap;
   }

   public void setCurrentRule(Rule var1) {
      this.currentRule = var1;
   }

   @Override
   public Rule getCurrentRule() {
      return this.currentRule;
   }
}
