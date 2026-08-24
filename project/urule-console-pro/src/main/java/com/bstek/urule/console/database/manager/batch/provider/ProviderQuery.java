package com.bstek.urule.console.database.manager.batch.provider;

import java.util.List;

public interface ProviderQuery {
   ProviderQuery id(Long id);

   ProviderQuery batchId(Long batchId);

   ProviderQuery nameLike(String nameLike);

   ProviderQuery descLike(String descLike);

   List list();
}
