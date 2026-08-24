package com.bstek.urule.console.database.manager.group.user;

import com.bstek.urule.console.database.model.Page;

public interface UserQuery {
   UserQuery idLike(String id);

   UserQuery nameLike(String name);

   UserQuery idnameLike(String idnameLike);

   /**查询用户列表*/
   Page users(int pageIndex, int pageSize, String groupId);

   /**查询用户列表*/
   Page roleUsers(int pageIndex, int pageSize, String groupId, long roleId);
}
