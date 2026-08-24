package com.bstek.urule.dbstore;

import com.bstek.urule.dbstore.service.DbService;
import com.bstek.urule.dbstore.service.MysqlDbService;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

public class MysqlDatabaseStore implements DatabaseStore {
   private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS URULE_KP_STORE(ID_ VARCHAR(60) PRIMARY KEY,UPDATE_DATE_ BIGINT NOT NULL,CREATE_USER_ VARCHAR(60),DATA_ LONGBLOB)";
   private DataSource dataSource;

   @Override
   public void init(DataSource ds) throws Exception {
      this.dataSource = ds;
      Connection connection = ds.getConnection();
      Statement statement = connection.createStatement();
      statement.execute(CREATE_TABLE_SQL);
      statement.close();
      connection.close();
   }

   @Override
   public DbService getDbService() {
      return new MysqlDbService(this.dataSource);
   }

   @Override
   public boolean support(String dbname) {
      return dbname.toLowerCase().indexOf("mysql") > -1;
   }
}
