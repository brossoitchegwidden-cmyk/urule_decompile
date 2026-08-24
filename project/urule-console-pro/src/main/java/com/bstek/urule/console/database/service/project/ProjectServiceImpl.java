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
   public void add(Project project) {
      try {
         String name = project.getName();
         if (StringUtils.isBlank(name)) {
            throw new InfoException("项目名称不能为空.<br/>The project name can not empty.");
         } else if (StringUtils.hasSpecialChar(name)) {
            throw new InfoException("项目名称不能包含特殊字符.<br/>Project name cannot contain special characters.");
         } else {
            List items = ProjectManager.ins.newQuery().groupId(project.getGroupId()).name(project.getName()).list();
            if (items.size() > 0) {
               throw new InfoException("项目名称重复.<br/>Duplicate project name.");
            } else {
               ProjectManager.ins.add(project);
               User user = UserServiceManager.getUserService().get(project.getCreateUser());
               ProjectManager.ins.addProjectUser(project.getId(), project.getCreateUser(), user.getName());

               for(ProjectRoleEnum projectRoleEnum : ProjectRoleEnum.values()) {
                  ProjectRole projectRole = new ProjectRole();
                  projectRole.setProjectId(project.getId());
                  projectRole.setType("system");
                  projectRole.setName(projectRoleEnum.name());
                  projectRole.setCreateUser(project.getCreateUser());
                  ProjectRoleManager.ins.add(projectRole);
                  this.processProjectRole(projectRole);
                  if (projectRoleEnum == ProjectRoleEnum.Manager) {
                     ProjectRoleService.ins.addUserRole(project.getId(), project.getCreateUser(), projectRole.getId());
                  }
               }

            }
         }
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }

   private void processProjectRole(ProjectRole projectRole) {
      long id = projectRole.getId();
      List projectModules = PermissionProvider.getProjectModules();

      for(Module module : (Iterable<Module>)(Iterable<?>)(projectModules)) {
         for(Permission permission : (Iterable<Permission>)(Iterable<?>)(module.getItems())) {
            boolean flag = false;

            for(String text : (Iterable<String>)(Iterable<?>)(permission.getRoles())) {
               if (text.equals(projectRole.getName())) {
                  flag = true;
               }
            }

            permission.setChecked(flag);
            permission.setDisabled(false);
         }
      }

      AuthorityService.ins.initPermissions(id, projectModules);
   }
   public void update(Project project) {
      String name = project.getName();
      if (StringUtils.isBlank(name)) {
         throw new InfoException("项目名称不能为空.<br/>The project name can not empty.");
      } else if (StringUtils.hasSpecialChar(name)) {
         throw new InfoException("项目名称不能包含特殊字符.<br/>Project name cannot contain special characters.");
      } else {
         ProjectManager.ins.update(project);
      }
   }
   public List remove(long projectId) {
      try {
         Project project = ProjectManager.ins.get(projectId);
         List removeResult = PacketCache.ins.removeProject(projectId, project.getGroupId());
         OperationLogManager.ins.removeByProjectId(projectId);
         KnowledgeLogManager.ins.removeByProject(projectId);
         ScenarioManager.ins.deleteByProjectId(projectId);
         PacketApplyManager.ins.deleteByProjectId(projectId);
         PacketDeployManager.ins.deleteByProjectId(projectId);
         PacketManager.ins.deleteByProjectId(projectId);
         BatchManagerHelper.removeByProjectId(projectId);
         VersionFileManager.ins.deleteByProjectId(projectId);
         DirectoryManager.ins.deleteByProjectId(projectId);
         FileManager.ins.deleteByProjectId(projectId);

         for(Role role : (Iterable<Role>)(Iterable<?>)(ProjectRoleManager.ins.loadRoles(projectId))) {
            AuthorityManager.ins.removeByRole(RoleCategory.project.name(), role.getId());
         }

         ProjectRoleManager.ins.removeByProjectId(projectId);
         ProjectManager.ins.removeProjectUsers(projectId);
         ProjectManager.ins.remove(projectId);
         return removeResult;
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }
   public void addProjectuser(long projectId, String account) {
      User projectUser = UserManager.ins.getProjectUser(projectId, account);
      if (projectUser == null) {
         User user = UserServiceManager.getUserService().get(account);
         ProjectManager.ins.addProjectUser(projectId, account, user.getName());
         ProjectRole projectRole = ProjectRoleManager.ins.get(projectId, ProjectRoleEnum.User.name());
         if (projectRole != null) {
            ProjectRoleService.ins.addUserRole(projectId, account, ((Role)projectRole).getId());
         }
      }

   }
   public List getUserCommits(Long projectId, Date startDate, Date endDate) {
      FileCountQuery fileCountQuery = FileManager.ins.newCountQuery();
      List userCommits = fileCountQuery.projectId(projectId).updateDateBegin(startDate).updateDateEnd(endDate).getUserCommits();
      HashMap valuesByKey = new HashMap();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(userCommits)) {
         UserCommitVO userCommitVO = new UserCommitVO();
         if (!valuesByKey.containsKey(ruleFile.getUpdateUser())) {
            userCommitVO.setUserId(ruleFile.getUpdateUser());
            User user = UserServiceManager.getUserService().get(ruleFile.getUpdateUser());
            userCommitVO.setUserName(user != null ? user.getName() : ruleFile.getUpdateUser());
            valuesByKey.put(ruleFile.getUpdateUser(), userCommitVO);
         } else {
            userCommitVO = (UserCommitVO)valuesByKey.get(ruleFile.getUpdateUser());
         }

         userCommitVO.setCount(userCommitVO.getCount() + 1);
      }

      ArrayList items = new ArrayList(valuesByKey.values());
      Collections.sort(items, new Comparator<UserCommitVO>() {
         public int compare(UserCommitVO userCommitVO, UserCommitVO userCommitVO2) {
            return userCommitVO2.getCount() - userCommitVO.getCount();
         }
      });
      return (List)(items.size() > 5 ? items.subList(0, 4) : items);
   }
   public List getRuleCommits(Long projectId, Date startDate, Date endDate) {
      FileCountQuery fileCountQuery = FileManager.ins.newCountQuery();
      List ruleCommits = fileCountQuery.projectId(projectId).updateDateBegin(startDate).updateDateEnd(endDate).getRuleCommits();
      HashMap valuesByKey = new HashMap();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(ruleCommits)) {
         RuleCommitVO ruleCommitVO = new RuleCommitVO();
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(ruleFile.getModifyDate());
         calendar.set(11, 0);
         calendar.set(12, 0);
         calendar.set(13, 0);
         calendar.set(14, 0);
         if (!valuesByKey.containsKey(calendar.getTime())) {
            ruleCommitVO.setCreateDate(calendar.getTime());
            valuesByKey.put(calendar.getTime(), ruleCommitVO);
         } else {
            ruleCommitVO = (RuleCommitVO)valuesByKey.get(calendar.getTime());
         }

         ruleCommitVO.setCount(ruleCommitVO.getCount() + 1);
      }

      ArrayList ruleCommits2 = new ArrayList(valuesByKey.values());
      Collections.sort(ruleCommits2, new Comparator<RuleCommitVO>() {
         public int compare(RuleCommitVO ruleCommitVO, RuleCommitVO ruleCommitVO2) {
            return (int)(ruleCommitVO.getCreateDate().getTime() - ruleCommitVO2.getCreateDate().getTime());
         }
      });
      return ruleCommits2;
   }
   public List getRuleDeploys(Long projectId, Date startDate, Date endDate) {
      PacketApplyQuery packetApplyQuery = PacketApplyManager.ins.newQuery();
      List items = packetApplyQuery.projectId(projectId).startDate(startDate).endDate(endDate).type(ApplyType.deploy).status(ApplyStatus.pass).list();
      HashMap valuesByKey = new HashMap();

      for(PacketApply packetApply : (Iterable<PacketApply>)(Iterable<?>)(items)) {
         RuleDeployVO ruleDeployVO = new RuleDeployVO();
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(packetApply.getCreateDate());
         calendar.set(11, 0);
         calendar.set(12, 0);
         calendar.set(13, 0);
         calendar.set(14, 0);
         if (!valuesByKey.containsKey(calendar.getTime())) {
            valuesByKey.put(calendar.getTime(), ruleDeployVO);
         } else {
            ruleDeployVO = (RuleDeployVO)valuesByKey.get(calendar.getTime());
         }

         ruleDeployVO.setCreateDate(calendar.getTime());
         ruleDeployVO.setCount(ruleDeployVO.getCount() + 1);
      }

      ArrayList ruleDeploys = new ArrayList(valuesByKey.values());
      Collections.sort(ruleDeploys, new Comparator<RuleDeployVO>() {
         public int compare(RuleDeployVO ruleDeployVO, RuleDeployVO ruleDeployVO2) {
            return (int)(ruleDeployVO.getCreateDate().getTime() - ruleDeployVO2.getCreateDate().getTime());
         }
      });
      return ruleDeploys;
   }
   public List getRuleExecCount(Long projectId, Date startDate, Date endDate) {
      KnowledgeLogCountQuery knowledgeLogCountQuery = KnowledgeLogManager.ins.newCountQuery();
      knowledgeLogCountQuery.projectId(projectId);
      knowledgeLogCountQuery.dateBegin(startDate);
      knowledgeLogCountQuery.dateEnd(endDate);
      return knowledgeLogCountQuery.listExec();
   }
   public List getRuleExecTime(Long projectId, Date startDate, Date endDate) {
      KnowledgeLogCountQuery knowledgeLogCountQuery = KnowledgeLogManager.ins.newCountQuery();
      knowledgeLogCountQuery.projectId(projectId);
      knowledgeLogCountQuery.dateBegin(startDate);
      knowledgeLogCountQuery.dateEnd(endDate);
      List items = knowledgeLogCountQuery.listTime();
      return items.size() > 10 ? items.subList(0, 9) : items;
   }
}
