package com.bstek.urule.console.database.manager.repository;

import com.bstek.urule.console.database.model.datasource.DataSource;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class DataSourceManagerImpl implements DataSourceManager {
   public DataSource get(Long id) {
      List items = this.createQuery().id(id).list();
      return items.size() > 0 ? (DataSource)items.get(0) : null;
   }

   public void add(DataSource dataSource) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_DATASOURCE (NAME_, TYPE_, DATASOURCE_BEAN_, DB_JNDI_NAME_, DB_DRIVER_, DB_URL_, DB_USER_, DB_PWD_, DB_VALIDATION_QUERY_, DB_INITIAL_SIZE_, DB_MAX_TOTAL_, DB_MAX_IDLE_, DB_MIN_IDLE_, DESC_, CREATE_USER_, CREATE_DATE_, ID_, GROUP_ID_) values(?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?)");
         preparedStatement.setString(1, dataSource.getName());
         preparedStatement.setString(2, dataSource.getType().name());
         preparedStatement.setString(3, dataSource.getDataSourceBean());
         preparedStatement.setString(4, dataSource.getDbJndiName());
         preparedStatement.setString(5, dataSource.getDbDriver());
         preparedStatement.setString(6, dataSource.getDbUrl());
         preparedStatement.setString(7, dataSource.getDbUser());
         preparedStatement.setString(8, dataSource.getDbPwd());
         preparedStatement.setString(9, dataSource.getDbValidationQuery());
         preparedStatement.setInt(10, dataSource.getDbInitialsize());
         preparedStatement.setInt(11, dataSource.getDbMaxTotal());
         preparedStatement.setInt(12, dataSource.getDbMaxIdle());
         preparedStatement.setInt(13, dataSource.getDbMinIdle());
         preparedStatement.setString(14, dataSource.getDesc());
         preparedStatement.setString(15, dataSource.getCreateUser());
         preparedStatement.setTimestamp(16, new Timestamp(dataSource.getCreateDate().getTime()));
         preparedStatement.setLong(17, dataSource.getId());
         preparedStatement.setString(18, dataSource.getGroupId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(DataSource dataSource) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_DATASOURCE set NAME_=?, TYPE_=?, DATASOURCE_BEAN_=?, DB_JNDI_NAME_=?, DB_DRIVER_=?, DB_URL_=?, DB_USER_=?, DB_PWD_=?, DB_VALIDATION_QUERY_=?, DB_INITIAL_SIZE_=?, DB_MAX_TOTAL_=?, DB_MAX_IDLE_=?, DB_MIN_IDLE_=?, DESC_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         preparedStatement.setString(1, dataSource.getName());
         preparedStatement.setString(2, dataSource.getType().name());
         preparedStatement.setString(3, dataSource.getDataSourceBean());
         preparedStatement.setString(4, dataSource.getDbJndiName());
         preparedStatement.setString(5, dataSource.getDbDriver());
         preparedStatement.setString(6, dataSource.getDbUrl());
         preparedStatement.setString(7, dataSource.getDbUser());
         preparedStatement.setString(8, dataSource.getDbPwd());
         preparedStatement.setString(9, dataSource.getDbValidationQuery());
         preparedStatement.setInt(10, dataSource.getDbInitialsize());
         preparedStatement.setInt(11, dataSource.getDbMaxTotal());
         preparedStatement.setInt(12, dataSource.getDbMaxIdle());
         preparedStatement.setInt(13, dataSource.getDbMinIdle());
         preparedStatement.setString(14, dataSource.getDesc());
         preparedStatement.setString(15, dataSource.getUpdateUser());
         preparedStatement.setTimestamp(16, new Timestamp(dataSource.getUpdateDate().getTime()));
         preparedStatement.setLong(17, dataSource.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void remove(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_DATASOURCE where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public DataSourceQuery createQuery() {
      return new DataSourceQueryImpl();
   }
}
