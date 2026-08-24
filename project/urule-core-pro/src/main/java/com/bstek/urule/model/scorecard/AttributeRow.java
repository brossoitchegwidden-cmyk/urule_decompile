package com.bstek.urule.model.scorecard;

import java.util.List;

public class AttributeRow extends CardRow {
   private List<ConditionRow> conditionRows;

   public List<ConditionRow> getConditionRows() {
      return this.conditionRows;
   }

   public void setConditionRows(List<ConditionRow> conditionRows) {
      this.conditionRows = conditionRows;
   }
}
