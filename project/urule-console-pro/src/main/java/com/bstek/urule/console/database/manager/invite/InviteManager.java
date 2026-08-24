package com.bstek.urule.console.database.manager.invite;

import com.bstek.urule.console.database.model.Invite;

public interface InviteManager {
   InviteManagerImpl ins = new InviteManagerImpl();

   Invite get(String secretKey);

   void add(Invite invite);

   void update(Invite invite);

   void remove(String secretKey);

   void removeByGroupId(String groupId);
}
