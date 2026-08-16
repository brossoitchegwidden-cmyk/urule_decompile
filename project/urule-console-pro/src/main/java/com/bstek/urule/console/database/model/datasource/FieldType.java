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

   public Object parseValue(String var1) {
      if (var1 == null) {
         return null;
      } else {
         switch (this) {
            case BigDecimal:
               return Tools.toBigDecimal(var1);
            case Boolean:
               if (var1.contentEquals("1")) {
                  return true;
               } else {
                  if (var1.contentEquals("0")) {
                     return false;
                  }

                  return java.lang.Boolean.valueOf(var1);
               }
            case Date:
               return Tools.toDateOrDatatime(var1);
            case Double:
               BigDecimal var5 = Tools.toBigDecimal(var1);
               return var5.doubleValue();
            case Long:
               return Tools.toBigDecimal(var1).longValue();
            case Float:
               BigDecimal var4 = Tools.toBigDecimal(var1);
               return var4.floatValue();
            case Integer:
               BigDecimal var3 = Tools.toBigDecimal(var1);
               return var3.intValue();
            case Short:
               BigDecimal var2 = Tools.toBigDecimal(var1);
               return var2.shortValue();
            case String:
               return var1;
            case Other:
               return var1;
            default:
               return var1;
         }
      }
   }
}
