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
   String a = "_urule_login_user";

   public User getLoginUser(HttpServletRequest var1) {
      return (User)var1.getSession().getAttribute(this.a);
   }

   private User a(HttpServletRequest var1, String var2, String var3) {
      Object var4 = new ArrayList();
      com.bstek.urule.console.database.model.User var5 = UserServiceManager.getUserService().validate(var2, var3);
      if (UserServiceManager.isCustomUserService()) {
         HashMap var6 = new HashMap();
         if (var5.getGroups().size() > 0) {
            for(Group var8 : (Iterable<Group>)(Iterable<?>)(var5.getGroups())) {
               Group var9 = GroupManager.ins.get(var8.getId());
               if (var9 != null) {
                  ((List)var4).add(var9);
                  var6.put(var8.getId(), var9);
               }

               com.bstek.urule.console.database.model.User var10 = GroupManager.ins.getGroupUser(var8.getId(), var2);
               if (var10 == null && var9 != null) {
                  GroupService.ins.addGroupUser(var8.getId(), var2);
               }
            }
         }

         for(Group var13 : (Iterable<Group>)(Iterable<?>)(GroupManager.ins.createQuery().list(var5.getId()))) {
            if (!var6.containsKey(var13.getId())) {
               ((List)var4).add(var13);
            }
         }
      } else {
         var4 = var5.getGroups();
      }

      return new DefaultUser(var5.getId(), var5.getName(), (List)var4);
   }

   public void login(HttpServletRequest var1, String var2, String var3) {
      User var4 = this.a(var1, var2, var3);
      var1.getSession().setAttribute(this.a, var4);
   }

   public void logout(HttpServletRequest var1) {
      var1.getSession().removeAttribute(this.a);
   }
}
