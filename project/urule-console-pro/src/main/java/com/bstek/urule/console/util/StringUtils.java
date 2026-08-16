package com.bstek.urule.console.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
   public static boolean isEmpty(Object var0) {
      return var0 == null || "".equals(var0);
   }

   public static boolean isNotEmpty(Object var0) {
      return var0 != null && !"".equals(var0);
   }

   public static boolean isBlank(Object var0) {
      return var0 == null || "".equals(var0.toString().trim());
   }

   public static boolean isNotBlank(Object var0) {
      return var0 != null && !"".equals(var0.toString().trim());
   }

   public static boolean containsWhitespace(CharSequence var0) {
      if (!hasLength(var0)) {
         return false;
      } else {
         int var1 = var0.length();

         for(int var2 = 0; var2 < var1; ++var2) {
            if (Character.isWhitespace(var0.charAt(var2))) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean containsWhitespace(String var0) {
      return containsWhitespace((CharSequence)var0);
   }

   public static boolean hasLength(CharSequence var0) {
      return var0 != null && var0.length() > 0;
   }

   public static boolean hasChineseChar(String var0) {
      boolean var1 = false;

      for(char var5 : var0.toCharArray()) {
         if (var5 >= 19968 && var5 <= '龥') {
            var1 = true;
            break;
         }
      }

      return var1;
   }

   public static String trim(String var0) {
      return var0 == null ? null : var0.trim();
   }

   public static String trimToEmpty(String var0) {
      return var0 == null ? "" : var0.trim();
   }

   public static boolean isLetterDigitOrChinese(String var0) {
      String var1 = "^[_a-z0-9A-Z\\(\\)\\（\\）一-龥]+$";
      return var0.matches(var1);
   }

   public static boolean hasSpecialChar(String var0) {
      String var1 = "[ `~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
      Pattern var2 = Pattern.compile(var1);
      Matcher var3 = var2.matcher(var0);
      return var3.find();
   }
}
