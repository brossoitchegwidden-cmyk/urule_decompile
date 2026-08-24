package com.bstek.urule.console.batch.writer;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BatchWriter implements Writer {
   public void storeRecord(Map stmtMap, BatchDataResolver dataResolver, BatchDataResolverItem storeItem, GeneralEntity record) throws Exception {
      PreparedStatement preparedStatement = (PreparedStatement)stmtMap.get(storeItem.getName());
      this.bindStatementParameters(storeItem.getParams(), record, preparedStatement);
      preparedStatement.addBatch();
   }

   protected void bindStatementParameters(List parameters, Map valuesByName, PreparedStatement preparedStatement) throws Exception {
      for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(parameters)) {
         Object objectValue = valuesByName.get(dataParam.getName());
         int number = dataParam.getIndex() + 1;
         if (objectValue == null) {
            preparedStatement.setObject(number, (Object)null);
         } else if (objectValue instanceof String) {
            preparedStatement.setString(number, (String)objectValue);
         } else if (objectValue instanceof Integer) {
            preparedStatement.setInt(number, (Integer)objectValue);
         } else if (objectValue instanceof Long) {
            preparedStatement.setLong(number, (Long)objectValue);
         } else if (objectValue instanceof Boolean) {
            preparedStatement.setBoolean(number, (Boolean)objectValue);
         } else if (objectValue instanceof Array) {
            preparedStatement.setArray(number, (Array)objectValue);
         } else if (objectValue instanceof Date) {
            preparedStatement.setTimestamp(number, new Timestamp(((Date)objectValue).getTime()));
         } else {
            preparedStatement.setObject(number, objectValue);
         }
      }

   }
}
