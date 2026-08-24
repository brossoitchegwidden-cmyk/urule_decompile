package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import java.util.List;

public interface ResolverItemManager {
   ResolverItemManager ins = new ResolverItemManagerImpl();

   BatchDataResolverItem get(Long id);

   void add(BatchDataResolverItem item);

   void update(BatchDataResolverItem item);

   void remove(Long id);

   void removeByResolverId(Long id);

   void removeByBatchId(Long id);

   void removeByProjectId(Long id);

   void removeByGroupId(String groupId);

   List getMappings(Long id);

   ResolverItemQuery createQuery();
}
