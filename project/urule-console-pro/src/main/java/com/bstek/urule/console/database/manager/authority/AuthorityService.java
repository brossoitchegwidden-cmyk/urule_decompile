package com.bstek.urule.console.database.manager.authority;

import com.bstek.urule.console.database.model.Authority;
import com.bstek.urule.console.database.model.Role;
import java.util.List;

public interface AuthorityService {
   AuthorityService ins = new AuthorityServiceImpl();

   List getAuthoritysByRole(String var1, long var2);

   Authority get(String var1, long var2, String var4);

   void add(Authority var1);

   void remove(long var1);

   void removeByRole(String var1, long var2);

   List getGroupModels(Role var1);

   List getProjectModels(Role var1);

   void storePermissions(long var1, List var3);

   void initPermissions(long var1, List var3);
}
