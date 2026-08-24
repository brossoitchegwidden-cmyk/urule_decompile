package com.bstek.urule.console.database.manager.group;

import com.bstek.urule.console.database.manager.group.user.UserQuery;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.User;
import java.util.List;

public interface GroupManager {
   GroupManagerImpl ins = new GroupManagerImpl();

   /**获取Group对象*/
   Group get(String id);

   /**新增Group*/
   void add(Group group);

   /**更新Group*/
   void update(Group group);

   /**删除Group*/
   void remove(String id);

   /**获取Group用户*/
   User getGroupUser(String groupId, String account);

   /**新增Group用户*/
   void addGroupUser(String groupId, String account, String username);

   /**删除Group用户*/
   void removeGroupUser(String groupId, String account);

   /**删除Group的所用用户*/
   void removeGroupUsers(String groupId);

   /**查询Group的用户列表*/
   List getUsers(String groupId);

   /**获取总团队数量*/
   int count();

   GroupQuery createQuery();

   UserQuery createUserQuery();
}
