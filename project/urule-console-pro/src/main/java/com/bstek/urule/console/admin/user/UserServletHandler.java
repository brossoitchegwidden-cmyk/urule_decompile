package com.bstek.urule.console.admin.user;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.IllegalOperationException;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.admin.RegisterInfo;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.anonymous.captcha.CaptchaBuilder;
import com.bstek.urule.console.config.Configure;
import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.invite.InviteManager;
import com.bstek.urule.console.database.model.Invite;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.service.group.GroupService;
import com.bstek.urule.console.database.service.user.PersistUserService;
import com.bstek.urule.console.database.service.user.UserServiceImpl;
import com.bstek.urule.console.database.service.user.UserServiceManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthAnonymous;
import com.bstek.urule.console.security.provider.SecurityProvider;
import com.bstek.urule.console.util.MailInfo;
import com.bstek.urule.console.util.MailUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserServletHandler extends ApiServletHandler {
   private static final Log logger = LogFactory.getLog(UserServletHandler.class);

   /**注册账号*/
   @Transactional
   @URuleAuthAnonymous
   public void register(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持动态注册用户！");
      } else {
         String captchResult = CaptchaBuilder.ins.getCaptchResult(req);
         if (StringUtils.isBlank(captchResult)) {
            throw new InfoException("验证码过期，请刷新页面重试<br>Captcha is Expired");
         } else {
            RegisterInfo registerInfo = (RegisterInfo)this.createObjectMapper().readValue(req.getParameter("register"), RegisterInfo.class);
            String captcha = registerInfo.getCaptcha();
            if (StringUtils.isBlank(captcha)) {
               throw new InfoException("验证码不能为空<br>Captcha can not be null");
            } else if (!captcha.contentEquals(captchResult)) {
               throw new InfoException("验证码不正确<br>Captcha is Invalid");
            } else {
               String account = registerInfo.getAccount();
               if (account.length() < 3) {
                  throw new InfoException("账号至少3个字符<br>Account must be at least three characters");
               } else if (StringUtils.hasChineseChar(account)) {
                  throw new InfoException("账号不能包含中文字符<br>The account cannot contain Chinese characters");
               } else if (registerInfo.getPassword().length() < 8) {
                  throw new InfoException("密码至少8个字符<br>Password must be at least three characters");
               } else {
                  User user = ((UserServiceImpl)UserServiceManager.getUserService()).get(account);
                  if (null != user) {
                     throw new InfoException("用户账号  " + account + "  已存在<br>Account is already exist.");
                  } else {
                     user = new User();
                     user.setId(registerInfo.getAccount());
                     user.setEnable(true);
                     user.setName(registerInfo.getUsername());
                     user.setPassword(registerInfo.getPassword());
                     user.setCreateUser(account);
                     ((UserServiceImpl)UserServiceManager.getUserService()).add(user);
                     CaptchaBuilder.ins.cleanCaptch(req);
                  }
               }
            }
         }
      }
   }

   @URuleAuthAnonymous
   public void login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("key");
      Invite invite = null;
      if (StringUtils.isNotBlank(parameter)) {
         invite = InviteManager.ins.get(parameter);
         if (invite == null || System.currentTimeMillis() - invite.getExpirDate().getTime() > 1800000L) {
            throw new InfoException("链接无效或已经过期,请重新获取链接!");
         }
      }

      SecurityProvider securityProvider = SecurityUtils.getSecurityProvider();
      boolean flag = Configure.getConfigure().getBoolean("urule.login.useCaptcha", true);
      if (!SecurityUtils.isCustomProvider() && flag) {
         String captchResult = CaptchaBuilder.ins.getCaptchResult(req);
         if (StringUtils.isBlank(captchResult)) {
            throw new InfoException("验证码过期，请刷新页面重试<br>Captcha is Expired");
         }

         String parameter2 = req.getParameter("captcha");
         if (StringUtils.isBlank(parameter2)) {
            throw new InfoException("验证码不能为空<br>Captcha can not be null");
         }

         if (!parameter2.contentEquals(captchResult)) {
            throw new InfoException("验证码不正确<br>Captcha is Invalid");
         }
      }

      String parameter3 = req.getParameter("account");
      String parameter4 = req.getParameter("password");

      try {
         HashMap valuesByKey = new HashMap();
         securityProvider.login(req, parameter3, parameter4);
         if (StringUtils.isNotBlank(parameter) && invite != null) {
            GroupService.ins.addGroupUser(invite.getGroupId(), parameter3);
            List items = GroupManager.ins.createQuery().list(SecurityUtils.getLoginUsername(req));
            com.bstek.urule.console.security.entity.User loginUser = SecurityUtils.getLoginUser(req);
            loginUser.setGroups(items);
         }

         SystemLogUtils.addLoginLog(RequestHolder.getRequest());
         valuesByKey.put("user", SecurityUtils.getLoginUser(req));
         UserServletHandler.logger.debug("登录成功,登录用户:" + parameter3);
         CaptchaBuilder.ins.cleanCaptch(req);
         this.writeObjectToJson(resp, valuesByKey);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   /**获取登录用户详细信息*/
   public void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      User user = UserServiceManager.getUserService().get(SecurityUtils.getLoginUsername(req));
      user.setPassword("******");
      this.writeObjectToJson(resp, user);
   }

   /**获取登录用户信息*/
   public void getUserInfo(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("user", SecurityUtils.getLoginUser(req));
      this.writeObjectToJson(resp, valuesByKey);
   }

   @URuleAuthAnonymous
   public void logout(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      SecurityUtils.getSecurityProvider().logout(req);
   }

   /**邮件找回密码*/
   @URuleAuthAnonymous
   public void forgetPass(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持密码找回！");
      } else {
         String parameter = req.getParameter("account");
         User user = ((UserServiceImpl)UserServiceManager.getUserService()).get(parameter);
         if (null == user) {
            throw new InfoException("Account not exist!");
         } else if (StringUtils.isBlank(user.getEmail())) {
            throw new InfoException("Mail is not bound!");
         } else {
            try {
               Timestamp timestamp = new Timestamp(System.currentTimeMillis() + 1800000L);
               if (user.getExpirDate() != null && timestamp.getTime() - user.getExpirDate().getTime() < 60000L) {
                  throw new InfoException("Operation not supported!");
               } else {
                  String text = RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10);
                  user.setSecretKey(text);
                  user.setExpirDate(timestamp);
                  user.setUpdateUser(parameter);
                  ((UserServiceImpl)UserServiceManager.getUserService()).update(user);
                  String text2 = "Retrieve Password";
                  String text3 = "<b>亲爱的用户:您好!</b><br/><b>您正在修改密码，请在验证码输入框中输入： " + text + "，以完成操作。</b><br/>";
                  text3 = text3 + "注意：此操作可能会修改您的密码。如非本人操作，请及时登录并修改密码以保证帐户安全 \n（工作人员不会向你索取此验证码，请勿泄漏！)";
                  text3 = text3 + "<hr/>";
                  text3 = text3 + "此为系统邮件，请勿回复\n请保管好您的邮箱，避免账号被他人盗用";
                  MailInfo mailInfo = new MailInfo();
                  mailInfo.setToAddress(user.getEmail());
                  mailInfo.setSubject(text2);
                  mailInfo.setContent(text3);

                  try {
                     MailUtils.sendHtmlMail(mailInfo);
                  } catch (Exception exception) {
                     throw new RuleException("'" + text2 + "' Mail sending failed！", exception);
                  }
               }
            } catch (Exception exception2) {
               throw new RuleException("Retrieve Password Error!", exception2);
            }
         }
      }
   }

   @URuleAuthAnonymous
   public void resetPass(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持重置密码！");
      } else {
         String parameter = req.getParameter("verifyCode");
         String parameter2 = req.getParameter("account");
         String parameter3 = req.getParameter("password");
         HashMap valuesByKey = new HashMap();
         if (!StringUtils.isBlank(parameter) && !StringUtils.isBlank(parameter2) && !StringUtils.isBlank(parameter3)) {
            User user = ((UserServiceImpl)UserServiceManager.getUserService()).get(parameter2);
            if (user == null) {
               throw new InfoException("无法找到匹配用户<br>Invalid user");
            } else if (!parameter3.equals(user.getPassword()) && parameter3.length() >= 6) {
               Date expirDate = user.getExpirDate();
               if (expirDate != null && expirDate.getTime() > System.currentTimeMillis()) {
                  user.setPassword(parameter3);
                  user.setExpirDate((Date)null);
                  user.setSecretKey((String)null);
                  user.setUpdateUser(parameter2);
                  ((UserServiceImpl)UserServiceManager.getUserService()).update(user);
                  UserServletHandler.logger.debug("[" + parameter2 + "]密码修改成功!");
                  this.writeObjectToJson(resp, valuesByKey);
               } else {
                  throw new InfoException("验证码已经过期,请重新获取验证码.<br>SecretKey is Expired");
               }
            } else {
               throw new InfoException("无效密码<br>Invalid password");
            }
         } else {
            throw new InfoException("重置信息不完整<br>Invalid info");
         }
      }
   }

   public void changeName(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持名称修改！");
      } else {
         String loginUsername = SecurityUtils.getLoginUsername(req);
         String parameter = req.getParameter("name");
         User user = ((UserServiceImpl)UserServiceManager.getUserService()).get(loginUsername);
         user.setName(parameter);
         com.bstek.urule.console.security.entity.User loginUser = SecurityUtils.getLoginUser(req);
         user.setUpdateUser(loginUser.getName());
         ((UserServiceImpl)UserServiceManager.getUserService()).update(user);
         SecurityUtils.getSecurityProvider().login(req, user.getId(), user.getPassword());
      }
   }

   public void changeEMail(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持邮箱修改！");
      } else {
         String loginUsername = SecurityUtils.getLoginUsername(req);
         String parameter = req.getParameter("email");
         User user = ((UserServiceImpl)UserServiceManager.getUserService()).get(loginUsername);
         user.setEmail(parameter);
         com.bstek.urule.console.security.entity.User loginUser = SecurityUtils.getLoginUser(req);
         user.setUpdateUser(loginUser.getName());
         ((UserServiceImpl)UserServiceManager.getUserService()).update(user);
      }
   }

   public void changePwd(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持密码修改！");
      } else {
         String loginUsername = SecurityUtils.getLoginUsername(req);
         String parameter = req.getParameter("oldpwd");
         String parameter2 = req.getParameter("newpwd");
         User user = ((UserServiceImpl)UserServiceManager.getUserService()).get(loginUsername);
         if (user.getPassword().equals(parameter)) {
            user.setPassword(parameter2);
            com.bstek.urule.console.security.entity.User loginUser = SecurityUtils.getLoginUser(req);
            user.setUpdateUser(loginUser.getName());
            ((UserServiceImpl)UserServiceManager.getUserService()).update(user);
         } else {
            throw new InfoException("密码错误<br>Invalid password");
         }
      }
   }

   public String url() {
      return "/user";
   }
}
