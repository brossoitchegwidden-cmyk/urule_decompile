package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.batch.BatchLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BatchLogQueryImpl implements BatchLogQuery {
   private String groupId;
   private Long projectId;
   private Long batchId;
   private String user;
   private String batchName;
   private Date date;
   private Date endDate;
   private Boolean orderByExecutionTime;
   private String ip;
   private String status;
   private List queryParameters = new ArrayList();

   public BatchLogQuery groupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   public BatchLogQuery projectId(Long projectId) {
      this.projectId = projectId;
      return this;
   }

   public BatchLogQuery batchId(Long batchId) {
      this.batchId = batchId;
      return this;
   }

   public BatchLogQuery user(String user) {
      this.user = user;
      return this;
   }

   public BatchLogQuery batchNameLike(String batchName) {
      this.batchName = batchName;
      return this;
   }

   public BatchLogQuery dateBegin(Date date) {
      this.date = date;
      return this;
   }

   public BatchLogQuery dateEnd(Date date) {
      this.endDate = date;
      return this;
   }

   public Page paging(int pageIndex, int pageSize) {
      String pageSql = "select ID_, USER_, BATCH_ID_, BATCH_NAME_, STATUS_, TIME_, PROJECT_ID_, GROUP_ID_, IP_, USER_AGENT_, START_TIME_, END_TIME_, CREATE_DATE_, READ_COUNT_, FILTER_COUNT_ from URULE_LOG_BATCH";
      Connection connection = JdbcUtils.getConnection();

      Page page;
      try {
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            pageSql = pageSql + " where" + stringBuilder.toString();
         }

         if (this.orderByExecutionTime != null) {
            pageSql = pageSql + " order by TIME_ desc";
         } else {
            pageSql = pageSql + " order by CREATE_DATE_ desc";
         }

         Page page2 = new Page(pageIndex, pageSize);
         pageSql = JdbcUtils.getPageSql(connection, pageSql, page2.getStartRow(), pageSize);
         PreparedStatement preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readBatchLogs(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_LOG_BATCH";
         if (stringBuilder.length() > 0) {
            pageSql = pageSql + " where" + stringBuilder.toString();
         }

         preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            page2.setTotalRows(resultSet.getLong(1));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         page = page2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return page;
   }

   private List readBatchLogs(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         BatchLog batchLog = new BatchLog();
         batchLog.setId(resultSet.getLong(1));
         batchLog.setUserId(resultSet.getString(2));
         batchLog.setUsername(batchLog.getUserId());
         batchLog.setBatchId(resultSet.getLong(3));
         batchLog.setBatchName(resultSet.getString(4));
         batchLog.setStatus(BatchStatus.valueOf(resultSet.getString(5)));
         batchLog.setTime(resultSet.getLong(6));
         batchLog.setProjectId(resultSet.getLong(7));
         batchLog.setGroupId(resultSet.getString(8));
         batchLog.setIp(resultSet.getString(9));
         batchLog.setUserAgent(resultSet.getString(10));
         batchLog.setStartTime(resultSet.getTimestamp(11));
         batchLog.setEndTime(resultSet.getTimestamp(12));
         batchLog.setCreateDate(resultSet.getTimestamp(13));
         batchLog.setReadCount(resultSet.getInt(14));
         batchLog.setFilterCount(resultSet.getInt(15));
         items.add(batchLog);
      }

      return items;
   }

   private StringBuilder buildWhereClause() throws SQLException {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (StringUtils.isNotBlank(this.user)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" USER_ like ?");
         this.queryParameters.add("%" + this.user + "%");
      }

      if (StringUtils.isNotBlank(this.ip)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" IP_ = ?");
         this.queryParameters.add(this.ip);
      }

      if (this.projectId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROJECT_ID_ = ?");
         this.queryParameters.add(this.projectId);
      }

      if (StringUtils.isNotBlank(this.groupId)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" GROUP_ID_ = ?");
         this.queryParameters.add(this.groupId);
      }

      if (this.batchId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" BATCH_ID_ = ?");
         this.queryParameters.add(this.batchId);
      }

      if (this.status != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" STATUS_ = ?");
         this.queryParameters.add(this.status);
      }

      if (StringUtils.isNotBlank(this.batchName)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" BATCH_NAME_ like ?");
         this.queryParameters.add("%" + this.batchName + "%");
      }

      if (this.date != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" START_TEIM_ > ?");
         this.queryParameters.add(this.date);
      }

      if (this.endDate != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" END_TIME_ < ?");
         this.queryParameters.add(this.endDate);
      }

      return stringBuilder;
   }

   public BatchLogQuery orderTime() {
      this.orderByExecutionTime = true;
      return this;
   }

   public BatchLogQuery ip(String ip) {
      this.ip = ip;
      return this;
   }

   public BatchLogQuery status(String status) {
      this.status = status;
      return this;
   }
   public BatchLog details(Long id) {
      String text = "select IN_PARAMS_, MSG_, DETAIL_, ITEM_DATA_, PACKET_ID_, PACKET_PARAMS_ from URULE_LOG_BATCH WHERE ID_=?";
      Connection connection = JdbcUtils.getConnection();

      BatchLog batchLog;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();
         BatchLog batchLog2 = new BatchLog();
         if (resultSet.next()) {
            batchLog2.setInParams(resultSet.getString(1));
            batchLog2.setMsg(resultSet.getString(2));
            batchLog2.setDetail(resultSet.getString(3));
            batchLog2.setItemData(resultSet.getString(4));
            batchLog2.setPacketId(resultSet.getLong(5));
            batchLog2.setPacketParams(resultSet.getString(6));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         batchLog = batchLog2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return batchLog;
   }
}
