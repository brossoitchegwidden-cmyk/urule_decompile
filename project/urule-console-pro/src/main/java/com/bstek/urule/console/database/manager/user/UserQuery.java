package com.bstek.urule.console.database.manager.user;

import com.bstek.urule.console.database.model.Page;

public interface UserQuery {
   UserQuery idLike(String userId);

   UserQuery nameLike(String name);

   Page paging(int pageIndex, int pageSize, long projectId);

   Page paging(int pageIndex, int pageSize, String groupId);
}
