package com.bstek.urule.console.admin.permission;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.ParameterInvaidException;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.database.manager.authority.AuthorityService;
import com.bstek.urule.console.database.manager.group.role.GroupRoleManager;
import com.bstek.urule.console.database.manager.project.role.ProjectRoleManager;
import com.bstek.urule.console.database.model.GroupRole;
import com.bstek.urule.console.database.model.ProjectRole;
import com.bstek.urule.console.database.model.Role;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.security.entity.Module;
import com.bstek.urule.console.security.entity.Permission;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.type.GroupModule;
import com.bstek.urule.console.type.ProjectModule;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PermissionServletHandler extends ApiServletHandler {
   @URuleAuthorization(
      authType = "group",
      model = "permissions",
      code = "manager"
   )
   public void groupStore(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("roleId"));
      GroupRole var5 = GroupRoleManager.ins.get(var3);
      if (var5.getGroupId().equals(ContextHolder.getGroupId())) {
         this.a(var3, var1);
         SystemLogUtils.addGroupOperationLog(GroupModule.permissions.name(), "manager", var3, String.format("Modify the permissions of role %s[%s]", var5.getName(), var5.getId()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void projectStore(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("roleId"));
      ProjectRole var5 = ProjectRoleManager.ins.get(var3);
      if (var5.getProjectId() == ContextHolder.getProjectId()) {
         this.a(var3, var1);
         SystemLogUtils.addProjectOperationLog(ProjectModule.permissions.name(), "manager", var5.getId(), String.format("Modify %s[%s] permissions", var5.getName(), var5.getId()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   private void a(long var1, HttpServletRequest var3) throws Exception {
      List var4 = (List)this.a().readValue(var3.getParameter("models"), new TypeReference() {
      });
      AuthorityService.ins.storePermissions(var1, var4);
   }

   @URuleAuthorization(
      authType = "group",
      model = "permissions",
      code = "manager"
   )
   public void groupModels(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("roleId"));
      GroupRole var5 = GroupRoleManager.ins.get(var3);
      if (var5.getGroupId().equals(ContextHolder.getGroupId())) {
         this.a(var2, AuthorityService.ins.getGroupModels(var5));
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void projectModels(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("roleId"));
      ProjectRole var5 = ProjectRoleManager.ins.get(var3);
      if (var5.getProjectId() == ContextHolder.getProjectId()) {
         this.a(var2, AuthorityService.ins.getProjectModels(var5));
      } else {
         throw new ParameterInvaidException();
      }
   }

   public void group(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = ContextHolder.getGroupId();
      User var4 = SecurityUtils.getLoginUser(var1);
      List var5 = GroupRoleManager.ins.loadUserRoles(var3, var4.getName());
      Object var6 = new ArrayList();

      for(Role var8 : (Iterable<Role>)(Iterable<?>)(var5)) {
         List var9 = AuthorityService.ins.getGroupModels(var8);
         if (((List)var6).size() == 0) {
            var6 = var9;
         } else {
            this.a(var9, (List)var6);
         }
      }

      this.a(var2, var6);
   }

   public void project(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = ContextHolder.getProjectId();
      User var4 = SecurityUtils.getLoginUser(var1);
      List var5 = ProjectRoleManager.ins.loadUserRoles(var3, var4.getName());
      Object var6 = new ArrayList();

      for(Role var8 : (Iterable<Role>)(Iterable<?>)(var5)) {
         List var9 = AuthorityService.ins.getProjectModels(var8);
         if (((List)var6).size() == 0) {
            var6 = var9;
         } else {
            this.a(var9, (List)var6);
         }
      }

      this.a(var2, var6);
   }

   private void a(List var1, List var2) {
      for(Module var4 : (Iterable<Module>)(Iterable<?>)(var2)) {
         for(Module var6 : (Iterable<Module>)(Iterable<?>)(var1)) {
            if (var4.getCode().equals(var6.getCode())) {
               this.b(var6.getItems(), var4.getItems());
            }
         }
      }

   }

   private void b(List var1, List var2) {
      for(Permission var4 : (Iterable<Permission>)(Iterable<?>)(var2)) {
         for(Permission var6 : (Iterable<Permission>)(Iterable<?>)(var1)) {
            if (var4.getCode().equals(var6.getCode()) && var6.isChecked()) {
               var4.setChecked(true);
            }
         }
      }

   }

   public String url() {
      return "/permission";
   }
}
