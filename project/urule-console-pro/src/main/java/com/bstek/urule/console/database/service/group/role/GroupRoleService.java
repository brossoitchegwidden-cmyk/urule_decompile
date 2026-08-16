package com.bstek.urule.console.database.service.group.role;

import com.bstek.urule.console.database.model.GroupRole;
import java.util.List;

public interface GroupRoleService {
   GroupRoleServiceImpl ins = new GroupRoleServiceImpl();

   List loadRoles(String var1) throws Exception;

   List loadUserRoles(String var1, String var2) throws Exception;

   List users(String var1, long var2) throws Exception;

   GroupRole get(long var1) throws Exception;

   void add(GroupRole var1) throws Exception;

   void update(GroupRole var1) throws Exception;

   void remove(Long var1) throws Exception;

   void addUserRole(String var1, String var2, long var3) throws Exception;

   void removeUserRole(String var1, String var2, long var3) throws Exception;
}
