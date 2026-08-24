package com.bstek.urule.console.security.entity;

import java.util.List;

public interface User {
   /**获取用户账号*/
   String getName();

   /**获取用户描述*/
   String getDesc();

   /**获取用户所属团队列表*/
   List getGroups();

   /**设置用户的团队列表*/
   void setGroups(List groups);
}
