package com.bstek.urule.console.batch.reader;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import java.sql.Connection;
import java.util.List;

public interface ItemReader {
   int getTotleRows(BatchDataProvider dataProvider) throws Exception;

   List getPageDatas(Connection loadConnection, BatchContext context, int pageIndex, int pageSize) throws ReaderException;

   List getDatas(Connection loadConnection, BatchContext context) throws ReaderException;
}
