package com.bstek.urule.console.database.service.project.role;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.admin.project.ProjectRoleVO;
import com.bstek.urule.console.database.manager.project.role.ProjectRoleManager;
import com.bstek.urule.console.database.model.ProjectRole;
import com.bstek.urule.console.database.model.Role;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.service.user.UserServiceManager;
import com.bstek.urule.console.type.GroupRoleEnum;
import java.util.ArrayList;
import java.util.List;

public class ProjectRoleServiceImpl implements ProjectRoleService {
   public List loadRoles(long var1) {
      return ProjectRoleManager.ins.loadRoles(var1);
   }

   public void add(ProjectRole var1) {
      if (ProjectRoleManager.ins.checkExist(var1.getProjectId(), var1.getName())) {
         throw new InfoException("Duplicate role name!");
      } else {
         ProjectRoleManager.ins.add(var1);
      }
   }

   public void update(ProjectRole var1) {
      if (ProjectRoleManager.ins.checkExist(var1.getProjectId(), var1.getName())) {
         throw new InfoException("Duplicate role name!");
      } else {
         ProjectRoleManager.ins.update(var1);
      }
   }

   public void remove(Long var1) {
      ProjectRoleManager.ins.remove(var1);
   }

   public void addUserRole(long var1, String var3, long var4) {
      if (ProjectRoleManager.ins.getUserRole(var3, var4) == null) {
         ProjectRoleManager.ins.addUserRole(var1, var3, var4);
      }

   }

   public void removeUserRole(String var1, long var2) {
      ProjectRoleManager.ins.removeUserRole(var1, var2);
   }

   public List loadUserRoles(long var1, String var3) {
      List var4 = ProjectRoleManager.ins.loadRoles(var1);
      List var5 = ProjectRoleManager.ins.loadUserRoles(var1, var3);
      ArrayList var6 = new ArrayList();

      for(Role var8 : (Iterable<Role>)(Iterable<?>)(var4)) {
         if (!var8.getName().equals(GroupRoleEnum.Owner.name())) {
            ProjectRoleVO var9 = new ProjectRoleVO();
            var9.setId(var8.getId());
            var9.setName(var8.getName());
            var9.setType(var8.getType());
            var9.setProjectId(var1);

            for(Role var11 : (Iterable<Role>)(Iterable<?>)(var5)) {
               if (var11.getId() == var8.getId()) {
                  var9.setSelected(true);
                  break;
               }
            }

            var6.add(var9);
         }
      }

      return var6;
   }

   public List users(long var1, long var3) {
      List var5 = ProjectRoleManager.ins.loadRoleUsers(var1, var3);
      ArrayList var6 = new ArrayList();

      for(User var8 : (Iterable<User>)(Iterable<?>)(var5)) {
         var6.add(UserServiceManager.getUserService().get(var8.getId()));
      }

      return null;
   }
}
