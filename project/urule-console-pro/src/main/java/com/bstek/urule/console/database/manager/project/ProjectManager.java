package com.bstek.urule.console.database.manager.project;

import com.bstek.urule.console.database.manager.project.user.UserQuery;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Project;
import java.util.List;

public interface ProjectManager {
   ProjectManagerImpl ins = new ProjectManagerImpl();

   List getProjectsByGroupId(String var1);

   void add(Project var1);

   void update(Project var1);

   void remove(long var1);

   Project get(long var1);

   void addProjectUser(long var1, String var3, String var4);

   void removeProjectUser(long var1, String var3);

   void removeProjectUsers(long var1);

   String getApproveUser(long var1, ApplyType var3);

   void updateApproveUser(long var1, ApplyType var3, String var4);

   ProjectQuery newQuery();

   UserQuery createUserQuery();
}
