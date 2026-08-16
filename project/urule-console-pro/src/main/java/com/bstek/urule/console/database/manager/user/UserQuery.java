package com.bstek.urule.console.database.manager.user;

import com.bstek.urule.console.database.model.Page;

public interface UserQuery {
   UserQuery idLike(String var1);

   UserQuery nameLike(String var1);

   Page paging(int var1, int var2, long var3);

   Page paging(int var1, int var2, String var3);
}
