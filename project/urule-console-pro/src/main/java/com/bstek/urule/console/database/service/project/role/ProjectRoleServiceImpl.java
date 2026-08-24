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

/**项目角色服务类*/
public class ProjectRoleServiceImpl implements ProjectRoleService {
   public List loadRoles(long projectId) {
      return ProjectRoleManager.ins.loadRoles(projectId);
   }
   public void add(ProjectRole role) {
      if (ProjectRoleManager.ins.checkExist(role.getProjectId(), role.getName())) {
         throw new InfoException("Duplicate role name!");
      } else {
         ProjectRoleManager.ins.add(role);
      }
   }
   public void update(ProjectRole role) {
      if (ProjectRoleManager.ins.checkExist(role.getProjectId(), role.getName())) {
         throw new InfoException("Duplicate role name!");
      } else {
         ProjectRoleManager.ins.update(role);
      }
   }
   public void remove(Long id) {
      ProjectRoleManager.ins.remove(id);
   }
   public void addUserRole(long projectId, String userId, long roleId) {
      if (ProjectRoleManager.ins.getUserRole(userId, roleId) == null) {
         ProjectRoleManager.ins.addUserRole(projectId, userId, roleId);
      }

   }
   public void removeUserRole(String userId, long roleId) {
      ProjectRoleManager.ins.removeUserRole(userId, roleId);
   }
   public List loadUserRoles(long projectId, String account) {
      List roles = ProjectRoleManager.ins.loadRoles(projectId);
      List userRoles = ProjectRoleManager.ins.loadUserRoles(projectId, account);
      ArrayList userRoles2 = new ArrayList();

      for(Role role : (Iterable<Role>)(Iterable<?>)(roles)) {
         if (!role.getName().equals(GroupRoleEnum.Owner.name())) {
            ProjectRoleVO projectRoleVO = new ProjectRoleVO();
            projectRoleVO.setId(role.getId());
            projectRoleVO.setName(role.getName());
            projectRoleVO.setType(role.getType());
            projectRoleVO.setProjectId(projectId);

            for(Role role2 : (Iterable<Role>)(Iterable<?>)(userRoles)) {
               if (role2.getId() == role.getId()) {
                  projectRoleVO.setSelected(true);
                  break;
               }
            }

            userRoles2.add(projectRoleVO);
         }
      }

      return userRoles2;
   }
   public List users(long projectId, long roleId) {
      List roleUsers = ProjectRoleManager.ins.loadRoleUsers(projectId, roleId);
      ArrayList items = new ArrayList();

      for(User user : (Iterable<User>)(Iterable<?>)(roleUsers)) {
         items.add(UserServiceManager.getUserService().get(user.getId()));
      }

      return null;
   }
}
