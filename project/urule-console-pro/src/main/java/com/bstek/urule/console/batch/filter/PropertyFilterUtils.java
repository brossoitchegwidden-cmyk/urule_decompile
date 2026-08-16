package com.bstek.urule.console.batch.filter;

import com.bstek.urule.console.util.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PropertyFilterUtils {
   public static boolean valueMatchFilters(PropertyFilter var0, Object var1) {
      if ("and".equals(var0.getAndorType())) {
         boolean var6 = true;

         for(PropertyFilterValue var8 : (Iterable<PropertyFilterValue>)(Iterable<?>)(var0.getValues())) {
            if ((!StringUtils.isEmpty(var8.getValue()) || "null".equals(var8.getOp()) || "notNull".equals(var8.getOp())) && !a(var8, var1)) {
               var6 = false;
               break;
            }
         }

         return var6;
      } else if (!"or".equals(var0.getAndorType())) {
         return false;
      } else {
         boolean var2 = false;
         boolean var3 = false;

         for(PropertyFilterValue var5 : (Iterable<PropertyFilterValue>)(Iterable<?>)(var0.getValues())) {
            if (!StringUtils.isEmpty(var5.getValue()) || "null".equals(var5.getOp()) || "notNull".equals(var5.getOp())) {
               if (a(var5, var1)) {
                  var2 = true;
                  break;
               }

               var3 = true;
            }
         }

         return var3 ? var2 : true;
      }
   }

   private static boolean a(PropertyFilterValue var0, Object var1) {
      return b(var0, var1);
   }

   private static boolean b(PropertyFilterValue var0, Object var1) {
      String var2 = var0.getOp();
      String var3 = var0.getValue();
      boolean var4 = false;
      String var5 = null;
      if (StringUtils.isBlank(var1)) {
         var5 = "";
      } else {
         var5 = var1.toString();
      }

      if (var1 instanceof Date) {
         long var6 = 0L;
         if (var0.getObjectValue() == null) {
            if (var3.length() == 10) {
               var6 = LocalDate.parse(var3, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(0, 0, 0).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
            } else {
               var6 = LocalDateTime.parse(var3, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
            }

            var0.setObjectValue(var6);
         } else {
            var6 = (Long)var0.getObjectValue();
         }

         Date var8 = (Date)var1;
         long var9 = var8.getTime();
         if ("greaterThen".equals(var2)) {
            var4 = var9 > var6;
         } else if ("lessThen".equals(var2)) {
            var4 = var9 < var6;
         } else if ("greaterThenEquals".equals(var2)) {
            var4 = var9 >= var6;
         } else if ("lessThenEquals".equals(var2)) {
            var4 = var9 <= var6;
         } else if ("null".equals(var2)) {
            var4 = StringUtils.isBlank(var5);
         } else if ("notNull".equals(var2)) {
            var4 = StringUtils.isNotBlank(var5);
         }
      } else if ("equals".equals(var2)) {
         var4 = var3.equals(var5);
      } else if ("equalsIgnoreCase".equals(var2)) {
         var4 = var3.toLowerCase().equals(var5.toLowerCase());
      } else if ("notEquals".equals(var2)) {
         var4 = !var3.equals(var5);
      } else if ("notEqualsIgnoreCase".equals(var2)) {
         var4 = !var3.toLowerCase().equals(var5.toLowerCase());
      } else if ("greaterThen".equals(var2)) {
         var4 = Float.parseFloat(var5) > Float.parseFloat(var3);
      } else if ("lessThen".equals(var2)) {
         var4 = Float.parseFloat(var5) < Float.parseFloat(var3);
      } else if ("greaterThenEquals".equals(var2)) {
         var4 = Float.parseFloat(var5) >= Float.parseFloat(var3);
      } else if ("lessThenEquals".equals(var2)) {
         var4 = Float.parseFloat(var5) <= Float.parseFloat(var3);
      } else if ("contain".equals(var2)) {
         var4 = var5.indexOf(var3) > -1;
      } else if ("containIgnoreCase".equals(var2)) {
         var4 = var5.toLowerCase().indexOf(var3.toLowerCase()) > -1;
      } else if ("notContain".equals(var2)) {
         var4 = var5.indexOf(var3) == -1;
      } else if ("notContainIgnoreCase".equals(var2)) {
         var4 = var5.toLowerCase().indexOf(var3.toLowerCase()) == -1;
      } else if ("startsWidth".equals(var2)) {
         var4 = var5.indexOf(var3) == 0;
      } else if ("notStartsWidth=".equals(var2)) {
         var4 = var5.indexOf(var3) > 0;
      } else if ("endWith".equals(var2)) {
         var4 = var5.indexOf(var3) == var5.toString().length() - 1;
      } else if ("notEndWith".equals(var2)) {
         var4 = var5.indexOf(var3) < var5.toString().length() - 1;
      } else if ("null".equals(var2)) {
         var4 = StringUtils.isBlank(var5);
      } else if ("notNull".equals(var2)) {
         var4 = StringUtils.isNotBlank(var5);
      }

      return var4;
   }
}
