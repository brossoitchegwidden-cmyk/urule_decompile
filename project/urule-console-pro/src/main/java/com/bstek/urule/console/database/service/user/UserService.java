package com.bstek.urule.console.database.service.user;

import com.bstek.urule.console.database.model.User;

/**用户服务类接口*/
public interface UserService {
   String BEAN_ID = "urule.userService";

   /**获取用户对象*/
   User get(String account);

   /**用户密码验证*/
   User validate(String account, String password);
}
