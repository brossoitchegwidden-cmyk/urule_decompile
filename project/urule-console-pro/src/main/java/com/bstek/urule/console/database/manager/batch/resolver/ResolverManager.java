package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import java.util.List;

public interface ResolverManager {
   ResolverManager ins = new ResolverManagerImpl();

   BatchDataResolver get(Long id);

   void add(BatchDataResolver resolver);

   void update(BatchDataResolver resolver);

   void remove(Long id);

   void removeByBatchId(Long id);

   void removeByProjectId(Long id);

   void removeByGroupId(String groupId);

   List getResolverItemss(Long id);

   ResolverQuery createQuery();
}
