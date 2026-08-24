package com.bstek.urule.console.database.manager.repository;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.batch.DataSourceType;
import com.bstek.urule.console.database.model.datasource.DataSource;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataSourceQueryImpl implements DataSourceQuery {
   private Long id;
   private String nameLike;
   private String type;
   private String createUser;
   private String groupId;
   private List queryParameters = new ArrayList();

   public DataSourceQuery id(Long id) {
      this.id = id;
      return this;
   }

   public DataSourceQuery groupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   private StringBuilder buildWhereClause() {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (this.groupId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" GROUP_ID_=?");
         this.queryParameters.add(this.groupId);
      }

      if (this.id != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ID_=?");
         this.queryParameters.add(this.id);
      }

      if (this.nameLike != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NAME_=?");
         this.queryParameters.add(this.nameLike);
      }

      if (this.type != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" TYPE_=?");
         this.queryParameters.add(this.type);
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

   public List list() {
      Connection connection = JdbcUtils.getConnection();

      List listResult;
      try {
         String text = this.buildSelectSql();
         List items = this.queryDataSources(connection, text);
         listResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listResult;
   }

   private List queryDataSources(Connection connection, String text) throws Exception {
      PreparedStatement preparedStatement = connection.prepareStatement(text);
      JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
      ArrayList items = new ArrayList();
      ResultSet resultSet = preparedStatement.executeQuery();

      while(resultSet.next()) {
         DataSource dataSource = this.mapDataSource(resultSet);
         items.add(dataSource);
      }

      JdbcUtils.closeResultSet(resultSet);
      JdbcUtils.closeStatement(preparedStatement);
      return items;
   }

   private DataSource mapDataSource(ResultSet resultSet) throws SQLException {
      DataSource dataSource = new DataSource();
      dataSource.setName(resultSet.getString(1));
      dataSource.setType(DataSourceType.valueOf(resultSet.getString(2)));
      dataSource.setDataSourceBean(resultSet.getString(3));
      dataSource.setDbJndiName(resultSet.getString(4));
      dataSource.setDbDriver(resultSet.getString(5));
      dataSource.setDbUrl(resultSet.getString(6));
      dataSource.setDbUser(resultSet.getString(7));
      dataSource.setDbPwd(resultSet.getString(8));
      dataSource.setDbValidationQuery(resultSet.getString(9));
      dataSource.setDbInitialsize(resultSet.getInt(10));
      dataSource.setDbMaxTotal(resultSet.getInt(11));
      dataSource.setDbMaxIdle(resultSet.getInt(12));
      dataSource.setDbMinIdle(resultSet.getInt(13));
      dataSource.setDesc(resultSet.getString(14));
      dataSource.setCreateUser(resultSet.getString(15));
      dataSource.setCreateDate(resultSet.getTimestamp(16));
      dataSource.setUpdateUser(resultSet.getString(17));
      dataSource.setUpdateDate(resultSet.getTimestamp(18));
      dataSource.setId(resultSet.getLong(19));
      dataSource.setGroupId(resultSet.getString(20));
      return dataSource;
   }

   public void page(Page page) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = this.buildSelectSql();
         String pageSql = text + " ORDER BY NAME_ ";
         pageSql = JdbcUtils.getPageSql(connection, pageSql, page.getStartRow(), page.getPageSize());
         page.setData(this.queryDataSources(connection, pageSql));
         PreparedStatement preparedStatement = connection.prepareStatement(JdbcUtils.getCountSql(text));
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

   private String buildSelectSql() {
      String text = "SELECT NAME_, TYPE_, DATASOURCE_BEAN_, DB_JNDI_NAME_, DB_DRIVER_, DB_URL_, DB_USER_, DB_PWD_, DB_VALIDATION_QUERY_, DB_INITIAL_SIZE_, DB_MAX_TOTAL_, DB_MAX_IDLE_, DB_MIN_IDLE_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, GROUP_ID_ FROM URULE_DATASOURCE ";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where" + stringBuilder.toString();
      }

      return text;
   }

   public DataSourceQuery nameLike(String nameLike) {
      this.nameLike = nameLike;
      return this;
   }

   public DataSourceQuery createUserLike(String createUser) {
      this.createUser = createUser;
      return this;
   }

   public DataSourceQuery type(String type) {
      this.type = type;
      return this;
   }
}
