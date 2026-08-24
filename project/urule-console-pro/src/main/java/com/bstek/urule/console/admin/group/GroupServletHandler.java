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
   public void list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String loginUsername = SecurityUtils.getLoginUsername(req);
      this.writeObjectToJson(resp, GroupManager.ins.createQuery().list(loginUsername));
   }

   @Transactional
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Group group = (Group)this.createObjectMapper().readValue(req.getParameter("group"), Group.class);
      if (group.getId().length() < 3) {
         throw new InfoException("团队ID的至少3个字符<br>Team id must be at least three characters");
      } else {
         group.setCreateUser(SecurityUtils.getLoginUsername(req));
         GroupService.ins.add(group);
         List items = GroupManager.ins.createQuery().list(SecurityUtils.getLoginUsername(req));
         User loginUser = SecurityUtils.getLoginUser(req);
         loginUser.setGroups(items);
         this.writeObjectToJson(resp, group);
      }
   }

   public void remove(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      final String parameter = req.getParameter("groupId");
      Group group = GroupManager.ins.get(parameter);
      if (!SecurityUtils.getLoginUsername(req).equals(group.getCreateUser())) {
         throw new PermissionDeniedException("Permission denied for team [" + parameter + "]");
      } else {
         this.doInTransactional(new TransactionalInvoke() {
            public void doTransactional() {
               GroupService.ins.remove(parameter);
            }
         });
         List items = GroupManager.ins.createQuery().list(SecurityUtils.getLoginUsername(req));
         User loginUser = SecurityUtils.getLoginUser(req);
         loginUser.setGroups(items);
         List items2 = ((PacketCacheImpl)PacketCache.ins).recacheAllPackets(parameter);
         this.writeObjectToJson(resp, items2);
      }
   }

   public void update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Group group = (Group)this.createObjectMapper().readValue(req.getParameter("group"), Group.class);
      if (group.getId().equals(ContextHolder.getGroupId())) {
         Group group2 = GroupManager.ins.get(group.getId());
         if (!SecurityUtils.getLoginUsername(req).equals(group.getCreateUser())) {
            throw new PermissionDeniedException("Permission denied for team [" + group.getId() + "]");
         } else {
            group2.setName(group.getName());
            group2.setDesc(group.getDesc());
            group2.setUpdateUser(SecurityUtils.getLoginUsername(req));
            GroupManager.ins.update(group2);
            this.writeObjectToJson(resp, group2);
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   public void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      Group group = GroupManager.ins.get(parameter);
      this.writeObjectToJson(resp, group);
   }

   /**获取是否可以自由创建Group的系统配置*/
   @URuleAuthAnonymous
   public void freeCreate(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      this.writeObjectToJson(resp, GroupService.ins.isFreeCreate(SecurityUtils.getLoginUsername(req)));
   }

   /**获取团队的角色列表*/
   public void roles(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      this.writeObjectToJson(resp, GroupRoleService.ins.loadRoles(parameter));
   }

   /**获取团队的角色列表*/
   public void userRoles(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      String parameter2 = req.getParameter("account");
      this.writeObjectToJson(resp, GroupRoleService.ins.loadUserRoles(parameter, parameter2));
   }

   /**为团队添加角色*/
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "permissions"
   )
   public void addRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("roleName");
      if (StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         GroupRole groupRole = new GroupRole();
         groupRole.setName(parameter);
         groupRole.setGroupId(ContextHolder.getGroupId());
         groupRole.setType("custom");
         GroupRoleService.ins.add(groupRole);
         this.writeObjectToJson(resp, groupRole);
         SystemLogUtils.addGroupOperationLog(GroupModule.permissions.name(), "manager", groupRole.getId(), String.format("Create role %s[%s]", groupRole.getName(), groupRole.getId()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**团队角色修改名称*/
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "permissions"
   )
   public void renameRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("roleId"));
      String parameter = req.getParameter("roleName");
      GroupRole groupRole = GroupRoleService.ins.get(longValue);
      if (groupRole.getGroupId().equals(ContextHolder.getGroupId())) {
         String name = groupRole.getName();
         groupRole.setName(parameter);
         GroupRoleService.ins.update(groupRole);
         SystemLogUtils.addGroupOperationLog(GroupModule.permissions.name(), "manager", groupRole.getId(), String.format("Role name [%s] updated to [%s]", name, parameter));
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**删除团队角色*/
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "permissions"
   )
   public void removeRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("roleId"));
      GroupRole groupRole = GroupRoleManager.ins.get(longValue);
      if (groupRole.getGroupId().equals(ContextHolder.getGroupId())) {
         GroupRoleService.ins.remove(longValue);
         SystemLogUtils.addGroupOperationLog(GroupModule.permissions.name(), "manager", groupRole.getId(), String.format("Remove role %s[%s]", groupRole.getName(), groupRole.getId()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**添加团队用户*/
   @URuleAuthorization(
      authType = "group",
      model = "members",
      code = "add"
   )
   @Transactional
   public void addUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      com.bstek.urule.console.database.model.User user = (com.bstek.urule.console.database.model.User)this.createObjectMapper().readValue(req.getParameter("user"), com.bstek.urule.console.database.model.User.class);
      String id = user.getId();
      if (StringUtils.isNotBlank(ContextHolder.getGroupId()) && StringUtils.isNotBlank(id)) {
         Group group = GroupManager.ins.get(ContextHolder.getGroupId());
         if (id.equals(group.getCreateUser())) {
            throw new IllegalOperationException("该账号是团队管理员,无法重复添加!");
         } else {
            com.bstek.urule.console.database.model.User user2 = UserManager.ins.get(id);
            if (user2 != null) {
               throw new IllegalOperationException("该账号以存在,无法重复添加!");
            } else {
               user.setEnable(true);
               user.setCreateUser(SecurityUtils.getLoginUsername(req));
               UserManager.ins.add(user);
               GroupService.ins.addGroupUser(ContextHolder.getGroupId(), id);
               GroupRole groupRole = GroupRoleManager.ins.get(ContextHolder.getGroupId(), GroupRoleEnum.User.name());
               GroupRoleService.ins.addUserRole(ContextHolder.getGroupId(), id, groupRole.getId());
               SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "add", id, String.format("Add team member %s", id));
            }
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**添加团队用户*/
   @URuleAuthorization(
      authType = "group",
      model = "members",
      code = "add"
   )
   @Transactional
   public void addUserByAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("userId");
      if (StringUtils.isNotBlank(parameter) && StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         Group group = GroupManager.ins.get(ContextHolder.getGroupId());
         if (parameter.equals(group.getCreateUser())) {
            throw new RuleException("不能添加团队管理员账号!<br>Illegal Operation! The account is group owner.");
         } else {
            com.bstek.urule.console.database.model.User user = UserManager.ins.get(parameter);
            if (user == null) {
               throw new RuleException("用户账号不存在!<br>Account does not exist!");
            } else {
               com.bstek.urule.console.database.model.User groupUser = GroupManager.ins.getGroupUser(parameter, parameter);
               if (groupUser != null) {
                  throw new RuleException("账号已经在当前团队中,无需重复加入!<br>The account is already in the team!");
               } else {
                  GroupService.ins.addGroupUser(ContextHolder.getGroupId(), parameter);
                  GroupRole groupRole = GroupRoleManager.ins.get(ContextHolder.getGroupId(), GroupRoleEnum.User.name());
                  GroupRoleService.ins.addUserRole(ContextHolder.getGroupId(), parameter, groupRole.getId());
                  SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "add", parameter, String.format("Add team member %s", parameter));
               }
            }
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**删除团队用户*/
   @URuleAuthorization(
      authType = "group",
      model = "members",
      code = "remove"
   )
   @Transactional
   public void removeUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("userId");
      if (StringUtils.isNotBlank(parameter) && StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         Group group = GroupManager.ins.get(ContextHolder.getGroupId());
         if (parameter.equals(group.getCreateUser())) {
            throw new IllegalOperationException("当前账号是团队所有者，无法删除！");
         } else {
            GroupService.ins.removeGroupUser(ContextHolder.getGroupId(), parameter);
            SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "remove", parameter, String.format("Remove team member %s", parameter));
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**退出团队*/
   @Transactional
   public void removeUserSelf(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         String loginUsername = SecurityUtils.getLoginUsername(req);
         Group group = GroupManager.ins.get(ContextHolder.getGroupId());
         if (loginUsername.equals(group.getCreateUser())) {
            throw new IllegalOperationException("您是当前团队拥有者，无法推出团队！");
         } else {
            GroupService.ins.removeGroupUser(ContextHolder.getGroupId(), loginUsername);
            SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "remove", loginUsername, String.format("User %s leaving of team by self", loginUsername));
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**为团队用户添加角色*/
   @URuleAuthorization(
      authType = "group",
      code = "userrole",
      model = "members"
   )
   public void addUserRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("userId");
      long longValue = Long.parseLong(req.getParameter("roleId"));
      GroupRole groupRole = GroupRoleService.ins.get(longValue);
      if (groupRole.getGroupId().equals(ContextHolder.getGroupId())) {
         GroupRoleService.ins.addUserRole(groupRole.getGroupId(), parameter, longValue);
         SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "userrole", parameter, String.format("Add user %s of role %s", parameter, groupRole.getName()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**删除团队用户的角色*/
   @URuleAuthorization(
      authType = "group",
      code = "userrole",
      model = "members"
   )
   public void removeUserRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("userId");
      long longValue = Long.parseLong(req.getParameter("roleId"));
      GroupRole groupRole = GroupRoleService.ins.get(longValue);
      if (groupRole.getGroupId().equals(ContextHolder.getGroupId())) {
         GroupRoleService.ins.removeUserRole(ContextHolder.getGroupId(), parameter, longValue);
         SystemLogUtils.addGroupOperationLog(GroupModule.members.name(), "userrole", parameter, String.format("Remove role [%s] of user [%s]", parameter, groupRole.getName()));
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**获取团队下的用户列表*/
   public void users(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      String parameter2 = req.getParameter("roleId");
      String parameter3 = req.getParameter("keyword");
      int number = Integer.parseInt(req.getParameter("pageIndex"));
      int number2 = Integer.parseInt(req.getParameter("pageSize"));
      UserQuery userQuery = GroupManager.ins.createUserQuery();
      if (StringUtils.isNotBlank(parameter3)) {
         userQuery.idnameLike(parameter3);
      }

      if (!StringUtils.isBlank(parameter2) && !"-1".equals(parameter2)) {
         Page page = userQuery.roleUsers(number, number2, parameter, Long.parseLong(parameter2));
         this.enrichUsersWithRoles(parameter, page);
         this.writeObjectToJson(resp, page);
      } else {
         Page page2 = userQuery.users(number, number2, parameter);
         this.enrichUsersWithRoles(parameter, page2);
         this.writeObjectToJson(resp, page2);
      }

   }

   private void enrichUsersWithRoles(String text, Page page) throws Exception {
      ArrayList items = new ArrayList();

      for(com.bstek.urule.console.database.model.User user : (Iterable<com.bstek.urule.console.database.model.User>)(Iterable<?>)(page.getData())) {
         GroupUserVO groupUserVO = new GroupUserVO();
         groupUserVO.setId(user.getId());
         groupUserVO.setName(user.getName());
         groupUserVO.setCreateDate(user.getCreateDate());
         groupUserVO.setRoles(GroupRoleManager.ins.loadUserRoles(text, user.getId()));
         items.add(groupUserVO);
      }

      page.setData(items);
   }

   /**获取角色的用户列表*/
   public void roleUsers(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("roleId");
      String parameter2 = req.getParameter("groupId");
      this.writeObjectToJson(resp, GroupRoleManager.ins.loadRoleUsers(parameter2, Long.parseLong(parameter)));
   }

   /**加载项目列表*/
   public void projects(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      this.writeProjectList(req, resp, true);
   }

   private void writeProjectList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, boolean flag) throws Exception {
      String groupId = ContextHolder.getGroupId();
      String parameter = httpServletRequest.getParameter("keyword");
      String parameter2 = httpServletRequest.getParameter("type");
      String parameter3 = httpServletRequest.getParameter("sortByName");
      String parameter4 = httpServletRequest.getParameter("sortByAsc");
      ProjectQuery projectQuery = ProjectManager.ins.newQuery();
      String loginUsername = SecurityUtils.getLoginUsername(httpServletRequest);
      if (flag) {
         projectQuery.userId(loginUsername);
      }

      if (StringUtils.isNotBlank(parameter)) {
         projectQuery.nameLike(parameter);
      }

      if (StringUtils.isNotBlank(parameter2)) {
         projectQuery.type(parameter2);
      }

      if (StringUtils.isNotBlank(groupId)) {
         projectQuery.groupId(groupId);
      }

      if (StringUtils.isNotBlank(parameter3) && StringUtils.isNotBlank(parameter4)) {
         if ("NAME_".equals(parameter3)) {
            projectQuery.orderbyName(parameter4);
         }

         if ("CREATE_DATE_".equals(parameter3)) {
            projectQuery.orderbyCreateDate(parameter4);
         }
      }

      List items = projectQuery.list();
      ArrayList items2 = new ArrayList();
      User loginUser = SecurityUtils.getLoginUser(httpServletRequest);
      List items3 = ProjectManager.ins.newQuery().groupId(groupId).userId(loginUsername).listIds();

      for(Project project : (Iterable<Project>)(Iterable<?>)(items)) {
         ProjectVO projectVO = new ProjectVO();
         BeanUtils.copyProperties(projectVO, project);
         ContextHolder.setProjectId(project.getId());
         boolean flag2 = AuthenticationManager.decide(loginUser, RoleCategory.group, GroupModule.projects.toString(), "remove");
         if (!flag2) {
            flag2 = AuthenticationManager.decide(loginUser, RoleCategory.project, ProjectModule.project.toString(), "remove");
         }

         projectVO.setRemoveAble(flag2);
         flag2 = AuthenticationManager.decide(loginUser, RoleCategory.group, GroupModule.projects.toString(), "export");
         if (!flag2) {
            flag2 = AuthenticationManager.decide(loginUser, RoleCategory.project, ProjectModule.project.toString(), "export");
         }

         projectVO.setExportAble(flag2);
         if (items3.contains(project.getId())) {
            projectVO.setAccessable(true);
         }

         items2.add(projectVO);
      }

      this.writeObjectToJson(httpServletResponse, items2);
   }

   public void projectList(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      this.writeProjectList(req, resp, false);
   }

   /**统计近期用户规则文件执行情况*/
   public void countUserLogin(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String groupId = ContextHolder.getGroupId();
      Calendar calendar = Calendar.getInstance();
      calendar.add(5, -7);
      calendar.set(10, 0);
      calendar.set(12, 0);
      calendar.set(13, 0);
      calendar.set(14, 0);
      Date time = calendar.getTime();
      calendar = Calendar.getInstance();
      calendar.add(5, 1);
      calendar.set(10, 0);
      calendar.set(12, 0);
      calendar.set(13, 0);
      calendar.set(14, 0);
      Date time2 = calendar.getTime();
      List userLoginCountByDay = ReportGroupQuery.getUserLoginCountByDay(groupId, time, time2);
      this.writeObjectToJson(resp, userLoginCountByDay);
   }

   /**统计项目规则文件数量*/
   public void countRuleProjectCount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String groupId = ContextHolder.getGroupId();
      List items = ReportGroupQuery.countProjectRuleFiles(groupId);
      this.writeObjectToJson(resp, items);
   }

   public void listPacketDeploys(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String groupId = ContextHolder.getGroupId();
      List items = ReportGroupQuery.listPacketDeploys(groupId);
      if (items.size() > 5) {
         this.writeObjectToJson(resp, items.subList(0, 5));
      } else {
         this.writeObjectToJson(resp, items);
      }

   }

   public void listUserProjects(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String groupId = ContextHolder.getGroupId();
      List items = ReportGroupQuery.countUserCreateProjects(groupId);
      this.writeObjectToJson(resp, items);
   }

   public void getSummary(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String groupId = ContextHolder.getGroupId();
      int number = ProjectManager.ins.newQuery().groupId(groupId).list().size();
      int number2 = ReportGroupQuery.countKnByGroupId(groupId);
      int number3 = ReportGroupQuery.countFileByGroupId(groupId);
      int number4 = ReportGroupQuery.countBatchByGroupId(groupId);
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("projectCount", number);
      valuesByKey.put("packetCount", number2);
      valuesByKey.put("fileCount", number3);
      valuesByKey.put("batchCount", number4);
      this.writeObjectToJson(resp, valuesByKey);
   }

   public String url() {
      return "/group";
   }
}
