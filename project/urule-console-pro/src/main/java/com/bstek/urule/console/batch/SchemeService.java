package com.bstek.urule.console.batch;

import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;

public interface SchemeService {
   SchemeService ins = new SchemeServiceImpl();

   void add(Batch batchData, String account);

   void update(Batch batchData, String account);

   void enable(Long id, String account);

   void disable(Long id, String account);

   void stop(Long id, String account);

   Batch getBatchData(Long id);

   void remove(Long id);

   BatchDataProvider getProviderData(Long providerId);
}
