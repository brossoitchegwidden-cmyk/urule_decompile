package com.bstek.urule.console.database.manager.group.role;

import com.bstek.urule.console.database.model.GroupRole;
import com.bstek.urule.console.database.model.UserRole;
import java.util.List;

public interface GroupRoleManager {
   GroupRoleManagerImpl ins = new GroupRoleManagerImpl();

   /**获取团队角色列表*/
   List loadRoles(String groupId);

   /**获取用户的角色列表*/
   List loadUserRoles(String groupId, String account);

   /**获取角色的用户列表*/
   List loadRoleUsers(String groupId, long roleId);

   /**获取团队角色*/
   GroupRole get(long roleId);

   /**获取团队角色*/
   GroupRole get(String groupId, String name);

   /**新增团队角色*/
   void add(GroupRole role);

   /**更新团队角色*/
   void update(GroupRole role);

   /**删除团队角色*/
   void remove(Long id);

   /**删除团队的所有角色*/
   void removeByGroupId(String groupId);

   /**检查同一团队中的角色名称是否重复*/
   boolean checkExist(String groupId, String name);

   /**获取用户角色关系*/
   UserRole getUserRole(String userId, long roleId);

   /**添加用户角色关系*/
   void addUserRole(String groupId, String userId, long roleId);

   /**删除用户角色关系*/
   void removeUserRole(String groupId, String userId, long roleId);

   /**删除角色对应的用户列表*/
   void removeRoleUsers(long roleId);

   /**删除用户对应的角色列表*/
   void removeUserRoles(String groupId, String userId);
}
