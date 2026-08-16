package com.bstek.urule.console.database.service.project;

import com.bstek.urule.console.database.model.Project;
import java.util.Date;
import java.util.List;

public interface ProjectService {
   ProjectService ins = new ProjectServiceImpl();

   void add(Project var1);

   void update(Project var1);

   List remove(long var1);

   void addProjectuser(long var1, String var3);

   List getUserCommits(Long var1, Date var2, Date var3);

   List getRuleCommits(Long var1, Date var2, Date var3);

   List getRuleDeploys(Long var1, Date var2, Date var3);

   List getRuleExecCount(Long var1, Date var2, Date var3);

   List getRuleExecTime(Long var1, Date var2, Date var3);
}
