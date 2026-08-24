package com.bstek.urule.console.database.manager.batch.resolver;

import java.util.List;

public interface ResolverFieldQuery {
   ResolverFieldQuery id(Long id);

   ResolverFieldQuery itemId(Long itemId);

   List list();
}
