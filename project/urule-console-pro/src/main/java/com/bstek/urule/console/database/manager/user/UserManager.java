package com.bstek.urule.console.database.manager.user;

import com.bstek.urule.console.database.model.User;
import java.util.List;

public interface UserManager {
   UserManagerImpl ins = new UserManagerImpl();

   /**获取团队下的用户列表*/
   List getUsersByGroupId(String groupdId);

   /**获取团队下的用户列表*/
   List getUsersByRoleId(long roleId);

   /**获取项目下的用户列表*/
   List getUsersByProjectId(long projectId);

   void add(User user);

   void update(User user);

   void remove(String account);

   User get(String account);

   User getByEmail(String email);

   /**获取组用户*/
   User getGroupUser(String groupId, String account);

   /**获取项目用户*/
   User getProjectUser(long projectId, String account);

   /**密码修改*/
   void changePassword(String account, String password);

   /**邮箱修改*/
   void changeEmail(String account, String email);

   UserQuery newQuery();
}
