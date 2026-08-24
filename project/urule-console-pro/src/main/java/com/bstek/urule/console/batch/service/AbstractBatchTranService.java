package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.writer.BatchWriter;
import com.bstek.urule.console.batch.writer.WriterUtils;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractBatchTranService extends AbstractBatchService {
   private static Log logger = LogFactory.getLog(AbstractBatchTranService.class);
   private static BatchWriter batchWriter;

   private BatchWriter resolveBatchWriter() {
      if (AbstractBatchTranService.batchWriter == null) {
         AbstractBatchTranService.batchWriter = new BatchWriter();
      }

      return AbstractBatchTranService.batchWriter;
   }

   protected void closePreparedStatements(Map valuesByKey) {
      if (valuesByKey != null) {
         try {
            for(String text : (Iterable<String>)(Iterable<?>)(valuesByKey.keySet())) {
               JdbcUtils.closeStatement((PreparedStatement)valuesByKey.get(text));
            }
         } catch (Exception exception) {
            AbstractBatchTranService.logger.error(exception);
         }

      }
   }

   protected void executePreparedStatementBatches(Map valuesByKey) throws SQLException {
      try {
         for(String text : (Iterable<String>)(Iterable<?>)(valuesByKey.keySet())) {
            ((PreparedStatement)valuesByKey.get(text)).executeBatch();
         }

      } catch (Exception exception) {
         throw new SqlBatchException(exception.getMessage(), exception);
      }
   }

   protected Map prepareStmt(Connection conn, BatchDataResolver resolver) throws SQLException {
      HashMap prepareStmtResult = new HashMap();

      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(resolver.getItems())) {
         PreparedStatement preparedStatement = conn.prepareStatement(batchDataResolverItem.getUpdateSql());
         prepareStmtResult.put(batchDataResolverItem.getName(), preparedStatement);
      }

      return prepareStmtResult;
   }

   protected void processRecord(BatchContext batchContext, Connection connection, Map valuesByKey2, GeneralEntity generalEntity, Map valuesByKey3) throws Exception {
      Map valuesByKey = this.getRuleProcessor().fireRules(batchContext, generalEntity);
      WriterUtils.write(batchContext, this.resolveBatchWriter(), valuesByKey2, valuesByKey, generalEntity, valuesByKey3);
   }
}
