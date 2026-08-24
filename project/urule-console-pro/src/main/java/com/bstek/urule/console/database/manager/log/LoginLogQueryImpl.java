package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginLogQueryImpl implements LoginLogQuery {
   private String userId;
   private String userIdLike;
   private String ip;
   private String username;
   private Date date;
   private Date loginDateEnd;
   private List queryParameters = new ArrayList();

   protected LoginLogQueryImpl() {
   }

   public Page paging(int pageIndex, int pageSize) {
      String pageSql = "select ID_,USER_ID_,USER_NAME_,IP_,USER_AGENT_,CREATE_DATE_ from URULE_LOG_USERLOGIN";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         pageSql = pageSql + " where" + stringBuilder.toString();
      }

      pageSql = pageSql + " order by CREATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();

      Page page;
      try {
         Page page2 = new Page(pageIndex, pageSize);
         pageSql = JdbcUtils.getPageSql(pageSql, page2.getStartRow(), pageSize);
         PreparedStatement preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readLoginLogs(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_LOG_USERLOGIN";
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

   private List readLoginLogs(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         LoginLog loginLog = new LoginLog();
         loginLog.setId(resultSet.getLong(1));
         loginLog.setUserId(resultSet.getString(2));
         loginLog.setUsername(resultSet.getString(3));
         loginLog.setIp(resultSet.getString(4));
         loginLog.setUserAgent(resultSet.getString(5));
         loginLog.setCreateDate(resultSet.getTimestamp(6));
         items.add(loginLog);
      }

      return items;
   }

   private StringBuilder buildWhereClause() {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (StringUtils.isNotBlank(this.userId)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" USER_ID_ = ?");
         this.queryParameters.add(this.userId);
      }

      if (StringUtils.isNotBlank(this.userIdLike)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" USER_ID_ like ?");
         this.queryParameters.add("%" + this.userIdLike + "%");
      }

      if (StringUtils.isNotBlank(this.ip)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" IP_ = ?");
         this.queryParameters.add(this.ip);
      }

      if (StringUtils.isNotBlank(this.username)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" USER_NAME_ like ?");
         this.queryParameters.add("%" + this.username + "%");
      }

      if (this.date != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_DATE_ > ?");
         this.queryParameters.add(this.date);
      }

      if (this.loginDateEnd != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_DATE_ < ?");
         this.queryParameters.add(this.loginDateEnd);
      }

      return stringBuilder;
   }

   public LoginLogQuery userId(String userId) {
      this.userId = userId;
      return this;
   }

   public LoginLogQuery ip(String ip) {
      this.ip = ip;
      return this;
   }

   public LoginLogQuery username(String username) {
      this.username = username;
      return this;
   }

   public LoginLogQuery loginDateBegin(Date date) {
      this.date = date;
      return this;
   }

   public LoginLogQuery loginDateEnd(Date date) {
      this.loginDateEnd = date;
      return this;
   }

   public LoginLogQuery userIdLike(String userId) {
      this.userIdLike = userId;
      return this;
   }
}
