package com.bstek.urule.console.database.manager.batch.resolver;

import java.util.List;

public interface ResolverItemQuery {
   ResolverItemQuery id(Long id);

   ResolverItemQuery resolverId(Long resolverId);

   List list();
}
