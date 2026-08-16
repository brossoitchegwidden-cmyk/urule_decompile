package com.bstek.urule.console.database.service.user;

import com.bstek.urule.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

public class UserServiceManager {
   private static final Log a = LogFactory.getLog(UserServiceManager.class);
   private static UserService b;
   private static boolean c;

   public static UserService getUserService() {
      if (b == null) {
         ApplicationContext var0 = Utils.getApplicationContext();

         try {
            b = (UserService)var0.getBean("urule.userService");
            c = true;
         } catch (Exception var2) {
            a.warn("BeanID:UserService undefined!");
         }

         if (b == null) {
            b = new UserServiceImpl();
         }
      }

      return b;
   }

   public static boolean isCustomUserService() {
      return c;
   }
}
