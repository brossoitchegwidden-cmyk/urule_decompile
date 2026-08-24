package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.batch.BatchLog;
import java.util.Date;

public interface BatchLogQuery {
   BatchLogQuery groupId(String groupId);

   BatchLogQuery projectId(Long projectId);

   BatchLogQuery batchId(Long batchId);

   BatchLogQuery user(String user);

   BatchLogQuery batchNameLike(String batchName);

   BatchLogQuery orderTime();

   BatchLogQuery dateBegin(Date date);

   BatchLogQuery dateEnd(Date date);

   BatchLogQuery ip(String ip);

   BatchLogQuery status(String status);

   Page paging(int pageIndex, int pageSize);

   /**获取日志详情*/
   BatchLog details(Long id);
}
