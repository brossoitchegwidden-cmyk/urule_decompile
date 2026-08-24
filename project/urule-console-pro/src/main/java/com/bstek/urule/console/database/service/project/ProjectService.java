package com.bstek.urule.console.database.service.project;

import com.bstek.urule.console.database.model.Project;
import java.util.Date;
import java.util.List;

public interface ProjectService {
   ProjectService ins = new ProjectServiceImpl();

   /**添加项目*/
   void add(Project project);

   /**更新项目*/
   void update(Project project);

   /**删除项目*/
   List remove(long projectId);

   /**添加项目用户*/
   void addProjectuser(long projectId, String account);

   /**统计近期规则提交信息*/
   List getUserCommits(Long projectId, Date startDate, Date endDate);

   /**统计近期规则提交信息*/
   List getRuleCommits(Long projectId, Date startDate, Date endDate);

   /**统计近期规则发布信息*/
   List getRuleDeploys(Long projectId, Date startDate, Date endDate);

   /**统计近期规则调用次数信息*/
   List getRuleExecCount(Long projectId, Date startDate, Date endDate);

   /**统计近期规则执行耗时信息*/
   List getRuleExecTime(Long projectId, Date startDate, Date endDate);
}
