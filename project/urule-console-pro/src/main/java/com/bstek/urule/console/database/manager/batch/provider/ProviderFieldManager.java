package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProviderField;

public interface ProviderFieldManager {
   ProviderFieldManager ins = new ProviderFieldManagerImpl();

   BatchDataProviderField get(Long var1);

   void add(BatchDataProviderField var1);

   void update(BatchDataProviderField var1);

   void remove(Long var1);

   void removeByProviderId(Long var1);

   void removeByBatchId(Long var1);

   void removeByProjectId(Long var1);

   void removeByGroupId(String var1);

   ProviderFieldQuery createQuery();
}
