package com.bstek.urule.console.database.manager.project;

import com.bstek.urule.console.database.manager.project.user.UserQuery;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Project;
import java.util.List;

public interface ProjectManager {
   ProjectManagerImpl ins = new ProjectManagerImpl();

   /**根据团队ID获取对应的项目*/
   List getProjectsByGroupId(String groupId);

   /**新建项目*/
   void add(Project project);

   /**更新项目*/
   void update(Project project);

   /**删除项目*/
   void remove(long id);

   /**获取项目*/
   Project get(long id);

   /**添加项目用户*/
   void addProjectUser(long id, String account, String username);

   /**移除项目用户*/
   void removeProjectUser(long id, String account);

   /**删除项目的所有用户*/
   void removeProjectUsers(long id);

   /**获取审批用户*/
   String getApproveUser(long id, ApplyType type);

   /**更新审批用户*/
   void updateApproveUser(long id, ApplyType type, String account);

   ProjectQuery newQuery();

   UserQuery createUserQuery();
}
