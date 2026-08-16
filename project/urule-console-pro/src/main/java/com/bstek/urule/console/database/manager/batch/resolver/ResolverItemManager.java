package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import java.util.List;

public interface ResolverItemManager {
   ResolverItemManager ins = new ResolverItemManagerImpl();

   BatchDataResolverItem get(Long var1);

   void add(BatchDataResolverItem var1);

   void update(BatchDataResolverItem var1);

   void remove(Long var1);

   void removeByResolverId(Long var1);

   void removeByBatchId(Long var1);

   void removeByProjectId(Long var1);

   void removeByGroupId(String var1);

   List getMappings(Long var1);

   ResolverItemQuery createQuery();
}
