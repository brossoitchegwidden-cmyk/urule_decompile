package com.bstek.urule.console.security.provider;

import com.bstek.urule.console.security.entity.User;
import javax.servlet.http.HttpServletRequest;

public interface SecurityProvider {
   String BEAN_ID = "urule.securityProvider";

   /**获取登录用户对象,同时需要初始化用户的团队信息*/
   User getLoginUser(HttpServletRequest req);

   /**执行登录操作*/
   void login(HttpServletRequest req, String account, String password);

   /**执行登出操作*/
   void logout(HttpServletRequest req);
}
