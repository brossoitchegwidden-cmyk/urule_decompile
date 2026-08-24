package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProviderField;

public interface ProviderFieldManager {
   ProviderFieldManager ins = new ProviderFieldManagerImpl();

   BatchDataProviderField get(Long id);

   void add(BatchDataProviderField field);

   void update(BatchDataProviderField field);

   void remove(Long id);

   void removeByProviderId(Long id);

   void removeByBatchId(Long id);

   void removeByProjectId(Long id);

   void removeByGroupId(String groupId);

   ProviderFieldQuery createQuery();
}
