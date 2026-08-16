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
   private static final Log e = LogFactory.getLog(InviteServletHandler.class);

   @URuleAuthAnonymous
   public void group(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("key");
      Invite var4 = InviteManager.ins.get(var3);
      if (var4 != null && System.currentTimeMillis() - var4.getExpirDate().getTime() <= 1800000L) {
         Group var5 = GroupManager.ins.get(var4.getGroupId());
         HashMap var6 = new HashMap();
         var6.put("sendUser", var4.getCreateUser());
         var6.put("group", var5);
         this.a(var2, var6);
      } else {
         throw new InfoException("链接无效或已经过期,请重新获取链接!");
      }
   }

   @URuleAuthorization(
      authType = "group",
      code = "join",
      model = "members"
   )
   public void url(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      Group var4 = GroupManager.ins.get(var3);
      if (var4 == null) {
         throw new InfoException("团队不存在.");
      } else {
         HashMap var5 = new HashMap();

         try {
            String var6 = RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10) + "" + RandomUtils.nextInt(10);
            Timestamp var7 = new Timestamp(System.currentTimeMillis() + 1800000L);
            Invite var8 = new Invite();
            var8.setGroupId(var3);
            var8.setType("TIME");
            var8.setSecretKey(var6);
            var8.setExpirDate(var7);
            var8.setCreateUser(SecurityUtils.getLoginUsername(var1));
            InviteManager.ins.add(var8);
            String var9 = var1.getRequestURL().toString();
            String var10 = var9.substring(0, var9.length() - "api/invite/url".length()) + "invite?key=" + var6;
            var5.put("data", var10);
            SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "add", var8.getCreateUser(), String.format("User %s creates an invitation link", var8.getCreateUser()));
         } catch (Exception var11) {
            throw new InfoException("邀请链接生成失败！");
         }

         this.a(var2, var5);
      }
   }

   @URuleAuthAnonymous
   public void join(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("key");
      String var4 = var1.getParameter("account");
      String var5 = var1.getParameter("password");
      Invite var6 = InviteManager.ins.get(var3);
      if (var6 != null && System.currentTimeMillis() - var6.getExpirDate().getTime() <= 1800000L) {
         GroupService.ins.addGroupUser(var6.getGroupId(), var4);
         HashMap var7 = new HashMap();

         try {
            SecurityUtils.getSecurityProvider().login(var1, var4, var5);
            SystemLogUtils.addLoginLog(RequestHolder.getRequest());
            var7.put("user", SecurityUtils.getLoginUser(var1));
            e.debug("登录成功,登录用户:" + var4);
         } catch (Exception var9) {
            throw new InfoException(var9);
         }

         this.a(var2, var7);
      } else {
         throw new InfoException("链接无效或已经过期,请重新获取链接!");
      }
   }

   @Transactional
   @URuleAuthAnonymous
   public void register(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (!(UserServiceManager.getUserService() instanceof PersistUserService)) {
         throw new IllegalOperationException("当前用户服务类不支持动态注册用户！");
      } else {
         HashMap var3 = new HashMap();
         RegisterInfo var4 = (RegisterInfo)this.a().readValue(var1.getParameter("register"), RegisterInfo.class);
         String var5 = var4.getSecretKey();
         String var6 = var4.getAccount();
         if (StringUtils.isBlank(var5)) {
            throw new InfoException("参数不合法.");
         } else {
            Invite var7 = InviteManager.ins.get(var5);
            if (null == var7) {
               throw new InfoException("参数不合法.");
            } else if (System.currentTimeMillis() - var7.getExpirDate().getTime() > 1800000L) {
               throw new InfoException("链接无效或已经过期,请重新获取链接!");
            } else {
               User var8 = UserServiceManager.getUserService().get(var6);
               if (null != var8) {
                  throw new InfoException("用户" + var6 + "已经存在，请调整.");
               } else if (var6.length() < 3) {
                  throw new InfoException("账号的至少3个字符<br>Account must be at least three characters");
               } else if (var4.getPassword().length() < 8) {
                  throw new InfoException("密码的至少8个字符<br>Password must be at least three characters");
               } else {
                  var8 = new User();
                  var8.setId(var4.getAccount());
                  var8.setEnable(true);
                  var8.setName(var4.getUsername());
                  var8.setPassword(var4.getPassword());
                  ((UserServiceImpl)UserServiceManager.getUserService()).add(var8);
                  GroupService.ins.addGroupUser(var7.getGroupId(), var4.getAccount());
                  SecurityUtils.getSecurityProvider().login(var1, var4.getAccount(), var4.getPassword());
                  SystemLogUtils.addLoginLog(RequestHolder.getRequest());
                  var3.put("user", SecurityUtils.getLoginUser(var1));
                  this.a(var2, var3);
               }
            }
         }
      }
   }

   public String url() {
      return "/invite";
   }
}
