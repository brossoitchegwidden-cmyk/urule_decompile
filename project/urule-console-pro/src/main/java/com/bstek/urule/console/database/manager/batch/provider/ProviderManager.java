package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import java.util.List;

public interface ProviderManager {
   ProviderManager ins = new ProviderManagerImpl();

   BatchDataProvider get(Long var1);

   void add(BatchDataProvider var1);

   void update(BatchDataProvider var1);

   void remove(Long var1);

   void removeByBatchId(Long var1);

   void removeByProjectId(Long var1);

   void removeByGroupId(String var1);

   List getMappings(Long var1);

   ProviderQuery createQuery();
}
