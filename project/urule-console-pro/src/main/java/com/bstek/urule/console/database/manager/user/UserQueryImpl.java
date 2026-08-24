package com.bstek.urule.console.database.manager.user;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserQueryImpl implements UserQuery {
   private String name;
   private String id;
   private List queryParameters = new ArrayList();

   public UserQuery idLike(String id) {
      this.id = id;
      return this;
   }

   public UserQuery nameLike(String name) {
      this.name = name;
      return this;
   }

   public Page paging(int pageIndex, int pageSize, long projectId) {
      String pageSql = "select URULE_USER.ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, URULE_PROJECT_USER.CREATE_DATE_, UPDATE_DATE_ from URULE_USER left join URULE_PROJECT_USER on URULE_USER.ID_=URULE_PROJECT_USER.USER_ID_  where URULE_PROJECT_USER.PROJECT_ID_=?";
      this.queryParameters.clear();
      this.queryParameters.add(projectId);
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         pageSql = pageSql + " and" + stringBuilder.toString();
      }

      pageSql = pageSql + " order by URULE_PROJECT_USER.CREATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();

      Page page;
      try {
         Page page2 = new Page(pageIndex, pageSize);
         pageSql = JdbcUtils.getPageSql(pageSql, page2.getStartRow(), pageSize);
         PreparedStatement preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readUsers(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_USER left join URULE_PROJECT_USER on URULE_USER.ID_=URULE_PROJECT_USER.USER_ID_  where URULE_PROJECT_USER.PROJECT_ID_=?";
         if (stringBuilder.length() > 0) {
            pageSql = pageSql + " and" + stringBuilder.toString();
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

   private StringBuilder buildWhereClause() {
      StringBuilder stringBuilder = new StringBuilder();
      if (this.id != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" URULE_USER.ID_ like ?");
         this.queryParameters.add("%" + this.id + "%");
      }

      if (this.name != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" URULE_USER.NAME_ like ?");
         this.queryParameters.add("%" + this.name + "%");
      }

      return stringBuilder;
   }

   private List readUsers(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         User user = new User();
         user.setId(resultSet.getString(1));
         user.setName(resultSet.getString(2));
         user.setEmail(resultSet.getString(3));
         user.setSecretKey(resultSet.getString(4));
         user.setEnable(resultSet.getBoolean(5));
         user.setDesc(resultSet.getString(6));
         user.setExpirDate(resultSet.getTimestamp(7));
         user.setCreateDate(resultSet.getTimestamp(8));
         user.setUpdateDate(resultSet.getTimestamp(9));
         items.add(user);
      }

      return items;
   }

   public Page paging(int pageIndex, int pageSize, String groupId) {
      String pageSql = "select URULE_USER.ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, URULE_GROUP_USER.CREATE_DATE_, UPDATE_DATE_ from URULE_USER left join URULE_GROUP_USER on URULE_USER.ID_=URULE_GROUP_USER.USER_ID_  where URULE_GROUP_USER.PROJECT_ID_=?";
      this.queryParameters.clear();
      this.queryParameters.add(groupId);
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         pageSql = pageSql + " and" + stringBuilder.toString();
      }

      pageSql = pageSql + " order by URULE_GROUP_USER..CREATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();

      Page page;
      try {
         Page page2 = new Page(pageIndex, pageSize);
         pageSql = JdbcUtils.getPageSql(pageSql, page2.getStartRow(), pageSize);
         PreparedStatement preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readUsers(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_USER left join URULE_GROUP_USER on URULE_USER.ID_=URULE_GROUP_USER.USER_ID_  where URULE_GROUP_USER.PROJECT_ID_=?";
         if (stringBuilder.length() > 0) {
            pageSql = pageSql + " and" + stringBuilder.toString();
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
}
