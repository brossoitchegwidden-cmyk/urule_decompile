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
   public boolean isFreeCreate(String var1) {
      boolean var2 = Configure.getConfigure().isFreeCreateGroup();
      int var3 = GroupManager.ins.count();
      boolean var4 = false;

      for(Group var7 : (Iterable<Group>)(Iterable<?>)(GroupManager.ins.createQuery().list(var1))) {
         if (var1.equals(var7.getCreateUser())) {
            var4 = true;
         }
      }

      if (!var2 && (var3 == 0 || var4)) {
         var2 = true;
      }

      return var2;
   }

   public void add(Group var1) {
      User var2 = SecurityUtils.getLoginUser(RequestHolder.getRequest());
      if (!this.isFreeCreate(var2.getName())) {
         boolean var3 = false;

         for(Group var5 : (Iterable<Group>)(Iterable<?>)(var2.getGroups())) {
            if (var2.getName().equals(var5.getCreateUser())) {
               var3 = true;
               break;
            }
         }

         if (!var3) {
            throw new InfoException("权限不足，不能进行此操作.<br/>You are not authorized to do this action.");
         }
      }

      try {
         Group var12 = GroupManager.ins.get(var1.getId());
         if (null != var12) {
            throw new InfoException("团队编号重复.<br/>Duplicate team id.");
         } else {
            List var13 = GroupManager.ins.createQuery().name(var1.getName()).list();
            if (var13.size() > 0) {
               throw new InfoException("团队名称重复.<br/>Duplicate team name.");
            } else {
               GroupManager.ins.add(var1);
               com.bstek.urule.console.database.model.User var14 = UserServiceManager.getUserService().get(var1.getCreateUser());
               GroupManager.ins.addGroupUser(var1.getId(), var1.getCreateUser(), var14.getName());

               for(GroupRoleEnum var9 : GroupRoleEnum.values()) {
                  GroupRole var10 = new GroupRole();
                  var10.setGroupId(var1.getId());
                  var10.setType("system");
                  var10.setName(var9.name());
                  var10.setCreateUser(var1.getCreateUser());
                  GroupRoleManager.ins.add(var10);
                  this.a(var10);
                  if (GroupRoleEnum.Owner == var9) {
                     GroupRoleService.ins.addUserRole(var1.getId(), var1.getCreateUser(), var10.getId());
                  }
               }

            }
         }
      } catch (Exception var11) {
         throw new InfoException(var11);
      }
   }

   private void a(GroupRole var1) {
      if (!GroupRoleEnum.Owner.name().equals(var1.getName())) {
         List var2 = PermissionProvider.getGroupModules();

         for(Module var4 : (Iterable<Module>)(Iterable<?>)(var2)) {
            for(Permission var6 : (Iterable<Permission>)(Iterable<?>)(var4.getItems())) {
               boolean var7 = false;

               for(String var9 : (Iterable<String>)(Iterable<?>)(var6.getRoles())) {
                  if (var9.equals(var1.getName())) {
                     var7 = true;
                  }
               }

               var6.setChecked(var7);
               var6.setDisabled(false);
            }
         }

         AuthorityService.ins.initPermissions(var1.getId(), var2);
      }
   }

   public void remove(String var1) {
      try {
         OperationLogManager.ins.removeByGroupId(var1);

         for(Project var4 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.getProjectsByGroupId(var1))) {
            ProjectService.ins.remove(var4.getId());
         }

         InviteManager.ins.removeByGroupId(var1);

         for(Role var5 : (Iterable<Role>)(Iterable<?>)(GroupRoleManager.ins.loadRoles(var1))) {
            AuthorityManager.ins.removeByRole(RoleCategory.group.name(), var5.getId());
         }

         GroupRoleManager.ins.removeByGroupId(var1);
         GroupManager.ins.removeGroupUsers(var1);
         GroupManager.ins.remove(var1);
      } catch (Exception var6) {
         throw new InfoException(var6);
      }
   }

   public void addGroupUser(String var1, String var2) {
      com.bstek.urule.console.database.model.User var3 = UserManager.ins.getGroupUser(var1, var2);
      if (var3 == null) {
         com.bstek.urule.console.database.model.User var4 = UserServiceManager.getUserService().get(var2);
         GroupManager.ins.addGroupUser(var1, var2, var4.getName());
         GroupRole var5 = GroupRoleManager.ins.get(var1, GroupRoleEnum.User.name());
         if (var5 != null) {
            GroupRoleService.ins.addUserRole(var1, var2, ((Role)var5).getId());
         }
      }

   }

   public void removeGroupUser(String var1, String var2) {
      for(Project var5 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.getProjectsByGroupId(var1))) {
         ProjectRoleManager.ins.removeUserRoles(var2);
         ProjectManager.ins.removeProjectUser(var5.getId(), var2);
      }

      GroupRoleManager.ins.removeUserRoles(var1, var2);
      GroupManager.ins.removeGroupUser(var1, var2);
   }

   public Group get(String var1) {
      return GroupManager.ins.get(var1);
   }
}
