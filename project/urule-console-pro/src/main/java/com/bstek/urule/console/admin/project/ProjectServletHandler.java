package com.bstek.urule.console.admin.project;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.ParameterInvaidException;
import com.bstek.urule.console.PermissionDeniedException;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.TransactionalInvoke;
import com.bstek.urule.console.admin.group.GroupUserVO;
import com.bstek.urule.console.admin.group.ProjectVO;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.admin.project.in.ConfigInfo;
import com.bstek.urule.console.admin.project.in.ProjectImport;
import com.bstek.urule.console.admin.project.out.ProjectExport;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketCacheImpl;
import com.bstek.urule.console.database.manager.file.DirectoryManager;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyManager;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyQuery;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.manager.project.ProjectQuery;
import com.bstek.urule.console.database.manager.project.role.ProjectRoleManager;
import com.bstek.urule.console.database.manager.project.user.UserQuery;
import com.bstek.urule.console.database.manager.report.ReportGroupQuery;
import com.bstek.urule.console.database.manager.report.ReportProjectQuery;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.PacketApply;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectRole;
import com.bstek.urule.console.database.model.ProjectViewModel;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.service.file.FileService;
import com.bstek.urule.console.database.service.project.ProjectService;
import com.bstek.urule.console.database.service.project.role.ProjectRoleService;
import com.bstek.urule.console.database.service.user.UserServiceManager;
import com.bstek.urule.console.database.vo.RuleDeployVO;
import com.bstek.urule.console.security.AuthenticationManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.type.GroupModule;
import com.bstek.urule.console.type.ProjectModule;
import com.bstek.urule.console.type.RoleCategory;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.util.UploadFile;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Base64Utils;

public class ProjectServletHandler extends ApiServletHandler {
   public void doExport(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("projectId"));
      Project project = ProjectManager.ins.get(longValue);
      User loginUser = SecurityUtils.getLoginUser(req);
      boolean flag = AuthenticationManager.decide(loginUser, RoleCategory.group, GroupModule.projects.toString(), "export");
      if (!flag) {
         flag = AuthenticationManager.decide(loginUser, RoleCategory.project, ProjectModule.project.toString(), "export");
      }

