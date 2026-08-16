package com.bstek.urule.console.database.service.user;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.user.UserManager;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.util.List;

public class UserServiceImpl implements PersistUserService {
   public void add(User var1) {
      UserManager.ins.add(var1);
   }

   public void update(User var1) {
      UserManager.ins.update(var1);
   }

   public User get(String var1) {
      return UserManager.ins.get(var1);
   }

   public void remove(User var1) {
      UserManager.ins.remove(var1.getId());
   }

   public void changeEmail(String var1, String var2) {
      UserManager.ins.changeEmail(var1, var2);
   }

   public void changePassword(String var1, String var2) {
      UserManager.ins.changePassword(var1, var2);
   }

   public User validate(String var1, String var2) {
      User var3 = null;

      try {
         if (!StringUtils.isEmpty(var1) && !StringUtils.isEmpty(var2)) {
            var3 = this.get(var1);
            if (var3 == null) {
               throw new InfoException("账号或密码错误!<br>Username or Password is invalid.");
            } else if (!var2.equals(var3.getPassword())) {
               throw new InfoException("账号或密码错误!<br>Username or Password is invalid.");
            } else {
               List var4 = GroupManager.ins.createQuery().list(var3.getId());
               var3.setGroups(var4);
               return var3;
            }
         } else {
            throw new InfoException("账号和密码不能为空!<br>Username and Password can not be null.");
         }
      } catch (Exception var5) {
         throw new RuleException(var5);
      }
   }
}
