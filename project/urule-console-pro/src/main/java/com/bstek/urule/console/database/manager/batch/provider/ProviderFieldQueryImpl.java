package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProviderField;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProviderFieldQueryImpl implements ProviderFieldQuery {
   private Long id;
   private Long batchId;
   private Long providerId;
   private String nameLike;
   private String descLike;
   private List queryParameters = new ArrayList();

   public ProviderFieldQuery id(Long id) {
      this.id = id;
      return this;
   }

   public ProviderFieldQuery providerId(Long providerId) {
      this.providerId = providerId;
      return this;
   }

   public ProviderFieldQuery nameLike(String nameLike) {
      this.nameLike = nameLike;
      return this;
   }

   public ProviderFieldQuery descLike(String descLike) {
      this.descLike = descLike;
      return this;
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

      if (this.providerId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROVIDER_ID_=?");
         this.queryParameters.add(this.providerId);
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

   public List list() {
      Connection connection = JdbcUtils.getConnection();

      ArrayList listResult;
      try {
         String text = "SELECT SRC_PROPERTY_, DATA_TYPE_, DEST_PROPERTY_, CLAZZ_PATH_, DATA_PROVIDER_ID_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_, PROVIDER_ID_ FROM URULE_BATCH_PROVIDER_FIELD ";
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            BatchDataProviderField batchDataProviderField = new BatchDataProviderField();
            batchDataProviderField.setSrcProperty(resultSet.getString(1));
            batchDataProviderField.setDataType(resultSet.getString(2));
            batchDataProviderField.setDestProperty(resultSet.getString(3));
            batchDataProviderField.setClassPath(resultSet.getString(4));
            batchDataProviderField.setDataProviderId(resultSet.getLong(5));
            batchDataProviderField.setCreateUser(resultSet.getString(6));
            batchDataProviderField.setCreateDate(resultSet.getTimestamp(7));
            batchDataProviderField.setUpdateUser(resultSet.getString(8));
            batchDataProviderField.setUpdateDate(resultSet.getTimestamp(9));
            batchDataProviderField.setId(resultSet.getLong(10));
            batchDataProviderField.setProjectId(resultSet.getLong(11));
            batchDataProviderField.setBatchId(resultSet.getLong(12));
            batchDataProviderField.setProviderId(resultSet.getLong(13));
            items.add(batchDataProviderField);
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

   public ProviderFieldQuery batchId(Long batchId) {
      this.batchId = batchId;
      return this;
   }
}
