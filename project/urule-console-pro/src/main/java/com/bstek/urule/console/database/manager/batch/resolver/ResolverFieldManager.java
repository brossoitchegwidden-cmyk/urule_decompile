package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItemField;

public interface ResolverFieldManager {
   ResolverFieldManager ins = new ResolverFieldManagerImpl();

   BatchDataResolverItemField get(Long var1);

   void add(BatchDataResolverItemField var1);

   void update(BatchDataResolverItemField var1);

   void remove(Long var1);

   void removeByItemId(Long var1);

   void removeByResolverId(Long var1);

   void removeByBatchId(Long var1);

   void removeByProjectId(Long var1);

   void removeByGroupId(String var1);

   ResolverFieldQuery createQuery();
}
