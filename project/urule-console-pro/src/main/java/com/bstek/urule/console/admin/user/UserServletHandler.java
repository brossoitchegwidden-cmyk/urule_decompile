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
   private static final Log e = LogFactory.getLog(UserServletHandler.class);

   @Transactional
   @URuleAuthAnonymous
   public void register(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持动态注册用户！");
      } else {
         String var3 = CaptchaBuilder.ins.getCaptchResult(var1);
         if (StringUtils.isBlank(var3)) {
            throw new InfoException("验证码过期，请刷新页面重试<br>Captcha is Expired");
         } else {
            RegisterInfo var4 = (RegisterInfo)this.a().readValue(var1.getParameter("register"), RegisterInfo.class);
            String var5 = var4.getCaptcha();
            if (StringUtils.isBlank(var5)) {
               throw new InfoException("验证码不能为空<br>Captcha can not be null");
            } else if (!var5.contentEquals(var3)) {
               throw new InfoException("验证码不正确<br>Captcha is Invalid");
            } else {
               String var6 = var4.getAccount();
               if (var6.length() < 3) {
                  throw new InfoException("账号至少3个字符<br>Account must be at least three characters");
               } else if (StringUtils.hasChineseChar(var6)) {
                  throw new InfoException("账号不能包含中文字符<br>The account cannot contain Chinese characters");
               } else if (var4.getPassword().length() < 8) {
                  throw new InfoException("密码至少8个字符<br>Password must be at least three characters");
               } else {
                  User var7 = ((UserServiceImpl)UserServiceManager.getUserService()).get(var6);
                  if (null != var7) {
                     throw new InfoException("用户账号  " + var6 + "  已存在<br>Account is already exist.");
                  } else {
                     var7 = new User();
                     var7.setId(var4.getAccount());
                     var7.setEnable(true);
                     var7.setName(var4.getUsername());
                     var7.setPassword(var4.getPassword());
                     var7.setCreateUser(var6);
                     ((UserServiceImpl)UserServiceManager.getUserService()).add(var7);
                     CaptchaBuilder.ins.cleanCaptch(var1);
                  }
               }
            }
         }
      }
   }

   @URuleAuthAnonymous
   public void login(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("key");
      Invite var4 = null;
      if (StringUtils.isNotBlank(var3)) {
         var4 = InviteManager.ins.get(var3);
         if (var4 == null || System.currentTimeMillis() - var4.getExpirDate().getTime() > 1800000L) {
            throw new InfoException("链接无效或已经过期,请重新获取链接!");
         }
      }

      SecurityProvider var5 = SecurityUtils.getSecurityProvider();
      boolean var6 = Configure.getConfigure().getBoolean("urule.login.useCaptcha", true);
      if (!SecurityUtils.isCustomProvider() && var6) {
         String var7 = CaptchaBuilder.ins.getCaptchResult(var1);
         if (StringUtils.isBlank(var7)) {
            throw new InfoException("验证码过期，请刷新页面重试<br>Captcha is Expired");
         }

         String var8 = var1.getParameter("captcha");
         if (StringUtils.isBlank(var8)) {
            throw new InfoException("验证码不能为空<br>Captcha can not be null");
         }

         if (!var8.contentEquals(var7)) {
            throw new InfoException("验证码不正确<br>Captcha is Invalid");
         }
      }

      String var13 = var1.getParameter("account");
      String var14 = var1.getParameter("password");

      try {
         HashMap var9 = new HashMap();
         var5.login(var1, var13, var14);
         if (StringUtils.isNotBlank(var3) && var4 != null) {
            GroupService.ins.addGroupUser(var4.getGroupId(), var13);
            List var10 = GroupManager.ins.createQuery().list(SecurityUtils.getLoginUsername(var1));
            com.bstek.urule.console.security.entity.User var11 = SecurityUtils.getLoginUser(var1);
            var11.setGroups(var10);
         }

         SystemLogUtils.addLoginLog(RequestHolder.getRequest());
         var9.put("user", SecurityUtils.getLoginUser(var1));
         e.debug("登录成功,登录用户:" + var13);
         CaptchaBuilder.ins.cleanCaptch(var1);
         this.a(var2, var9);
      } catch (Exception var12) {
         throw new RuleException(var12);
      }
   }

   public void get(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      User var3 = UserServiceManager.getUserService().get(SecurityUtils.getLoginUsername(var1));
      var3.setPassword("******");
      this.a(var2, var3);
   }

   public void getUserInfo(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      HashMap var3 = new HashMap();
      var3.put("user", SecurityUtils.getLoginUser(var1));
      this.a(var2, var3);
   }

   @URuleAuthAnonymous
   public void logout(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      SecurityUtils.getSecurityProvider().logout(var1);
   }

   @URuleAuthAnonymous
   public void forgetPass(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持密码找回！");
      } else {
         String var3 = var1.getParameter("account");
         User var4 = ((UserServiceImpl)UserServiceManager.getUserService()).get(var3);
         if (null == var4) {
            throw new InfoException("Account not exist!");
         } else if (StringUtils.isBlank(var4.getEmail())) {
            throw new InfoException("Mail is not bound!");
         } else {
            try {
               Timestamp var5 = new Timestamp(System.currentTimeMillis() + 1800000L);
               if (var4.getExpirDate() != null && var5.getTime() - var4.getExpirDate().getTime() < 60000L) {
                  throw new InfoException("Operation not supported!");
               } else {
                  String var6 = RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10);
                  var4.setSecretKey(var6);
                  var4.setExpirDate(var5);
                  var4.setUpdateUser(var3);
                  ((UserServiceImpl)UserServiceManager.getUserService()).update(var4);
                  String var7 = "Retrieve Password";
                  String var8 = "<b>亲爱的用户:您好!</b><br/><b>您正在修改密码，请在验证码输入框中输入： " + var6 + "，以完成操作。</b><br/>";
                  var8 = var8 + "注意：此操作可能会修改您的密码。如非本人操作，请及时登录并修改密码以保证帐户安全 \n（工作人员不会向你索取此验证码，请勿泄漏！)";
                  var8 = var8 + "<hr/>";
                  var8 = var8 + "此为系统邮件，请勿回复\n请保管好您的邮箱，避免账号被他人盗用";
                  MailInfo var9 = new MailInfo();
                  var9.setToAddress(var4.getEmail());
                  var9.setSubject(var7);
                  var9.setContent(var8);

                  try {
                     MailUtils.sendHtmlMail(var9);
                  } catch (Exception var11) {
                     throw new RuleException("'" + var7 + "' Mail sending failed！", var11);
                  }
               }
            } catch (Exception var12) {
               throw new RuleException("Retrieve Password Error!", var12);
            }
         }
      }
   }

   @URuleAuthAnonymous
   public void resetPass(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持重置密码！");
      } else {
         String var3 = var1.getParameter("verifyCode");
         String var4 = var1.getParameter("account");
         String var5 = var1.getParameter("password");
         HashMap var6 = new HashMap();
         if (!StringUtils.isBlank(var3) && !StringUtils.isBlank(var4) && !StringUtils.isBlank(var5)) {
            User var7 = ((UserServiceImpl)UserServiceManager.getUserService()).get(var4);
            if (var7 == null) {
               throw new InfoException("无法找到匹配用户<br>Invalid user");
            } else if (!var5.equals(var7.getPassword()) && var5.length() >= 6) {
               Date var8 = var7.getExpirDate();
               if (var8 != null && var8.getTime() > System.currentTimeMillis()) {
                  var7.setPassword(var5);
                  var7.setExpirDate((Date)null);
                  var7.setSecretKey((String)null);
                  var7.setUpdateUser(var4);
                  ((UserServiceImpl)UserServiceManager.getUserService()).update(var7);
                  e.debug("[" + var4 + "]密码修改成功!");
                  this.a(var2, var6);
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

   public void changeName(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持名称修改！");
      } else {
         String var3 = SecurityUtils.getLoginUsername(var1);
         String var4 = var1.getParameter("name");
         User var5 = ((UserServiceImpl)UserServiceManager.getUserService()).get(var3);
         var5.setName(var4);
         com.bstek.urule.console.security.entity.User var6 = SecurityUtils.getLoginUser(var1);
         var5.setUpdateUser(var6.getName());
         ((UserServiceImpl)UserServiceManager.getUserService()).update(var5);
         SecurityUtils.getSecurityProvider().login(var1, var5.getId(), var5.getPassword());
      }
   }

   public void changeEMail(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持邮箱修改！");
      } else {
         String var3 = SecurityUtils.getLoginUsername(var1);
         String var4 = var1.getParameter("email");
         User var5 = ((UserServiceImpl)UserServiceManager.getUserService()).get(var3);
         var5.setEmail(var4);
         com.bstek.urule.console.security.entity.User var6 = SecurityUtils.getLoginUser(var1);
         var5.setUpdateUser(var6.getName());
         ((UserServiceImpl)UserServiceManager.getUserService()).update(var5);
      }
   }

   public void changePwd(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持密码修改！");
      } else {
         String var3 = SecurityUtils.getLoginUsername(var1);
         String var4 = var1.getParameter("oldpwd");
         String var5 = var1.getParameter("newpwd");
         User var6 = ((UserServiceImpl)UserServiceManager.getUserService()).get(var3);
         if (var6.getPassword().equals(var4)) {
            var6.setPassword(var5);
            com.bstek.urule.console.security.entity.User var7 = SecurityUtils.getLoginUser(var1);
            var6.setUpdateUser(var7.getName());
            ((UserServiceImpl)UserServiceManager.getUserService()).update(var6);
         } else {
            throw new InfoException("密码错误<br>Invalid password");
         }
      }
   }

   public String url() {
      return "/user";
   }
}
