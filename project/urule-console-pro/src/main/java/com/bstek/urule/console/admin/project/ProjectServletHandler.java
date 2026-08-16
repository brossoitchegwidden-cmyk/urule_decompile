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
   public void doExport(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("projectId"));
      Project var5 = ProjectManager.ins.get(var3);
      User var6 = SecurityUtils.getLoginUser(var1);
      boolean var7 = AuthenticationManager.decide(var6, RoleCategory.group, GroupModule.projects.toString(), "export");
      if (!var7) {
         var7 = AuthenticationManager.decide(var6, RoleCategory.project, ProjectModule.project.toString(), "export");
      }

      if (!var7) {
         throw new PermissionDeniedException("Permission denied for project [" + var3 + "]");
      } else {
         SimpleDateFormat var8 = new SimpleDateFormat("yyyyMMddHHmmss");
         String var9 = var5.getName() + "-" + var8.format(new Date()) + ".urule.bak";
         var2.setContentType("application/octet-stream;charset=ISO8859-1");
         var9 = new String(var9.getBytes("UTF-8"), "ISO8859-1");
         var2.setHeader("Content-Disposition", "attachment;filename=\"" + var9 + "\"");
         ServletOutputStream var10 = var2.getOutputStream();
         ProjectExport.ins.doExport(var10, var5);
         var10.flush();
         var10.close();
      }
   }

   @Transactional
   @URuleAuthorization(
      authType = "group",
      model = "projects",
      code = "import"
   )
   public void doImport(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      Group var4 = GroupManager.ins.get(var3);
      UploadFile var5 = FileUtils.uploadFile(var1);
      InputStream var6 = var5.getInputStream();
      ConfigInfo var7 = new ConfigInfo();
      var7.setReplace(Boolean.parseBoolean(var1.getParameter("replace")));
      var7.setNewPacketCode(Boolean.parseBoolean(var1.getParameter("newPacketCode")));
      var7.setForceLock(Boolean.parseBoolean(var1.getParameter("forceLock")));
      (new ProjectImport()).doImport(var6, var4, var7);
      IOUtils.closeQuietly(var6);
   }

   public void list(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = ContextHolder.getGroupId();
      String var4 = var1.getParameter("keyword");
      String var5 = var1.getParameter("type");
      String var6 = var1.getParameter("sortByName");
      String var7 = var1.getParameter("sortByAsc");
      ProjectQuery var8 = ProjectManager.ins.newQuery();
      String var9 = SecurityUtils.getLoginUsername(var1);
      var8.userId(var9);
      if (StringUtils.isNotBlank(var4)) {
         var8.nameLike(var4);
      }

      if (StringUtils.isNotBlank(var5)) {
         var8.type(var5);
      }

      if (StringUtils.isNotBlank(var3)) {
         var8.groupId(var3);
      }

      if (StringUtils.isNotBlank(var6) && StringUtils.isNotBlank(var7)) {
         if ("NAME_".equals(var6)) {
            var8.orderbyName(var7);
         }

         if ("CREATE_DATE_".equals(var6)) {
            var8.orderbyCreateDate(var7);
         }
      }

      this.a((HttpServletResponse)var2, (Object)var8.list());
   }

   public void get(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = ContextHolder.getProjectId();
      Project var4 = ProjectManager.ins.get(var3);
      this.a((HttpServletResponse)var2, (Object)var4);
   }

   @URuleAuthorization(
      authType = "group",
      model = "projects",
      code = "add"
   )
   @Transactional
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Project var3 = (Project)this.a().readValue(var1.getParameter("project"), Project.class);
      if (var3.getGroupId().equals(ContextHolder.getGroupId())) {
         var3.setCreateUser(SecurityUtils.getLoginUsername(var1));
         ProjectService.ins.add(var3);
         SystemLogUtils.addGroupOperationLog(GroupModule.projects.name(), "add", var3.getId(), String.format("Create project %s[%s]", var3.getName(), var3.getId()));
         User var4 = SecurityUtils.getLoginUser(var1);
         ProjectVO var5 = new ProjectVO();
         BeanUtils.copyProperties(var5, var3);
         ContextHolder.setProjectId(var3.getId());
         boolean var6 = AuthenticationManager.decide(var4, RoleCategory.group, GroupModule.projects.toString(), "remove");
         if (!var6) {
            var6 = AuthenticationManager.decide(var4, RoleCategory.project, ProjectModule.project.toString(), "remove");
         }

         var5.setRemoveAble(var6);
         var6 = AuthenticationManager.decide(var4, RoleCategory.group, GroupModule.projects.toString(), "export");
         if (!var6) {
            var6 = AuthenticationManager.decide(var4, RoleCategory.project, ProjectModule.project.toString(), "export");
         }

         var5.setExportAble(var6);
         this.a((HttpServletResponse)var2, (Object)var5);
      } else {
         throw new ParameterInvaidException();
      }
   }

   public void reload(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      List var3 = ((PacketCacheImpl)PacketCache.ins).recacheAllPackets(var1.getParameter("groupId"));
      this.a((HttpServletResponse)var2, (Object)var3);
   }

   private void a(long var1, boolean var3) {
      if (!var3) {
         DirectoryManager.ins.changeGeneral(var1);
      } else {
         HashMap var4 = new HashMap();
         List var5 = FileService.ins.tree(var1, 0L);
         String var6 = "";
         this.a(var5, var6, var4);
         this.a(var4, var1, RuleFileType.Library);
         this.a(var4, var1, RuleFileType.RuleSet);
         this.a(var4, var1, RuleFileType.DecisionTable);
         this.a(var4, var1, RuleFileType.DecisionTree);
         this.a(var4, var1, RuleFileType.Scorecard);
         this.a(var4, var1, RuleFileType.Flow);
         this.a(var4, var1, RuleFileType.ActionTemplate);
         this.a(var4, var1, RuleFileType.ConditionTemplate);
         DirectoryManager.ins.changeGeneral(var1);
      }

   }

   private void a(List var1, String var2, Map var3) {
      for(RuleFile var5 : (Iterable<RuleFile>)(Iterable<?>)(var1)) {
         if (var5.isDirectory()) {
            String var6 = "";
            String var7 = Base64Utils.encodeToString(var5.getName().trim().getBytes());
            if (StringUtils.isBlank(var2)) {
               var6 = var7;
            } else {
               var6 = var2 + "." + var7;
            }

            if (!StringUtils.isBlank(var6)) {
               var5.setPath(var6);
               if (!var3.containsKey(var6)) {
                  var3.put(var6, var5);
               }

               if (var5.getChildren() != null && var5.getChildren().size() > 0) {
                  this.a(var5.getChildren(), var6, var3);
               }
            }
         }
      }

   }

   private void a(List var1, String var2) {
      for(RuleFile var4 : (Iterable<RuleFile>)(Iterable<?>)(var1)) {
         String var5 = var2;
         if (var4.isDirectory()) {
            String var6 = Base64Utils.encodeToString(var4.getName().trim().getBytes());
            if (StringUtils.isBlank(var2)) {
               var5 = var6;
            } else {
               var5 = var2 + "." + var6;
            }
         }

         var4.setPath(var5);
         if (var4.getChildren() != null && var4.getChildren().size() > 0) {
            this.a(var4.getChildren(), var5);
         }
      }

   }

   private void a(Map var1, long var2, RuleFileType var4) {
      List var5 = FileService.ins.tree(var2, var4);
      String var6 = "";
      this.a(var5, var6);
      this.a(var1, var5);
   }

   private void a(Map var1, List var2) {
      for(RuleFile var4 : (Iterable<RuleFile>)(Iterable<?>)(var2)) {
         if (!var4.isDirectory()) {
            String var10 = var4.getPath();
            if (StringUtils.isNotBlank(var10)) {
               RuleFile var12 = (RuleFile)var1.get(var10);
               FileManager.ins.changeParent(var4.getId(), var12.getId());
            }
         } else {
            if (var1.containsKey(var4.getPath())) {
               RuleFile var5 = (RuleFile)var1.get(var4.getPath());
               if (var5.getId() != var4.getId()) {
                  DirectoryManager.ins.remove(var4.getId());
                  if (var4.getChildren() != null && var4.getChildren().size() > 0) {
                     for(RuleFile var7 : (Iterable<RuleFile>)(Iterable<?>)(var4.getChildren())) {
                        if (var7.isDirectory()) {
                           DirectoryManager.ins.changeParent(var7.getId(), var5.getId());
                        } else {
                           FileManager.ins.changeParent(var7.getId(), var5.getId());
                        }
                     }
                  }
               }
            } else {
               String var8 = var4.getPath();
               var1.put(var4.getPath(), var4);
               var8 = var8.substring(0, var8.lastIndexOf("."));
               RuleFile var11 = (RuleFile)var1.get(var8);
               DirectoryManager.ins.changeParent(var4.getId(), var11.getId());
            }

            if (var4.getChildren() != null && var4.getChildren().size() > 0) {
               this.a(var1, var4.getChildren());
            }
         }
      }

   }

   public void hasTypeFolder(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      boolean var3 = DirectoryManager.ins.hasTypeFolder(ContextHolder.getProjectId());
      this.a((HttpServletResponse)var2, (Object)var3);
   }

   @URuleAuthorization(
      authType = "group",
      model = "projects",
      code = "update"
   )
   @Transactional
   public void update(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Project var3 = (Project)this.a().readValue(var1.getParameter("project"), Project.class);
      if (var3.getGroupId().equals(ContextHolder.getGroupId())) {
         Project var4 = ProjectManager.ins.get(var3.getId());
         if (!var4.getType().equals(var3.getType())) {
            throw new ParameterInvaidException();
         } else {
            var3.setUpdateUser(SecurityUtils.getLoginUsername(var1));
            var3.setUpdateDate(new Date());
            String var5 = var4.getName();
            String var6 = var3.getName();
            if (!var5.equals(var6)) {
               List var7 = ProjectManager.ins.newQuery().groupId(var3.getGroupId()).name(var6).list();
               if (var7.size() > 0) {
                  boolean var8 = false;

                  for(Project var10 : (Iterable<Project>)(Iterable<?>)(var7)) {
                     if (var3.getId() != var10.getId()) {
                        var8 = true;
                     }
                  }

                  if (var8) {
                     throw new InfoException("项目名称重复.<br/>Duplicate project name.");
                  }
               }
            }

            ProjectService.ins.update(var3);
            if (var4.getViewModel() != var3.getViewModel() && var3.getViewModel() == ProjectViewModel.general) {
               boolean var11 = Boolean.parseBoolean(var1.getParameter("mergeSameNameDir"));
               this.a(var3.getId(), var11);
            }

            SystemLogUtils.addGroupOperationLog(GroupModule.projects.name(), "update", var3.getId(), String.format("Update project %s[%s]", var3.getName(), var3.getId()));
            this.a((HttpServletResponse)var2, (Object)var3);
         }
      } else {
         throw new ParameterInvaidException();
      }
   }

   public void remove(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      final Long var3 = ContextHolder.getProjectId();
      Project var4 = ProjectManager.ins.get(var3);
      User var5 = SecurityUtils.getLoginUser(var1);
      boolean var6 = AuthenticationManager.decide(var5, RoleCategory.group, GroupModule.projects.toString(), "remove");
      if (!var6) {
         var6 = AuthenticationManager.decide(var5, RoleCategory.project, ProjectModule.project.toString(), "remove");
      }

      if (!var6) {
         throw new PermissionDeniedException("Permission denied for project [" + var3 + "]");
      } else if (var4.getGroupId().equals(ContextHolder.getGroupId())) {
         this.a(new TransactionalInvoke() {
            public void doTransactional() {
               Project var1 = ProjectManager.ins.get(var3);
               if (var1 != null) {
                  ProjectService.ins.remove(var3);
                  SystemLogUtils.addGroupOperationLog(GroupModule.projects.name(), "remove", var3, String.format("Remove project %s[%s]", var1.getName(), var3));
               }

            }
         });
         List var7 = PacketCache.ins.removeProject(var4.getId(), var4.getGroupId());
         this.a((HttpServletResponse)var2, (Object)var7);
      } else {
         throw new ParameterInvaidException();
      }
   }

   public void roles(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      this.a((HttpServletResponse)var2, (Object)ProjectRoleService.ins.loadRoles(var3));
   }

   @URuleAuthorization(
      authType = "project",
      model = "members",
      code = "userrole"
   )
   public void addUserRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("userId");
      long var4 = Long.parseLong(var1.getParameter("roleId"));
      ProjectRole var6 = ProjectRoleManager.ins.get(var4);
      if (var6.getProjectId() == ContextHolder.getProjectId()) {
         ProjectRoleService.ins.addUserRole(var6.getProjectId(), var3, var4);
         SystemLogUtils.addProjectOperationLog(ProjectModule.members.name(), "addUserRole", var6.getId(), String.format("Add user %s for role %s[%s]", var3, var6.getName(), var4));
      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "project",
      model = "members",
      code = "userrole"
   )
   public void removeUserRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("userId");
      long var4 = Long.parseLong(var1.getParameter("roleId"));
      ProjectRole var6 = ProjectRoleManager.ins.get(var4);
      if (var6.getProjectId() == ContextHolder.getProjectId()) {
         SystemLogUtils.addProjectOperationLog(ProjectModule.members.name(), "removeUserRole", var6.getId(), String.format("Remove user %s for role %s[%s]", var3, var6.getName(), var6.getId()));
         ProjectRoleService.ins.removeUserRole(var3, var4);
      } else {
         throw new ParameterInvaidException();
      }
   }

   public void users(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = ContextHolder.getProjectId();
      String var4 = var1.getParameter("roleId");
      String var5 = var1.getParameter("keyword");
      int var6 = Integer.parseInt(var1.getParameter("pageIndex"));
      int var7 = Integer.parseInt(var1.getParameter("pageSize"));
      UserQuery var8 = ProjectManager.ins.createUserQuery();
      var8.idnameLike(var5);
      if (!StringUtils.isBlank(var4) && !"-1".equals(var4)) {
         Page var10 = var8.roleUsers(var6, var7, var3, Long.parseLong(var4));
         this.a(var3, var10);
         this.a((HttpServletResponse)var2, (Object)var10);
      } else {
         Page var9 = var8.users(var6, var7, var3);
         this.a(var3, var9);
         this.a((HttpServletResponse)var2, (Object)var9);
      }

   }

   private void a(long var1, Page var3) throws Exception {
      ArrayList var4 = new ArrayList();

      for(com.bstek.urule.console.database.model.User var7 : (Iterable<com.bstek.urule.console.database.model.User>)(Iterable<?>)(var3.getData())) {
         GroupUserVO var8 = new GroupUserVO();
         var8.setId(var7.getId());
         var8.setName(var7.getName());
         var8.setCreateDate(var7.getCreateDate());
         var8.setRoles(ProjectRoleManager.ins.loadUserRoles(var1, var7.getId()));
         var4.add(var8);
      }

      var3.setData(var4);
   }

   public void userRoles(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("account");
      List var4 = ProjectRoleService.ins.loadUserRoles(ContextHolder.getProjectId(), var3);
      this.a((HttpServletResponse)var2, (Object)var4);
   }

   @URuleAuthorization(
      authType = "project",
      model = "members",
      code = "add"
   )
   public void addUser(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("userId");
      if (StringUtils.isNotBlank(var3)) {
         Project var4 = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (var4 != null) {
            for(String var8 : var3.split(",")) {
               if (StringUtils.isNotBlank(var8)) {
                  ProjectService.ins.addProjectuser(ContextHolder.getProjectId(), var8);
                  com.bstek.urule.console.database.model.User var9 = UserServiceManager.getUserService().get(var8);
                  SystemLogUtils.addProjectOperationLog(ProjectModule.members.name(), "add", var9.getId(), String.format("Add member %s", var8));
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
   public void removeUser(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("userId");
      if (StringUtils.isNotBlank(var3)) {
         com.bstek.urule.console.database.model.User var4 = UserServiceManager.getUserService().get(var3);
         Project var5 = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (var5 != null) {
            ProjectManager.ins.removeProjectUser(ContextHolder.getProjectId(), var3);
            SystemLogUtils.addProjectOperationLog(ProjectModule.members.name(), "remove", var4.getId(), String.format("Remove member %s", var3));
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void addRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("roleName");
      if (ContextHolder.getProjectId() != null) {
         Project var4 = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (var4 != null) {
            ProjectRole var5 = new ProjectRole();
            var5.setName(var3);
            var5.setProjectId(ContextHolder.getProjectId());
            var5.setType("custom");
            var5.setCreateUser(SecurityUtils.getLoginUsername(var1));
            ProjectRoleService.ins.add(var5);
            this.a((HttpServletResponse)var2, (Object)var5);
            SystemLogUtils.addProjectOperationLog(ProjectModule.permissions.name(), "manager", var5.getId(), String.format("Add role %s[%s]", var3, var5.getId()));
         }

      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void renameRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("roleId"));
      String var5 = var1.getParameter("roleName");
      ProjectRole var6 = ProjectRoleManager.ins.get(var3);
      if (ContextHolder.getProjectId() == var6.getProjectId()) {
         Project var7 = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (var7 != null) {
            String var8 = var6.getName();
            var6.setName(var5);
            var6.setUpdateUser(SecurityUtils.getLoginUsername(var1));
            ProjectRoleService.ins.update(var6);
            SystemLogUtils.addProjectOperationLog(ProjectModule.permissions.name(), "manager", var6.getId(), String.format("Change the name of role %s to %s", var5, var8));
         }

      } else {
         throw new ParameterInvaidException();
      }
   }

   @URuleAuthorization(
      authType = "project",
      model = "permissions",
      code = "manager"
   )
   public void removeRole(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("roleId"));
      ProjectRole var5 = ProjectRoleManager.ins.get(var3);
      if (var5 != null && ContextHolder.getProjectId() == var5.getProjectId()) {
         Project var6 = ProjectManager.ins.get(ContextHolder.getProjectId());
         if (var6 != null) {
            ProjectRoleService.ins.remove(var3);
            SystemLogUtils.addProjectOperationLog(ProjectModule.permissions.name(), "manager", var5.getId(), String.format("Remove role %s[%s]", var5.getName(), var3, var6.getName(), var6.getId()));
         }

      } else {
         throw new ParameterInvaidException();
      }
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      model = "setting",
      code = "approveUser"
   )
   public void updateApproveUser(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = ContextHolder.getProjectId();
      String var4 = var1.getParameter("disableApproveUser");
      String var5 = var1.getParameter("enableApproveUser");
      String var6 = var1.getParameter("deployApproveUser");
      if (StringUtils.isNotEmpty(var4)) {
         ProjectManager.ins.updateApproveUser(var3, ApplyType.enable, var5);
         SystemLogUtils.addProjectOperationLog(ProjectModule.setting.name(), "approveUser", var5, String.format("Set packet to enable approvers %s", var5));
      }

      if (StringUtils.isNotEmpty(var4)) {
         ProjectManager.ins.updateApproveUser(var3, ApplyType.disable, var4);
         SystemLogUtils.addProjectOperationLog(ProjectModule.setting.name(), "approveUser", var4, String.format("Set packet to disable approvers %s", var5));
      }

      if (StringUtils.isNotEmpty(var6)) {
         ProjectManager.ins.updateApproveUser(var3, ApplyType.deploy, var6);
         SystemLogUtils.addProjectOperationLog(ProjectModule.setting.name(), "approveUser", var6, String.format("Set packet publishing Approver %s", var5));
      }

   }

   public void countRule(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Integer var3 = FileManager.ins.newCountQuery().projectId(ContextHolder.getProjectId()).getRuleCount();
      Integer var4 = PacketManager.ins.getCount(ContextHolder.getProjectId());
      HashMap var5 = new HashMap();
      var5.put("ruleCount", var3);
      var5.put("packetCount", var4);
      this.a((HttpServletResponse)var2, (Object)var5);
   }

   public void countUserCommits(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      Calendar var5 = Calendar.getInstance();
      var5.add(5, -14);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var6 = var5.getTime();
      var5 = Calendar.getInstance();
      var5.add(5, 1);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var7 = var5.getTime();
      List var8 = ProjectService.ins.getUserCommits(var3, var6, var7);
      this.a((HttpServletResponse)var2, (Object)var8);
   }

   public void countRuleCommits(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      Calendar var5 = Calendar.getInstance();
      var5.add(5, -7);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var6 = var5.getTime();
      var5 = Calendar.getInstance();
      var5.add(5, 1);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var7 = var5.getTime();
      List var8 = ProjectService.ins.getRuleCommits(var3, var6, var7);
      this.a((HttpServletResponse)var2, (Object)var8);
   }

   public void countRuleExecCount(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      Calendar var5 = Calendar.getInstance();
      var5.add(5, -7);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var6 = var5.getTime();
      var5 = Calendar.getInstance();
      var5.add(5, 1);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var7 = var5.getTime();
      List var8 = ProjectService.ins.getRuleExecCount(var3, var6, var7);
      this.a((HttpServletResponse)var2, (Object)var8);
   }

   public void countRuleExecTime(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      Calendar var5 = Calendar.getInstance();
      var5.add(5, -7);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var6 = var5.getTime();
      var5 = Calendar.getInstance();
      var5.add(5, 1);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var7 = var5.getTime();
      List var8 = ProjectService.ins.getRuleExecTime(var3, var6, var7);
      this.a((HttpServletResponse)var2, (Object)var8);
   }

   public void countRuleDeploys(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      Calendar var5 = Calendar.getInstance();
      var5.add(5, -7);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var6 = var5.getTime();
      var5 = Calendar.getInstance();
      var5.add(5, 1);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var7 = var5.getTime();
      PacketApplyQuery var8 = PacketApplyManager.ins.newQuery();
      List var9 = var8.projectId(var3).startDate(var6).endDate(var7).type(ApplyType.deploy).status(ApplyStatus.pass).list();
      HashMap var10 = new HashMap();

      for(PacketApply var12 : (Iterable<PacketApply>)(Iterable<?>)(var9)) {
         RuleDeployVO var13 = new RuleDeployVO();
         Calendar var14 = Calendar.getInstance();
         var14.setTime(var12.getCreateDate());
         var14.set(11, 0);
         var14.set(12, 0);
         var14.set(13, 0);
         var14.set(14, 0);
         if (!var10.containsKey(var14.getTime())) {
            var10.put(var14.getTime(), var13);
         } else {
            var13 = (RuleDeployVO)var10.get(var14.getTime());
         }

         var13.setCreateDate(var14.getTime());
         var13.setCount(var13.getCount() + 1);
      }

      ArrayList var16 = new ArrayList(var10.values());
      Collections.sort(var16, new Comparator<RuleDeployVO>() {
         public int compare(RuleDeployVO var1, RuleDeployVO var2) {
            return (int)(var1.getCreateDate().getTime() - var2.getCreateDate().getTime());
         }
      });
      this.a((HttpServletResponse)var2, (Object)var16);
   }

   public void listPacketDeploys(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = ContextHolder.getGroupId();
      List var4 = ReportGroupQuery.listPacketDeploys(var3);
      if (var4.size() > 5) {
         this.a((HttpServletResponse)var2, (Object)var4.subList(0, 5));
      } else {
         this.a((HttpServletResponse)var2, (Object)var4);
      }

   }

   public void countUserRuleFiles(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      List var5 = ReportProjectQuery.countUserRuleFiles(var3);
      this.a((HttpServletResponse)var2, (Object)var5);
   }

   public void countUserLogin(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      Calendar var5 = Calendar.getInstance();
      var5.add(5, -7);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var6 = var5.getTime();
      var5 = Calendar.getInstance();
      var5.add(5, 1);
      var5.set(10, 0);
      var5.set(12, 0);
      var5.set(13, 0);
      var5.set(14, 0);
      Date var7 = var5.getTime();
      List var8 = ReportProjectQuery.countUserLogin(var3, var6, var7);
      this.a((HttpServletResponse)var2, (Object)var8);
   }

   public void listRecentFiles(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      List var5 = ReportProjectQuery.listLastModifyFiles(var3);
      if (var5.size() > 5) {
         this.a((HttpServletResponse)var2, (Object)var5.subList(0, 5));
      } else {
         this.a((HttpServletResponse)var2, (Object)var5);
      }

   }

   public void getSummary(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      int var5 = ReportProjectQuery.countPacket(var3);
      int var6 = ReportProjectQuery.countFile(var3);
      int var7 = ReportProjectQuery.countBatch(var3);
      HashMap var8 = new HashMap();
      var8.put("packetCount", var5);
      var8.put("fileCount", var6);
      var8.put("batchCount", var7);
      this.a((HttpServletResponse)var2, (Object)var8);
   }

   public String url() {
      return "/project";
   }
}
