package com.bstek.urule.console.database.service.project;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.database.manager.authority.AuthorityManager;
import com.bstek.urule.console.database.manager.authority.AuthorityService;
import com.bstek.urule.console.database.manager.batch.BatchManagerHelper;
import com.bstek.urule.console.database.manager.file.DirectoryManager;
import com.bstek.urule.console.database.manager.file.FileCountQuery;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.version.VersionFileManager;
import com.bstek.urule.console.database.manager.log.KnowledgeLogCountQuery;
import com.bstek.urule.console.database.manager.log.KnowledgeLogManager;
import com.bstek.urule.console.database.manager.log.OperationLogManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyManager;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyQuery;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.manager.packet.scenario.ScenarioManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.manager.project.role.ProjectRoleManager;
import com.bstek.urule.console.database.manager.user.UserManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.PacketApply;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectRole;
import com.bstek.urule.console.database.model.Role;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.service.project.role.ProjectRoleService;
import com.bstek.urule.console.database.service.user.UserServiceManager;
import com.bstek.urule.console.database.vo.RuleCommitVO;
import com.bstek.urule.console.database.vo.RuleDeployVO;
import com.bstek.urule.console.database.vo.UserCommitVO;
import com.bstek.urule.console.security.entity.Module;
import com.bstek.urule.console.security.entity.Permission;
import com.bstek.urule.console.security.provider.PermissionProvider;
import com.bstek.urule.console.type.ProjectRoleEnum;
import com.bstek.urule.console.type.RoleCategory;
import com.bstek.urule.console.util.StringUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {
   public void add(Project var1) {
      try {
         String var2 = var1.getName();
         if (StringUtils.isBlank(var2)) {
            throw new InfoException("项目名称不能为空.<br/>The project name can not empty.");
         } else if (StringUtils.hasSpecialChar(var2)) {
            throw new InfoException("项目名称不能包含特殊字符.<br/>Project name cannot contain special characters.");
         } else {
            List var3 = ProjectManager.ins.newQuery().groupId(var1.getGroupId()).name(var1.getName()).list();
            if (var3.size() > 0) {
               throw new InfoException("项目名称重复.<br/>Duplicate project name.");
            } else {
               ProjectManager.ins.add(var1);
               User var4 = UserServiceManager.getUserService().get(var1.getCreateUser());
               ProjectManager.ins.addProjectUser(var1.getId(), var1.getCreateUser(), var4.getName());

               for(ProjectRoleEnum var8 : ProjectRoleEnum.values()) {
                  ProjectRole var9 = new ProjectRole();
                  var9.setProjectId(var1.getId());
                  var9.setType("system");
                  var9.setName(var8.name());
                  var9.setCreateUser(var1.getCreateUser());
                  ProjectRoleManager.ins.add(var9);
                  this.a(var9);
                  if (var8 == ProjectRoleEnum.Manager) {
                     ProjectRoleService.ins.addUserRole(var1.getId(), var1.getCreateUser(), var9.getId());
                  }
               }

            }
         }
      } catch (Exception var10) {
         throw new InfoException(var10);
      }
   }

   private void a(ProjectRole var1) {
      long var2 = var1.getId();
      List var4 = PermissionProvider.getProjectModules();

      for(Module var6 : (Iterable<Module>)(Iterable<?>)(var4)) {
         for(Permission var8 : (Iterable<Permission>)(Iterable<?>)(var6.getItems())) {
            boolean var9 = false;

            for(String var11 : (Iterable<String>)(Iterable<?>)(var8.getRoles())) {
               if (var11.equals(var1.getName())) {
                  var9 = true;
               }
            }

            var8.setChecked(var9);
            var8.setDisabled(false);
         }
      }

      AuthorityService.ins.initPermissions(var2, var4);
   }

   public void update(Project var1) {
      String var2 = var1.getName();
      if (StringUtils.isBlank(var2)) {
         throw new InfoException("项目名称不能为空.<br/>The project name can not empty.");
      } else if (StringUtils.hasSpecialChar(var2)) {
         throw new InfoException("项目名称不能包含特殊字符.<br/>Project name cannot contain special characters.");
      } else {
         ProjectManager.ins.update(var1);
      }
   }

   public List remove(long var1) {
      try {
         Project var3 = ProjectManager.ins.get(var1);
         List var4 = PacketCache.ins.removeProject(var1, var3.getGroupId());
         OperationLogManager.ins.removeByProjectId(var1);
         KnowledgeLogManager.ins.removeByProject(var1);
         ScenarioManager.ins.deleteByProjectId(var1);
         PacketApplyManager.ins.deleteByProjectId(var1);
         PacketDeployManager.ins.deleteByProjectId(var1);
         PacketManager.ins.deleteByProjectId(var1);
         BatchManagerHelper.removeByProjectId(var1);
         VersionFileManager.ins.deleteByProjectId(var1);
         DirectoryManager.ins.deleteByProjectId(var1);
         FileManager.ins.deleteByProjectId(var1);

         for(Role var7 : (Iterable<Role>)(Iterable<?>)(ProjectRoleManager.ins.loadRoles(var1))) {
            AuthorityManager.ins.removeByRole(RoleCategory.project.name(), var7.getId());
         }

         ProjectRoleManager.ins.removeByProjectId(var1);
         ProjectManager.ins.removeProjectUsers(var1);
         ProjectManager.ins.remove(var1);
         return var4;
      } catch (Exception var8) {
         throw new InfoException(var8);
      }
   }

   public void addProjectuser(long var1, String var3) {
      User var4 = UserManager.ins.getProjectUser(var1, var3);
      if (var4 == null) {
         User var5 = UserServiceManager.getUserService().get(var3);
         ProjectManager.ins.addProjectUser(var1, var3, var5.getName());
         ProjectRole var6 = ProjectRoleManager.ins.get(var1, ProjectRoleEnum.User.name());
         if (var6 != null) {
            ProjectRoleService.ins.addUserRole(var1, var3, ((Role)var6).getId());
         }
      }

   }

   public List getUserCommits(Long var1, Date var2, Date var3) {
      FileCountQuery var4 = FileManager.ins.newCountQuery();
      List var5 = var4.projectId(var1).updateDateBegin(var2).updateDateEnd(var3).getUserCommits();
      HashMap var6 = new HashMap();

      for(RuleFile var8 : (Iterable<RuleFile>)(Iterable<?>)(var5)) {
         UserCommitVO var9 = new UserCommitVO();
         if (!var6.containsKey(var8.getUpdateUser())) {
            var9.setUserId(var8.getUpdateUser());
            User var10 = UserServiceManager.getUserService().get(var8.getUpdateUser());
            var9.setUserName(var10 != null ? var10.getName() : var8.getUpdateUser());
            var6.put(var8.getUpdateUser(), var9);
         } else {
            var9 = (UserCommitVO)var6.get(var8.getUpdateUser());
         }

         var9.setCount(var9.getCount() + 1);
      }

      ArrayList var11 = new ArrayList(var6.values());
      Collections.sort(var11, new Comparator<UserCommitVO>() {
         public int compare(UserCommitVO var1, UserCommitVO var2) {
            return var2.getCount() - var1.getCount();
         }
      });
      return (List)(var11.size() > 5 ? var11.subList(0, 4) : var11);
   }

   public List getRuleCommits(Long var1, Date var2, Date var3) {
      FileCountQuery var4 = FileManager.ins.newCountQuery();
      List var5 = var4.projectId(var1).updateDateBegin(var2).updateDateEnd(var3).getRuleCommits();
      HashMap var6 = new HashMap();

      for(RuleFile var8 : (Iterable<RuleFile>)(Iterable<?>)(var5)) {
         RuleCommitVO var9 = new RuleCommitVO();
         Calendar var10 = Calendar.getInstance();
         var10.setTime(var8.getModifyDate());
         var10.set(11, 0);
         var10.set(12, 0);
         var10.set(13, 0);
         var10.set(14, 0);
         if (!var6.containsKey(var10.getTime())) {
            var9.setCreateDate(var10.getTime());
            var6.put(var10.getTime(), var9);
         } else {
            var9 = (RuleCommitVO)var6.get(var10.getTime());
         }

         var9.setCount(var9.getCount() + 1);
      }

      ArrayList var11 = new ArrayList(var6.values());
      Collections.sort(var11, new Comparator<RuleCommitVO>() {
         public int compare(RuleCommitVO var1, RuleCommitVO var2) {
            return (int)(var1.getCreateDate().getTime() - var2.getCreateDate().getTime());
         }
      });
      return var11;
   }

   public List getRuleDeploys(Long var1, Date var2, Date var3) {
      PacketApplyQuery var4 = PacketApplyManager.ins.newQuery();
      List var5 = var4.projectId(var1).startDate(var2).endDate(var3).type(ApplyType.deploy).status(ApplyStatus.pass).list();
      HashMap var6 = new HashMap();

      for(PacketApply var8 : (Iterable<PacketApply>)(Iterable<?>)(var5)) {
         RuleDeployVO var9 = new RuleDeployVO();
         Calendar var10 = Calendar.getInstance();
         var10.setTime(var8.getCreateDate());
         var10.set(11, 0);
         var10.set(12, 0);
         var10.set(13, 0);
         var10.set(14, 0);
         if (!var6.containsKey(var10.getTime())) {
            var6.put(var10.getTime(), var9);
         } else {
            var9 = (RuleDeployVO)var6.get(var10.getTime());
         }

         var9.setCreateDate(var10.getTime());
         var9.setCount(var9.getCount() + 1);
      }

      ArrayList var11 = new ArrayList(var6.values());
      Collections.sort(var11, new Comparator<RuleDeployVO>() {
         public int compare(RuleDeployVO var1, RuleDeployVO var2) {
            return (int)(var1.getCreateDate().getTime() - var2.getCreateDate().getTime());
         }
      });
      return var11;
   }

   public List getRuleExecCount(Long var1, Date var2, Date var3) {
      KnowledgeLogCountQuery var4 = KnowledgeLogManager.ins.newCountQuery();
      var4.projectId(var1);
      var4.dateBegin(var2);
      var4.dateEnd(var3);
      return var4.listExec();
   }

   public List getRuleExecTime(Long var1, Date var2, Date var3) {
      KnowledgeLogCountQuery var4 = KnowledgeLogManager.ins.newCountQuery();
      var4.projectId(var1);
      var4.dateBegin(var2);
      var4.dateEnd(var3);
      List var5 = var4.listTime();
      return var5.size() > 10 ? var5.subList(0, 9) : var5;
   }
}
