package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import java.util.List;

public interface ProviderManager {
   ProviderManager ins = new ProviderManagerImpl();

   BatchDataProvider get(Long id);

   void add(BatchDataProvider provider);

   void update(BatchDataProvider provider);

   void remove(Long id);

   void removeByBatchId(Long batchId);

   void removeByProjectId(Long id);

   void removeByGroupId(String groupId);

   List getMappings(Long id);

   ProviderQuery createQuery();
}
