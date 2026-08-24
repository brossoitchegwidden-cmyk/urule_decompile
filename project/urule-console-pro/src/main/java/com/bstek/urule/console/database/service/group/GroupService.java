package com.bstek.urule.console.database.service.group;

import com.bstek.urule.console.database.model.Group;

public interface GroupService {
   GroupService ins = new GroupServiceImpl();

   /**获取Group对象*/
   Group get(String groupId);

   /**添加Group对象*/
   void add(Group group);

   /**删除团队对象*/
   void remove(String groupId);

   /**添加团队用户*/
   void addGroupUser(String groupId, String account);

   /**删除团队用户,并清理用户对应的角色配置信息*/
   void removeGroupUser(String groupId, String account);

   /**是否开放团队创建*/
   boolean isFreeCreate(String account);
}
