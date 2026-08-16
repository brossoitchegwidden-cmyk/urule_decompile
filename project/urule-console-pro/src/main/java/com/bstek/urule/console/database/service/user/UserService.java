package com.bstek.urule.console.database.service.user;

import com.bstek.urule.console.database.model.User;

public interface UserService {
   String BEAN_ID = "urule.userService";

   User get(String var1);

   User validate(String var1, String var2);
}
