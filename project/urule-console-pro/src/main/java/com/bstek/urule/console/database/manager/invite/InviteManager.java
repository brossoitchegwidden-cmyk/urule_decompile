package com.bstek.urule.console.database.manager.invite;

import com.bstek.urule.console.database.model.Invite;

public interface InviteManager {
   InviteManagerImpl ins = new InviteManagerImpl();

   Invite get(String var1);

   void add(Invite var1);

   void update(Invite var1);

   void remove(String var1);

   void removeByGroupId(String var1);
}
