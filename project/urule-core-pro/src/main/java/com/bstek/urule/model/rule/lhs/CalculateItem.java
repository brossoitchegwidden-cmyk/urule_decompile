package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.EvaluationContext;
import java.math.BigDecimal;
import java.util.Map;

public class CalculateItem {
   private CalculateType type;
   private Value value;
   private boolean enableAssignment;
   private String assignTargetType;
   private String assignCategoryUuid;
   private String assignVariableCategory;
   private String assignVariable;
   private String assignVariableUuid;
   private String assignVariableLabel;
   private Datatype assignDatatype;
   private String keyCategoryUuid;
   private String keyUuid;
   private String keyLabel;
   private String keyName;

   public void init(Map<String, CalculateData> resultMap) {
      CalculateData calculateData = new CalculateData(this);
      calculateData.setCount(0);
      calculateData.setValue(new BigDecimal(0));
      resultMap.put(this.type.name(), calculateData);
   }

   public void calculate(EvaluationContext context, Map<String, Object> factMap, Object obj, Map<String, CalculateData> resultMap) {
      switch (this.type) {
         case count:
            CalculateData calculateData = (CalculateData)resultMap.get(this.type.toString());
            if (calculateData == null) {
               calculateData = new CalculateData(this);
               resultMap.put(this.type.name(), calculateData);
            } else {
               calculateData.setCount(calculateData.getCount() + 1);
            }
            break;
         case avg:
            this.doCal(context, factMap, resultMap);
            break;
         case max:
            this.doCal(context, factMap, resultMap);
            break;
         case min:
            this.doCal(context, factMap, resultMap);
            break;
         case sum:
            this.doCal(context, factMap, resultMap);
            break;
         case collect:
            CalculateData calculateData2 = (CalculateData)resultMap.get(this.type.toString());
            if (calculateData2 == null) {
               calculateData2 = new CalculateData(this);
               calculateData2.addObject(obj);
               resultMap.put(this.type.name(), calculateData2);
            } else {
               calculateData2.addObject(obj);
            }
      }
   }

   private void doCal(EvaluationContext evaluationContext, Map<String, Object> valuesByKey, Map<String, CalculateData> valuesByKey2) {
      Object objectValue = evaluationContext.getValueCompute().complexValueCompute(this.value, evaluationContext, valuesByKey);
      BigDecimal decimalValue = null;
      if (objectValue != null) {
         decimalValue = Utils.toBigDecimal(objectValue);
      }

      CalculateData calculateData = (CalculateData)valuesByKey2.get(this.type.name());
      if (calculateData == null) {
         calculateData = new CalculateData(this);
         calculateData.setValue(decimalValue);
         valuesByKey2.put(this.type.name(), calculateData);
      } else {
         calculateData.setCount(calculateData.getCount() + 1);
         if (decimalValue != null) {
            BigDecimal decimalValue2 = calculateData.getValue();
            if (decimalValue2 != null) {
               if (this.type.equals(CalculateType.max)) {
                  int number = decimalValue.compareTo(decimalValue2);
                  if (number == 1) {
                     calculateData.setValue(decimalValue);
                  }
               } else if (this.type.equals(CalculateType.min)) {
                  int number2 = decimalValue.compareTo(decimalValue2);
                  if (number2 == 0) {
                     calculateData.setValue(decimalValue);
                  }
               } else {
                  BigDecimal decimalValue3 = Utils.toBigDecimal(decimalValue2);
                  calculateData.setValue(decimalValue.add(decimalValue3));
               }
            } else {
               calculateData.setValue(decimalValue);
            }
         }
      }
   }

   public CalculateType getType() {
      return this.type;
   }

   public void setType(CalculateType type) {
      this.type = type;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   public boolean isEnableAssignment() {
      return this.enableAssignment;
   }

   public void setEnableAssignment(boolean enableAssignment) {
      this.enableAssignment = enableAssignment;
   }

   public String getAssignTargetType() {
      return this.assignTargetType;
   }

   public void setAssignTargetType(String assignTargetType) {
      this.assignTargetType = assignTargetType;
   }

   public String getAssignVariableCategory() {
      return this.assignVariableCategory;
   }

   public void setAssignVariableCategory(String assignVariableCategory) {
      this.assignVariableCategory = assignVariableCategory;
   }

   public String getAssignVariable() {
      return this.assignVariable;
   }

   public void setAssignVariable(String assignVariable) {
      this.assignVariable = assignVariable;
   }

   public String getAssignVariableLabel() {
      return this.assignVariableLabel;
   }

   public void setAssignVariableLabel(String assignVariableLabel) {
      this.assignVariableLabel = assignVariableLabel;
   }

   public String getAssignCategoryUuid() {
      return this.assignCategoryUuid;
   }

   public String getKeyCategoryUuid() {
      return this.keyCategoryUuid;
   }

   public void setKeyCategoryUuid(String keyCategoryUuid) {
      this.keyCategoryUuid = keyCategoryUuid;
   }

   public String getKeyUuid() {
      return this.keyUuid;
   }

   public void setKeyUuid(String keyUuid) {
      this.keyUuid = keyUuid;
   }

   public void setAssignCategoryUuid(String assignCategoryUuid) {
      this.assignCategoryUuid = assignCategoryUuid;
   }

   public String getAssignVariableUuid() {
      return this.assignVariableUuid;
   }

   public void setAssignVariableUuid(String assignVariableUuid) {
      this.assignVariableUuid = assignVariableUuid;
   }

   public Datatype getAssignDatatype() {
      return this.assignDatatype;
   }

   public void setAssignDatatype(Datatype assignDatatype) {
      this.assignDatatype = assignDatatype;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String keyLabel) {
      this.keyLabel = keyLabel;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }
}
