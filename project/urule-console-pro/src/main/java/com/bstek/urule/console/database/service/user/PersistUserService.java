package com.bstek.urule.console.database.service.user;

import com.bstek.urule.console.database.model.User;

/**用户服务类接口,支持用户对象持久化操作*/
public interface PersistUserService extends UserService {
   /**插入用户对象 如果不支持该操作建议抛出异常com.bstek.urule.exception.RuleException*/
   void add(User user);

   /**更新用户对象 如果不支持该操作建议抛出异常com.bstek.urule.exception.RuleException*/
   void update(User user);

   /**删除用户对象 如果不支持该操作建议抛出异常com.bstek.urule.exception.RuleException*/
   void remove(User user);

   /**邮箱修改 如果不支持该操作建议抛出异常com.bstek.urule.exception.RuleException*/
   void changeEmail(String account, String email);

   /**更改密码 如果不支持该操作建议抛出异常com.bstek.urule.exception.RuleException*/
   void changePassword(String account, String password);
}
