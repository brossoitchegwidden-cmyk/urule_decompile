package com.bstek.urule.console.security;

import com.bstek.urule.Utils;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.security.provider.DefaultSecurityProvider;
import com.bstek.urule.console.security.provider.SecurityProvider;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;

public class SecurityUtils {
   private static SecurityProvider securityProvider;
   private static boolean customProvider = false;

   public static String getLoginUsername(HttpServletRequest req) {
      User loginUser = getLoginUser(req);
      return loginUser == null ? null : loginUser.getName();
   }

   public static User getLoginUser(HttpServletRequest req) {
      return SecurityUtils.securityProvider.getLoginUser(req);
   }

   public static SecurityProvider getSecurityProvider() {
      return SecurityUtils.securityProvider;
   }

   public static boolean isCustomProvider() {
      return SecurityUtils.customProvider;
   }

   static {
      ApplicationContext applicationContext = Utils.getApplicationContext();

      try {
         SecurityUtils.securityProvider = (SecurityProvider)applicationContext.getBean("urule.securityProvider");
         SecurityUtils.customProvider = true;
      } catch (Exception ignored) {
      }

      if (SecurityUtils.securityProvider == null) {
         SecurityUtils.securityProvider = new DefaultSecurityProvider();
      }

   }
}
