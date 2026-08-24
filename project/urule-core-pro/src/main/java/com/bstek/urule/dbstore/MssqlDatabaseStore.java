package com.bstek.urule.dbstore;

import com.bstek.urule.dbstore.service.DbService;
import com.bstek.urule.dbstore.service.MssqlDbService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

public class MssqlDatabaseStore implements DatabaseStore {
   private static final String CREATE_TABLE_SQL = "CREATE TABLE URULE_KP_STORE(ID_ varchar(60) primary key,UPDATE_DATE_ bigint not null,CREATE_USER_ varchar(60),DATA_ varbinary(max))";
   private DataSource dataSource;

   @Override
   public void init(DataSource ds) throws Exception {
      this.dataSource = ds;
      Connection connection = ds.getConnection();
      String text = "SELECT count(*) FROM sysobjects WHERE name='URULE_KP_STORE'";
      Statement statement = ds.getConnection().createStatement();
      ResultSet resultSet = statement.executeQuery(text);
      int number = 0;
      if (resultSet.next()) {
         number = resultSet.getInt(1);
      }

      resultSet.close();
      if (number == 0) {
         statement.execute("CREATE TABLE URULE_KP_STORE(ID_ varchar(60) primary key,UPDATE_DATE_ bigint not null,CREATE_USER_ varchar(60),DATA_ varbinary(max))");
      }

      statement.close();
      connection.close();
   }

   @Override
   public DbService getDbService() {
      return new MssqlDbService(this.dataSource);
   }

   @Override
   public boolean support(String dbname) {
      return dbname.toLowerCase().indexOf("sql server") > -1;
   }
}
