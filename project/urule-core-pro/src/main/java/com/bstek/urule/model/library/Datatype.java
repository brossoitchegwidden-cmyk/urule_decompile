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

   private static final Set<String> SUPPORTED_TYPE_NAMES = new HashSet<>();

   public static final Datatype parse(String type) {
      if (type.equals(String.toString())) {
         return String;
      } else if (type.equals(Integer.toString())) {
         return Integer;
      } else if (type.equals(Char.toString())) {
         return Char;
      } else if (type.equals(Double.toString())) {
         return Double;
      } else if (type.equals(Long.toString())) {
         return Long;
      } else if (type.equals(Float.toString())) {
         return Float;
      } else if (type.equals(BigDecimal.toString())) {
         return BigDecimal;
      } else if (type.equals(Boolean.toString())) {
         return Boolean;
      } else if (type.equals(Date.toString())) {
         return Date;
      } else if (type.equals(List.toString())) {
         return List;
      } else if (type.equals(Set.toString())) {
         return Set;
      } else if (type.equals(Map.toString())) {
         return Map;
      } else {
         return type.equals(Enum.toString()) ? Enum : Object;
      }
   }

   public static boolean isType(String type) {
      return SUPPORTED_TYPE_NAMES.contains(type);
   }

   public String convertObjectToString(Object value) {
      if (value == null) {
         return "";
      }

      if (value instanceof String) {
         return value.toString();
      }

      switch (this) {
         case Object:
            return value.toString();
         case Date:
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format((Date)value);
         case List:
            List items = (List)value;
            String text = "";

            for (int index = 0; index < items.size(); index++) {
               Object objectValue = items.get(index);
               if (index > 0) {
                  text = text + ",";
               }

               text = text + objectValue;
            }

            return (String)text;
         case Set:
            Set uniqueItems = (Set)value;
            String text2 = "";
            int number = 0;

            for (Object objectValue2 : uniqueItems) {
               if (number > 0) {
                  text2 = text2 + ",";
               }

               text2 = text2 + objectValue2;
               number++;
            }

            return (String)text2;
         case BigDecimal:
            BigDecimal decimalValue = Utils.toBigDecimal(value);
            return decimalValue.floatValue() + "";
         case Double:
            Double doubleValue = Utils.toBigDecimal(value).doubleValue();
            return doubleValue.floatValue() + "";
         default:
            return value.toString();
      }
   }

   public Object convert(Object value) {
      switch (this) {
         case Object:
            return value;
         case Date:
            if (value == null) {
               return null;
            } else {
               try {
                  if (value instanceof Date) {
                     return (Date)value;
                  } else if (value.toString().equals("")) {
                     return null;
                  } else {
                     try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        return simpleDateFormat.parse(value.toString());
                     } catch (Exception exception) {
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                        try {
                           return simpleDateFormat2.parse(value.toString());
                        } catch (Exception exception2) {
                           simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                           return simpleDateFormat2.parse(value.toString());
                        }
                     }
                  }
               } catch (ParseException parseException) {
                  throw new RuleException(parseException);
               }
            }
         case List:
            if (value == null) {
               return null;
            } else if (value instanceof List) {
               return (List)value;
            } else if (value instanceof Collection) {
               Collection items = (Collection)value;
               return new ArrayList(items);
            } else {
               String text = value.toString();
               if (text.startsWith("[") && text.endsWith("]")) {
                  ObjectMapper objectMapper = JsonMapper.builder().build();

                  try {
                     return objectMapper.readValue(value.toString(), ArrayList.class);
                  } catch (Exception exception3) {
                     throw new RuleException(exception3);
                  }
               }

               ArrayList convertResult = new ArrayList();
               String[] parts = text.split(",");

               for (String text2 : parts) {
                  convertResult.add(text2);
               }

               return convertResult;
            }
         case Set:
            if (value == null) {
               return null;
            } else if (value instanceof Set) {
               return (Set)value;
            } else if (value instanceof Collection) {
               Collection items2 = (Collection)value;
               return new HashSet(items2);
            } else {
               String text3 = value.toString();
               if (text3.startsWith("[") && text3.endsWith("]")) {
                  ObjectMapper objectMapper2 = JsonMapper.builder().build();

                  try {
                     return objectMapper2.readValue(value.toString(), HashSet.class);
                  } catch (Exception exception4) {
                     throw new RuleException(exception4);
                  }
               }

               TreeSet treeSet = new TreeSet();

               for (String text4 : text3.split(",")) {
                  treeSet.add(text4);
               }

               return treeSet;
            }
         case BigDecimal:
            if (value == null) {
               value = "0";
            }

            return Utils.toBigDecimal(value);
         case Double:
            if (value == null) {
               value = "0";
            }

            return Utils.toBigDecimal(value).doubleValue();
         case String:
            if (value == null) {
               return value;
            }

            if (value instanceof Number) {
               BigDecimal decimalValue = Utils.toBigDecimal(value);
               value = decimalValue.toPlainString();
            }

            return value.toString();
         case Integer:
            if (value == null || value.toString().equals("")) {
               value = "0";
            }

            return Utils.toBigDecimal(value).intValue();
         case Char:
            if (value == null) {
               return '\u0000';
            } else if (value instanceof Character) {
               return (Character)value;
            } else {
               String text5 = value.toString();
               if (text5.length() == 1) {
                  return text5.toCharArray()[0];
               }

               int number = Utils.toBigDecimal(value).intValue();
               return (char)number;
            }
         case Long:
            if (value == null) {
               value = "0";
            }

            return Utils.toBigDecimal(value).longValue();
         case Float:
            if (value == null) {
               value = "0";
            }

            return Utils.toBigDecimal(value).floatValue();
         case Boolean:
            if (value == null) {
               value = "false";
            }

            return java.lang.Boolean.valueOf(value.toString());
         case Map:
            if (value == null) {
               return null;
            } else if (value instanceof Map) {
               return (Map)value;
            } else {
               ObjectMapper objectMapper3 = JsonMapper.builder().build();

               try {
                  return (Map)objectMapper3.readValue(value.toString(), new HashMapTypeReference());
               } catch (Exception exception5) {
                  throw new RuleException(exception5);
               }
            }
         case Enum:
            return value;
         default:
            return null;
      }
   }

   static {
      Datatype[] datatypes = values();

      for (Datatype datatype : datatypes) {
         SUPPORTED_TYPE_NAMES.add(datatype.name());
      }
   }
}
