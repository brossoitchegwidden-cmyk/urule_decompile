package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.batch.BatchLog;
import java.util.Date;

public interface BatchLogQuery {
   BatchLogQuery groupId(String var1);

   BatchLogQuery projectId(Long var1);

   BatchLogQuery batchId(Long var1);

   BatchLogQuery user(String var1);

   BatchLogQuery batchNameLike(String var1);

   BatchLogQuery orderTime();

   BatchLogQuery dateBegin(Date var1);

   BatchLogQuery dateEnd(Date var1);

   BatchLogQuery ip(String var1);

   BatchLogQuery status(String var1);

   Page paging(int var1, int var2);

   BatchLog details(Long var1);
}
