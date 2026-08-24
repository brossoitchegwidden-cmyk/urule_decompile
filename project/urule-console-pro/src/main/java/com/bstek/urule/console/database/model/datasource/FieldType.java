package com.bstek.urule.console.database.model.datasource;

import com.bstek.urule.console.util.Tools;
import java.math.BigDecimal;

public enum FieldType {
   String,
   Integer,
   Double,
   Long,
   Float,
   BigDecimal,
   Short,
   Boolean,
   Date,
   Other;

   public Object parseValue(String value) {
      if (value == null) {
         return null;
      } else {
         switch (this) {
            case BigDecimal:
               return Tools.toBigDecimal(value);
            case Boolean:
               if (value.contentEquals("1")) {
                  return true;
               } else {
                  if (value.contentEquals("0")) {
                     return false;
                  }

                  return java.lang.Boolean.valueOf(value);
               }
            case Date:
               return Tools.toDateOrDatatime(value);
            case Double:
               BigDecimal decimalValue = Tools.toBigDecimal(value);
               return decimalValue.doubleValue();
            case Long:
               return Tools.toBigDecimal(value).longValue();
            case Float:
               BigDecimal decimalValue2 = Tools.toBigDecimal(value);
               return decimalValue2.floatValue();
            case Integer:
               BigDecimal decimalValue3 = Tools.toBigDecimal(value);
               return decimalValue3.intValue();
            case Short:
               BigDecimal decimalValue4 = Tools.toBigDecimal(value);
               return decimalValue4.shortValue();
            case String:
               return value;
            case Other:
               return value;
            default:
               return value;
         }
      }
   }
}
