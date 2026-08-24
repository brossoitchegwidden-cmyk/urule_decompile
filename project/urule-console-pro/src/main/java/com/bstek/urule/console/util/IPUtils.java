package com.bstek.urule.console.util;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {
   /**获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;*/
   public static String getIpAddress(HttpServletRequest request) {
      String header = request.getHeader("X-Forwarded-For");
      if (header != null && header.length() != 0 && !"unknown".equalsIgnoreCase(header)) {
         if (header.length() > 15) {
            String[] parts = header.split(",");

            for(int index = 0; index < parts.length; ++index) {
               String text = parts[index];
               if (!"unknown".equalsIgnoreCase(text)) {
                  header = text;
                  break;
               }
            }
         }
      } else {
         if (header == null || header.length() == 0 || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("Proxy-Client-IP");
         }

         if (header == null || header.length() == 0 || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("WL-Proxy-Client-IP");
         }

         if (header == null || header.length() == 0 || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("HTTP_CLIENT_IP");
         }

         if (header == null || header.length() == 0 || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("HTTP_X_FORWARDED_FOR");
         }

         if (header == null || header.length() == 0 || "unknown".equalsIgnoreCase(header)) {
            header = request.getRemoteAddr();
         }
      }

      return header;
   }
}
