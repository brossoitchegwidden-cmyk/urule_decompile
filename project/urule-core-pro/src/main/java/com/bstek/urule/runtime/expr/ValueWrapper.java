package com.bstek.urule.runtime.expr;

import com.bstek.urule.Utils;
import java.math.BigDecimal;

public class ValueWrapper {
   private BigDecimal bigDecimalValue;
   private Object originalValue;
   private String valueId;

   public ValueWrapper(Object originalValue, String valueId) {
      this.originalValue = originalValue;
      this.valueId = valueId;
   }

   public ValueWrapper(Object originalValue, BigDecimal bigDecimalValue, String valueId) {
      this.originalValue = originalValue;
      this.bigDecimalValue = bigDecimalValue;
      this.valueId = valueId;
   }

   public BigDecimal getBigDecimalValue() {
      if (this.bigDecimalValue != null) {
         return this.bigDecimalValue;
      }

      if (this.originalValue != null && !this.originalValue.equals("")) {
         try {
            this.bigDecimalValue = Utils.toBigDecimal(this.originalValue);
         } catch (Exception exception) {
         }
      }

      if (Utils.isSpaceToZero() && this.originalValue != null && this.originalValue.equals("")) {
         this.bigDecimalValue = BigDecimal.valueOf(0L);
      }

      return this.bigDecimalValue;
   }

   public Object getData() {
      return this.bigDecimalValue != null ? this.bigDecimalValue : this.originalValue;
   }

   public Object getOriginalValue() {
      return this.originalValue;
   }

   public String originalValueToString() {
      if (this.originalValue == null) {
         return "null";
      } else if (this.originalValue instanceof Number) {
         BigDecimal decimalValue = Utils.toBigDecimal(this.originalValue);
         return decimalValue.toPlainString();
      } else {
         return this.originalValue.toString();
      }
   }

   public String getValueId() {
      return this.valueId;
   }
}
