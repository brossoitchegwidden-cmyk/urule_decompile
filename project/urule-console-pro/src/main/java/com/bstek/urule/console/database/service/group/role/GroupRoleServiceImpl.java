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

public class GroupRoleServiceImpl implements GroupRoleService {
   public List loadRoles(String var1) throws Exception {
      return GroupRoleManager.ins.loadRoles(var1);
   }

   public List loadUserRoles(String var1, String var2) throws Exception {
      List var3 = GroupRoleManager.ins.loadRoles(var1);
      List var4 = GroupRoleManager.ins.loadUserRoles(var1, var2);
      ArrayList var5 = new ArrayList();

      for(Role var7 : (Iterable<Role>)(Iterable<?>)(var3)) {
         GroupRoleVO var8 = new GroupRoleVO();
         var8.setId(var7.getId());
         var8.setName(var7.getName());
         var8.setType(var7.getType());

         for(Role var10 : (Iterable<Role>)(Iterable<?>)(var4)) {
            if (var10.getId() == var7.getId()) {
               var8.setSelected(true);
               break;
            }
         }

         var5.add(var8);
      }

      return var5;
   }

   public void add(GroupRole var1) throws Exception {
      if (GroupRoleManager.ins.checkExist(var1.getGroupId(), var1.getName())) {
         throw new InfoException("Duplicate role name!");
      } else {
         GroupRoleManager.ins.add(var1);
      }
   }

   public void update(GroupRole var1) throws Exception {
      if (GroupRoleManager.ins.checkExist(var1.getGroupId(), var1.getName())) {
         throw new InfoException("Duplicate role name!");
      } else {
         GroupRoleManager.ins.update(var1);
      }
   }

   public void remove(Long var1) throws Exception {
      GroupRoleManager.ins.remove(var1);
   }

   public void addUserRole(String var1, String var2, long var3) {
      if (GroupRoleManager.ins.getUserRole(var2, var3) == null) {
         GroupRoleManager.ins.addUserRole(var1, var2, var3);
      }

   }

   public void removeUserRole(String var1, String var2, long var3) throws Exception {
      GroupRole var5 = GroupRoleManager.ins.get(var3);
      if (((Role)var5).getName().equals(GroupRoleEnum.Owner.name())) {
         throw new InfoException("系统不支持该操作!");
      } else {
         GroupRoleManager.ins.removeUserRole(var1, var2, var3);
      }
   }

   public GroupRole get(long var1) throws Exception {
      return GroupRoleManager.ins.get(var1);
   }

   public List users(String var1, long var2) throws Exception {
      List var4 = GroupRoleManager.ins.loadRoleUsers(var1, var2);
      ArrayList var5 = new ArrayList();

      for(User var7 : (Iterable<User>)(Iterable<?>)(var4)) {
         var5.add(UserServiceManager.getUserService().get(var7.getId()));
      }

      return null;
   }
}
