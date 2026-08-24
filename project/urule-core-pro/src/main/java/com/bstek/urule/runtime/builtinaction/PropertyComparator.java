package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.Utils;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

/** Compares objects by one or more comma-separated property names. */
final class PropertyComparator implements Comparator<Object> {
   private final String propertyNames;
   private final boolean ascending;

   PropertyComparator(String propertyNames, boolean ascending) {
      this.propertyNames = propertyNames;
      this.ascending = ascending;
   }

   @Override
   public int compare(Object left, Object right) {
      int result = 0;
      for (String propertyName : this.propertyNames.split(",")) {
         result = this.comparePropertyValues(propertyName, left, right);
         if (result != 0) {
            break;
         }
      }
      return result;
   }

   private int comparePropertyValues(String propertyName, Object left, Object right) {
      Object leftValue = Utils.getObjectProperty(left, propertyName);
      Object rightValue = Utils.getObjectProperty(right, propertyName);
      if (leftValue == null) {
         return this.ascending ? 0 : 1;
      }
      if (rightValue == null) {
         return this.ascending ? 1 : 0;
      }
      if (leftValue instanceof String) {
         return this.ascending
               ? ((String)leftValue).compareTo(rightValue.toString())
               : ((String)rightValue).compareTo(leftValue.toString());
      }
      if (leftValue instanceof Date) {
         return this.ascending
               ? ((Date)leftValue).compareTo((Date)rightValue)
               : ((Date)rightValue).compareTo((Date)leftValue);
      }
      if (leftValue instanceof Number) {
         BigDecimal leftDecimal = Utils.toBigDecimal(leftValue);
         BigDecimal rightDecimal = Utils.toBigDecimal(rightValue);
         return this.ascending
               ? leftDecimal.compareTo(rightDecimal)
               : rightDecimal.compareTo(leftDecimal);
      }
      return 0;
   }
}
