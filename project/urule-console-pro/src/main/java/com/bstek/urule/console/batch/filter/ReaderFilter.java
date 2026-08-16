package com.bstek.urule.console.batch.filter;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;

public interface ReaderFilter {
   boolean filter(BatchContext var1, BatchDataProvider var2, Object var3);
}
