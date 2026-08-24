package com.bstek.urule.runtime.percent;

import com.bstek.urule.model.flow.DecisionItem;
import com.bstek.urule.model.flow.ProcessDefinition;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryPercentDataStore implements PercentDataStore {
   private Map<String, PercentUnit> percentUnitsByDecisionNode = new ConcurrentHashMap<>();

   @Override
   public PercentUnit getDecisionNodePercent(ProcessDefinition pd, List<DecisionItem> items, String decisionNodeName) {
      int number = this.calculateDecisionUnitId(items);
      String text = this.buildDecisionNodeKey(pd, decisionNodeName);
      if (this.percentUnitsByDecisionNode.containsKey(text)) {
         PercentUnit percentUnit = this.percentUnitsByDecisionNode.get(text);
         if (percentUnit.getUnitId() == number && percentUnit.getTotal() + 100L < Long.MAX_VALUE) {
            return percentUnit;
         }

         this.percentUnitsByDecisionNode.remove(text);
      }

      PercentUnit decisionNodePercent = new PercentUnit();
      decisionNodePercent.setUnitId(number);
      decisionNodePercent.setDecisionNodeName(decisionNodeName);
      decisionNodePercent.setFlowId(pd.getId());
      decisionNodePercent.setTotal(0L);
      this.percentUnitsByDecisionNode.put(text, decisionNodePercent);
      return decisionNodePercent;
   }

   private String buildDecisionNodeKey(ProcessDefinition processDefinition, String text) {
      return processDefinition.getFile() == null ? "" : processDefinition.getFile() + "." + processDefinition.getId() + "." + text;
   }

   private int calculateDecisionUnitId(List<DecisionItem> decisionItems) {
      StringBuilder stringBuilder = new StringBuilder();

      for (DecisionItem decisionItem : decisionItems) {
         stringBuilder.append(decisionItem.getTo() + decisionItem.getPercent());
      }

      return stringBuilder.toString().hashCode();
   }
}
