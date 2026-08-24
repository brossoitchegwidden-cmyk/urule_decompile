package com.bstek.urule.console.security.provider;

import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.service.group.GroupService;
import com.bstek.urule.console.database.service.user.UserServiceManager;
import com.bstek.urule.console.security.entity.DefaultUser;
import com.bstek.urule.console.security.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class DefaultSecurityProvider implements SecurityProvider {
   static final String LOGIN_USER_SESSION_KEY = "_urule_login_user";
   public User getLoginUser(HttpServletRequest req) {
      return (User)req.getSession().getAttribute(LOGIN_USER_SESSION_KEY);
   }

   private User resolveUser(HttpServletRequest httpServletRequest, String text, String text2) {
      Object groups = new ArrayList();
      com.bstek.urule.console.database.model.User user = UserServiceManager.getUserService().validate(text, text2);
      if (UserServiceManager.isCustomUserService()) {
         HashMap valuesByKey = new HashMap();
         if (user.getGroups().size() > 0) {
            for(Group group : (Iterable<Group>)(Iterable<?>)(user.getGroups())) {
               Group group2 = GroupManager.ins.get(group.getId());
               if (group2 != null) {
                  ((List)groups).add(group2);
                  valuesByKey.put(group.getId(), group2);
               }

               com.bstek.urule.console.database.model.User groupUser = GroupManager.ins.getGroupUser(group.getId(), text);
               if (groupUser == null && group2 != null) {
                  GroupService.ins.addGroupUser(group.getId(), text);
               }
            }
         }

         for(Group group3 : (Iterable<Group>)(Iterable<?>)(GroupManager.ins.createQuery().list(user.getId()))) {
            if (!valuesByKey.containsKey(group3.getId())) {
               ((List)groups).add(group3);
            }
         }
      } else {
         groups = user.getGroups();
      }

      return new DefaultUser(user.getId(), user.getName(), (List)groups);
   }
   public void login(HttpServletRequest req, String account, String password) {
      User user = this.resolveUser(req, account, password);
      req.getSession().setAttribute(LOGIN_USER_SESSION_KEY, user);
   }
   public void logout(HttpServletRequest req) {
      req.getSession().removeAttribute(LOGIN_USER_SESSION_KEY);
   }
}
