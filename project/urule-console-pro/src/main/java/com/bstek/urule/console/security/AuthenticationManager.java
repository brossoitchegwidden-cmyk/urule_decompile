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
   private static final Log a = LogFactory.getLog(AuthenticationManager.class);
   public static final int NO_AUTHORITYS = 2;
   public static final int NO_GRANT = 0;
   public static final int GRANT = 1;

   private static boolean a() {
      return Configure.getConfigure().getBoolean("urule.security.useConservativeStrategy", true);
   }

   public static boolean isGranted(int var0) {
      if (var0 == 1) {
         return true;
      } else if (var0 == 0) {
         return false;
      } else if (var0 == 2) {
         return !a();
      } else {
         return true;
      }
   }

   public static boolean decide(User var0, RoleCategory var1, String var2, String var3) {
      a.debug("user:" + var0 + ",roleType:" + var1.toString() + ",modelCode:" + var2 + ",resourceCode:" + var3);

      try {
         String var4 = var2 + "_" + var3;
         int var5 = a(var0, var1, var4);
         return isGranted(var5);
      } catch (Exception var6) {
         var6.printStackTrace();
         a.error(var6.getMessage(), var6);
         return false;
      }
   }

   public static boolean decide(Role var0, RoleCategory var1, String var2, String var3) {
      a.debug("role:" + var0 + ",roleType:" + var1.toString() + ",modelCode:" + var2 + ",resourceCode:" + var3);

      try {
         String var4 = var2 + "_" + var3;
         ArrayList var5 = new ArrayList();

         for(Authority var8 : (Iterable<Authority>)(Iterable<?>)(AuthorityManager.ins.getAuthoritysByRole(var1.name(), var0.getId()))) {
            if (var4.equals(var8.getResourceCode())) {
               var5.add(var8);
            }
         }

         int var10 = a(var5, var0, var1, var4);
         return var10 == 1;
      } catch (Exception var9) {
         a.error(var9.getMessage(), var9);
         return false;
      }
   }

   private static int a(User var0, RoleCategory var1, String var2) throws Exception {
      int var3 = 2;
      List var4 = null;
      if (var1 == RoleCategory.group) {
         var4 = GroupRoleManager.ins.loadUserRoles(ContextHolder.getGroupId(), var0.getName());
      } else {
         var4 = ProjectRoleManager.ins.loadUserRoles(ContextHolder.getProjectId(), var0.getName());
      }

      ArrayList var5 = new ArrayList();

      for(Role var7 : (Iterable<Role>)(Iterable<?>)(var4)) {
         ArrayList var8 = new ArrayList();

         for(Authority var11 : (Iterable<Authority>)(Iterable<?>)(AuthorityManager.ins.getAuthoritysByRole(var1.name(), var7.getId()))) {
            if (var2.equals(var11.getResourceCode())) {
               var8.add(var11);
            }
         }

         var5.addAll(var8);
      }

      for(Role var15 : (Iterable<Role>)(Iterable<?>)(var4)) {
         var3 = a(var5, var15, var1, var2);
         if (var3 == 1) {
            break;
         }
      }

      a.debug("roles:" + var4);

      for(Role var16 : (Iterable<Role>)(Iterable<?>)(var4)) {
         var3 = a(var5, var16, var1, var2);
         if (var3 == 1) {
            break;
         }
      }

      if (var3 == 1) {
         return 1;
      } else {
         return 0;
      }
   }

   private static int a(List var0, Role var1, RoleCategory var2, String var3) throws Exception {
      if (!var1.getName().equals(GroupRoleEnum.Owner.name()) && !var1.getName().equals(ProjectRoleEnum.Manager.name())) {
         byte var4 = 2;
         if (var0.size() == 0) {
            return 2;
         } else {
            for(Authority var6 : (Iterable<Authority>)(Iterable<?>)(var0)) {
               if (var6.getRoleId() == var1.getId()) {
                  var4 = 1;
                  break;
               }
            }

            return var4 == 1 ? 1 : 0;
         }
      } else {
         return 1;
      }
   }
}
