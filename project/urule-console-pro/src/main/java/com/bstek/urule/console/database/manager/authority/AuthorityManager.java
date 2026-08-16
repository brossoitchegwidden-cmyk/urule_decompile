package com.bstek.urule.console.database.manager.authority;

import com.bstek.urule.console.database.model.Authority;
import java.sql.Connection;
import java.util.List;

public interface AuthorityManager {
   AuthorityManager ins = new AuthorityManagerImpl();

   List getAuthoritysByCode(String var1, String var2);

   List getAuthoritysByRole(String var1, long var2);

   Authority get(String var1, long var2, String var4);

   void add(Connection var1, Authority var2);

   void remove(Connection var1, long var2);

   void remove(Connection var1, String var2, long var3, String var5, String var6);

   void removeByRole(String var1, long var2);
}
