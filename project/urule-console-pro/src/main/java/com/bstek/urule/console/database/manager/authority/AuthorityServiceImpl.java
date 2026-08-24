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
   public List getAuthoritysByRole(String roleType, long roleCode) {
      return AuthorityManager.ins.getAuthoritysByRole(roleType, roleCode);
   }
   public void add(Authority auth) {
      Connection connection = JdbcUtils.getConnection();

      try {
         AuthorityManager.ins.add(connection, auth);
      } catch (Exception exception) {
         throw exception;
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void remove(long authId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         AuthorityManager.ins.remove(connection, authId);
      } catch (Exception exception) {
         throw exception;
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeByRole(String roleType, long roleCode) {
      AuthorityManager.ins.removeByRole(roleType, roleCode);
   }
   public Authority get(String roleType, long roleId, String code) {
      return AuthorityManager.ins.get(roleType, roleId, code);
   }
   public List getGroupModels(Role role) {
      List groupModules = PermissionProvider.getGroupModules();

      for(Module module : (Iterable<Module>)(Iterable<?>)(groupModules)) {
         if (role.getName().equals(GroupRoleEnum.Owner.name())) {
            module.setChecked(true);
            module.setDisabled(true);
         }

         for(Permission permission : (Iterable<Permission>)(Iterable<?>)(module.getItems())) {
            if (role.getName().equals(GroupRoleEnum.Owner.name())) {
               permission.setChecked(true);
               permission.setDisabled(true);
            } else {
               permission.setChecked(AuthenticationManager.decide(role, RoleCategory.group, module.getCode(), permission.getCode()));
            }
         }
      }

      return groupModules;
   }
   public List getProjectModels(Role role) {
      List projectModules = PermissionProvider.getProjectModules();

      for(Module module : (Iterable<Module>)(Iterable<?>)(projectModules)) {
         if (role.getName().equals(ProjectRoleEnum.Manager.name())) {
            module.setChecked(true);
            module.setDisabled(true);
         }

         for(Permission permission : (Iterable<Permission>)(Iterable<?>)(module.getItems())) {
            if (role.getName().equals(ProjectRoleEnum.Manager.name())) {
               permission.setChecked(true);
               permission.setDisabled(true);
            } else {
               permission.setChecked(AuthenticationManager.decide(role, RoleCategory.project, module.getCode(), permission.getCode()));
            }
         }
      }

      return projectModules;
   }

   private void storePermission(long longValue, Module module, Permission permission, boolean flag) {
      Connection connection = JdbcUtils.getConnection();

      try {
         Authority authority = new Authority();
         authority.setAuth(1);
         authority.setRoleId(longValue);
         authority.setRoleType(module.getType().toString());
         String text = module.getCode() + "_" + permission.getCode();
         authority.setResourceCode(text);
         authority.setResourceType(permission.getType().toString());
         if (permission.isChecked()) {
            AuthorityManager.ins.add(connection, authority);
         } else if (flag) {
            this.remove(connection, module.getType().toString(), longValue, text, permission.getType().toString());
         }
      } catch (Exception exception) {
         throw exception;
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   /**删除资源的授权信息*/
   public void remove(Connection conn, String roleType, long roleId, String resourceCode, String resourceType) {
      AuthorityManager.ins.remove(conn, roleType, roleId, resourceCode, resourceType);
   }

   public void storePermissions(long roleId, List models) {
      for(Module module : (Iterable<Module>)(Iterable<?>)(models)) {
         for(Permission permission : (Iterable<Permission>)(Iterable<?>)(module.getItems())) {
            this.storePermission(roleId, module, permission, true);
         }
      }

   }

   public void initPermissions(long roleId, List models) {
      for(Module module : (Iterable<Module>)(Iterable<?>)(models)) {
         for(Permission permission : (Iterable<Permission>)(Iterable<?>)(module.getItems())) {
            this.storePermission(roleId, module, permission, false);
         }
      }

   }
}
