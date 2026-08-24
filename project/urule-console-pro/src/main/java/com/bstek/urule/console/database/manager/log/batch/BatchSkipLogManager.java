package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.model.batch.BatchSkipLog;

public interface BatchSkipLogManager {
   BatchSkipLogManager ins = new BatchSkipLogManagerImpl();

   /**新增日志*/
   void add(BatchSkipLog log);

   /**删除用户对应的日志*/
   void removeByGroupId(String groupId);

   /**删除项目对应的日志*/
   void removeByProject(Long projectId);

   BatchSkipLogQuery newQuery();
}
