package com.bstek.urule.console.util;

import com.bstek.urule.exception.RuleException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.PropertyUtils;

public class Tools {
   private static Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
   private static final List dateFormats = new ArrayList();
   private static final List dateTimeFormats = new ArrayList();

   public static boolean isNumber(Object obj) {
      if (obj == null) {
         return false;
      } else if (obj instanceof String) {
         String obj2 = (String)obj;
         return Tools.pattern.matcher(obj2).matches();
      } else {
         return obj instanceof Number;
      }
   }

   public static BigDecimal toBigDecimal(Object obj) {
      if (obj == null) {
         return null;
      } else if (obj instanceof BigDecimal) {
         return (BigDecimal)obj;
      } else if (obj instanceof String) {
         try {
            String trimmedText = obj.toString().trim();
            return new BigDecimal(trimmedText);
         } catch (Exception exception) {
            throw new RuleException("Can not convert " + obj + " to BigDecimal.");
         }
      } else if (obj instanceof Number) {
         Number number = (Number)obj;
         if (number instanceof Integer) {
            return new BigDecimal(Integer.toString(number.intValue()));
         } else if (number instanceof Float) {
            return new BigDecimal(Float.toString(number.floatValue()));
         } else if (number instanceof Double) {
            return new BigDecimal(Double.toString(number.doubleValue()));
         } else if (number instanceof Long) {
            return new BigDecimal(Long.toString(number.longValue()));
         } else {
            return number instanceof Byte ? new BigDecimal(Byte.toString(number.byteValue())) : new BigDecimal(number.toString());
         }
      } else {
         throw new RuleException("Can not convert " + obj + " to BigDecimal.");
      }
   }

   public static Date toDateOrDatatime(String value) {
      try {
         return toDate(value);
      } catch (Exception exception) {
         return toDatetime(value);
      }
   }

   public static Date toDate(String value) {
      for(String text : (Iterable<String>)(Iterable<?>)(Tools.dateFormats)) {
         try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(text);
            return simpleDateFormat.parse(value);
         } catch (Exception exception) {
         }
      }

      throw new RuleException("Can not convert [" + value + "] to date");
   }

   public static Date toDatetime(String value) {
      for(String text : (Iterable<String>)(Iterable<?>)(Tools.dateTimeFormats)) {
         try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(text);
            return simpleDateFormat.parse(value);
         } catch (Exception exception) {
         }
      }

      throw new RuleException("Can not convert [" + value + "] to date time");
   }

   public static List toList(String value) {
      String[] parts = value.split(",");
      ArrayList toListResult = new ArrayList();

      for(String text : parts) {
         toListResult.add(text);
      }

      return toListResult;
   }

   public static Object getProperty(Object obj, String property) {
      if (obj == null) {
         return null;
      } else {
         try {
            if (obj instanceof Map && property.indexOf(".") == -1) {
               Map obj2 = (Map)obj;
               return obj2.get(property);
            } else {
               return PropertyUtils.getProperty(obj, property);
            }
         } catch (Exception exception) {
            throw new RuleException(exception);
         }
      }
   }

   public static int processSpan(int span) {
      if (span == 1) {
         span = 0;
      } else if (span > 1) {
         --span;
      }

      return span;
   }

   public static Date toDate(String value, String pattern) {
      if (StringUtils.isEmpty(value)) {
         return null;
      } else if (StringUtils.isEmpty(pattern)) {
         throw new RuleException("日期格式的格式不能为空！");
      } else {
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

         try {
            return simpleDateFormat.parse(value.toString());
         } catch (ParseException parseException) {
            throw new RuleException(parseException);
         }
      }
   }

   public static String formatDate(Object value, String pattern) {
      if (StringUtils.isEmpty(value)) {
         return "";
      } else if (StringUtils.isEmpty(pattern)) {
         throw new RuleException("日期格式的格式不能为空！");
      } else {
         Date dateValue = (Date)value;
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
         return simpleDateFormat.format(dateValue);
      }
   }

   public static String formatNumber(Object value, String pattern) {
      if (StringUtils.isEmpty(value)) {
         return "";
      } else if (StringUtils.isEmpty(pattern)) {
         throw new RuleException("数字格式化的格式不能为空！");
      } else {
         DecimalFormat decimalFormat = new DecimalFormat(pattern);
         return decimalFormat.format(toBigDecimal(value).doubleValue());
      }
   }

   static {
      Tools.dateFormats.add("yyyy-MM-dd");
      Tools.dateFormats.add("yyyy/MM/dd");
      Tools.dateFormats.add("yyyy.MM.dd");
      Tools.dateFormats.add("yyyy年MM月dd日");
      Tools.dateTimeFormats.add("yyyy-MM-dd HH:mm:ss");
      Tools.dateTimeFormats.add("yyyy/MM/dd HH:mm:ss");
      Tools.dateTimeFormats.add("yyyy.MM.dd HH:mm:ss");
      Tools.dateTimeFormats.add("yyyy年MM月dd日 HH:mm:ss");
   }
}
