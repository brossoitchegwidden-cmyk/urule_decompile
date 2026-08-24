package com.bstek.urule.console.database.manager.batch.resolver;

import java.util.List;

public interface ResolverQuery {
   ResolverQuery id(Long id);

   ResolverQuery batchId(Long batchId);

   List list();
}
