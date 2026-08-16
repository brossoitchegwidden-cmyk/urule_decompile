package com.bstek.urule.console.database.manager.batch.provider;

import java.util.List;

public interface ProviderFieldQuery {
   ProviderFieldQuery id(Long var1);

   ProviderFieldQuery batchId(Long var1);

   ProviderFieldQuery providerId(Long var1);

   ProviderFieldQuery nameLike(String var1);

   ProviderFieldQuery descLike(String var1);

   List list();
}
