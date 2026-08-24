package com.bstek.urule.console.database.manager.batch.provider;

import java.util.List;

public interface ProviderFieldQuery {
   ProviderFieldQuery id(Long id);

   ProviderFieldQuery batchId(Long batchId);

   ProviderFieldQuery providerId(Long providerId);

   ProviderFieldQuery nameLike(String nameLike);

   ProviderFieldQuery descLike(String descLike);

   List list();
}
