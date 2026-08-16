package com.bstek.urule.console.batch.filter;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.model.GeneralEntity;
import java.util.Map;

public interface WriterFilter {
   boolean filter(BatchContext var1, BatchDataResolverItem var2, GeneralEntity var3, Map var4, Object var5);
}
