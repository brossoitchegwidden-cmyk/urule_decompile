package com.bstek.urule.console.database.manager.batch.resolver;

import java.util.List;

public interface ResolverItemQuery {
   ResolverItemQuery id(Long var1);

   ResolverItemQuery resolverId(Long var1);

   List list();
}
