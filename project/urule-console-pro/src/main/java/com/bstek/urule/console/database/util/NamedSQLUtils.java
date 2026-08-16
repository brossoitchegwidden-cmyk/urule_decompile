package com.bstek.urule.console.database.util;

import com.bstek.urule.exception.RuleException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NamedSQLUtils {
   private static final char[] a = new char[]{'"', '\'', ':', '&', ',', ';', '(', ')', '|', '=', '+', '-', '*', '%', '/', '\\', '<', '>', '^'};
   private static final String[] b = new String[]{"'", "\"", "--", "/*"};
   private static final String[] c = new String[]{"'", "\"", "\n", "*/"};

   public static ParsedSql parseSql(String var0) {
      HashSet var1 = new HashSet();
      String var2 = var0;
      ArrayList var3 = new ArrayList();
      char[] var4 = var0.toCharArray();
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;

      while(true) {
         if (var9 < var4.length) {
            while(var9 < var4.length) {
               int var10 = a(var4, var9);
               if (var9 == var10) {
                  break;
               }

               var9 = var10;
            }

            if (var9 < var4.length) {
               char var15 = var4[var9];
               if (var15 != ':' && var15 != '&') {
                  if (var15 == '\\') {
                     int var17 = var9 + 1;
                     if (var17 < var4.length && var4[var17] == ':') {
                        var2 = var2.substring(0, var9 - var8) + var2.substring(var9 - var8 + 1);
                        ++var8;
                        var9 += 2;
                        continue;
                     }
                  }

                  if (var15 == '?') {
                     int var18 = var9 + 1;
                     if (var18 < var4.length && (var4[var18] == '?' || var4[var18] == '|' || var4[var18] == '&')) {
                        var9 += 2;
                        continue;
                     }

                     ++var6;
                     ++var7;
                  }
               } else {
                  int var16 = var9 + 1;
                  if (var16 < var4.length && var4[var16] == ':' && var15 == ':') {
                     var9 += 2;
                     continue;
                  }

                  Object var13 = null;
                  if (var16 < var4.length && var15 == ':' && var4[var16] == '{') {
                     while(true) {
                        if (var16 >= var4.length || '}' == var4[var16]) {
                           if (var16 >= var4.length) {
                              throw new RuleException("Non-terminated named parameter declaration at position " + var9 + " in statement: " + var0);
                           }

                           if (var16 - var9 > 3) {
                              String var20 = var0.substring(var9 + 2, var16);
                              var5 = a(var1, var5, var20);
                              var7 = a(var3, var7, var8, var9, var16 + 1, var20);
                           }

                           ++var16;
                           break;
                        }

                        ++var16;
                        if (':' == var4[var16] || '{' == var4[var16]) {
                           throw new RuleException("Parameter name contains invalid character '" + var4[var16] + "' at position " + var9 + " in statement: " + var0);
                        }
                     }
                  } else {
                     while(var16 < var4.length && !a(var4[var16])) {
                        ++var16;
                     }

                     if (var16 - var9 > 1) {
                        String var19 = var0.substring(var9 + 1, var16);
                        var5 = a(var1, var5, var19);
                        var7 = a(var3, var7, var8, var9, var16, var19);
                     }
                  }

                  var9 = var16 - 1;
               }

               ++var9;
               continue;
            }
         }

         ParsedSql var14 = new ParsedSql(var2);

         for(ParameterHolder var12 : (Iterable<ParameterHolder>)(Iterable<?>)(var3)) {
            var14.a(var12.getParameterName(), var12.getStartIndex(), var12.getEndIndex());
         }

         var14.a(var5);
         var14.b(var6);
         var14.c(var7);
         return var14;
      }
   }

   private static int a(List var0, int var1, int var2, int var3, int var4, String var5) {
      var0.add(new ParameterHolder(var5, var3 - var2, var4 - var2));
      ++var1;
      return var1;
   }

   private static int a(Set var0, int var1, String var2) {
      if (!var0.contains(var2)) {
         var0.add(var2);
         ++var1;
      }

      return var1;
   }

   private static boolean a(char var0) {
      if (Character.isWhitespace(var0)) {
         return true;
      } else {
         for(char var4 : a) {
            if (var0 == var4) {
               return true;
            }
         }

         return false;
      }
   }

   private static int a(char[] var0, int var1) {
      for(int var2 = 0; var2 < b.length; ++var2) {
         if (var0[var1] == b[var2].charAt(0)) {
            boolean var3 = true;

            for(int var4 = 1; var4 < b[var2].length(); ++var4) {
               if (var0[var1 + var4] != b[var2].charAt(var4)) {
                  var3 = false;
                  break;
               }
            }

            if (var3) {
               int var9 = b[var2].length();

               for(int var5 = var1 + var9; var5 < var0.length; ++var5) {
                  if (var0[var5] == c[var2].charAt(0)) {
                     boolean var6 = true;
                     int var7 = var5;

                     for(int var8 = 1; var8 < c[var2].length(); ++var8) {
                        if (var5 + var8 >= var0.length) {
                           return var0.length;
                        }

                        if (var0[var5 + var8] != c[var2].charAt(var8)) {
                           var6 = false;
                           break;
                        }

                        var7 = var5 + var8;
                     }

                     if (var6) {
                        return var7 + 1;
                     }
                  }
               }

               return var0.length;
            }
         }
      }

      return var1;
   }

   private static class ParameterHolder {
      private final String a;
      private final int b;
      private final int c;

      public ParameterHolder(String var1, int var2, int var3) {
         this.a = var1;
         this.b = var2;
         this.c = var3;
      }

      public String getParameterName() {
         return this.a;
      }

      public int getStartIndex() {
         return this.b;
      }

      public int getEndIndex() {
         return this.c;
      }
   }
}
