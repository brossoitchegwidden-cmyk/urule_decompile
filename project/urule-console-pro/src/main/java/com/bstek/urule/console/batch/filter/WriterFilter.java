package com.bstek.urule.console.batch.filter;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.model.GeneralEntity;
import java.util.Map;

public interface WriterFilter {
   boolean filter(BatchContext batchContext, BatchDataResolverItem resolverItem, GeneralEntity data, Map outParams, Object resultData);
}
