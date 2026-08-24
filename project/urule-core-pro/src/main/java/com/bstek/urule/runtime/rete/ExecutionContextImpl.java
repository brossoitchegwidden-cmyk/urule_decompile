package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.Criteria;
import java.util.Map;
import java.util.Set;

public class ExecutionContextImpl implements ExecutionContext {
   private Rule currentRule;
   private Set<Criteria> currentRuleCriterias;
   private Map<String, Object> currentRuleFactMap;

   public void setCurrentRuleCriterias(Set<Criteria> currentRuleCriterias) {
      this.currentRuleCriterias = currentRuleCriterias;
   }

   @Override
   public Set<Criteria> getCurrentRuleCriterias() {
      return this.currentRuleCriterias;
   }

   public void setCurrentRuleFactMap(Map<String, Object> currentRuleFactMap) {
      this.currentRuleFactMap = currentRuleFactMap;
   }

   @Override
   public Map<String, Object> getCurrentRuleFactMap() {
      return this.currentRuleFactMap;
   }

   public void setCurrentRule(Rule currentRule) {
      this.currentRule = currentRule;
   }

   @Override
   public Rule getCurrentRule() {
      return this.currentRule;
   }
}
