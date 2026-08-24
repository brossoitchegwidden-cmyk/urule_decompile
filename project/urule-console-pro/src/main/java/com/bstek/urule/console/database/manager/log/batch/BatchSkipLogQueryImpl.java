package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.model.batch.BatchSkipLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchSkipLogQueryImpl implements BatchSkipLogQuery {
   private Long batchLogId;
   private List queryParameters = new ArrayList();

   public BatchSkipLogQuery batchLogId(Long batchLogId) {
      this.batchLogId = batchLogId;
      return this;
   }

   private StringBuilder buildWhereClause() throws SQLException {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (StringUtils.isNotBlank(this.batchLogId)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" LOG_ID_ = ?");
         this.queryParameters.add(this.batchLogId);
      }

      return stringBuilder;
   }

   public List list() {
      String text = "select ID_, LOG_ID_, BATCH_ID_, TYPE_, MSG_, CREATE_DATE_ from URULE_LOG_BATCH_SKIP";
      Connection connection = JdbcUtils.getConnection();

      List listResult;
      try {
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         text = text + " order by CREATE_DATE_ desc";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readSkippedBatchRecords(resultSet);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         listResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listResult;
   }

   private List readSkippedBatchRecords(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         BatchSkipLog batchSkipLog = new BatchSkipLog();
         batchSkipLog.setId(resultSet.getLong(1));
         batchSkipLog.setLogId(resultSet.getLong(2));
         batchSkipLog.setBatchId(resultSet.getLong(3));
         batchSkipLog.setType(resultSet.getString(4));
         batchSkipLog.setMsg(resultSet.getString(5));
         batchSkipLog.setCreateDate(resultSet.getTimestamp(6));
         items.add(batchSkipLog);
      }

      return items;
   }

   public BatchSkipLog details(Long id) {
      String text = "select MSG_, DETAIL_, DATA_ from URULE_LOG_BATCH_SKIP WHERE ID_=?";
      Connection connection = JdbcUtils.getConnection();

      BatchSkipLog batchSkipLog;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();
         BatchSkipLog batchSkipLog2 = new BatchSkipLog();
         if (resultSet.next()) {
            batchSkipLog2.setMsg(resultSet.getString(1));
            batchSkipLog2.setDetail(resultSet.getString(2));
            batchSkipLog2.setData(resultSet.getString(3));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         batchSkipLog = batchSkipLog2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return batchSkipLog;
   }
}
