package com.bstek.urule.console.database.manager.group;

import java.util.List;

public interface GroupQuery {
   GroupQuery name(String name);

   /**查询用户相关的团队列表*/
   List list(String userId);

   /**查询团队列表*/
   List list();
}
