package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItemField;

public interface ResolverFieldManager {
   ResolverFieldManager ins = new ResolverFieldManagerImpl();

   BatchDataResolverItemField get(Long id);

   void add(BatchDataResolverItemField field);

   void update(BatchDataResolverItemField field);

   void remove(Long id);

   void removeByItemId(Long id);

   void removeByResolverId(Long id);

   void removeByBatchId(Long id);

   void removeByProjectId(Long id);

   void removeByGroupId(String groupId);

   ResolverFieldQuery createQuery();
}
