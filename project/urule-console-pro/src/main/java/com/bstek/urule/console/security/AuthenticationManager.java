package com.bstek.urule.console.security;

import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.config.Configure;
import com.bstek.urule.console.database.manager.authority.AuthorityManager;
import com.bstek.urule.console.database.manager.group.role.GroupRoleManager;
import com.bstek.urule.console.database.manager.project.role.ProjectRoleManager;
import com.bstek.urule.console.database.model.Authority;
import com.bstek.urule.console.database.model.Role;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.type.GroupRoleEnum;
import com.bstek.urule.console.type.ProjectRoleEnum;
import com.bstek.urule.console.type.RoleCategory;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuthenticationManager {
   private static final Log logger = LogFactory.getLog(AuthenticationManager.class);
   public static final int NO_AUTHORITYS = 2;
   public static final int NO_GRANT = 0;
   public static final int GRANT = 1;

   private static boolean evaluatePermission() {
      return Configure.getConfigure().getBoolean("urule.security.useConservativeStrategy", true);
   }

   public static boolean isGranted(int granted) {
      if (granted == 1) {
         return true;
      } else if (granted == 0) {
         return false;
      } else if (granted == 2) {
         return !evaluatePermission();
      } else {
         return true;
      }
   }

   /**判断访问权限*/
   public static boolean decide(User user, RoleCategory roleType, String modelCode, String resourceCode) {
      AuthenticationManager.logger.debug("user:" + user + ",roleType:" + roleType.toString() + ",modelCode:" + modelCode + ",resourceCode:" + resourceCode);

      try {
         String text = modelCode + "_" + resourceCode;
         int number = evaluatePermission(user, roleType, text);
         return isGranted(number);
      } catch (Exception exception) {
         java.util.logging.Logger.getLogger(AuthenticationManager.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
         AuthenticationManager.logger.error(exception.getMessage(), exception);
         return false;
      }
   }

   public static boolean decide(Role role, RoleCategory roleType, String modelCode, String resourceCode) {
      AuthenticationManager.logger.debug("role:" + role + ",roleType:" + roleType.toString() + ",modelCode:" + modelCode + ",resourceCode:" + resourceCode);

      try {
         String text = modelCode + "_" + resourceCode;
         ArrayList items = new ArrayList();

         for(Authority authority : (Iterable<Authority>)(Iterable<?>)(AuthorityManager.ins.getAuthoritysByRole(roleType.name(), role.getId()))) {
            if (text.equals(authority.getResourceCode())) {
               items.add(authority);
            }
         }

         int number = evaluatePermission(items, role, roleType, text);
         return number == 1;
      } catch (Exception exception) {
         AuthenticationManager.logger.error(exception.getMessage(), exception);
         return false;
      }
   }

   private static int evaluatePermission(User user, RoleCategory roleCategory, String text) throws Exception {
      int number = 2;
      List userRoles = null;
      if (roleCategory == RoleCategory.group) {
         userRoles = GroupRoleManager.ins.loadUserRoles(ContextHolder.getGroupId(), user.getName());
      } else {
         userRoles = ProjectRoleManager.ins.loadUserRoles(ContextHolder.getProjectId(), user.getName());
      }

      ArrayList items = new ArrayList();

      for(Role role : (Iterable<Role>)(Iterable<?>)(userRoles)) {
         ArrayList items2 = new ArrayList();

         for(Authority authority : (Iterable<Authority>)(Iterable<?>)(AuthorityManager.ins.getAuthoritysByRole(roleCategory.name(), role.getId()))) {
            if (text.equals(authority.getResourceCode())) {
               items2.add(authority);
            }
         }

         items.addAll(items2);
      }

      for(Role role2 : (Iterable<Role>)(Iterable<?>)(userRoles)) {
         number = evaluatePermission(items, role2, roleCategory, text);
         if (number == 1) {
            break;
         }
      }

      AuthenticationManager.logger.debug("roles:" + userRoles);

      for(Role role3 : (Iterable<Role>)(Iterable<?>)(userRoles)) {
         number = evaluatePermission(items, role3, roleCategory, text);
         if (number == 1) {
            break;
         }
      }

      if (number == 1) {
         return 1;
      } else {
         return 0;
      }
   }

   private static int evaluatePermission(List items, Role role, RoleCategory roleCategory, String text) throws Exception {
      if (!role.getName().equals(GroupRoleEnum.Owner.name()) && !role.getName().equals(ProjectRoleEnum.Manager.name())) {
         byte byteValue = 2;
         if (items.size() == 0) {
            return 2;
         } else {
            for(Authority authority : (Iterable<Authority>)(Iterable<?>)(items)) {
               if (authority.getRoleId() == role.getId()) {
                  byteValue = 1;
                  break;
               }
            }

            return byteValue == 1 ? 1 : 0;
         }
      } else {
         return 1;
      }
   }
}
