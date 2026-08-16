package com.bstek.urule.console.database.manager.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.batch.Batch;

public interface BatchManager {
   BatchManager ins = new BatchManagerImpl();

   Batch get(Long var1);

   void add(Batch var1);

   void update(Batch var1);

   void remove(Long var1);

   void removeByProjectId(Long var1);

   void removeByGroupId(String var1);

   void updateStatus(Long var1, BatchStatus var2);

   BatchQuery createQuery();
}
