package com.bstek.urule.console.database.manager.batch.resolver;

import java.util.List;

public interface ResolverQuery {
   ResolverQuery id(Long var1);

   ResolverQuery batchId(Long var1);

   List list();
}
