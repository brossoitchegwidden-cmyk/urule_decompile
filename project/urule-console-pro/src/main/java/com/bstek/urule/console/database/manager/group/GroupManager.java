package com.bstek.urule.console.database.manager.group;

import com.bstek.urule.console.database.manager.group.user.UserQuery;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.User;
import java.util.List;

public interface GroupManager {
   GroupManagerImpl ins = new GroupManagerImpl();

   Group get(String var1);

   void add(Group var1);

   void update(Group var1);

   void remove(String var1);

   User getGroupUser(String var1, String var2);

   void addGroupUser(String var1, String var2, String var3);

   void removeGroupUser(String var1, String var2);

   void removeGroupUsers(String var1);

   List getUsers(String var1);

   int count();

   GroupQuery createQuery();

   UserQuery createUserQuery();
}
