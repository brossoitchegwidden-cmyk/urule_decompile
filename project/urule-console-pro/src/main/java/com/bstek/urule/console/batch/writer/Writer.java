package com.bstek.urule.console.batch.writer;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.model.GeneralEntity;
import java.util.Map;

public interface Writer {
   void storeRecord(Map stmtMap, BatchDataResolver dataResolver, BatchDataResolverItem storeItem, GeneralEntity record) throws Exception;
}