      if (!flag) {
         throw new PermissionDeniedException("Permission denied for project [" + longValue + "]");
      } else {
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
         String string = project.getName() + "-" + simpleDateFormat.format(new Date()) + ".urule.bak";
         resp.setContentType("application/octet-stream;charset=ISO8859-1");
         string = new String(string.getBytes("UTF-8"), "ISO8859-1");
         resp.setHeader("Content-Disposition", "attachment;filename=\"" + string + "\"");
         ServletOutputStream outputStream = resp.getOutputStream();
         ProjectExport.ins.doExport(outputStream, project);
         outputStream.flush();
         outputStream.close();
      }
   }

   @Transactional
   @URuleAuthorization(
      authType = "group",
      model = "projects",
      code = "import"
   )
   public void doImport(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      Group group = GroupManager.ins.get(parameter);
      UploadFile uploadFile = FileUtils.uploadFile(req);
      InputStream inputStream = uploadFile.getInputStream();
      ConfigInfo configInfo = new ConfigInfo();
      configInfo.setReplace(Boolean.parseBoolean(req.getParameter("replace")));
      configInfo.setNewPacketCode(Boolean.parseBoolean(req.getParameter("newPacketCode")));
      configInfo.setForceLock(Boolean.parseBoolean(req.getParameter("forceLock")));
      (new ProjectImport()).doImport(inputStream, group, configInfo);
      IOUtils.closeQuietly(inputStream);
   }

   /**加载项目列表*/
   public void list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String groupId = ContextHolder.getGroupId();
      String parameter = req.getParameter("keyword");
      String parameter2 = req.getParameter("type");
      String parameter3 = req.getParameter("sortByName");
      String parameter4 = req.getParameter("sortByAsc");
      ProjectQuery projectQuery = ProjectManager.ins.newQuery();
      String loginUsername = SecurityUtils.getLoginUsername(req);
      projectQuery.userId(loginUsername);
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

      this.writeObjectToJson((HttpServletResponse)resp, (Object)projectQuery.list());
   }

   /**获取项目对象*/
   public void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long projectId = ContextHolder.getProjectId();
      Project project = ProjectManager.ins.get(projectId);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)project);
   }

   /**新增项目*/
   @URuleAuthorization(
      authType = "group",
      model = "projects",
      code = "add"
   )
   @Transactional
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Project project = (Project)this.createObjectMapper().readValue(req.getParameter("project"), Project.class);
      if (project.getGroupId().equals(ContextHolder.getGroupId())) {
         project.setCreateUser(SecurityUtils.getLoginUsername(req));
         ProjectService.ins.add(project);
         SystemLogUtils.addGroupOperationLog(GroupModule.projects.name(), "add", project.getId(), String.format("Create project %s[%s]", project.getName(), project.getId()));
         User loginUser = SecurityUtils.getLoginUser(req);
         ProjectVO projectVO = new ProjectVO();
         BeanUtils.copyProperties(projectVO, project);
         ContextHolder.setProjectId(project.getId());
         boolean flag = AuthenticationManager.decide(loginUser, RoleCategory.group, GroupModule.projects.toString(), "remove");
         if (!flag) {
            flag = AuthenticationManager.decide(loginUser, RoleCategory.project, ProjectModule.project.toString(), "remove");
         }

         projectVO.setRemoveAble(flag);
         flag = AuthenticationManager.decide(loginUser, RoleCategory.group, GroupModule.projects.toString(), "export");
         if (!flag) {
            flag = AuthenticationManager.decide(loginUser, RoleCategory.project, ProjectModule.project.toString(), "export");
         }

         projectVO.setExportAble(flag);
         this.writeObjectToJson((HttpServletResponse)resp, (Object)projectVO);
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**用于在数据库迁移后重新加载需要缓存的数据*/
   public void reload(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      List items = ((PacketCacheImpl)PacketCache.ins).recacheAllPackets(req.getParameter("groupId"));
      this.writeObjectToJson((HttpServletResponse)resp, (Object)items);
   }

   private void migrateToGeneralView(long longValue, boolean flag) {
      if (!flag) {
         DirectoryManager.ins.changeGeneral(longValue);
      } else {
         HashMap valuesByKey = new HashMap();
         List items = FileService.ins.tree(longValue, 0L);
         String text = "";
         this.indexGeneralDirectories(items, text, valuesByKey);
         this.mergeTypedDirectories(valuesByKey, longValue, RuleFileType.Library);
         this.mergeTypedDirectories(valuesByKey, longValue, RuleFileType.RuleSet);
         this.mergeTypedDirectories(valuesByKey, longValue, RuleFileType.DecisionTable);
         this.mergeTypedDirectories(valuesByKey, longValue, RuleFileType.DecisionTree);
         this.mergeTypedDirectories(valuesByKey, longValue, RuleFileType.Scorecard);
         this.mergeTypedDirectories(valuesByKey, longValue, RuleFileType.Flow);
         this.mergeTypedDirectories(valuesByKey, longValue, RuleFileType.ActionTemplate);
         this.mergeTypedDirectories(valuesByKey, longValue, RuleFileType.ConditionTemplate);
         DirectoryManager.ins.changeGeneral(longValue);
      }

   }

   private void indexGeneralDirectories(List items, String text, Map valuesByKey) {
      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
         if (ruleFile.isDirectory()) {
            String text2 = "";
            String text3 = Base64Utils.encodeToString(ruleFile.getName().trim().getBytes());
            if (StringUtils.isBlank(text)) {
               text2 = text3;
            } else {
               text2 = text + "." + text3;
            }

            if (!StringUtils.isBlank(text2)) {
               ruleFile.setPath(text2);
               if (!valuesByKey.containsKey(text2)) {
                  valuesByKey.put(text2, ruleFile);
               }

               if (ruleFile.getChildren() != null && ruleFile.getChildren().size() > 0) {
                  this.indexGeneralDirectories(ruleFile.getChildren(), text2, valuesByKey);
               }
            }
         }
      }

   }

   private void assignEncodedPaths(List items, String text) {
      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
         String text2 = text;
         if (ruleFile.isDirectory()) {
            String text3 = Base64Utils.encodeToString(ruleFile.getName().trim().getBytes());
            if (StringUtils.isBlank(text)) {
               text2 = text3;
            } else {
               text2 = text + "." + text3;
            }
         }

         ruleFile.setPath(text2);
         if (ruleFile.getChildren() != null && ruleFile.getChildren().size() > 0) {
            this.assignEncodedPaths(ruleFile.getChildren(), text2);
         }
      }

   }

   private void mergeTypedDirectories(Map valuesByKey, long longValue, RuleFileType ruleFileType) {
      List items = FileService.ins.tree(longValue, ruleFileType);
      String text = "";
      this.assignEncodedPaths(items, text);
      this.mergeDirectoryTree(valuesByKey, items);
   }

   private void mergeDirectoryTree(Map valuesByKey, List items) {
      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
         if (!ruleFile.isDirectory()) {
            String path = ruleFile.getPath();
            if (StringUtils.isNotBlank(path)) {
               RuleFile ruleFile2 = (RuleFile)valuesByKey.get(path);
               FileManager.ins.changeParent(ruleFile.getId(), ruleFile2.getId());
            }
         } else {
            if (valuesByKey.containsKey(ruleFile.getPath())) {
               RuleFile ruleFile3 = (RuleFile)valuesByKey.get(ruleFile.getPath());
               if (ruleFile3.getId() != ruleFile.getId()) {
                  DirectoryManager.ins.remove(ruleFile.getId());
                  if (ruleFile.getChildren() != null && ruleFile.getChildren().size() > 0) {
                     for(RuleFile ruleFile4 : (Iterable<RuleFile>)(Iterable<?>)(ruleFile.getChildren())) {
                        if (ruleFile4.isDirectory()) {
                           DirectoryManager.ins.changeParent(ruleFile4.getId(), ruleFile3.getId());
                        } else {
                           FileManager.ins.changeParent(ruleFile4.getId(), ruleFile3.getId());
                        }
                     }
                  }
               }
            } else {
               String substring = ruleFile.getPath();
               valuesByKey.put(ruleFile.getPath(), ruleFile);
               substring = substring.substring(0, substring.lastIndexOf("."));
               RuleFile ruleFile5 = (RuleFile)valuesByKey.get(substring);
               DirectoryManager.ins.changeParent(ruleFile.getId(), ruleFile5.getId());
            }

            if (ruleFile.getChildren() != null && ruleFile.getChildren().size() > 0) {
               this.mergeDirectoryTree(valuesByKey, ruleFile.getChildren());
            }
         }
      }

   }

   public void hasTypeFolder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      boolean flag = DirectoryManager.ins.hasTypeFolder(ContextHolder.getProjectId());
      this.writeObjectToJson((HttpServletResponse)resp, (Object)flag);
   }

   /**更新项目*/
   @URuleAuthorization(
      authType = "group",
      model = "projects",
      code = "update"
   )
   @Transactional
   public void update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Project project = (Project)this.createObjectMapper().readValue(req.getParameter("project"), Project.class);
      if (project.getGroupId().equals(ContextHolder.getGroupId())) {
         Project project2 = ProjectManager.ins.get(project.getId());
         if (!project2.getType().equals(project.getType())) {
            throw new ParameterInvaidException();
         } else {
            project.setUpdateUser(SecurityUtils.getLoginUsername(req));
            project.setUpdateDate(new Date());
            String name = project2.getName();
            String name2 = project.getName();
            if (!name.equals(name2)) {
               List items = ProjectManager.ins.newQuery().groupId(project.getGroupId()).name(name2).list();
               if (items.size() > 0) {
                  boolean flag = false;

                  for(Project project3 : (Iterable<Project>)(Iterable<?>)(items)) {
                     if (project.getId() != project3.getId()) {
                        flag = true;
                     }
                  }

                  if (flag) {
                     throw new InfoException("项目名称重复.<br/>Duplicate project name.");
                  }
               }
            }

            ProjectService.ins.update(project);
            if (project2.getViewModel() != project.getViewModel() && project.getViewModel() == ProjectViewModel.general) {
               boolean flag2 = Boolean.parseBoolean(req.getParameter("mergeSameNameDir"));
               this.migrateToGeneralView(project.getId(), flag2);
            }

            SystemLogUtils.addGroupOperationLog(GroupModule.projects.name(), "update", project.getId(), String.format("Update project %s[%s]", project.getName(), project.getId()));
            this.writeObjectToJson((HttpServletResponse)resp, (Object)project);
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**删除项目*/
   public void remove(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      final Long projectId = ContextHolder.getProjectId();
      Project project = ProjectManager.ins.get(projectId);
      User loginUser = SecurityUtils.getLoginUser(req);
      boolean flag = AuthenticationManager.decide(loginUser, RoleCategory.group, GroupModule.projects.toString(), "remove");
      if (!flag) {
         flag = AuthenticationManager.decide(loginUser, RoleCategory.project, ProjectModule.project.toString(), "remove");
      }

      if (!flag) {
         throw new PermissionDeniedException("Permission denied for project [" + projectId + "]");
      } else if (project.getGroupId().equals(ContextHolder.getGroupId())) {
         this.doInTransactional(new TransactionalInvoke() {
            public void doTransactional() {
               Project project = ProjectManager.ins.get(projectId);
               if (project != null) {
                  ProjectService.ins.remove(projectId);
                  SystemLogUtils.addGroupOperationLog(GroupModule.projects.name(), "remove", projectId, String.format("Remove project %s[%s]", project.getName(), projectId));
               }

            }
         });
         List items = PacketCache.ins.removeProject(project.getId(), project.getGroupId());
         this.writeObjectToJson((HttpServletResponse)resp, (Object)items);
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**获取项目的角色列表*/
   public void roles(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
      this.writeObjectToJson((HttpServletResponse)resp, (Object)ProjectRoleService.ins.loadRoles(projectId));
   }

   /**为用户添加项目角色*/
   @URuleAuthorization(
      authType = "project",
      model = "members",
      code = "userrole"
   )
   public void addUserRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("userId");
      long longValue = Long.parseLong(req.getParameter("roleId"));
      ProjectRole projectRole = ProjectRoleManager.ins.get(longValue);
      if (projectRole.getProjectId() == ContextHolder.getProjectId()) {
         ProjectRoleService.ins.addUserRole(projectRole.getProjectId(), parameter, longValue);
         SystemLogUtils.addProjectOperationLog(ProjectModule.members.name(), "addUserRole", projectRole.getId(), String.format("Add user %s for role %s[%s]", parameter, projectRole.getName(), longValue));
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**删除用户的项目角色*/
   @URuleAuthorization(
      authType = "project",
      model = "members",
      code = "userrole"
   )
   public void removeUserRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("userId");
      long longValue = Long.parseLong(req.getParameter("roleId"));
      ProjectRole projectRole = ProjectRoleManager.ins.get(longValue);
      if (projectRole.getProjectId() == ContextHolder.getProjectId()) {
         SystemLogUtils.addProjectOperationLog(ProjectModule.members.name(), "removeUserRole", projectRole.getId(), String.format("Remove user %s for role %s[%s]", parameter, projectRole.getName(), projectRole.getId()));
         ProjectRoleService.ins.removeUserRole(parameter, longValue);
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**获取项目下的用户列表*/
   public void users(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long projectId = ContextHolder.getProjectId();
      String parameter = req.getParameter("roleId");
      String parameter2 = req.getParameter("keyword");
      int number = Integer.parseInt(req.getParameter("pageIndex"));
      int number2 = Integer.parseInt(req.getParameter("pageSize"));
      UserQuery userQuery = ProjectManager.ins.createUserQuery();
      userQuery.idnameLike(parameter2);
      if (!StringUtils.isBlank(parameter) && !"-1".equals(parameter)) {
         Page page = userQuery.roleUsers(number, number2, projectId, Long.parseLong(parameter));
         this.enrichUsersWithRoles(projectId, page);
         this.writeObjectToJson((HttpServletResponse)resp, (Object)page);
      } else {
         Page page2 = userQuery.users(number, number2, projectId);
         this.enrichUsersWithRoles(projectId, page2);
         this.writeObjectToJson((HttpServletResponse)resp, (Object)page2);
      }

   }

   private void enrichUsersWithRoles(long longValue, Page page) throws Exception {
      ArrayList items = new ArrayList();

      for(com.bstek.urule.console.database.model.User user : (Iterable<com.bstek.urule.console.database.model.User>)(Iterable<?>)(page.getData())) {
         GroupUserVO groupUserVO = new GroupUserVO();
         groupUserVO.setId(user.getId());
         groupUserVO.setName(user.getName());
         groupUserVO.setCreateDate(user.getCreateDate());
         groupUserVO.setRoles(ProjectRoleManager.ins.loadUserRoles(longValue, user.getId()));
         items.add(groupUserVO);
      }

      page.setData(items);
   }

   /**获取用户在项目中的角色列表*/
   public void userRoles(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("account");
      List userRoles = ProjectRoleService.ins.loadUserRoles(ContextHolder.getProjectId(), parameter);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)userRoles);
   }

   /**项目成员维护*/
   @URuleAuthorization(
      authType = "project",
      model = "members",
      code = "add"
   )
   public void addUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("userId");
      if (StringUtils.isNotBlank(parameter)) {
         Project project = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (project != null) {
            for(String text : parameter.split(",")) {
               if (StringUtils.isNotBlank(text)) {
                  ProjectService.ins.addProjectuser(ContextHolder.getProjectId(), text);
                  com.bstek.urule.console.database.model.User user = UserServiceManager.getUserService().get(text);
                  SystemLogUtils.addProjectOperationLog(ProjectModule.members.name(), "add", user.getId(), String.format("Add member %s", text));
               }
            }
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      model = "members",
      code = "remove"
   )
   public void removeUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("userId");
      if (StringUtils.isNotBlank(parameter)) {
         com.bstek.urule.console.database.model.User user = UserServiceManager.getUserService().get(parameter);
         Project project = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (project != null) {
            ProjectManager.ins.removeProjectUser(ContextHolder.getProjectId(), parameter);
            SystemLogUtils.addProjectOperationLog(ProjectModule.members.name(), "remove", user.getId(), String.format("Remove member %s", parameter));
         }
      }

   }

   /**为项目添加角色*/
   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void addRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("roleName");
      if (ContextHolder.getProjectId() != null) {
         Project project = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (project != null) {
            ProjectRole projectRole = new ProjectRole();
            projectRole.setName(parameter);
            projectRole.setProjectId(ContextHolder.getProjectId());
            projectRole.setType("custom");
            projectRole.setCreateUser(SecurityUtils.getLoginUsername(req));
            ProjectRoleService.ins.add(projectRole);
            this.writeObjectToJson((HttpServletResponse)resp, (Object)projectRole);
            SystemLogUtils.addProjectOperationLog(ProjectModule.permissions.name(), "manager", projectRole.getId(), String.format("Add role %s[%s]", parameter, projectRole.getId()));
         }

      } else {
         throw new ParameterInvaidException();
      }
   }

   /**项目角色修改名称*/
   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void renameRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("roleId"));
      String parameter = req.getParameter("roleName");
      ProjectRole projectRole = ProjectRoleManager.ins.get(longValue);
      if (ContextHolder.getProjectId() == projectRole.getProjectId()) {
         Project project = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (project != null) {
            String name = projectRole.getName();
            projectRole.setName(parameter);
            projectRole.setUpdateUser(SecurityUtils.getLoginUsername(req));
            ProjectRoleService.ins.update(projectRole);
            SystemLogUtils.addProjectOperationLog(ProjectModule.permissions.name(), "manager", projectRole.getId(), String.format("Change the name of role %s to %s", parameter, name));
         }

      } else {
         throw new ParameterInvaidException();
      }
   }

   /**删除项目角色*/
   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void removeRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("roleId"));
      ProjectRole projectRole = ProjectRoleManager.ins.get(longValue);
      if (projectRole != null && ContextHolder.getProjectId() == projectRole.getProjectId()) {
         Project project = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (project != null) {
            ProjectRoleService.ins.remove(longValue);
            SystemLogUtils.addProjectOperationLog(ProjectModule.permissions.name(), "manager", projectRole.getId(), String.format("Remove role %s[%s]", projectRole.getName(), longValue, project.getName(), project.getId()));
         }

      } else {
         throw new ParameterInvaidException();
      }
   }

   /**更新审批用户信息*/
   @Transactional
   @URuleAuthorization(
      authType = "project",
      model = "setting",
      code = "approveUser"
   )
   public void updateApproveUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long projectId = ContextHolder.getProjectId();
      String parameter = req.getParameter("disableApproveUser");
      String parameter2 = req.getParameter("enableApproveUser");
      String parameter3 = req.getParameter("deployApproveUser");
      if (StringUtils.isNotEmpty(parameter)) {
         ProjectManager.ins.updateApproveUser(projectId, ApplyType.enable, parameter2);
         SystemLogUtils.addProjectOperationLog(ProjectModule.setting.name(), "approveUser", parameter2, String.format("Set packet to enable approvers %s", parameter2));
      }

      if (StringUtils.isNotEmpty(parameter)) {
         ProjectManager.ins.updateApproveUser(projectId, ApplyType.disable, parameter);
         SystemLogUtils.addProjectOperationLog(ProjectModule.setting.name(), "approveUser", parameter, String.format("Set packet to disable approvers %s", parameter2));
      }

      if (StringUtils.isNotEmpty(parameter3)) {
         ProjectManager.ins.updateApproveUser(projectId, ApplyType.deploy, parameter3);
         SystemLogUtils.addProjectOperationLog(ProjectModule.setting.name(), "approveUser", parameter3, String.format("Set packet publishing Approver %s", parameter2));
      }

   }

   /**统计规则数据*/
   public void countRule(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Integer ruleCount = FileManager.ins.newCountQuery().projectId(ContextHolder.getProjectId()).getRuleCount();
      Integer count = PacketManager.ins.getCount(ContextHolder.getProjectId());
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("ruleCount", ruleCount);
      valuesByKey.put("packetCount", count);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)valuesByKey);
   }

   /**统计近期用户规则文件提交情况*/
   public void countUserCommits(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
      Calendar calendar = Calendar.getInstance();
      calendar.add(5, -14);
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
      List userCommits = ProjectService.ins.getUserCommits(projectId, time, time2);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)userCommits);
   }

   /**统计近期用户规则文件提交情况*/
   public void countRuleCommits(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
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
      List ruleCommits = ProjectService.ins.getRuleCommits(projectId, time, time2);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)ruleCommits);
   }

   /**统计近期用户规则文件执行情况*/
   public void countRuleExecCount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
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
      List ruleExecCount = ProjectService.ins.getRuleExecCount(projectId, time, time2);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)ruleExecCount);
   }

   /**统计近期用户规则文件执行情况*/
   public void countRuleExecTime(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
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
      List ruleExecTime = ProjectService.ins.getRuleExecTime(projectId, time, time2);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)ruleExecTime);
   }

   /**统计近期用户知识包发布情况*/
   public void countRuleDeploys(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
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
      PacketApplyQuery packetApplyQuery = PacketApplyManager.ins.newQuery();
      List items = packetApplyQuery.projectId(projectId).startDate(time).endDate(time2).type(ApplyType.deploy).status(ApplyStatus.pass).list();
      HashMap valuesByKey = new HashMap();

      for(PacketApply packetApply : (Iterable<PacketApply>)(Iterable<?>)(items)) {
         RuleDeployVO ruleDeployVO = new RuleDeployVO();
         Calendar calendar2 = Calendar.getInstance();
         calendar2.setTime(packetApply.getCreateDate());
         calendar2.set(11, 0);
         calendar2.set(12, 0);
         calendar2.set(13, 0);
         calendar2.set(14, 0);
         if (!valuesByKey.containsKey(calendar2.getTime())) {
            valuesByKey.put(calendar2.getTime(), ruleDeployVO);
         } else {
            ruleDeployVO = (RuleDeployVO)valuesByKey.get(calendar2.getTime());
         }

         ruleDeployVO.setCreateDate(calendar2.getTime());
         ruleDeployVO.setCount(ruleDeployVO.getCount() + 1);
      }

      ArrayList items2 = new ArrayList(valuesByKey.values());
      Collections.sort(items2, new Comparator<RuleDeployVO>() {
         public int compare(RuleDeployVO ruleDeployVO, RuleDeployVO ruleDeployVO2) {
            return (int)(ruleDeployVO.getCreateDate().getTime() - ruleDeployVO2.getCreateDate().getTime());
         }
      });
      this.writeObjectToJson((HttpServletResponse)resp, (Object)items2);
   }

   public void listPacketDeploys(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String groupId = ContextHolder.getGroupId();
      List items = ReportGroupQuery.listPacketDeploys(groupId);
      if (items.size() > 5) {
         this.writeObjectToJson((HttpServletResponse)resp, (Object)items.subList(0, 5));
      } else {
         this.writeObjectToJson((HttpServletResponse)resp, (Object)items);
      }

   }

   /**统计用户规则文件数量*/
   public void countUserRuleFiles(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
      List items = ReportProjectQuery.countUserRuleFiles(projectId);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)items);
   }

   /**用户登录统计*/
   public void countUserLogin(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
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
      List items = ReportProjectQuery.countUserLogin(projectId, time, time2);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)items);
   }

   public void listRecentFiles(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
      List items = ReportProjectQuery.listLastModifyFiles(projectId);
      if (items.size() > 5) {
         this.writeObjectToJson((HttpServletResponse)resp, (Object)items.subList(0, 5));
      } else {
         this.writeObjectToJson((HttpServletResponse)resp, (Object)items);
      }

   }

   public void getSummary(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
      int number = ReportProjectQuery.countPacket(projectId);
      int number2 = ReportProjectQuery.countFile(projectId);
      int number3 = ReportProjectQuery.countBatch(projectId);
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("packetCount", number);
      valuesByKey.put("fileCount", number2);
      valuesByKey.put("batchCount", number3);
      this.writeObjectToJson((HttpServletResponse)resp, (Object)valuesByKey);
   }

   public String url() {
      return "/project";
   }
}
