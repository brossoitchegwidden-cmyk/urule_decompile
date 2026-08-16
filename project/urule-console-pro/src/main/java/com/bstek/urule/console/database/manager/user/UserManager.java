package com.bstek.urule.console.database.manager.user;

import com.bstek.urule.console.database.model.User;
import java.util.List;

public interface UserManager {
   UserManagerImpl ins = new UserManagerImpl();

   List getUsersByGroupId(String var1);

   List getUsersByRoleId(long var1);

   List getUsersByProjectId(long var1);

   void add(User var1);

   void update(User var1);

   void remove(String var1);

   User get(String var1);

   User getByEmail(String var1);

   User getGroupUser(String var1, String var2);

   User getProjectUser(long var1, String var3);

   void changePassword(String var1, String var2);

   void changeEmail(String var1, String var2);

   UserQuery newQuery();
}
