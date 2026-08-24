package com.bstek.urule.dbstore.manager;

import com.bstek.urule.PropertyConfigurer;
import com.bstek.urule.dbstore.DatabaseStore;
import com.bstek.urule.dbstore.MssqlDatabaseStore;
import com.bstek.urule.dbstore.MysqlDatabaseStore;
import com.bstek.urule.dbstore.OracleDatabaseStore;
import com.bstek.urule.dbstore.service.DatabaseKnowledgePackageFileService;
import com.bstek.urule.dbstore.service.DbService;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.service.KnowledgeServiceImpl;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class KnowledgePackageManager implements ApplicationContextAware {
   public static final String BEAN_ID = "urule.dbstore.knowledgePackageManager";
   private static final String URULE_KNOWLEDGEPACKAGEDATABASESTORE_DATASOURCE = "urule.knowledgePackageDatabaseStore.dataSource";
   private DataSource dataSource;
   private static List<DatabaseStore> databaseStores = new ArrayList<>();

   public int removeKnowledgePackage(String id) throws Exception {
      Connection connection = this.resolveConnection();
      Statement statement = null;
      String text = "DELETE FROM URULE_KP_STORE where ID_=?";

      try {
         statement = connection.createStatement();
         return statement.executeUpdate(text);
      } finally {
         if (statement != null) {
            statement.close();
         }

         connection.close();
      }
   }

   public boolean saveKnowledgePackage(InputStream inputStream, String id, String createUser) throws Exception {
      String text = "SELECT count(*) FROM URULE_KP_STORE WHERE ID_=?";
      Connection connection = this.resolveConnection();
      PreparedStatement preparedStatement = null;

      try {
         preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();
         int number = 0;
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         resultSet.close();
         preparedStatement.close();
         if (number > 0) {
            text = "UPDATE URULE_KP_STORE SET UPDATE_DATE_=?,CREATE_USER_=?,DATA_=? WHERE ID_=?";
            preparedStatement = connection.prepareStatement(text);
            preparedStatement.setLong(1, new Date().getTime());
            preparedStatement.setString(2, createUser);
            preparedStatement.setBlob(3, inputStream);
            preparedStatement.setString(4, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return false;
         } else {
            text = "INSERT INTO URULE_KP_STORE(ID_,UPDATE_DATE_,CREATE_USER_,DATA_) VALUES(?,?,?,?)";
            preparedStatement = connection.prepareStatement(text);
            preparedStatement.setString(1, id);
            preparedStatement.setLong(2, new Date().getTime());
            preparedStatement.setString(3, createUser);
            preparedStatement.setBlob(4, inputStream);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
         }
      } finally {
         if (preparedStatement != null && !preparedStatement.isClosed()) {
            preparedStatement.close();
         }

         connection.close();
      }
   }

   private Connection resolveConnection() throws SQLException {
      return this.dataSource.getConnection();
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      String property = PropertyConfigurer.getProperty("urule.knowledgePackageDatabaseStore.dataSource");
      if (!StringUtils.isBlank(property) && applicationContext.containsBean(property)) {
         this.dataSource = (DataSource)applicationContext.getBean(property);
         String databaseProductName = null;
         DbService dbService = null;
         Connection connection = null;

         try {
            connection = this.dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            databaseProductName = metaData.getDatabaseProductName();

            for (DatabaseStore databaseStore : KnowledgePackageManager.databaseStores) {
               if (databaseStore.support(databaseProductName)) {
                  System.out.println(">>>初始化" + databaseProductName + "数据库中知识包存储表,目标数据库中如不存在知识包存储表将会自动创建...");
                  databaseStore.init(this.dataSource);
                  dbService = databaseStore.getDbService();
                  break;
               }
            }
         } catch (Exception exception) {
            throw new RuleException(exception);
         } finally {
            try {
               if (connection != null) {
                  connection.close();
               }
            } catch (SQLException sQLException) {
               java.util.logging.Logger.getLogger(KnowledgePackageManager.class.getName()).log(java.util.logging.Level.SEVERE, sQLException.getMessage(), sQLException);
            }
         }

         if (dbService != null) {
            DatabaseKnowledgePackageFileService databaseKnowledgePackageFileService = new DatabaseKnowledgePackageFileService(dbService);
            KnowledgeServiceImpl knowledgeServiceImpl = (KnowledgeServiceImpl)applicationContext.getBean("urule.knowledgeService");
            knowledgeServiceImpl.setKnowledgePackageFileService(databaseKnowledgePackageFileService);
         } else {
            System.out.println(">>>知识包数据存储不支持当前数据类型【" + databaseProductName + "】...");
         }
      }
   }

   static {
      KnowledgePackageManager.databaseStores.add(new MysqlDatabaseStore());
      KnowledgePackageManager.databaseStores.add(new OracleDatabaseStore());
      KnowledgePackageManager.databaseStores.add(new MssqlDatabaseStore());
   }
}
