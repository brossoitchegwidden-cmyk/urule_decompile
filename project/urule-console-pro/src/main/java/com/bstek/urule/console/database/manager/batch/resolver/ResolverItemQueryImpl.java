package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.BatchUpdateMode;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResolverItemQueryImpl implements ResolverItemQuery {
   private Long id;
   private Long resolverId;
   private List queryParameters = new ArrayList();

   public ResolverItemQuery id(Long id) {
      this.id = id;
      return this;
   }

   public ResolverItemQuery resolverId(Long resolverId) {
      this.resolverId = resolverId;
      return this;
   }

   private StringBuilder buildWhereClause() {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (this.id != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ID_=?");
         this.queryParameters.add(this.id);
      }

      if (this.resolverId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" RESOLVER_ID_=?");
         this.queryParameters.add(this.resolverId);
      }

      return stringBuilder;
   }

   public List list() {
      Connection connection = JdbcUtils.getConnection();

      ArrayList listResult;
      try {
         String text = "SELECT NAME_, UPDATE_MODE_, TABLE_NAME_, VALIDATOR_DATA_, PARTITION_NAME_, PARTITION_VALUE_, COMMIT_LIMIT_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, BATCH_ID_, PROJECT_ID_, RESOLVER_ID_ FROM URULE_BATCH_RESOLVER_ITEM ";
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            BatchDataResolverItem batchDataResolverItem = new BatchDataResolverItem();
            batchDataResolverItem.setName(resultSet.getString(1));
            batchDataResolverItem.setUpdateMode(BatchUpdateMode.valueOf(resultSet.getString(2)));
            batchDataResolverItem.setTableName(resultSet.getString(3));
            batchDataResolverItem.setFilterData(resultSet.getString(4));
            batchDataResolverItem.setPartitionName(resultSet.getString(5));
            batchDataResolverItem.setPartitionValue(resultSet.getString(6));
            batchDataResolverItem.setCommitLimit(resultSet.getInt(7));
            batchDataResolverItem.setDesc(resultSet.getString(8));
            batchDataResolverItem.setCreateUser(resultSet.getString(9));
            batchDataResolverItem.setCreateDate(resultSet.getTimestamp(10));
            batchDataResolverItem.setUpdateUser(resultSet.getString(11));
            batchDataResolverItem.setUpdateDate(resultSet.getTimestamp(12));
            batchDataResolverItem.setId(resultSet.getLong(13));
            batchDataResolverItem.setBatchId(resultSet.getLong(14));
            batchDataResolverItem.setProjectId(resultSet.getLong(15));
            batchDataResolverItem.setResolverId(resultSet.getLong(16));
            items.add(batchDataResolverItem);
         }

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
}
