package com.bstek.urule.console.batch.reader;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import java.sql.Connection;
import java.util.List;

public interface ItemReader {
   int getTotleRows(BatchDataProvider var1) throws Exception;

   List getPageDatas(Connection var1, BatchContext var2, int var3, int var4) throws ReaderException;

   List getDatas(Connection var1, BatchContext var2) throws ReaderException;
}
