package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.model.batch.BatchSkipLog;

public interface BatchSkipLogManager {
   BatchSkipLogManager ins = new BatchSkipLogManagerImpl();

   void add(BatchSkipLog var1);

   void removeByGroupId(String var1);

   void removeByProject(Long var1);

   BatchSkipLogQuery newQuery();
}
