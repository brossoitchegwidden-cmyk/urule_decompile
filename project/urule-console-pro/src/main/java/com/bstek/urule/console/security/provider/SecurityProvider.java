package com.bstek.urule.console.security.provider;

import com.bstek.urule.console.security.entity.User;
import javax.servlet.http.HttpServletRequest;

public interface SecurityProvider {
   String BEAN_ID = "urule.securityProvider";

   User getLoginUser(HttpServletRequest var1);

   void login(HttpServletRequest var1, String var2, String var3);

   void logout(HttpServletRequest var1);
}
