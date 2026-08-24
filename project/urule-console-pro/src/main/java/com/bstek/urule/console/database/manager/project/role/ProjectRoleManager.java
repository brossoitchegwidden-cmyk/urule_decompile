package com.bstek.urule.console.database.manager.project.role;

import com.bstek.urule.console.database.model.ProjectRole;
import com.bstek.urule.console.database.model.UserRole;
import java.util.List;

public interface ProjectRoleManager {
   ProjectRoleManagerImpl ins = new ProjectRoleManagerImpl();

   /**获取项目对应的角色*/
   List loadRoles(long projectId);

   /**获取用户的角色列表*/
   List loadUserRoles(long projectId, String account);

   /**获取角色的用户列表*/
   List loadRoleUsers(long projectId, long roleId);

   /**新增角色*/
   void add(ProjectRole role);

   /**更新角色*/
   void update(ProjectRole role);

   /**删除角色*/
   void remove(Long id);

   /**删除项目的所有角色*/
   void removeByProjectId(Long id);

   /**检测相同项目下是否有相同名称的角色*/
   boolean checkExist(long projectId, String name);

   /**添加用户角色关系*/
   void addUserRole(long projectId, String userId, long roleId);

   /**删除用户角色关系*/
   void removeUserRole(String userId, long roleId);

   /**删除角色对应的用户列表*/
   void removeRoleUsers(long roleId);

   /**删除用户对应的角色列表*/
   void removeUserRoles(String userId);

   /**获取项目角色*/
   ProjectRole get(long roleId);

   /**获取项目角色*/
   ProjectRole get(long projectId, String name);

   /**获取用户角色关系*/
   UserRole getUserRole(String userId, long roleId);
}
