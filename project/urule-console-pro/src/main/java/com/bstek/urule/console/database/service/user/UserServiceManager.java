package com.bstek.urule.console.database.service.user;

import com.bstek.urule.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

public class UserServiceManager {
   private static final Log logger = LogFactory.getLog(UserServiceManager.class);
   private static UserService userServiceImpl;
   private static boolean customUserService;

   public static UserService getUserService() {
      if (UserServiceManager.userServiceImpl == null) {
         ApplicationContext applicationContext = Utils.getApplicationContext();

         try {
            UserServiceManager.userServiceImpl = (UserService)applicationContext.getBean("urule.userService");
            UserServiceManager.customUserService = true;
         } catch (Exception exception) {
            UserServiceManager.logger.warn("BeanID:UserService undefined!");
         }

         if (UserServiceManager.userServiceImpl == null) {
            UserServiceManager.userServiceImpl = new UserServiceImpl();
         }
      }

      return UserServiceManager.userServiceImpl;
   }

   public static boolean isCustomUserService() {
      return UserServiceManager.customUserService;
   }
}
