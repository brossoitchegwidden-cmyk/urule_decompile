package com.bstek.urule.console.batch.writer;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.model.GeneralEntity;
import java.util.Map;

public interface Writer {
   void storeRecord(Map var1, BatchDataResolver var2, BatchDataResolverItem var3, GeneralEntity var4) throws Exception;
}
