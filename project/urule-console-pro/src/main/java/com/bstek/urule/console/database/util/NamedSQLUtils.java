package com.bstek.urule.console.database.util;

import com.bstek.urule.exception.RuleException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NamedSQLUtils {
   private static final char[] PARAMETER_SEPARATORS = new char[]{'"', '\'', ':', '&', ',', ';', '(', ')', '|', '=', '+', '-', '*', '%', '/', '\\', '<', '>', '^'};
   private static final String[] COMMENT_START_SEQUENCES = new String[]{"'", "\"", "--", "/*"};
   private static final String[] COMMENT_END_SEQUENCES = new String[]{"'", "\"", "\n", "*/"};

   public static ParsedSql parseSql(String sql) {
      HashSet uniqueItems = new HashSet();
      String sql2 = sql;
      ArrayList items = new ArrayList();
      char[] values = sql.toCharArray();
      int number = 0;
      int number2 = 0;
      int number3 = 0;
      int number4 = 0;
      int number5 = 0;

      while(true) {
         if (number5 < values.length) {
            while(number5 < values.length) {
               int number6 = skipCommentsAndQuotedText(values, number5);
               if (number5 == number6) {
                  break;
               }

               number5 = number6;
            }

            if (number5 < values.length) {
               char text = values[number5];
               if (text != ':' && text != '&') {
                  if (text == '\\') {
                     int number7 = number5 + 1;
                     if (number7 < values.length && values[number7] == ':') {
                        sql2 = sql2.substring(0, number5 - number4) + sql2.substring(number5 - number4 + 1);
                        ++number4;
                        number5 += 2;
                        continue;
                     }
                  }

                  if (text == '?') {
                     int number8 = number5 + 1;
                     if (number8 < values.length && (values[number8] == '?' || values[number8] == '|' || values[number8] == '&')) {
                        number5 += 2;
                        continue;
                     }

                     ++number2;
                     ++number3;
                  }
               } else {
                  int number9 = number5 + 1;
                  if (number9 < values.length && values[number9] == ':' && text == ':') {
                     number5 += 2;
                     continue;
                  }

                  Object objectValue = null;
                  if (number9 < values.length && text == ':' && values[number9] == '{') {
                     while(true) {
                        if (number9 >= values.length || '}' == values[number9]) {
                           if (number9 >= values.length) {
                              throw new RuleException("Non-terminated named parameter declaration at position " + number5 + " in statement: " + sql);
                           }

                           if (number9 - number5 > 3) {
                              String substring = sql.substring(number5 + 2, number9);
                              number = countUniqueParameter(uniqueItems, number, substring);
                              number3 = addNamedParameter(items, number3, number4, number5, number9 + 1, substring);
                           }

                           ++number9;
                           break;
                        }

                        ++number9;
                        if (':' == values[number9] || '{' == values[number9]) {
                           throw new RuleException("Parameter name contains invalid character '" + values[number9] + "' at position " + number5 + " in statement: " + sql);
                        }
                     }
                  } else {
                     while(number9 < values.length && !isParameterSeparator(values[number9])) {
                        ++number9;
                     }

                     if (number9 - number5 > 1) {
                        String substring2 = sql.substring(number5 + 1, number9);
                        number = countUniqueParameter(uniqueItems, number, substring2);
                        number3 = addNamedParameter(items, number3, number4, number5, number9, substring2);
                     }
                  }

                  number5 = number9 - 1;
               }

               ++number5;
               continue;
            }
         }

         ParsedSql parsedSql = new ParsedSql(sql2);

         for(ParameterHolder parameterHolder : (Iterable<ParameterHolder>)(Iterable<?>)(items)) {
            parsedSql.addNamedParameter(parameterHolder.getParameterName(), parameterHolder.getStartIndex(), parameterHolder.getEndIndex());
         }

         parsedSql.setNamedParameterCount(number);
         parsedSql.setUnnamedParameterCount(number2);
         parsedSql.setTotalParameterCount(number3);
         return parsedSql;
      }
   }

   private static int addNamedParameter(List items, int number, int number2, int number3, int number4, String text) {
      items.add(new ParameterHolder(text, number3 - number2, number4 - number2));
      ++number;
      return number;
   }

   private static int countUniqueParameter(Set uniqueItems, int number, String text) {
      if (!uniqueItems.contains(text)) {
         uniqueItems.add(text);
         ++number;
      }

      return number;
   }

   private static boolean isParameterSeparator(char text) {
      if (Character.isWhitespace(text)) {
         return true;
      } else {
         for(char text2 : NamedSQLUtils.PARAMETER_SEPARATORS) {
            if (text == text2) {
               return true;
            }
         }

         return false;
      }
   }

   private static int skipCommentsAndQuotedText(char[] values, int number) {
      for(int index = 0; index < NamedSQLUtils.COMMENT_START_SEQUENCES.length; ++index) {
         if (values[number] == NamedSQLUtils.COMMENT_START_SEQUENCES[index].charAt(0)) {
            boolean flag = true;

            for(int index2 = 1; index2 < NamedSQLUtils.COMMENT_START_SEQUENCES[index].length(); ++index2) {
               if (values[number + index2] != NamedSQLUtils.COMMENT_START_SEQUENCES[index].charAt(index2)) {
                  flag = false;
                  break;
               }
            }

            if (flag) {
               int number2 = NamedSQLUtils.COMMENT_START_SEQUENCES[index].length();

               for(int index3 = number + number2; index3 < values.length; ++index3) {
                  if (values[index3] == NamedSQLUtils.COMMENT_END_SEQUENCES[index].charAt(0)) {
                     boolean flag2 = true;
                     int index32 = index3;

                     for(int index4 = 1; index4 < NamedSQLUtils.COMMENT_END_SEQUENCES[index].length(); ++index4) {
                        if (index3 + index4 >= values.length) {
                           return values.length;
                        }

                        if (values[index3 + index4] != NamedSQLUtils.COMMENT_END_SEQUENCES[index].charAt(index4)) {
                           flag2 = false;
                           break;
                        }

                        index32 = index3 + index4;
                     }

                     if (flag2) {
                        return index32 + 1;
                     }
                  }
               }

               return values.length;
            }
         }
      }

      return number;
   }

   private static class ParameterHolder {
      private final String parameterName;
      private final int startIndex;
      private final int endIndex;

      public ParameterHolder(String text, int number, int number2) {
         this.parameterName = text;
         this.startIndex = number;
         this.endIndex = number2;
      }

      public String getParameterName() {
         return this.parameterName;
      }

      public int getStartIndex() {
         return this.startIndex;
      }

      public int getEndIndex() {
         return this.endIndex;
      }
   }
}
