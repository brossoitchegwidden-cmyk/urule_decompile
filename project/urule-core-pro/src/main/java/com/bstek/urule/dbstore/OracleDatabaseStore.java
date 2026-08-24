package com.bstek.urule.dbstore;

import com.bstek.urule.dbstore.service.DbService;
import com.bstek.urule.dbstore.service.OracleDbService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

public class OracleDatabaseStore implements DatabaseStore {
   private static final String CREATE_TABLE_SQL = "CREATE TABLE URULE_KP_STORE(ID_ VARCHAR2(60) PRIMARY KEY,UPDATE_DATE_ NUMBER NOT NULL,CREATE_USER_ VARCHAR2(60),DATA_ BLOB)";
   private DataSource dataSource;

   @Override
   public void init(DataSource ds) throws Exception {
      this.dataSource = ds;
      Connection connection = ds.getConnection();
      String text = "SELECT COUNT(*) FROM User_Tables WHERE table_name = 'URULE_KP_STORE'";
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(text);
      int number = 0;
      if (resultSet.next()) {
         number = resultSet.getInt(1);
      }

      resultSet.close();
      if (number == 0) {
         statement.execute("CREATE TABLE URULE_KP_STORE(ID_ VARCHAR2(60) PRIMARY KEY,UPDATE_DATE_ NUMBER NOT NULL,CREATE_USER_ VARCHAR2(60),DATA_ BLOB)");
      }

      statement.close();
      connection.close();
   }

   @Override
   public DbService getDbService() {
      return new OracleDbService(this.dataSource);
   }

   @Override
   public boolean support(String dbname) {
      return dbname.toLowerCase().indexOf("oracle") > -1;
   }
}
