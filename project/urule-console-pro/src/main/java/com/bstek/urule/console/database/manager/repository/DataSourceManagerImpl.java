package com.bstek.urule.console.database.manager.repository;

import com.bstek.urule.console.database.model.datasource.DataSource;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class DataSourceManagerImpl implements DataSourceManager {
   public DataSource get(Long var1) {
      List var2 = this.createQuery().id(var1).list();
      return var2.size() > 0 ? (DataSource)var2.get(0) : null;
   }

   public void add(DataSource var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_DATASOURCE (NAME_, TYPE_, DATASOURCE_BEAN_, DB_JNDI_NAME_, DB_DRIVER_, DB_URL_, DB_USER_, DB_PWD_, DB_VALIDATION_QUERY_, DB_INITIAL_SIZE_, DB_MAX_TOTAL_, DB_MAX_IDLE_, DB_MIN_IDLE_, DESC_, CREATE_USER_, CREATE_DATE_, ID_, GROUP_ID_) values(?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?)");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getType().name());
         var3.setString(3, var1.getDataSourceBean());
         var3.setString(4, var1.getDbJndiName());
         var3.setString(5, var1.getDbDriver());
         var3.setString(6, var1.getDbUrl());
         var3.setString(7, var1.getDbUser());
         var3.setString(8, var1.getDbPwd());
         var3.setString(9, var1.getDbValidationQuery());
         var3.setInt(10, var1.getDbInitialsize());
         var3.setInt(11, var1.getDbMaxTotal());
         var3.setInt(12, var1.getDbMaxIdle());
         var3.setInt(13, var1.getDbMinIdle());
         var3.setString(14, var1.getDesc());
         var3.setString(15, var1.getCreateUser());
         var3.setTimestamp(16, new Timestamp(var1.getCreateDate().getTime()));
         var3.setLong(17, var1.getId());
         var3.setString(18, var1.getGroupId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(DataSource var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("update URULE_DATASOURCE set NAME_=?, TYPE_=?, DATASOURCE_BEAN_=?, DB_JNDI_NAME_=?, DB_DRIVER_=?, DB_URL_=?, DB_USER_=?, DB_PWD_=?, DB_VALIDATION_QUERY_=?, DB_INITIAL_SIZE_=?, DB_MAX_TOTAL_=?, DB_MAX_IDLE_=?, DB_MIN_IDLE_=?, DESC_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getType().name());
         var3.setString(3, var1.getDataSourceBean());
         var3.setString(4, var1.getDbJndiName());
         var3.setString(5, var1.getDbDriver());
         var3.setString(6, var1.getDbUrl());
         var3.setString(7, var1.getDbUser());
         var3.setString(8, var1.getDbPwd());
         var3.setString(9, var1.getDbValidationQuery());
         var3.setInt(10, var1.getDbInitialsize());
         var3.setInt(11, var1.getDbMaxTotal());
         var3.setInt(12, var1.getDbMaxIdle());
         var3.setInt(13, var1.getDbMinIdle());
         var3.setString(14, var1.getDesc());
         var3.setString(15, var1.getUpdateUser());
         var3.setTimestamp(16, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setLong(17, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void remove(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_DATASOURCE where ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public DataSourceQuery createQuery() {
      return new DataSourceQueryImpl();
   }
}
