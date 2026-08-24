package com.bstek.urule;

import java.util.Locale;

public class LocaleHolder {
   private static Locale locale = null;
   private static final ThreadLocal<Locale> CURRENT_LOCALE = new ThreadLocal<>();

   public static void set(Locale locale) {
      CURRENT_LOCALE.set(locale);
   }

   public static Locale get() {
      Locale locale = CURRENT_LOCALE.get();
      if (locale == null) {
         locale = LocaleHolder.locale;
      }

      return locale == null ? Locale.SIMPLIFIED_CHINESE : locale;
   }

   public static void setLocale(Locale locale) {
      if (LocaleHolder.locale == null) {
         LocaleHolder.locale = locale;
      }
   }

   public static boolean isEnglish() {
      return "en".equals(get().getLanguage());
   }
}
