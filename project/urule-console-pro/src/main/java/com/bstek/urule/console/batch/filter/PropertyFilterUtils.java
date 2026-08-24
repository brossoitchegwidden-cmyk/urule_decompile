package com.bstek.urule.console.batch.filter;

import com.bstek.urule.console.util.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PropertyFilterUtils {
   /**值匹配判断*/
   public static boolean valueMatchFilters(PropertyFilter filter, Object value) {
      if ("and".equals(filter.getAndorType())) {
         boolean valueMatchFiltersResult = true;

         for(PropertyFilterValue propertyFilterValue : (Iterable<PropertyFilterValue>)(Iterable<?>)(filter.getValues())) {
            if ((!StringUtils.isEmpty(propertyFilterValue.getValue()) || "null".equals(propertyFilterValue.getOp()) || "notNull".equals(propertyFilterValue.getOp())) && !evaluateCondition(propertyFilterValue, value)) {
               valueMatchFiltersResult = false;
               break;
            }
         }

         return valueMatchFiltersResult;
      } else if (!"or".equals(filter.getAndorType())) {
         return false;
      } else {
         boolean flag = false;
         boolean flag2 = false;

         for(PropertyFilterValue propertyFilterValue2 : (Iterable<PropertyFilterValue>)(Iterable<?>)(filter.getValues())) {
            if (!StringUtils.isEmpty(propertyFilterValue2.getValue()) || "null".equals(propertyFilterValue2.getOp()) || "notNull".equals(propertyFilterValue2.getOp())) {
               if (evaluateCondition(propertyFilterValue2, value)) {
                  flag = true;
                  break;
               }

               flag2 = true;
            }
         }

         return flag2 ? flag : true;
      }
   }

   private static boolean evaluateCondition(PropertyFilterValue propertyFilterValue, Object objectValue) {
      return evaluateConditionInternal(propertyFilterValue, objectValue);
   }

   private static boolean evaluateConditionInternal(PropertyFilterValue propertyFilterValue, Object objectValue) {
      String op = propertyFilterValue.getOp();
      String text = propertyFilterValue.getValue();
      boolean flag = false;
      String text2 = null;
      if (StringUtils.isBlank(objectValue)) {
         text2 = "";
      } else {
         text2 = objectValue.toString();
      }

      if (objectValue instanceof Date) {
         long objectValue2 = 0L;
         if (propertyFilterValue.getObjectValue() == null) {
            if (text.length() == 10) {
               objectValue2 = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(0, 0, 0).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
            } else {
               objectValue2 = LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
            }

            propertyFilterValue.setObjectValue(objectValue2);
         } else {
            objectValue2 = (Long)propertyFilterValue.getObjectValue();
         }

         Date objectValue3 = (Date)objectValue;
         long time = objectValue3.getTime();
         if ("greaterThen".equals(op)) {
            flag = time > objectValue2;
         } else if ("lessThen".equals(op)) {
            flag = time < objectValue2;
         } else if ("greaterThenEquals".equals(op)) {
            flag = time >= objectValue2;
         } else if ("lessThenEquals".equals(op)) {
            flag = time <= objectValue2;
         } else if ("null".equals(op)) {
            flag = StringUtils.isBlank(text2);
         } else if ("notNull".equals(op)) {
            flag = StringUtils.isNotBlank(text2);
         }
      } else if ("equals".equals(op)) {
         flag = text.equals(text2);
      } else if ("equalsIgnoreCase".equals(op)) {
         flag = text.toLowerCase().equals(text2.toLowerCase());
      } else if ("notEquals".equals(op)) {
         flag = !text.equals(text2);
      } else if ("notEqualsIgnoreCase".equals(op)) {
         flag = !text.toLowerCase().equals(text2.toLowerCase());
      } else if ("greaterThen".equals(op)) {
         flag = Float.parseFloat(text2) > Float.parseFloat(text);
      } else if ("lessThen".equals(op)) {
         flag = Float.parseFloat(text2) < Float.parseFloat(text);
      } else if ("greaterThenEquals".equals(op)) {
         flag = Float.parseFloat(text2) >= Float.parseFloat(text);
      } else if ("lessThenEquals".equals(op)) {
         flag = Float.parseFloat(text2) <= Float.parseFloat(text);
      } else if ("contain".equals(op)) {
         flag = text2.indexOf(text) > -1;
      } else if ("containIgnoreCase".equals(op)) {
         flag = text2.toLowerCase().indexOf(text.toLowerCase()) > -1;
      } else if ("notContain".equals(op)) {
         flag = text2.indexOf(text) == -1;
      } else if ("notContainIgnoreCase".equals(op)) {
         flag = text2.toLowerCase().indexOf(text.toLowerCase()) == -1;
      } else if ("startsWidth".equals(op)) {
         flag = text2.indexOf(text) == 0;
      } else if ("notStartsWidth=".equals(op)) {
         flag = text2.indexOf(text) > 0;
      } else if ("endWith".equals(op)) {
         flag = text2.indexOf(text) == text2.toString().length() - 1;
      } else if ("notEndWith".equals(op)) {
         flag = text2.indexOf(text) < text2.toString().length() - 1;
      } else if ("null".equals(op)) {
         flag = StringUtils.isBlank(text2);
      } else if ("notNull".equals(op)) {
         flag = StringUtils.isNotBlank(text2);
      }

      return flag;
   }
}
