package com.bstek.urule.console.security;

import com.bstek.urule.Utils;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.security.provider.DefaultSecurityProvider;
import com.bstek.urule.console.security.provider.SecurityProvider;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;

public class SecurityUtils {
   private static SecurityProvider a;
   private static boolean b = false;

   public static String getLoginUsername(HttpServletRequest var0) {
      User var1 = getLoginUser(var0);
      return var1 == null ? null : var1.getName();
   }

   public static User getLoginUser(HttpServletRequest var0) {
      return a.getLoginUser(var0);
   }

   public static SecurityProvider getSecurityProvider() {
      return a;
   }

   public static boolean isCustomProvider() {
      return b;
   }

   static {
      ApplicationContext var0 = Utils.getApplicationContext();

      try {
         a = (SecurityProvider)var0.getBean("urule.securityProvider");
         b = true;
      } catch (Exception var2) {
      }

      if (a == null) {
         a = new DefaultSecurityProvider();
      }

   }
}
