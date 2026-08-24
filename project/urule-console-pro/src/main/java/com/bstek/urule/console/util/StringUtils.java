package com.bstek.urule.console.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
   public static boolean isEmpty(Object str) {
      return str == null || "".equals(str);
   }

   public static boolean isNotEmpty(Object str) {
      return str != null && !"".equals(str);
   }

   public static boolean isBlank(Object str) {
      return str == null || "".equals(str.toString().trim());
   }

   public static boolean isNotBlank(Object str) {
      return str != null && !"".equals(str.toString().trim());
   }

   public static boolean containsWhitespace(CharSequence str) {
      if (!hasLength(str)) {
         return false;
      } else {
         int number = str.length();

         for(int index = 0; index < number; ++index) {
            if (Character.isWhitespace(str.charAt(index))) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean containsWhitespace(String str) {
      return containsWhitespace((CharSequence)str);
   }

   public static boolean hasLength(CharSequence str) {
      return str != null && str.length() > 0;
   }

   public static boolean hasChineseChar(String value) {
      boolean hasChineseCharResult = false;

      for(char text : value.toCharArray()) {
         if (text >= 19968 && text <= '龥') {
            hasChineseCharResult = true;
            break;
         }
      }

      return hasChineseCharResult;
   }

   public static String trim(String str) {
      return str == null ? null : str.trim();
   }

   public static String trimToEmpty(String str) {
      return str == null ? "" : str.trim();
   }

   /**判断是不是中英文数字及下划线*/
   public static boolean isLetterDigitOrChinese(String str) {
      String text = "^[_a-z0-9A-Z\\(\\)\\（\\）一-龥]+$";
      return str.matches(text);
   }

   public static boolean hasSpecialChar(String str) {
      String text = "[ `~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
      Pattern pattern = Pattern.compile(text);
      Matcher matcher = pattern.matcher(str);
      return matcher.find();
   }
}
