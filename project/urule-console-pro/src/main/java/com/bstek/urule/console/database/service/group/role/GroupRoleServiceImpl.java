package com.bstek.urule.console.database.service.group.role;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.admin.group.GroupRoleVO;
import com.bstek.urule.console.database.manager.group.role.GroupRoleManager;
import com.bstek.urule.console.database.model.GroupRole;
import com.bstek.urule.console.database.model.Role;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.service.user.UserServiceManager;
import com.bstek.urule.console.type.GroupRoleEnum;
import java.util.ArrayList;
import java.util.List;

/**团队角色服务类*/
public class GroupRoleServiceImpl implements GroupRoleService {
   public List loadRoles(String groupId) throws Exception {
      return GroupRoleManager.ins.loadRoles(groupId);
   }
   public List loadUserRoles(String groupId, String account) throws Exception {
      List roles = GroupRoleManager.ins.loadRoles(groupId);
      List userRoles = GroupRoleManager.ins.loadUserRoles(groupId, account);
      ArrayList userRoles2 = new ArrayList();

      for(Role role : (Iterable<Role>)(Iterable<?>)(roles)) {
         GroupRoleVO groupRoleVO = new GroupRoleVO();
         groupRoleVO.setId(role.getId());
         groupRoleVO.setName(role.getName());
         groupRoleVO.setType(role.getType());

         for(Role role2 : (Iterable<Role>)(Iterable<?>)(userRoles)) {
            if (role2.getId() == role.getId()) {
               groupRoleVO.setSelected(true);
               break;
            }
         }

         userRoles2.add(groupRoleVO);
      }

      return userRoles2;
   }
   public void add(GroupRole role) throws Exception {
      if (GroupRoleManager.ins.checkExist(role.getGroupId(), role.getName())) {
         throw new InfoException("Duplicate role name!");
      } else {
         GroupRoleManager.ins.add(role);
      }
   }
   public void update(GroupRole role) throws Exception {
      if (GroupRoleManager.ins.checkExist(role.getGroupId(), role.getName())) {
         throw new InfoException("Duplicate role name!");
      } else {
         GroupRoleManager.ins.update(role);
      }
   }
   public void remove(Long id) throws Exception {
      GroupRoleManager.ins.remove(id);
   }
   public void addUserRole(String groupId, String userId, long roleId) {
      if (GroupRoleManager.ins.getUserRole(userId, roleId) == null) {
         GroupRoleManager.ins.addUserRole(groupId, userId, roleId);
      }

   }
   public void removeUserRole(String groupId, String userId, long roleId) throws Exception {
      GroupRole groupRole = GroupRoleManager.ins.get(roleId);
      if (((Role)groupRole).getName().equals(GroupRoleEnum.Owner.name())) {
         throw new InfoException("系统不支持该操作!");
      } else {
         GroupRoleManager.ins.removeUserRole(groupId, userId, roleId);
      }
   }
   public GroupRole get(long roleId) throws Exception {
      return GroupRoleManager.ins.get(roleId);
   }
   public List users(String groupId, long roleId) throws Exception {
      List roleUsers = GroupRoleManager.ins.loadRoleUsers(groupId, roleId);
      ArrayList items = new ArrayList();

      for(User user : (Iterable<User>)(Iterable<?>)(roleUsers)) {
         items.add(UserServiceManager.getUserService().get(user.getId()));
      }

      return null;
   }
}
