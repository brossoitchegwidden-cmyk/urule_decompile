package com.bstek.urule.model.library;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public enum Datatype {
   String,
   Integer,
   Char,
   Double,
   Long,
   Float,
   BigDecimal,
   Boolean,
   Date,
   List,
   Set,
   Map,
   Enum,
   Object;

   private static Set<String> set = new HashSet<>();

   public static final Datatype parse(String var0) {
      if (var0.equals(String.toString())) {
         return String;
      } else if (var0.equals(Integer.toString())) {
         return Integer;
      } else if (var0.equals(Char.toString())) {
         return Char;
      } else if (var0.equals(Double.toString())) {
         return Double;
      } else if (var0.equals(Long.toString())) {
         return Long;
      } else if (var0.equals(Float.toString())) {
         return Float;
      } else if (var0.equals(BigDecimal.toString())) {
         return BigDecimal;
      } else if (var0.equals(Boolean.toString())) {
         return Boolean;
      } else if (var0.equals(Date.toString())) {
         return Date;
      } else if (var0.equals(List.toString())) {
         return List;
      } else if (var0.equals(Set.toString())) {
         return Set;
      } else if (var0.equals(Map.toString())) {
         return Map;
      } else {
         return var0.equals(Enum.toString()) ? Enum : Object;
      }
   }

   public static boolean isType(String var0) {
      return set.contains(var0);
   }

   public String convertObjectToString(Object var1) {
      if (var1 == null) {
         return "";
      }

      if (var1 instanceof String) {
         return var1.toString();
      }

      switch (this) {
         case Object:
            return var1.toString();
         case Date:
            SimpleDateFormat var2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return var2.format((Date)var1);
         case List:
            List var3 = (List)var1;
            String var4 = "";

            for (int var10 = 0; var10 < var3.size(); var10++) {
               Object var11 = var3.get(var10);
               if (var10 > 0) {
                  var4 = var4 + ",";
               }

               var4 = var4 + var11;
            }

            return (String)var4;
         case Set:
            Set var5 = (Set)var1;
            String var6 = "";
            int var7 = 0;

            for (Object var13 : var5) {
               if (var7 > 0) {
                  var6 = var6 + ",";
               }

               var6 = var6 + var13;
               var7++;
            }

            return (String)var6;
         case BigDecimal:
            BigDecimal var8 = Utils.toBigDecimal(var1);
            return var8.floatValue() + "";
         case Double:
            Double var9 = Utils.toBigDecimal(var1).doubleValue();
            return var9.floatValue() + "";
         default:
            return var1.toString();
      }
   }

   public Object convert(Object var1) {
      switch (this) {
         case Object:
            return var1;
         case Date:
            if (var1 == null) {
               return null;
            } else {
               try {
                  if (var1 instanceof Date) {
                     return (Date)var1;
                  } else if (var1.toString().equals("")) {
                     return null;
                  } else {
                     try {
                        SimpleDateFormat var21 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        return var21.parse(var1.toString());
                     } catch (Exception var16) {
                        SimpleDateFormat var23 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                        try {
                           return var23.parse(var1.toString());
                        } catch (Exception var12) {
                           var23 = new SimpleDateFormat("yyyy-MM-dd");
                           return var23.parse(var1.toString());
                        }
                     }
                  }
               } catch (ParseException var17) {
                  throw new RuleException(var17);
               }
            }
         case List:
            if (var1 == null) {
               return null;
            } else if (var1 instanceof List) {
               return (List)var1;
            } else if (var1 instanceof Collection) {
               Collection var20 = (Collection)var1;
               return new ArrayList(var20);
            } else {
               String var19 = var1.toString();
               if (var19.startsWith("[") && var19.endsWith("]")) {
                  ObjectMapper var22 = JsonMapper.builder().build();

                  try {
                     return var22.readValue(var1.toString(), ArrayList.class);
                  } catch (Exception var15) {
                     throw new RuleException(var15);
                  }
               }

               ArrayList var4 = new ArrayList();
               String[] var5 = var19.split(",");

               for (String var31 : var5) {
                  var4.add(var31);
               }

               return var4;
            }
         case Set:
            if (var1 == null) {
               return null;
            } else if (var1 instanceof Set) {
               return (Set)var1;
            } else if (var1 instanceof Collection) {
               Collection var25 = (Collection)var1;
               return new HashSet(var25);
            } else {
               String var6 = var1.toString();
               if (var6.startsWith("[") && var6.endsWith("]")) {
                  ObjectMapper var27 = JsonMapper.builder().build();

                  try {
                     return var27.readValue(var1.toString(), HashSet.class);
                  } catch (Exception var14) {
                     throw new RuleException(var14);
                  }
               }

               TreeSet var7 = new TreeSet();

               for (String var11 : var6.split(",")) {
                  var7.add(var11);
               }

               return var7;
            }
         case BigDecimal:
            if (var1 == null) {
               var1 = "0";
            }

            return Utils.toBigDecimal(var1);
         case Double:
            if (var1 == null) {
               var1 = "0";
            }

            return Utils.toBigDecimal(var1).doubleValue();
         case String:
            if (var1 == null) {
               return var1;
            }

            if (var1 instanceof Number) {
               BigDecimal var18 = Utils.toBigDecimal(var1);
               var1 = var18.toPlainString();
            }

            return var1.toString();
         case Integer:
            if (var1 == null || var1.toString().equals("")) {
               var1 = "0";
            }

            return Utils.toBigDecimal(var1).intValue();
         case Char:
            if (var1 == null) {
               return '\u0000';
            } else if (var1 instanceof Character) {
               return (Character)var1;
            } else {
               String var2 = var1.toString();
               if (var2.length() == 1) {
                  return var2.toCharArray()[0];
               }

               int var3 = Utils.toBigDecimal(var1).intValue();
               return (char)var3;
            }
         case Long:
            if (var1 == null) {
               var1 = "0";
            }

            return Utils.toBigDecimal(var1).longValue();
         case Float:
            if (var1 == null) {
               var1 = "0";
            }

            return Utils.toBigDecimal(var1).floatValue();
         case Boolean:
            if (var1 == null) {
               var1 = "false";
            }

            return java.lang.Boolean.valueOf(var1.toString());
         case Map:
            if (var1 == null) {
               return null;
            } else if (var1 instanceof Map) {
               return (Map)var1;
            } else {
               ObjectMapper var8 = JsonMapper.builder().build();

               try {
                  return (Map)var8.readValue(var1.toString(), new HashMapTypeReference());
               } catch (Exception var13) {
                  throw new RuleException(var13);
               }
            }
         case Enum:
            return var1;
         default:
            return null;
      }
   }

   static {
      Datatype[] var0 = values();

      for (Datatype var4 : var0) {
         set.add(var4.name());
      }
   }
}
