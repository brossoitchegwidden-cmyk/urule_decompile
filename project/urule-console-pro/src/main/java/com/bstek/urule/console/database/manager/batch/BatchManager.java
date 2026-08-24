package com.bstek.urule.console.database.manager.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.batch.Batch;

public interface BatchManager {
   BatchManager ins = new BatchManagerImpl();

   Batch get(Long id);

   void add(Batch batch);

   void update(Batch batch);

   void remove(Long id);

   void removeByProjectId(Long id);

   void removeByGroupId(String groupId);

   void updateStatus(Long batchId, BatchStatus status);

   BatchQuery createQuery();
}
