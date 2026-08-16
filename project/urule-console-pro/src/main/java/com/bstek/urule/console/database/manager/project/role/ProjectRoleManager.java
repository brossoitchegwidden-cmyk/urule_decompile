package com.bstek.urule.console.database.manager.project.role;

import com.bstek.urule.console.database.model.ProjectRole;
import com.bstek.urule.console.database.model.UserRole;
import java.util.List;

public interface ProjectRoleManager {
   ProjectRoleManagerImpl ins = new ProjectRoleManagerImpl();

   List loadRoles(long var1);

   List loadUserRoles(long var1, String var3);

   List loadRoleUsers(long var1, long var3);

   void add(ProjectRole var1);

   void update(ProjectRole var1);

   void remove(Long var1);

   void removeByProjectId(Long var1);

   boolean checkExist(long var1, String var3);

   void addUserRole(long var1, String var3, long var4);

   void removeUserRole(String var1, long var2);

   void removeRoleUsers(long var1);

   void removeUserRoles(String var1);

   ProjectRole get(long var1);

   ProjectRole get(long var1, String var3);

   UserRole getUserRole(String var1, long var2);
}
