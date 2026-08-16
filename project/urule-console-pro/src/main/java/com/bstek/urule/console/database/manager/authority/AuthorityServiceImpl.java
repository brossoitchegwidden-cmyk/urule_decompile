package com.bstek.urule.console.database.manager.authority;

import com.bstek.urule.console.database.model.Authority;
import com.bstek.urule.console.database.model.Role;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.security.AuthenticationManager;
import com.bstek.urule.console.security.entity.Module;
import com.bstek.urule.console.security.entity.Permission;
import com.bstek.urule.console.security.provider.PermissionProvider;
import com.bstek.urule.console.type.GroupRoleEnum;
import com.bstek.urule.console.type.ProjectRoleEnum;
import com.bstek.urule.console.type.RoleCategory;
import java.sql.Connection;
import java.util.List;

public class AuthorityServiceImpl implements AuthorityService {
   public List getAuthoritysByRole(String var1, long var2) {
      return AuthorityManager.ins.getAuthoritysByRole(var1, var2);
   }

   public void add(Authority var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         AuthorityManager.ins.add(var2, var1);
      } catch (Exception var7) {
         throw var7;
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void remove(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         AuthorityManager.ins.remove(var3, var1);
      } catch (Exception var8) {
         throw var8;
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void removeByRole(String var1, long var2) {
      AuthorityManager.ins.removeByRole(var1, var2);
   }

   public Authority get(String var1, long var2, String var4) {
      return AuthorityManager.ins.get(var1, var2, var4);
   }

   public List getGroupModels(Role var1) {
      List var2 = PermissionProvider.getGroupModules();

      for(Module var4 : (Iterable<Module>)(Iterable<?>)(var2)) {
         if (var1.getName().equals(GroupRoleEnum.Owner.name())) {
            var4.setChecked(true);
            var4.setDisabled(true);
         }

         for(Permission var6 : (Iterable<Permission>)(Iterable<?>)(var4.getItems())) {
            if (var1.getName().equals(GroupRoleEnum.Owner.name())) {
               var6.setChecked(true);
               var6.setDisabled(true);
            } else {
               var6.setChecked(AuthenticationManager.decide(var1, RoleCategory.group, var4.getCode(), var6.getCode()));
            }
         }
      }

      return var2;
   }

   public List getProjectModels(Role var1) {
      List var2 = PermissionProvider.getProjectModules();

      for(Module var4 : (Iterable<Module>)(Iterable<?>)(var2)) {
         if (var1.getName().equals(ProjectRoleEnum.Manager.name())) {
            var4.setChecked(true);
            var4.setDisabled(true);
         }

         for(Permission var6 : (Iterable<Permission>)(Iterable<?>)(var4.getItems())) {
            if (var1.getName().equals(ProjectRoleEnum.Manager.name())) {
               var6.setChecked(true);
               var6.setDisabled(true);
            } else {
               var6.setChecked(AuthenticationManager.decide(var1, RoleCategory.project, var4.getCode(), var6.getCode()));
            }
         }
      }

      return var2;
   }

   private void a(long var1, Module var3, Permission var4, boolean var5) {
      Connection var6 = JdbcUtils.getConnection();

      try {
         Authority var7 = new Authority();
         var7.setAuth(1);
         var7.setRoleId(var1);
         var7.setRoleType(var3.getType().toString());
         String var8 = var3.getCode() + "_" + var4.getCode();
         var7.setResourceCode(var8);
         var7.setResourceType(var4.getType().toString());
         if (var4.isChecked()) {
            AuthorityManager.ins.add(var6, var7);
         } else if (var5) {
            this.remove(var6, var3.getType().toString(), var1, var8, var4.getType().toString());
         }
      } catch (Exception var12) {
         throw var12;
      } finally {
         JdbcUtils.closeConnection(var6);
      }

   }

   public void remove(Connection var1, String var2, long var3, String var5, String var6) {
      AuthorityManager.ins.remove(var1, var2, var3, var5, var6);
   }

   public void storePermissions(long var1, List var3) {
      for(Module var5 : (Iterable<Module>)(Iterable<?>)(var3)) {
         for(Permission var7 : (Iterable<Permission>)(Iterable<?>)(var5.getItems())) {
            this.a(var1, var5, var7, true);
         }
      }

   }

   public void initPermissions(long var1, List var3) {
      for(Module var5 : (Iterable<Module>)(Iterable<?>)(var3)) {
         for(Permission var7 : (Iterable<Permission>)(Iterable<?>)(var5.getItems())) {
            this.a(var1, var5, var7, false);
         }
      }

   }
}
