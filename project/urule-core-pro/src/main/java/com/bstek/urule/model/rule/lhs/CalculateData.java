package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.model.rule.ObjectValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.EvaluationContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalculateData {
   private CalculateItem item;
   private int count = 1;
   private BigDecimal value;
   private Object resultValue;
   private boolean doAssignment;
   private List<Object> collectObjects = new ArrayList<>();

   public CalculateData(CalculateItem item) {
      this.item = item;
   }

   public Object getResultValue() {
      return this.resultValue;
   }

   public void buildValue(EvaluationContext context, Map<String, Object> factMap) {
      BigDecimal decimalValue = BigDecimal.valueOf(this.count);
      switch (this.item.getType()) {
         case count:
            this.resultValue = decimalValue;
            break;
         case avg:
            if (this.value != null) {
               BigDecimal decimalValue2 = this.value.divide(decimalValue, 8, 4).stripTrailingZeros();
               this.resultValue = decimalValue2;
            } else {
               this.resultValue = null;
            }
            break;
         case max:
            this.resultValue = this.value;
            break;
         case min:
            this.resultValue = this.value;
            break;
         case sum:
            this.resultValue = this.value;
            break;
         case collect:
            this.resultValue = this.collectObjects;
      }

      if (this.item.isEnableAssignment() && !this.doAssignment) {
         ObjectValue objectValue = new ObjectValue(this.resultValue);
         this.doAssignment(objectValue, context, factMap);
         this.doAssignment = true;
      }
   }

   private void doAssignment(Value localValue, EvaluationContext evaluationContext, Map<String, Object> valuesByKey) {
      VariableAssignAction variableAssignAction = new VariableAssignAction();
      variableAssignAction.setValue(localValue);
      variableAssignAction.setDatatype(this.item.getAssignDatatype());
      variableAssignAction.setVariableName(this.item.getAssignVariable());
      variableAssignAction.setVariableLabel(this.item.getAssignVariableLabel());
      variableAssignAction.setVariableCategory(this.item.getAssignVariableCategory());
      variableAssignAction.setKeyLabel(this.item.getKeyLabel());
      variableAssignAction.setKeyName(this.item.getKeyName());
      variableAssignAction.execute(evaluationContext, valuesByKey);
   }

   public void addObject(Object obj) {
      this.collectObjects.add(obj);
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public BigDecimal getValue() {
      return this.value;
   }

   public void setValue(BigDecimal value) {
      this.value = value;
   }
}
