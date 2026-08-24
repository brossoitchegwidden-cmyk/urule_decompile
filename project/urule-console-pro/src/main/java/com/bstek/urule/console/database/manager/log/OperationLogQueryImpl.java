package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.OperationLog;
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

public class OperationLogQueryImpl implements OperationLogQuery {
   private String exactUserId;
   private String userIdLike;
   private String username;
   private String groupId;
   private Long projectId;
   private String exactCategory;
   private String categoryLike;
   private List categorys;
   private List actions;
   private Date startDate;
   private Date endDate;
   private List queryParameters = new ArrayList();

   protected OperationLogQueryImpl() {
   }

   public Page paging(int pageIndex, int pageSize) {
      String pageSql = "select ID_,USER_ID_,USER_NAME_,GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CATEGORY_, ACTION_, ITEM_ID_, CONTENT_, CREATE_DATE_ from URULE_LOG_OPERATION";
      Connection connection = JdbcUtils.getConnection();

      Page page;
      try {
         StringBuilder stringBuilder = this.buildWhereClause(connection);
         if (stringBuilder.length() > 0) {
            pageSql = pageSql + " where" + stringBuilder.toString();
         }

         pageSql = pageSql + " order by CREATE_DATE_ desc";
         Page page2 = new Page(pageIndex, pageSize);
         pageSql = JdbcUtils.getPageSql(pageSql, page2.getStartRow(), pageSize);
         PreparedStatement preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readOperationLogs(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_LOG_OPERATION";
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
      String text = "select ID_,USER_ID_,USER_NAME_,GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CATEGORY_, ACTION_, ITEM_ID_, CONTENT_, CREATE_DATE_ from URULE_LOG_OPERATION";
      Connection connection = JdbcUtils.getConnection();

      List listResult;
      try {
         StringBuilder stringBuilder = this.buildWhereClause(connection);
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         text = text + " order by CREATE_DATE_ desc";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readOperationLogs(resultSet);
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

   private List readOperationLogs(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         OperationLog operationLog = new OperationLog();
         operationLog.setId(resultSet.getLong(1));
         operationLog.setUserId(resultSet.getString(2));
         operationLog.setUsername(resultSet.getString(3));
         operationLog.setGroupId(resultSet.getString(4));
         operationLog.setGroupName(resultSet.getString(5));
         operationLog.setProjectId(resultSet.getLong(6));
         operationLog.setProjectName(resultSet.getString(7));
         operationLog.setCategory(resultSet.getString(8));
         operationLog.setAction(resultSet.getString(9));
         operationLog.setItemId(resultSet.getString(10));
         operationLog.setContent(resultSet.getString(11));
         operationLog.setCreateDate(resultSet.getTimestamp(12));
         items.add(operationLog);
      }

      return items;
   }

   private StringBuilder buildWhereClause(Connection connection) throws SQLException {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (StringUtils.isNotBlank(this.exactUserId)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" USER_ID_ = ?");
         this.queryParameters.add(this.exactUserId);
      }

      if (StringUtils.isNotBlank(this.userIdLike)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" USER_ID_ like ?");
         this.queryParameters.add("%" + this.userIdLike + "%");
      }

      if (StringUtils.isNotBlank(this.username)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" USER_NAME_ like ?");
         this.queryParameters.add("%" + this.username + "%");
      }

      if (this.groupId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" GROUP_ID_ = ?");
         this.queryParameters.add(this.groupId);
      }

      if (this.projectId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROJECT_ID_ = ?");
         this.queryParameters.add(this.projectId);
      } else {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROJECT_ID_ is null");
      }

      if (StringUtils.isNotBlank(this.exactCategory)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CATEGORY_ = ?");
         this.queryParameters.add(this.exactCategory);
      }

      if (StringUtils.isNotBlank(this.categoryLike)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CATEGORY_ like ?");
         this.queryParameters.add("%" + this.categoryLike + "%");
      }

      if (this.categorys != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CATEGORY_ in (");

         for(int index = 0; index < this.categorys.size(); ++index) {
            String text = (String)this.categorys.get(index);
            stringBuilder.append("?");
            if (index + 1 < this.categorys.size()) {
               stringBuilder.append(",");
            }

            this.queryParameters.add(text);
         }

         stringBuilder.append(")");
      }

      if (this.actions != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ACTION_ in (");

         for(int index2 = 0; index2 < this.actions.size(); ++index2) {
            String text2 = (String)this.actions.get(index2);
            stringBuilder.append("?");
            if (index2 + 1 < this.actions.size()) {
               stringBuilder.append(",");
            }

            this.queryParameters.add(text2);
         }

         stringBuilder.append(")");
      }

      if (this.startDate != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_DATE_ > ?");
         this.queryParameters.add(this.startDate);
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

   public OperationLogQuery userId(String userId) {
      this.exactUserId = userId;
      return this;
   }

   public OperationLogQuery groupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   public OperationLogQuery username(String username) {
      this.username = username;
      return this;
   }

   public OperationLogQuery projectId(Long projectId) {
      this.projectId = projectId;
      return this;
   }

   public OperationLogQuery category(String category) {
      this.exactCategory = category;
      return this;
   }

   public OperationLogQuery categoryIn(List categorys) {
      this.categorys = categorys;
      return this;
   }

   public OperationLogQuery actionIn(List actions) {
      this.actions = actions;
      return this;
   }

   public OperationLogQuery dateBegin(Date date) {
      this.startDate = date;
      return this;
   }

   public OperationLogQuery dateEnd(Date date) {
      this.endDate = date;
      return this;
   }

   public OperationLogQuery userIdLike(String userId) {
      this.userIdLike = userId;
      return this;
   }

   public OperationLogQuery categoryLike(String category) {
      this.categoryLike = category;
      return this;
   }
}
