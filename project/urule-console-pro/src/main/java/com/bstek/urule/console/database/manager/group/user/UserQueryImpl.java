package com.bstek.urule.console.database.manager.group.user;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserQueryImpl implements UserQuery {
   private String id;
   private String name;
   private String idnameLike;
   List queryParameters = new ArrayList();
   public Page users(int pageIndex, int pageSize, String groupId) {
      String pageSql = "SELECT USER_ID_, USER_NAME_, CREATE_DATE_ FROM URULE_GROUP_USER  WHERE GROUP_ID_=?";
      this.queryParameters.clear();
      this.queryParameters.add(groupId);
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         pageSql = pageSql + " and" + stringBuilder.toString();
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
         List items = this.readUsers(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_GROUP_USER where GROUP_ID_=?";
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
      if (StringUtils.isNotBlank(this.id)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" URULE_GROUP_USER.USER_ID_ like ?");
         this.queryParameters.add("%" + this.id + "%");
      }

      if (StringUtils.isNotBlank(this.name)) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" URULE_GROUP_USER.USER_NAME_ like ?");
         this.queryParameters.add("%" + this.name + "%");
      }

      if (this.idnameLike != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" (URULE_GROUP_USER.USER_ID_ like ? or URULE_GROUP_USER.USER_NAME_ like ?) ");
         this.queryParameters.add("%" + this.idnameLike + "%");
         this.queryParameters.add("%" + this.idnameLike + "%");
      }

      return stringBuilder;
   }

   private List readUsers(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         User user = new User();
         user.setId(resultSet.getString(1));
         user.setName(resultSet.getString(2));
         user.setCreateDate(resultSet.getTimestamp(3));
         items.add(user);
      }

      return items;
   }

   public UserQuery idLike(String id) {
      this.id = id;
      return this;
   }

   public UserQuery nameLike(String name) {
      this.name = name;
      return this;
   }
   public Page roleUsers(int pageIndex, int pageSize, String groupId, long roleId) {
      String pageSql = "SELECT URULE_GROUP_USER.USER_ID_, URULE_GROUP_USER.USER_NAME_, URULE_GROUP_USER.CREATE_DATE_ FROM URULE_GROUP_USER  LEFT JOIN URULE_GROUP_USER_ROLE on URULE_GROUP_USER_ROLE.USER_ID_=URULE_GROUP_USER.USER_ID_ WHERE URULE_GROUP_USER_ROLE.ROLE_ID_=? and URULE_GROUP_USER.GROUP_ID_=?";
      this.queryParameters.clear();
      this.queryParameters.add(roleId);
      this.queryParameters.add(groupId);
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         pageSql = pageSql + " and" + stringBuilder.toString();
      }

      pageSql = pageSql + " order by URULE_GROUP_USER.CREATE_DATE_ desc";
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
         pageSql = "select count(*) FROM URULE_GROUP_USER  LEFT JOIN URULE_GROUP_USER_ROLE on URULE_GROUP_USER_ROLE.USER_ID_=URULE_GROUP_USER.USER_ID_ WHERE URULE_GROUP_USER_ROLE.ROLE_ID_=? and URULE_GROUP_USER.GROUP_ID_=?";
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

   public UserQuery idnameLike(String idnameLike) {
      this.idnameLike = idnameLike;
      return this;
   }
}
