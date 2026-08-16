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
   private static Pattern a = Pattern.compile("-?[0-9]+\\.?[0-9]*");
   private static final List b = new ArrayList();
   private static final List c = new ArrayList();

   public static boolean isNumber(Object var0) {
      if (var0 == null) {
         return false;
      } else if (var0 instanceof String) {
         String var1 = (String)var0;
         return a.matcher(var1).matches();
      } else {
         return var0 instanceof Number;
      }
   }

   public static BigDecimal toBigDecimal(Object var0) {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof BigDecimal) {
         return (BigDecimal)var0;
      } else if (var0 instanceof String) {
         try {
            String var3 = var0.toString().trim();
            return new BigDecimal(var3);
         } catch (Exception var2) {
            throw new RuleException("Can not convert " + var0 + " to BigDecimal.");
         }
      } else if (var0 instanceof Number) {
         Number var1 = (Number)var0;
         if (var1 instanceof Integer) {
            return new BigDecimal(Integer.toString(var1.intValue()));
         } else if (var1 instanceof Float) {
            return new BigDecimal(Float.toString(var1.floatValue()));
         } else if (var1 instanceof Double) {
            return new BigDecimal(Double.toString(var1.doubleValue()));
         } else if (var1 instanceof Long) {
            return new BigDecimal(Long.toString(var1.longValue()));
         } else {
            return var1 instanceof Byte ? new BigDecimal(Byte.toString(var1.byteValue())) : new BigDecimal(var1.toString());
         }
      } else {
         throw new RuleException("Can not convert " + var0 + " to BigDecimal.");
      }
   }

   public static Date toDateOrDatatime(String var0) {
      try {
         return toDate(var0);
      } catch (Exception var2) {
         return toDatetime(var0);
      }
   }

   public static Date toDate(String var0) {
      for(String var2 : (Iterable<String>)(Iterable<?>)(b)) {
         try {
            SimpleDateFormat var3 = new SimpleDateFormat(var2);
            return var3.parse(var0);
         } catch (Exception var4) {
         }
      }

      throw new RuleException("Can not convert [" + var0 + "] to date");
   }

   public static Date toDatetime(String var0) {
      for(String var2 : (Iterable<String>)(Iterable<?>)(c)) {
         try {
            SimpleDateFormat var3 = new SimpleDateFormat(var2);
            return var3.parse(var0);
         } catch (Exception var4) {
         }
      }

      throw new RuleException("Can not convert [" + var0 + "] to date time");
   }

   public static List toList(String var0) {
      String[] var1 = var0.split(",");
      ArrayList var2 = new ArrayList();

      for(String var6 : var1) {
         var2.add(var6);
      }

      return var2;
   }

   public static Object getProperty(Object var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         try {
            if (var0 instanceof Map && var1.indexOf(".") == -1) {
               Map var2 = (Map)var0;
               return var2.get(var1);
            } else {
               return PropertyUtils.getProperty(var0, var1);
            }
         } catch (Exception var3) {
            throw new RuleException(var3);
         }
      }
   }

   public static int processSpan(int var0) {
      if (var0 == 1) {
         var0 = 0;
      } else if (var0 > 1) {
         --var0;
      }

      return var0;
   }

   public static Date toDate(String var0, String var1) {
      if (StringUtils.isEmpty(var0)) {
         return null;
      } else if (StringUtils.isEmpty(var1)) {
         throw new RuleException("日期格式的格式不能为空！");
      } else {
         SimpleDateFormat var2 = new SimpleDateFormat(var1);

         try {
            return var2.parse(var0.toString());
         } catch (ParseException var4) {
            throw new RuleException(var4);
         }
      }
   }

   public static String formatDate(Object var0, String var1) {
      if (StringUtils.isEmpty(var0)) {
         return "";
      } else if (StringUtils.isEmpty(var1)) {
         throw new RuleException("日期格式的格式不能为空！");
      } else {
         Date var2 = (Date)var0;
         SimpleDateFormat var3 = new SimpleDateFormat(var1);
         return var3.format(var2);
      }
   }

   public static String formatNumber(Object var0, String var1) {
      if (StringUtils.isEmpty(var0)) {
         return "";
      } else if (StringUtils.isEmpty(var1)) {
         throw new RuleException("数字格式化的格式不能为空！");
      } else {
         DecimalFormat var2 = new DecimalFormat(var1);
         return var2.format(toBigDecimal(var0).doubleValue());
      }
   }

   static {
      b.add("yyyy-MM-dd");
      b.add("yyyy/MM/dd");
      b.add("yyyy.MM.dd");
      b.add("yyyy年MM月dd日");
      c.add("yyyy-MM-dd HH:mm:ss");
      c.add("yyyy/MM/dd HH:mm:ss");
      c.add("yyyy.MM.dd HH:mm:ss");
      c.add("yyyy年MM月dd日 HH:mm:ss");
   }
}
