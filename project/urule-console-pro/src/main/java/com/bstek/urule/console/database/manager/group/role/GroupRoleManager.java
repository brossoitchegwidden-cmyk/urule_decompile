package com.bstek.urule.console.database.manager.group.role;

import com.bstek.urule.console.database.model.GroupRole;
import com.bstek.urule.console.database.model.UserRole;
import java.util.List;

public interface GroupRoleManager {
   GroupRoleManagerImpl ins = new GroupRoleManagerImpl();

   List loadRoles(String var1);

   List loadUserRoles(String var1, String var2);

   List loadRoleUsers(String var1, long var2);

   GroupRole get(long var1);

   GroupRole get(String var1, String var2);

   void add(GroupRole var1);

   void update(GroupRole var1);

   void remove(Long var1);

   void removeByGroupId(String var1);

   boolean checkExist(String var1, String var2);

   UserRole getUserRole(String var1, long var2);

   void addUserRole(String var1, String var2, long var3);

   void removeUserRole(String var1, String var2, long var3);

   void removeRoleUsers(long var1);

   void removeUserRoles(String var1, String var2);
}
