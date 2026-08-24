package com.bstek.urule.console.admin.invite;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.IllegalOperationException;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.admin.RegisterInfo;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.invite.InviteManager;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.Invite;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.service.group.GroupService;
import com.bstek.urule.console.database.service.user.PersistUserService;
import com.bstek.urule.console.database.service.user.UserServiceImpl;
import com.bstek.urule.console.database.service.user.UserServiceManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthAnonymous;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.type.GroupModule;
import com.bstek.urule.console.util.StringUtils;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InviteServletHandler extends ApiServletHandler {
   private static final Log logger = LogFactory.getLog(InviteServletHandler.class);

   /**获取要加入的Group的信息*/
   @URuleAuthAnonymous
   public void group(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("key");
      Invite invite = InviteManager.ins.get(parameter);
      if (invite != null && System.currentTimeMillis() - invite.getExpirDate().getTime() <= 1800000L) {
         Group group = GroupManager.ins.get(invite.getGroupId());
         HashMap valuesByKey = new HashMap();
         valuesByKey.put("sendUser", invite.getCreateUser());
         valuesByKey.put("group", group);
         this.writeObjectToJson(resp, valuesByKey);
      } else {
         throw new InfoException("链接无效或已经过期,请重新获取链接!");
      }
   }

   /**生成具有时效性的邀请链接*/
   @URuleAuthorization(
      authType = "group",
      code = "join",
      model = "members"
   )
   public void url(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      Group group = GroupManager.ins.get(parameter);
      if (group == null) {
         throw new InfoException("团队不存在.");
      } else {
         HashMap valuesByKey = new HashMap();

         try {
            String text = RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis() + 1800000L);
            Invite invite = new Invite();
            invite.setGroupId(parameter);
            invite.setType("TIME");
            invite.setSecretKey(text);
            invite.setExpirDate(timestamp);
            invite.setCreateUser(SecurityUtils.getLoginUsername(req));
            InviteManager.ins.add(invite);
            String text2 = req.getRequestURL().toString();
            String text3 = text2.substring(0, text2.length() - "api/invite/url".length()) + "invite?key=" + text;
            valuesByKey.put("data", text3);
            SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "add", invite.getCreateUser(), String.format("User %s creates an invitation link", invite.getCreateUser()));
         } catch (Exception exception) {
            throw new InfoException("邀请链接生成失败！");
         }

         this.writeObjectToJson(resp, valuesByKey);
      }
   }

   /**根据邀请链接加入团队*/
   @URuleAuthAnonymous
   public void join(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("key");
      String parameter2 = req.getParameter("account");
      String parameter3 = req.getParameter("password");
      Invite invite = InviteManager.ins.get(parameter);
      if (invite != null && System.currentTimeMillis() - invite.getExpirDate().getTime() <= 1800000L) {
         GroupService.ins.addGroupUser(invite.getGroupId(), parameter2);
         HashMap valuesByKey = new HashMap();

         try {
            SecurityUtils.getSecurityProvider().login(req, parameter2, parameter3);
            SystemLogUtils.addLoginLog(RequestHolder.getRequest());
            valuesByKey.put("user", SecurityUtils.getLoginUser(req));
            InviteServletHandler.logger.debug("登录成功,登录用户:" + parameter2);
         } catch (Exception exception) {
            throw new InfoException(exception);
         }

         this.writeObjectToJson(resp, valuesByKey);
      } else {
         throw new InfoException("链接无效或已经过期,请重新获取链接!");
      }
   }

   /**注册账号并加入团队*/
   @Transactional
   @URuleAuthAnonymous
   public void register(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持动态注册用户！");
      } else {
         HashMap valuesByKey = new HashMap();
         RegisterInfo registerInfo = (RegisterInfo)this.createObjectMapper().readValue(req.getParameter("register"), RegisterInfo.class);
         String secretKey = registerInfo.getSecretKey();
         String account = registerInfo.getAccount();
         if (StringUtils.isBlank(secretKey)) {
            throw new InfoException("参数不合法.");
         } else {
            Invite invite = InviteManager.ins.get(secretKey);
            if (null == invite) {
               throw new InfoException("参数不合法.");
            } else if (System.currentTimeMillis() - invite.getExpirDate().getTime() > 1800000L) {
               throw new InfoException("链接无效或已经过期,请重新获取链接!");
            } else {
               User user = UserServiceManager.getUserService().get(account);
               if (null != user) {
                  throw new InfoException("用户" + account + "已经存在，请调整.");
               } else if (account.length() < 3) {
                  throw new InfoException("账号的至少3个字符<br>Account must be at least three characters");
               } else if (registerInfo.getPassword().length() < 8) {
                  throw new InfoException("密码的至少8个字符<br>Password must be at least three characters");
               } else {
                  user = new User();
                  user.setId(registerInfo.getAccount());
                  user.setEnable(true);
                  user.setName(registerInfo.getUsername());
                  user.setPassword(registerInfo.getPassword());
                  ((UserServiceImpl)UserServiceManager.getUserService()).add(user);
                  GroupService.ins.addGroupUser(invite.getGroupId(), registerInfo.getAccount());
                  SecurityUtils.getSecurityProvider().login(req, registerInfo.getAccount(), registerInfo.getPassword());
                  SystemLogUtils.addLoginLog(RequestHolder.getRequest());
                  valuesByKey.put("user", SecurityUtils.getLoginUser(req));
                  this.writeObjectToJson(resp, valuesByKey);
               }
            }
         }
      }
   }

   public String url() {
      return "/invite";
   }
}
