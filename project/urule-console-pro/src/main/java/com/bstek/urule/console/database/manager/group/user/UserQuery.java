package com.bstek.urule.console.database.manager.group.user;

import com.bstek.urule.console.database.model.Page;

public interface UserQuery {
   UserQuery idLike(String var1);

   UserQuery nameLike(String var1);

   UserQuery idnameLike(String var1);

   Page users(int var1, int var2, String var3);

   Page roleUsers(int var1, int var2, String var3, long var4);
}
