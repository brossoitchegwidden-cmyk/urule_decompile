package com.bstek.urule.console.database.manager.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchQueryImpl implements BatchQuery {
   private Long id;
   private String nameLike;
   private String descLike;
   private String createUser;
   private Boolean enable;
   private Boolean async;
   private BatchStatus status;
   private Long packetId;
   private Long projectId;
   private List queryParameters = new ArrayList();

   public BatchQuery id(Long id) {
      this.id = id;
      return this;
   }

   public BatchQuery nameLike(String nameLike) {
      this.nameLike = nameLike;
      return this;
   }

   public BatchQuery descLike(String descLike) {
      this.descLike = descLike;
      return this;
   }

   public BatchQuery projectId(Long projectId) {
      this.projectId = projectId;
      return this;
   }

   public List list() {
      Connection connection = JdbcUtils.getConnection();

      List listResult;
      try {
         String text = this.buildSelectSql();
         List items = this.queryBatches(connection, text);
         listResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listResult;
   }

   public void page(Page page) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = this.buildSelectSql();
         String text2 = text + " ORDER BY NAME_";
         String pageSql = JdbcUtils.getPageSql(connection, text2, page.getStartRow(), page.getPageSize());
         List items = this.queryBatches(connection, pageSql);
         page.setData(items);
         String countSql = JdbcUtils.getCountSql(text);
         PreparedStatement preparedStatement = connection.prepareStatement(countSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            page.setTotalRows((long)resultSet.getInt(1));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   private List queryBatches(Connection connection, String text) throws Exception {
      PreparedStatement preparedStatement = connection.prepareStatement(text);
      JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
      ArrayList items = new ArrayList();
      ResultSet resultSet = preparedStatement.executeQuery();

      while(resultSet.next()) {
         Batch batch = this.mapBatch(resultSet);
         items.add(batch);
      }

      JdbcUtils.closeResultSet(resultSet);
      JdbcUtils.closeStatement(preparedStatement);
      return items;
   }

   private String buildSelectSql() {
      String text = "SELECT NAME_, ASYNC_, CALLBACK_URL_, STATUS_, LISTENER_, SKIP_LIMIT_, THREAD_MULTI_, THREAD_SIZE_, THREAD_DATA_SIZE_, PROVIDER_ID_, RESOLVER_ID_, PACKET_ID_, PACKET_INPUT_DATA_, REST_ENABLE_, REST_SECURITY_ENABLE_, REST_SECURITY_USER_, REST_SECURITY_PASSWORD_, INPUT_DATA_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, PROJECT_ID_, ENABLE_ FROM URULE_BATCH ";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where" + stringBuilder.toString();
      }

      return text;
   }

   private Batch mapBatch(ResultSet resultSet) throws SQLException {
      Batch batch = new Batch();
      batch.setName(resultSet.getString(1));
      batch.setAsync(resultSet.getBoolean(2));
      batch.setCallbackUrl(resultSet.getString(3));
      batch.setStatus(BatchStatus.valueOf(resultSet.getString(4)));
      batch.setListener(resultSet.getString(5));
      batch.setSkipLimit(resultSet.getInt(6));
      batch.setThreadMulti(resultSet.getBoolean(7));
      batch.setThreadSize(resultSet.getInt(8));
      batch.setThreadDataSize(resultSet.getInt(9));
      batch.setProviderId(resultSet.getLong(10));
      batch.setResolverId(resultSet.getLong(11));
      batch.setPacketId(resultSet.getLong(12));
      batch.setPacketInputData(resultSet.getString(13));
      batch.setRestEnable(resultSet.getBoolean(14));
      batch.setRestSecurityEnable(resultSet.getBoolean(15));
      batch.setRestSecurityUser(resultSet.getString(16));
      batch.setRestSecurityPassword(resultSet.getString(17));
      batch.setInputData(resultSet.getString(18));
      batch.setDesc(resultSet.getString(19));
      batch.setCreateUser(resultSet.getString(20));
      batch.setCreateDate(resultSet.getTimestamp(21));
      batch.setUpdateUser(resultSet.getString(22));
      batch.setUpdateDate(resultSet.getTimestamp(23));
      batch.setId(resultSet.getLong(24));
      batch.setProjectId(resultSet.getLong(25));
      batch.setEnable(resultSet.getBoolean(26));
      return batch;
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

      if (this.projectId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROJECT_ID_=?");
         this.queryParameters.add(this.projectId);
      }

      if (this.packetId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PACKET_ID_=?");
         this.queryParameters.add(this.packetId);
      }

      if (this.enable != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ENABLE_=?");
         this.queryParameters.add(this.enable);
      }

      if (this.async != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ASYNC_=?");
         this.queryParameters.add(this.async);
      }

      if (this.status != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" STATUS_=?");
         this.queryParameters.add(this.status.name());
      }

      if (this.createUser != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_USER_ like ?");
         this.queryParameters.add("%" + this.createUser + "%");
      }

      return stringBuilder;
   }

   public BatchQuery enable(Boolean enable) {
      this.enable = enable;
      return this;
   }

   public BatchQuery async(Boolean async) {
      this.async = async;
      return this;
   }

   public BatchQuery status(BatchStatus status) {
      this.status = status;
      return this;
   }

   public BatchQuery packetId(Long packetId) {
      this.packetId = packetId;
      return this;
   }

   public BatchQuery createUserLike(String createUser) {
      this.createUser = createUser;
      return this;
   }
}
