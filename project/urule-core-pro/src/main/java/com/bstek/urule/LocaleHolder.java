package com.bstek.urule;

import java.util.Locale;

public class LocaleHolder {
   private static Locale a = null;
   private static final ThreadLocal<Locale> b = new ThreadLocal<>();

   public static void set(Locale var0) {
      b.set(var0);
   }

   public static Locale get() {
      Locale var0 = b.get();
      if (var0 == null) {
         var0 = a;
      }

      return var0 == null ? Locale.SIMPLIFIED_CHINESE : var0;
   }

   public static void setLocale(Locale var0) {
      if (a == null) {
         a = var0;
      }
   }

   public static boolean isEnglish() {
      return "en".equals(get().getLanguage());
   }
}
