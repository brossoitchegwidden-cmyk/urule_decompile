package com.bstek.urule.console.util;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {
   public static String getIpAddress(HttpServletRequest var0) {
      String var1 = var0.getHeader("X-Forwarded-For");
      if (var1 != null && var1.length() != 0 && !"unknown".equalsIgnoreCase(var1)) {
         if (var1.length() > 15) {
            String[] var2 = var1.split(",");

            for(int var3 = 0; var3 < var2.length; ++var3) {
               String var4 = var2[var3];
               if (!"unknown".equalsIgnoreCase(var4)) {
                  var1 = var4;
                  break;
               }
            }
         }
      } else {
         if (var1 == null || var1.length() == 0 || "unknown".equalsIgnoreCase(var1)) {
            var1 = var0.getHeader("Proxy-Client-IP");
         }

         if (var1 == null || var1.length() == 0 || "unknown".equalsIgnoreCase(var1)) {
            var1 = var0.getHeader("WL-Proxy-Client-IP");
         }

         if (var1 == null || var1.length() == 0 || "unknown".equalsIgnoreCase(var1)) {
            var1 = var0.getHeader("HTTP_CLIENT_IP");
         }

         if (var1 == null || var1.length() == 0 || "unknown".equalsIgnoreCase(var1)) {
            var1 = var0.getHeader("HTTP_X_FORWARDED_FOR");
         }

         if (var1 == null || var1.length() == 0 || "unknown".equalsIgnoreCase(var1)) {
            var1 = var0.getRemoteAddr();
         }
      }

      return var1;
   }
}
