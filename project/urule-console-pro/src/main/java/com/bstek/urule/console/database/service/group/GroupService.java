package com.bstek.urule.console.database.service.group;

import com.bstek.urule.console.database.model.Group;

public interface GroupService {
   GroupService ins = new GroupServiceImpl();

   Group get(String var1);

   void add(Group var1);

   void remove(String var1);

   void addGroupUser(String var1, String var2);

   void removeGroupUser(String var1, String var2);

   boolean isFreeCreate(String var1);
}
