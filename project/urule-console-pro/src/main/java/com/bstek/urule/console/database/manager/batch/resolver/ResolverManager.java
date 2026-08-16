package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import java.util.List;

public interface ResolverManager {
   ResolverManager ins = new ResolverManagerImpl();

   BatchDataResolver get(Long var1);

   void add(BatchDataResolver var1);

   void update(BatchDataResolver var1);

   void remove(Long var1);

   void removeByBatchId(Long var1);

   void removeByProjectId(Long var1);

   void removeByGroupId(String var1);

   List getResolverItemss(Long var1);

   ResolverQuery createQuery();
}
