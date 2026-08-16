package com.bstek.urule.console.batch;

import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;

public interface SchemeService {
   SchemeService ins = new SchemeServiceImpl();

   void add(Batch var1, String var2);

   void update(Batch var1, String var2);

   void enable(Long var1, String var2);

   void disable(Long var1, String var2);

   void stop(Long var1, String var2);

   Batch getBatchData(Long var1);

   void remove(Long var1);

   BatchDataProvider getProviderData(Long var1);
}
