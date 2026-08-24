package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItemField;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResolverFieldQueryImpl implements ResolverFieldQuery {
   private Long id;
   private Long itemId;
   private List queryParameters = new ArrayList();

   public ResolverFieldQuery id(Long id) {
      this.id = id;
      return this;
   }

   public ResolverFieldQuery itemId(Long itemId) {
      this.itemId = itemId;
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

      if (this.itemId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ITEM_ID_=?");
         this.queryParameters.add(this.itemId);
      }

      return stringBuilder;
   }

   public List list() {
      Connection connection = JdbcUtils.getConnection();

      ArrayList listResult;
      try {
         String text = "SELECT SRC_PROPERTY_, DATA_TYPE_, DEST_PROPERTY_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_, KEY_, RESOLVER_ID_, ITEM_ID_ FROM URULE_BATCH_RESOLVER_FIELD ";
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            BatchDataResolverItemField batchDataResolverItemField = new BatchDataResolverItemField();
            batchDataResolverItemField.setSrcProperty(resultSet.getString(1));
            batchDataResolverItemField.setDataType(resultSet.getString(2));
            batchDataResolverItemField.setDestProperty(resultSet.getString(3));
            batchDataResolverItemField.setCreateUser(resultSet.getString(4));
            batchDataResolverItemField.setCreateDate(resultSet.getTimestamp(5));
            batchDataResolverItemField.setUpdateUser(resultSet.getString(6));
            batchDataResolverItemField.setUpdateDate(resultSet.getTimestamp(7));
            batchDataResolverItemField.setId(resultSet.getLong(8));
            batchDataResolverItemField.setProjectId(resultSet.getLong(9));
            batchDataResolverItemField.setBatchId(resultSet.getLong(10));
            batchDataResolverItemField.setKey(resultSet.getBoolean(11));
            batchDataResolverItemField.setResolverId(resultSet.getLong(12));
            batchDataResolverItemField.setResolverItemId(resultSet.getLong(13));
            items.add(batchDataResolverItemField);
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
