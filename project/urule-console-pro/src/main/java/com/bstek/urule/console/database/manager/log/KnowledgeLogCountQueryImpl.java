package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.database.vo.RuleExecVO;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KnowledgeLogCountQueryImpl implements KnowledgeLogCountQuery {
   private Long projectId;
   private String groupId;
   private String user;
   private Long packageId;
   private String packageName;
   private Date date;
   private Date endDate;
   private List queryParameters = new ArrayList();

   public KnowledgeLogCountQuery projectId(Long projectId) {
      this.projectId = projectId;
      return this;
   }

   public KnowledgeLogCountQuery groupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   public KnowledgeLogCountQuery user(String user) {
      this.user = user;
      return this;
   }

   public KnowledgeLogCountQuery packageId(Long packageId) {
      this.packageId = packageId;
      return this;
   }

   public KnowledgeLogCountQuery packageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public KnowledgeLogCountQuery dateBegin(Date date) {
      this.date = date;
      return this;
   }

   public KnowledgeLogCountQuery dateEnd(Date date) {
      this.endDate = date;
      return this;
   }

   public List listTime() {
      String text = "select KNOWLEDGE_ID_, KNOWLEDGE_NAME_, COUNT(TIME_) AS COUNT_TIME_ from URULE_LOG_KNOWLEDGE";
      Connection connection = JdbcUtils.getConnection();

      List listTimeResult;
      try {
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         text = text + " GROUP BY KNOWLEDGE_ID_, KNOWLEDGE_NAME_ order by COUNT_TIME_ DESC";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readKnowledgeLogCounts(resultSet);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         listTimeResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listTimeResult;
   }

   private List readKnowledgeLogCounts(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         RuleExecVO ruleExecVO = new RuleExecVO();
         ruleExecVO.setKnowledgeId(resultSet.getLong(1));
         ruleExecVO.setKnowledgeName(resultSet.getString(2));
         ruleExecVO.setTime(resultSet.getInt(3));
         items.add(ruleExecVO);
      }

      return items;
   }

   public List listExec() {
      String text = "select KNOWLEDGE_ID_, KNOWLEDGE_NAME_, COUNT(ID_) as RECORD_COUNT from URULE_LOG_KNOWLEDGE";
      Connection connection = JdbcUtils.getConnection();

      List listExecResult;
      try {
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         text = text + " group by KNOWLEDGE_ID_, KNOWLEDGE_NAME_ order by RECORD_COUNT DESC";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readRuleExecutionCounts(resultSet);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         listExecResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listExecResult;
   }

   private List readRuleExecutionCounts(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         RuleExecVO ruleExecVO = new RuleExecVO();
         ruleExecVO.setKnowledgeId(resultSet.getLong(1));
         ruleExecVO.setKnowledgeName(resultSet.getString(2));
         ruleExecVO.setCount(resultSet.getInt(3));
         items.add(ruleExecVO);
      }

      return items;
   }

   private StringBuilder buildWhereClause() throws SQLException {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (this.user != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" USER_ = ?");
         this.queryParameters.add(this.user);
      }

      if (this.projectId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROJECT_ID_ = ?");
         this.queryParameters.add(this.projectId);
      }

      if (this.groupId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" GROUP_ID_ = ?");
         this.queryParameters.add(this.groupId);
      }

      if (this.packageId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PACKAGE_ID_ = ?");
         this.queryParameters.add(this.packageId);
      }

      if (this.packageName != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PACKAGE_NAME_ = ?");
         this.queryParameters.add(this.packageName);
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
}
