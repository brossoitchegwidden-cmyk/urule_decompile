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
   /**保存团队权限配置*/
   @URuleAuthorization(
      authType = "group",
      model = "permissions",
      code = "manager"
   )
   public void groupStore(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("roleId"));
      GroupRole groupRole = GroupRoleManager.ins.get(longValue);
      if (groupRole.getGroupId().equals(ContextHolder.getGroupId())) {
         this.storePermissions(longValue, req);
         SystemLogUtils.addGroupOperationLog(GroupModule.permissions.name(), "manager", longValue, String.format("Modify the permissions of role %s[%s]", groupRole.getName(), groupRole.getId()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**保存项目权限配置*/
   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void projectStore(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("roleId"));
      ProjectRole projectRole = ProjectRoleManager.ins.get(longValue);
      if (projectRole.getProjectId() == ContextHolder.getProjectId()) {
         this.storePermissions(longValue, req);
         SystemLogUtils.addProjectOperationLog(ProjectModule.permissions.name(), "manager", projectRole.getId(), String.format("Modify %s[%s] permissions", projectRole.getName(), projectRole.getId()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   private void storePermissions(long longValue, HttpServletRequest httpServletRequest) throws Exception {
      List items = (List)this.createObjectMapper().readValue(httpServletRequest.getParameter("models"), new TypeReference() {
      });
      AuthorityService.ins.storePermissions(longValue, items);
   }

   /**获取团队角色的权限配置*/
   @URuleAuthorization(
      authType = "group",
      model = "permissions",
      code = "manager"
   )
   public void groupModels(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("roleId"));
      GroupRole groupRole = GroupRoleManager.ins.get(longValue);
      if (groupRole.getGroupId().equals(ContextHolder.getGroupId())) {
         this.writeObjectToJson(resp, AuthorityService.ins.getGroupModels(groupRole));
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**获取项目角色的权限配置*/
   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void projectModels(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("roleId"));
      ProjectRole projectRole = ProjectRoleManager.ins.get(longValue);
      if (projectRole.getProjectId() == ContextHolder.getProjectId()) {
         this.writeObjectToJson(resp, AuthorityService.ins.getProjectModels(projectRole));
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**获取登录用户的团队权限信息*/
   public void group(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String groupId = ContextHolder.getGroupId();
      User loginUser = SecurityUtils.getLoginUser(req);
      List userRoles = GroupRoleManager.ins.loadUserRoles(groupId, loginUser.getName());
      Object groupModels2 = new ArrayList();

      for(Role role : (Iterable<Role>)(Iterable<?>)(userRoles)) {
         List groupModels = AuthorityService.ins.getGroupModels(role);
         if (((List)groupModels2).size() == 0) {
            groupModels2 = groupModels;
         } else {
            this.mergeModulePermissions(groupModels, (List)groupModels2);
         }
      }

      this.writeObjectToJson(resp, groupModels2);
   }

   /**获取登录用户对应项目的权限信息*/
   public void project(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long projectId = ContextHolder.getProjectId();
      User loginUser = SecurityUtils.getLoginUser(req);
      List userRoles = ProjectRoleManager.ins.loadUserRoles(projectId, loginUser.getName());
      Object projectModels2 = new ArrayList();

      for(Role role : (Iterable<Role>)(Iterable<?>)(userRoles)) {
         List projectModels = AuthorityService.ins.getProjectModels(role);
         if (((List)projectModels2).size() == 0) {
            projectModels2 = projectModels;
         } else {
            this.mergeModulePermissions(projectModels, (List)projectModels2);
         }
      }

      this.writeObjectToJson(resp, projectModels2);
   }

   private void mergeModulePermissions(List items, List items2) {
      for(Module module : (Iterable<Module>)(Iterable<?>)(items2)) {
         for(Module module2 : (Iterable<Module>)(Iterable<?>)(items)) {
            if (module.getCode().equals(module2.getCode())) {
               this.mergePermissionChecks(module2.getItems(), module.getItems());
            }
         }
      }

   }

   private void mergePermissionChecks(List items, List items2) {
      for(Permission permission : (Iterable<Permission>)(Iterable<?>)(items2)) {
         for(Permission permission2 : (Iterable<Permission>)(Iterable<?>)(items)) {
            if (permission.getCode().equals(permission2.getCode()) && permission2.isChecked()) {
               permission.setChecked(true);
            }
         }
      }

   }

   public String url() {
      return "/permission";
   }
}
