package com.bstek.urule.console.database.service.project.role;

import com.bstek.urule.console.database.model.ProjectRole;
import java.util.List;

public interface ProjectRoleService {
   ProjectRoleServiceImpl ins = new ProjectRoleServiceImpl();

   List loadRoles(long var1);

   List users(long var1, long var3);

   void add(ProjectRole var1);

   void update(ProjectRole var1);

   void remove(Long var1);

   void addUserRole(long var1, String var3, long var4);

   void removeUserRole(String var1, long var2);

   List loadUserRoles(long var1, String var3);
}
