package com.bstek.urule.console.database.manager.file;

import java.util.Date;
import java.util.List;

public interface FileCountQuery {
   FileCountQuery projectId(Long var1);

   FileCountQuery updateDateBegin(Date var1);

   FileCountQuery updateDateEnd(Date var1);

   Integer getRuleCount();

   List getRuleCommits();

   List getUserCommits();
}
