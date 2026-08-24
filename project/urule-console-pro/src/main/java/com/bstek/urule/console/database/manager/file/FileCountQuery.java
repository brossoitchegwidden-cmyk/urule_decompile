package com.bstek.urule.console.database.manager.file;

import java.util.Date;
import java.util.List;

public interface FileCountQuery {
   FileCountQuery projectId(Long projectId);

   FileCountQuery updateDateBegin(Date date);

   FileCountQuery updateDateEnd(Date date);

   Integer getRuleCount();

   List getRuleCommits();

   List getUserCommits();
}
