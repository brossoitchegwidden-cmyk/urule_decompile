package com.bstek.urule.console.database.service.group.role;

import com.bstek.urule.console.database.model.GroupRole;
import java.util.List;

public interface GroupRoleService {
   GroupRoleServiceImpl ins = new GroupRoleServiceImpl();

   /**获取团队下的角色列表*/
   List loadRoles(String groupId) throws Exception;

   /**获取角色列表*/
   List loadUserRoles(String groupId, String account) throws Exception;

   /**获取角色的用户列表*/
   List users(String groupId, long roleId) throws Exception;

   /**获取团队角色*/
   GroupRole get(long roleId) throws Exception;

   /**插入角色对象*/
   void add(GroupRole role) throws Exception;

   /**更新角色对象*/
   void update(GroupRole role) throws Exception;

   /**删除角色对象*/
   void remove(Long id) throws Exception;

   /**添加用户角色关系*/
   void addUserRole(String groupId, String userId, long roleId) throws Exception;

   /**删除用户角色关系*/
   void removeUserRole(String groupId, String userId, long roleId) throws Exception;
}
