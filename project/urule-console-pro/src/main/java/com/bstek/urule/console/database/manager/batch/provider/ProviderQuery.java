package com.bstek.urule.console.database.manager.batch.provider;

import java.util.List;

public interface ProviderQuery {
   ProviderQuery id(Long var1);

   ProviderQuery batchId(Long var1);

   ProviderQuery nameLike(String var1);

   ProviderQuery descLike(String var1);

   List list();
}
