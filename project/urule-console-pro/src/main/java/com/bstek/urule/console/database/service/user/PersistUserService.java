package com.bstek.urule.console.database.service.user;

import com.bstek.urule.console.database.model.User;

public interface PersistUserService extends UserService {
   void add(User var1);

   void update(User var1);

   void remove(User var1);

   void changeEmail(String var1, String var2);

   void changePassword(String var1, String var2);
}
