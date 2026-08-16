package com.bstek.urule.console.admin.group;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.IllegalOperationException;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.ParameterInvaidException;
import com.bstek.urule.console.PermissionDeniedException;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.TransactionalInvoke;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketCacheImpl;
import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.group.role.GroupRoleManager;
import com.bstek.urule.console.database.manager.group.user.UserQuery;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.manager.project.ProjectQuery;
import com.bstek.urule.console.database.manager.report.ReportGroupQuery;
import com.bstek.urule.console.database.manager.user.UserManager;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.GroupRole;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.service.group.GroupService;
import com.bstek.urule.console.database.service.group.role.GroupRoleService;
import com.bstek.urule.console.security.AuthenticationManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthAnonymous;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.type.GroupModule;
import com.bstek.urule.console.type.GroupRoleEnum;
import com.bstek.urule.console.type.ProjectModule;
import com.bstek.urule.console.type.RoleCategory;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

public class GroupServletHandler extends ApiServletHandler {
   public void list(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = SecurityUtils.getLoginUsername(var1);
      this.a(var2, GroupManager.ins.createQuery().list(var3));
   }

   @Transactional
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Group var3 = (Group)this.a().readValue(var1.getParameter("group"), Group.class);
      if (var3.getId().length() < 3) {
         throw new InfoException("团队ID的至少3个字符<br>Team id must be at least three characters");
      } else {
         var3.setCreateUser(SecurityUtils.getLoginUsername(var1));
         GroupService.ins.add(var3);
         List var4 = GroupManager.ins.createQuery().list(SecurityUtils.getLoginUsername(var1));
         User var5 = SecurityUtils.getLoginUser(var1);
         var5.setGroups(var4);
         this.a(var2, var3);
      }
   }

   public void remove(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      final String var3 = var1.getParameter("groupId");
      Group var4 = GroupManager.ins.get(var3);
      if (!SecurityUtils.getLoginUsername(var1).equals(var4.getCreateUser())) {
         throw new PermissionDeniedException("Permission denied for team [" + var3 + "]");
      } else {
         this.a(new TransactionalInvoke() {
            public void doTransactional() {
               GroupService.ins.remove(var3);
            }
         });
         List var5 = GroupManager.ins.createQuery().list(SecurityUtils.getLoginUsername(var1));
         User var6 = SecurityUtils.getLoginUser(var1);
         var6.setGroups(var5);
         List var7 = ((PacketCacheImpl)PacketCache.ins).recacheAllPackets(var3);
         this.a(var2, var7);
      }
   }

   public void update(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Group var3 = (Group)this.a().readValue(var1.getParameter("group"), Group.class);
      if (var3.getId().equals(ContextHolder.getGroupId())) {
         Group var4 = GroupManager.ins.get(var3.getId());
         if (!SecurityUtils.getLoginUsername(var1).equals(var3.getCreateUser())) {
            throw new PermissionDeniedException("Permission denied for team [" + var3.getId() + "]");
         } else {
            var4.setName(var3.getName());
            var4.setDesc(var3.getDesc());
            var4.setUpdateUser(SecurityUtils.getLoginUsername(var1));
            GroupManager.ins.update(var4);
            this.a(var2, var4);
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   public void get(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      Group var4 = GroupManager.ins.get(var3);
      this.a(var2, var4);
   }

   @URuleAuthAnonymous
   public void freeCreate(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      this.a(var2, GroupService.ins.isFreeCreate(SecurityUtils.getLoginUsername(var1)));
   }

   public void roles(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      this.a(var2, GroupRoleService.ins.loadRoles(var3));
   }

   public void userRoles(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      String var4 = var1.getParameter("account");
      this.a(var2, GroupRoleService.ins.loadUserRoles(var3, var4));
   }

   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "permissions"
   )
   public void addRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("roleName");
      if (StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         GroupRole var4 = new GroupRole();
         var4.setName(var3);
         var4.setGroupId(ContextHolder.getGroupId());
         var4.setType("custom");
         GroupRoleService.ins.add(var4);
         this.a(var2, var4);
         SystemLogUtils.addGroupOperationLog(GroupModule.permissions.name(), "manager", var4.getId(), String.format("Create role %s[%s]", var4.getName(), var4.getId()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "permissions"
   )
   public void renameRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("roleId"));
      String var5 = var1.getParameter("roleName");
      GroupRole var6 = GroupRoleService.ins.get(var3);
      if (var6.getGroupId().equals(ContextHolder.getGroupId())) {
         String var7 = var6.getName();
         var6.setName(var5);
         GroupRoleService.ins.update(var6);
         SystemLogUtils.addGroupOperationLog(GroupModule.permissions.name(), "manager", var6.getId(), String.format("Role name [%s] updated to [%s]", var7, var5));
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "permissions"
   )
   public void removeRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("roleId"));
      GroupRole var5 = GroupRoleManager.ins.get(var3);
      if (var5.getGroupId().equals(ContextHolder.getGroupId())) {
         GroupRoleService.ins.remove(var3);
         SystemLogUtils.addGroupOperationLog(GroupModule.permissions.name(), "manager", var5.getId(), String.format("Remove role %s[%s]", var5.getName(), var5.getId()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "group",
      model = "members",
      code = "add"
   )
   @Transactional
   public void addUser(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      com.bstek.urule.console.database.model.User var3 = (com.bstek.urule.console.database.model.User)this.a().readValue(var1.getParameter("user"), com.bstek.urule.console.database.model.User.class);
      String var4 = var3.getId();
      if (StringUtils.isNotBlank(ContextHolder.getGroupId()) && StringUtils.isNotBlank(var4)) {
         Group var5 = GroupManager.ins.get(ContextHolder.getGroupId());
         if (var4.equals(var5.getCreateUser())) {
            throw new IllegalOperationException("该账号是团队管理员,无法重复添加!");
         } else {
            com.bstek.urule.console.database.model.User var6 = UserManager.ins.get(var4);
            if (var6 != null) {
               throw new IllegalOperationException("该账号以存在,无法重复添加!");
            } else {
               var3.setEnable(true);
               var3.setCreateUser(SecurityUtils.getLoginUsername(var1));
               UserManager.ins.add(var3);
               GroupService.ins.addGroupUser(ContextHolder.getGroupId(), var4);
               GroupRole var7 = GroupRoleManager.ins.get(ContextHolder.getGroupId(), GroupRoleEnum.User.name());
               GroupRoleService.ins.addUserRole(ContextHolder.getGroupId(), var4, var7.getId());
               SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "add", var4, String.format("Add team member %s", var4));
            }
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "group",
      model = "members",
      code = "add"
   )
   @Transactional
   public void addUserByAccount(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("userId");
      if (StringUtils.isNotBlank(var3) && StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         Group var4 = GroupManager.ins.get(ContextHolder.getGroupId());
         if (var3.equals(var4.getCreateUser())) {
            throw new RuleException("不能添加团队管理员账号!<br>Illegal Operation! The account is group owner.");
         } else {
            com.bstek.urule.console.database.model.User var5 = UserManager.ins.get(var3);
            if (var5 == null) {
               throw new RuleException("用户账号不存在!<br>Account does not exist!");
            } else {
               com.bstek.urule.console.database.model.User var6 = GroupManager.ins.getGroupUser(var3, var3);
               if (var6 != null) {
                  throw new RuleException("账号已经在当前团队中,无需重复加入!<br>The account is already in the team!");
               } else {
                  GroupService.ins.addGroupUser(ContextHolder.getGroupId(), var3);
                  GroupRole var7 = GroupRoleManager.ins.get(ContextHolder.getGroupId(), GroupRoleEnum.User.name());
                  GroupRoleService.ins.addUserRole(ContextHolder.getGroupId(), var3, var7.getId());
                  SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "add", var3, String.format("Add team member %s", var3));
               }
            }
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "group",
      model = "members",
      code = "remove"
   )
   @Transactional
   public void removeUser(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("userId");
      if (StringUtils.isNotBlank(var3) && StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         Group var4 = GroupManager.ins.get(ContextHolder.getGroupId());
         if (var3.equals(var4.getCreateUser())) {
            throw new IllegalOperationException("当前账号是团队所有者，无法删除！");
         } else {
            GroupService.ins.removeGroupUser(ContextHolder.getGroupId(), var3);
            SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "remove", var3, String.format("Remove team member %s", var3));
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   @Transactional
   public void removeUserSelf(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         String var3 = SecurityUtils.getLoginUsername(var1);
         Group var4 = GroupManager.ins.get(ContextHolder.getGroupId());
         if (var3.equals(var4.getCreateUser())) {
            throw new IllegalOperationException("您是当前团队拥有者，无法推出团队！");
         } else {
            GroupService.ins.removeGroupUser(ContextHolder.getGroupId(), var3);
            SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "remove", var3, String.format("User %s leaving of team by self", var3));
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "group",
      code = "userrole",
      model = "members"
   )
   public void addUserRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("userId");
      long var4 = Long.parseLong(var1.getParameter("roleId"));
      GroupRole var6 = GroupRoleService.ins.get(var4);
      if (var6.getGroupId().equals(ContextHolder.getGroupId())) {
         GroupRoleService.ins.addUserRole(var6.getGroupId(), var3, var4);
         SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "userrole", var3, String.format("Add user %s of role %s", var3, var6.getName()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "group",
      code = "userrole",
      model = "members"
   )
   public void removeUserRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("userId");
      long var4 = Long.parseLong(var1.getParameter("roleId"));
      GroupRole var6 = GroupRoleService.ins.get(var4);
      if (var6.getGroupId().equals(ContextHolder.getGroupId())) {
         GroupRoleService.ins.removeUserRole(ContextHolder.getGroupId(), var3, var4);
         SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "userrole", var3, String.format("Remove role [%s] of user [%s]", var3, var6.getName()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   public void users(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      String var4 = var1.getParameter("roleId");
      String var5 = var1.getParameter("keyword");
      int var6 = Integer.parseInt(var1.getParameter("pageIndex"));
      int var7 = Integer.parseInt(var1.getParameter("pageSize"));
      UserQuery var8 = GroupManager.ins.createUserQuery();
      if (StringUtils.isNotBlank(var5)) {
         var8.idnameLike(var5);
      }

      if (!StringUtils.isBlank(var4) && !"-1".equals(var4)) {
         Page var10 = var8.roleUsers(var6, var7, var3, Long.parseLong(var4));
         this.a(var3, var10);
         this.a(var2, var10);
      } else {
         Page var9 = var8.users(var6, var7, var3);
         this.a(var3, var9);
         this.a(var2, var9);
      }

   }

   private void a(String var1, Page var2) throws Exception {
      ArrayList var3 = new ArrayList();

      for(com.bstek.urule.console.database.model.User var6 : (Iterable<com.bstek.urule.console.database.model.User>)(Iterable<?>)(var2.getData())) {
         GroupUserVO var7 = new GroupUserVO();
         var7.setId(var6.getId());
         var7.setName(var6.getName());
         var7.setCreateDate(var6.getCreateDate());
         var7.setRoles(GroupRoleManager.ins.loadUserRoles(var1, var6.getId()));
         var3.add(var7);
      }

      var2.setData(var3);
   }

   public void roleUsers(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("roleId");
      String var4 = var1.getParameter("groupId");
      this.a(var2, GroupRoleManager.ins.loadRoleUsers(var4, Long.parseLong(var3)));
   }

   public void projects(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      this.a(var1, var2, true);
   }

   private void a(HttpServletRequest var1, HttpServletResponse var2, boolean var3) throws Exception {
      String var4 = ContextHolder.getGroupId();
      String var5 = var1.getParameter("keyword");
      String var6 = var1.getParameter("type");
      String var7 = var1.getParameter("sortByName");
      String var8 = var1.getParameter("sortByAsc");
      ProjectQuery var9 = ProjectManager.ins.newQuery();
      String var10 = SecurityUtils.getLoginUsername(var1);
      if (var3) {
         var9.userId(var10);
      }

      if (StringUtils.isNotBlank(var5)) {
         var9.nameLike(var5);
      }

      if (StringUtils.isNotBlank(var6)) {
         var9.type(var6);
      }

      if (StringUtils.isNotBlank(var4)) {
         var9.groupId(var4);
      }

      if (StringUtils.isNotBlank(var7) && StringUtils.isNotBlank(var8)) {
         if ("NAME_".equals(var7)) {
            var9.orderbyName(var8);
         }

         if ("CREATE_DATE_".equals(var7)) {
            var9.orderbyCreateDate(var8);
         }
      }

      List var11 = var9.list();
      ArrayList var12 = new ArrayList();
      User var13 = SecurityUtils.getLoginUser(var1);
      List var14 = ProjectManager.ins.newQuery().groupId(var4).userId(var10).listIds();

      for(Project var16 : (Iterable<Project>)(Iterable<?>)(var11)) {
         ProjectVO var17 = new ProjectVO();
         BeanUtils.copyProperties(var17, var16);
         ContextHolder.setProjectId(var16.getId());
         boolean var18 = AuthenticationManager.decide(var13, RoleCategory.group, GroupModule.projects.toString(), "remove");
         if (!var18) {
            var18 = AuthenticationManager.decide(var13, RoleCategory.project, ProjectModule.project.toString(), "remove");
         }

         var17.setRemoveAble(var18);
         var18 = AuthenticationManager.decide(var13, RoleCategory.group, GroupModule.projects.toString(), "export");
         if (!var18) {
            var18 = AuthenticationManager.decide(var13, RoleCategory.project, ProjectModule.project.toString(), "export");
         }

         var17.setExportAble(var18);
         if (var14.contains(var16.getId())) {
            var17.setAccessable(true);
         }

         var12.add(var17);
      }

      this.a(var2, var12);
   }

   public void projectList(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      this.a(var1, var2, false);
   }

   public void countUserLogin(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = ContextHolder.getGroupId();
      Calendar var4 = Calendar.getInstance();
      var4.add(5, -7);
      var4.set(10, 0);
      var4.set(12, 0);
      var4.set(13, 0);
      var4.set(14, 0);
      Date var5 = var4.getTime();
      var4 = Calendar.getInstance();
      var4.add(5, 1);
      var4.set(10, 0);
      var4.set(12, 0);
      var4.set(13, 0);
      var4.set(14, 0);
      Date var6 = var4.getTime();
      List var7 = ReportGroupQuery.getUserLoginCountByDay(var3, var5, var6);
      this.a(var2, var7);
   }

   public void countRuleProjectCount(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = ContextHolder.getGroupId();
      List var4 = ReportGroupQuery.countProjectRuleFiles(var3);
      this.a(var2, var4);
   }

   public void listPacketDeploys(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = ContextHolder.getGroupId();
      List var4 = ReportGroupQuery.listPacketDeploys(var3);
      if (var4.size() > 5) {
         this.a(var2, var4.subList(0, 5));
      } else {
         this.a(var2, var4);
      }

   }

   public void listUserProjects(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = ContextHolder.getGroupId();
      List var4 = ReportGroupQuery.countUserCreateProjects(var3);
      this.a(var2, var4);
   }

   public void getSummary(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = ContextHolder.getGroupId();
      int var4 = ProjectManager.ins.newQuery().groupId(var3).list().size();
      int var5 = ReportGroupQuery.countKnByGroupId(var3);
      int var6 = ReportGroupQuery.countFileByGroupId(var3);
      int var7 = ReportGroupQuery.countBatchByGroupId(var3);
      HashMap var8 = new HashMap();
      var8.put("projectCount", var4);
      var8.put("packetCount", var5);
      var8.put("fileCount", var6);
      var8.put("batchCount", var7);
      this.a(var2, var8);
   }

   public String url() {
      return "/group";
   }
}
