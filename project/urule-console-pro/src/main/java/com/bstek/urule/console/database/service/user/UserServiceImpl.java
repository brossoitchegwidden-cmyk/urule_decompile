package com.bstek.urule.console.database.service.user;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.user.UserManager;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.util.List;

/**默认用户服务类*/
public class UserServiceImpl implements PersistUserService {
   public void add(User user) {
      UserManager.ins.add(user);
   }
   public void update(User user) {
      UserManager.ins.update(user);
   }
   public User get(String account) {
      return UserManager.ins.get(account);
   }
   public void remove(User user) {
      UserManager.ins.remove(user.getId());
   }
   public void changeEmail(String account, String email) {
      UserManager.ins.changeEmail(account, email);
   }
   public void changePassword(String account, String password) {
      UserManager.ins.changePassword(account, password);
   }
   public User validate(String account, String password) {
      User user = null;

      try {
         if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(password)) {
            user = this.get(account);
            if (user == null) {
               throw new InfoException("账号或密码错误!<br>Username or Password is invalid.");
            } else if (!password.equals(user.getPassword())) {
               throw new InfoException("账号或密码错误!<br>Username or Password is invalid.");
            } else {
               List items = GroupManager.ins.createQuery().list(user.getId());
               user.setGroups(items);
               return user;
            }
         } else {
            throw new InfoException("账号和密码不能为空!<br>Username and Password can not be null.");
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
}
