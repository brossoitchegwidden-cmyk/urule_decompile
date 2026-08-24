package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.model.batch.BatchLog;

public interface BatchLogManager {
   BatchLogManager ins = new BatchLogManagerImpl();

   void updateStatus(BatchLog log);

   /**新增日志*/
   void add(BatchLog log);

   /**删除用户对应的日志*/
   void removeByGroupId(String groupId);

   /**删除项目对应的日志*/
   void removeByProject(Long projectId);

   BatchLogQuery newQuery();
}
