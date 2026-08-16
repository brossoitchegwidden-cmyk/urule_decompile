package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.model.batch.BatchLog;

public interface BatchLogManager {
   BatchLogManager ins = new BatchLogManagerImpl();

   void updateStatus(BatchLog var1);

   void add(BatchLog var1);

   void removeByGroupId(String var1);

   void removeByProject(Long var1);

   BatchLogQuery newQuery();
}
