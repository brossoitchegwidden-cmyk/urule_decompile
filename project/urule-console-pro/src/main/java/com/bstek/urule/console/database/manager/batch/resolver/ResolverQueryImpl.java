package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.TranScope;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResolverQueryImpl implements ResolverQuery {
   private Long id;
   private Long batchId;
   private List queryParameters = new ArrayList();

   public ResolverQuery id(Long id) {
      this.id = id;
      return this;
   }

   public ResolverQuery batchId(Long batchId) {
      this.batchId = batchId;
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
         String text = "SELECT NAME_, TRAN_SCOPE_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, BATCH_ID_, PROJECT_ID_, DATASOURCE_ID_ FROM URULE_BATCH_DATA_RESOLVER ";
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            BatchDataResolver batchDataResolver = new BatchDataResolver();
            batchDataResolver.setName(resultSet.getString(1));
            batchDataResolver.setTranScope(TranScope.valueOf(resultSet.getString(2)));
            batchDataResolver.setDesc(resultSet.getString(3));
            batchDataResolver.setCreateUser(resultSet.getString(4));
            batchDataResolver.setCreateDate(resultSet.getTimestamp(5));
            batchDataResolver.setUpdateUser(resultSet.getString(6));
            batchDataResolver.setUpdateDate(resultSet.getTimestamp(7));
            batchDataResolver.setId(resultSet.getLong(8));
            batchDataResolver.setBatchId(resultSet.getLong(9));
            batchDataResolver.setProjectId(resultSet.getLong(10));
            batchDataResolver.setDatasourceId(resultSet.getLong(11));
            items.add(batchDataResolver);
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
