package com.bstek.urule.console.database.service.project.role;

import com.bstek.urule.console.database.model.ProjectRole;
import java.util.List;

public interface ProjectRoleService {
   ProjectRoleServiceImpl ins = new ProjectRoleServiceImpl();

   /**获取项目下的角色列表*/
   List loadRoles(long projectId);

   /**获取角色的用户列表*/
   List users(long projectId, long roleId);

   /**插入角色对象*/
   void add(ProjectRole role);

   /**更新角色对象*/
   void update(ProjectRole role);

   /**删除角色对象*/
   void remove(Long id);

   /**添加用户角色关系*/
   void addUserRole(long projectId, String userId, long roleId);

   /**删除用户角色关系*/
   void removeUserRole(String userId, long roleId);

   /**获取用户的角色列表*/
   List loadUserRoles(long projectId, String account);
}
