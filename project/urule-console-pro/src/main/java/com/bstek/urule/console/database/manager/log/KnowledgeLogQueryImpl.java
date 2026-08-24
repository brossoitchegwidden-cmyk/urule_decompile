package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.Page;
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

public class KnowledgeLogQueryImpl implements KnowledgeLogQuery {
   private String user;
   private String ip;
   private Long projectId;
   private String groupId;
   private Long packetId;
   private String packetName;
   private Date date;
   private Date endDate;
   private Boolean orderByExecutionTime;
   private List queryParameters = new ArrayList();

   protected KnowledgeLogQueryImpl() {
   }

   public KnowledgeLogQuery orderTime() {
      this.orderByExecutionTime = true;
      return this;
   }

   public Page paging(int pageIndex, int pageSize) {
      String pageSql = "select ID_, USER_, KNOWLEDGE_ID_, KNOWLEDGE_NAME_, VERSION_, TIME_, PROJECT_ID_, IP_, USER_AGENT_, START_TIME_, END_TIME_, CREATE_DATE_ from URULE_LOG_KNOWLEDGE";
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
         pageSql = JdbcUtils.getPageSql(pageSql, page2.getStartRow(), pageSize);
         PreparedStatement preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readKnowledgeLogs(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_LOG_KNOWLEDGE";
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

   public List list() {
      String text = "select ID_, USER_, KNOWLEDGE_ID_, KNOWLEDGE_NAME_, VERSION_, TIME_, PROJECT_ID_, IP_, USER_AGENT_, START_TIME_, END_TIME_, CREATE_DATE_ from URULE_LOG_KNOWLEDGE";
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
         List items = this.readKnowledgeLogs(resultSet);
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

   private List readKnowledgeLogs(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         KnowledgeLog knowledgeLog = new KnowledgeLog();
         knowledgeLog.setId(resultSet.getLong(1));
         knowledgeLog.setUserId(resultSet.getString(2));
         knowledgeLog.setUsername(knowledgeLog.getUserId());
         knowledgeLog.setKnowledgeId(resultSet.getLong(3));
         knowledgeLog.setKnowledgeName(resultSet.getString(4));
         knowledgeLog.setVersion(resultSet.getString(5));
         knowledgeLog.setTime(resultSet.getLong(6));
         knowledgeLog.setProjectId(resultSet.getLong(7));
         knowledgeLog.setIp(resultSet.getString(8));
         knowledgeLog.setUserAgent(resultSet.getString(9));
         knowledgeLog.setStartTime(resultSet.getTimestamp(10));
         knowledgeLog.setEndTime(resultSet.getTimestamp(11));
         knowledgeLog.setCreateDate(resultSet.getTimestamp(12));
         items.add(knowledgeLog);
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

      if (this.packetId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" KNOWLEDGE_ID_ = ?");
         this.queryParameters.add(this.packetId);
      }

      if (StringUtils.isNotBlank(this.packetName)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" KNOWLEDGE_NAME_ like ?");
         this.queryParameters.add("%" + this.packetName + "%");
      }

      if (this.date != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_DATE_ > ?");
         this.queryParameters.add(this.date);
      }

      if (this.endDate != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_DATE_ < ?");
         this.queryParameters.add(this.endDate);
      }

      return stringBuilder;
   }

   public KnowledgeLogQuery user(String user) {
      this.user = user;
      return this;
   }

   public KnowledgeLogQuery ip(String ip) {
      this.ip = ip;
      return this;
   }

   public KnowledgeLogQuery dateBegin(Date date) {
      this.date = date;
      return this;
   }

   public KnowledgeLogQuery dateEnd(Date date) {
      this.endDate = date;
      return this;
   }

   public KnowledgeLogQuery projectId(Long projectId) {
      this.projectId = projectId;
      return this;
   }

   public KnowledgeLogQuery packetId(Long packetId) {
      this.packetId = packetId;
      return this;
   }

   public KnowledgeLogQuery packetNameLike(String packetName) {
      this.packetName = packetName;
      return this;
   }

   public KnowledgeLogQuery groupId(String groupId) {
      this.groupId = groupId;
      return this;
   }
   public KnowledgeLog details(Long id) {
      String text = "select IN_PARAMS_, OUT_PARAMS_, LOGS_ from URULE_LOG_KNOWLEDGE WHERE ID_=?";
      Connection connection = JdbcUtils.getConnection();

      KnowledgeLog knowledgeLog;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();
         KnowledgeLog knowledgeLog2 = new KnowledgeLog();
         if (resultSet.next()) {
            knowledgeLog2.setInParams(resultSet.getString(1));
            knowledgeLog2.setOutParams(resultSet.getString(2));
            knowledgeLog2.setLogs(resultSet.getString(3));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         knowledgeLog = knowledgeLog2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return knowledgeLog;
   }
}
