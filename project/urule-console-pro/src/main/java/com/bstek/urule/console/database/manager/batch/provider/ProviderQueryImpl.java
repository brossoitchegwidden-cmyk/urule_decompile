package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProviderQueryImpl implements ProviderQuery {
   private Long id;
   private Long batchId;
   private String nameLike;
   private String descLike;
   private List queryParameters = new ArrayList();

   public ProviderQuery id(Long id) {
      this.id = id;
      return this;
   }

   public ProviderQuery batchId(Long batchId) {
      this.batchId = batchId;
      return this;
   }

   public ProviderQuery nameLike(String nameLike) {
      this.nameLike = nameLike;
      return this;
   }

   public ProviderQuery descLike(String descLike) {
      this.descLike = descLike;
      return this;
   }

   public List list() {
      Connection connection = JdbcUtils.getConnection();

      ArrayList listResult;
      try {
         String text = "SELECT NAME_, DATASOURCE_ID_, INPUT_DATA_, PACKET_VAR_NAME_, SUPPORT_PAGING_, PAGE_SIZE_, PAGE_SQL_, ORDER_FIELD_, ORDER_FIELD_PARAM_NAME_, PAGE_LIMIT_TYPE_, COUNT_SQL_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_ FROM URULE_BATCH_DATA_PROVIDER ";
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            BatchDataProvider batchDataProvider = new BatchDataProvider();
            batchDataProvider.setName(resultSet.getString(1));
            batchDataProvider.setDatasourceId(resultSet.getLong(2));
            batchDataProvider.setInputData(resultSet.getString(3));
            batchDataProvider.setPacketVarName(resultSet.getString(4));
            batchDataProvider.setSupportsPaging(resultSet.getBoolean(5));
            batchDataProvider.setPageSize(resultSet.getInt(6));
            batchDataProvider.setPageSql(resultSet.getString(7));
            batchDataProvider.setOrderField(resultSet.getString(8));
            batchDataProvider.setOrderFieldParamName(resultSet.getString(9));
            batchDataProvider.setPageLimitType(resultSet.getString(10));
            batchDataProvider.setCountSql(resultSet.getString(11));
            batchDataProvider.setDesc(resultSet.getString(12));
            batchDataProvider.setCreateUser(resultSet.getString(13));
            batchDataProvider.setCreateDate(resultSet.getTimestamp(14));
            batchDataProvider.setUpdateUser(resultSet.getString(15));
            batchDataProvider.setUpdateDate(resultSet.getTimestamp(16));
            batchDataProvider.setId(resultSet.getLong(17));
            batchDataProvider.setProjectId(resultSet.getLong(18));
            batchDataProvider.setBatchId(resultSet.getLong(19));
            items.add(batchDataProvider);
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

   private StringBuilder buildWhereClause() {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (this.nameLike != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NAME_ like ?");
         this.queryParameters.add("%" + this.nameLike + "%");
      }

      if (this.descLike != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" DESC_ like ?");
         this.queryParameters.add("%" + this.descLike + "%");
      }

      if (this.id != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ID_=?");
         this.queryParameters.add(this.id);
      }

      if (this.batchId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" BATCH_ID_=?");
         this.queryParameters.add(this.batchId);
      }

      return stringBuilder;
   }
}
