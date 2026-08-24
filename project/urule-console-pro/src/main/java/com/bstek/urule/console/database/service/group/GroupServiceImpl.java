package com.bstek.urule.console.database.service.group;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.config.Configure;
import com.bstek.urule.console.database.manager.authority.AuthorityManager;
import com.bstek.urule.console.database.manager.authority.AuthorityService;
import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.group.role.GroupRoleManager;
import com.bstek.urule.console.database.manager.invite.InviteManager;
import com.bstek.urule.console.database.manager.log.OperationLogManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.manager.project.role.ProjectRoleManager;
import com.bstek.urule.console.database.manager.user.UserManager;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.GroupRole;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.Role;
import com.bstek.urule.console.database.service.group.role.GroupRoleService;
import com.bstek.urule.console.database.service.project.ProjectService;
import com.bstek.urule.console.database.service.user.UserServiceManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.entity.Module;
import com.bstek.urule.console.security.entity.Permission;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.security.provider.PermissionProvider;
import com.bstek.urule.console.type.GroupRoleEnum;
import com.bstek.urule.console.type.RoleCategory;
import java.util.List;

public class GroupServiceImpl implements GroupService {
   public boolean isFreeCreate(String account) {
      boolean isFreeCreateResult = Configure.getConfigure().isFreeCreateGroup();
      int number = GroupManager.ins.count();
      boolean flag = false;

      for(Group group : (Iterable<Group>)(Iterable<?>)(GroupManager.ins.createQuery().list(account))) {
         if (account.equals(group.getCreateUser())) {
            flag = true;
         }
      }

      if (!isFreeCreateResult && (number == 0 || flag)) {
         isFreeCreateResult = true;
      }

      return isFreeCreateResult;
   }
   public void add(Group group) {
      User loginUser = SecurityUtils.getLoginUser(RequestHolder.getRequest());
      if (!this.isFreeCreate(loginUser.getName())) {
         boolean flag = false;

         for(Group group2 : (Iterable<Group>)(Iterable<?>)(loginUser.getGroups())) {
            if (loginUser.getName().equals(group2.getCreateUser())) {
               flag = true;
               break;
            }
         }

         if (!flag) {
            throw new InfoException("权限不足，不能进行此操作.<br/>You are not authorized to do this action.");
         }
      }

      try {
         Group group3 = GroupManager.ins.get(group.getId());
         if (null != group3) {
            throw new InfoException("团队编号重复.<br/>Duplicate team id.");
         } else {
            List items = GroupManager.ins.createQuery().name(group.getName()).list();
            if (items.size() > 0) {
               throw new InfoException("团队名称重复.<br/>Duplicate team name.");
            } else {
               GroupManager.ins.add(group);
               com.bstek.urule.console.database.model.User user = UserServiceManager.getUserService().get(group.getCreateUser());
               GroupManager.ins.addGroupUser(group.getId(), group.getCreateUser(), user.getName());

               for(GroupRoleEnum groupRoleEnum : GroupRoleEnum.values()) {
                  GroupRole groupRole = new GroupRole();
                  groupRole.setGroupId(group.getId());
                  groupRole.setType("system");
                  groupRole.setName(groupRoleEnum.name());
                  groupRole.setCreateUser(group.getCreateUser());
                  GroupRoleManager.ins.add(groupRole);
                  this.processGroupRole(groupRole);
                  if (GroupRoleEnum.Owner == groupRoleEnum) {
                     GroupRoleService.ins.addUserRole(group.getId(), group.getCreateUser(), groupRole.getId());
                  }
               }

            }
         }
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }

   private void processGroupRole(GroupRole groupRole) {
      if (!GroupRoleEnum.Owner.name().equals(groupRole.getName())) {
         List groupModules = PermissionProvider.getGroupModules();

         for(Module module : (Iterable<Module>)(Iterable<?>)(groupModules)) {
            for(Permission permission : (Iterable<Permission>)(Iterable<?>)(module.getItems())) {
               boolean flag = false;

               for(String text : (Iterable<String>)(Iterable<?>)(permission.getRoles())) {
                  if (text.equals(groupRole.getName())) {
                     flag = true;
                  }
               }

               permission.setChecked(flag);
               permission.setDisabled(false);
            }
         }

         AuthorityService.ins.initPermissions(groupRole.getId(), groupModules);
      }
   }
   public void remove(String groupId) {
      try {
         OperationLogManager.ins.removeByGroupId(groupId);

         for(Project project : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.getProjectsByGroupId(groupId))) {
            ProjectService.ins.remove(project.getId());
         }

         InviteManager.ins.removeByGroupId(groupId);

         for(Role role : (Iterable<Role>)(Iterable<?>)(GroupRoleManager.ins.loadRoles(groupId))) {
            AuthorityManager.ins.removeByRole(RoleCategory.group.name(), role.getId());
         }

         GroupRoleManager.ins.removeByGroupId(groupId);
         GroupManager.ins.removeGroupUsers(groupId);
         GroupManager.ins.remove(groupId);
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }
   public void addGroupUser(String groupId, String account) {
      com.bstek.urule.console.database.model.User groupUser = UserManager.ins.getGroupUser(groupId, account);
      if (groupUser == null) {
         com.bstek.urule.console.database.model.User user = UserServiceManager.getUserService().get(account);
         GroupManager.ins.addGroupUser(groupId, account, user.getName());
         GroupRole groupRole = GroupRoleManager.ins.get(groupId, GroupRoleEnum.User.name());
         if (groupRole != null) {
            GroupRoleService.ins.addUserRole(groupId, account, ((Role)groupRole).getId());
         }
      }

   }
   public void removeGroupUser(String groupId, String account) {
      for(Project project : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.getProjectsByGroupId(groupId))) {
         ProjectRoleManager.ins.removeUserRoles(account);
         ProjectManager.ins.removeProjectUser(project.getId(), account);
      }

      GroupRoleManager.ins.removeUserRoles(groupId, account);
      GroupManager.ins.removeGroupUser(groupId, account);
   }
   public Group get(String groupId) {
      return GroupManager.ins.get(groupId);
   }
}
