package com.bstek.urule.dbstore.service;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;

public abstract class DbService {
   private DataSource dataSource;

   public DbService(DataSource ds) {
      this.dataSource = ds;
   }

   public DataSource getDataSource() {
      return this.dataSource;
   }

   public KnowledgePackage loadKnowledgePackage(String packageId) {
      Connection connection = null;

      try {
         connection = this.acquireConnection();
         return this.queryKnowledgePackage(packageId, connection);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         try {
            if (connection != null) {
               connection.close();
            }
         } catch (SQLException sQLException) {
         }
      }
   }

   public KnowledgePackage verifyKnowledgePackage(String packageId, long fileModifyDate) {
      String text = "SELECT count(*) FROM URULE_KP_STORE WHERE ID_=? AND UPDATE_DATE_=?";
      Connection connection = null;
      ResultSet resultSet = null;
      PreparedStatement preparedStatement = null;

      try {
         connection = this.acquireConnection();
         preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, packageId);
         preparedStatement.setLong(2, fileModifyDate);
         resultSet = preparedStatement.executeQuery();
         int number = 0;
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         return number > 0 ? null : this.queryKnowledgePackage(packageId, connection);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         try {
            if (preparedStatement != null) {
               preparedStatement.close();
            }

            if (resultSet != null) {
               resultSet.close();
            }

            if (connection != null) {
               connection.close();
            }
         } catch (SQLException sQLException) {
         }
      }
   }

   protected Connection acquireConnection() throws SQLException {
      return this.dataSource.getConnection();
   }

   protected KnowledgePackage queryKnowledgePackage(String packageId, Connection conn) throws SQLException, IOException {
      String text = "select DATA_,UPDATE_DATE_ from URULE_KP_STORE where ID_=?";
      ResultSet resultSet = null;
      PreparedStatement preparedStatement = null;
      InputStream inputStream = null;

      try {
         preparedStatement = conn.prepareStatement(text);
         preparedStatement.setString(1, packageId);
         resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            Blob blob = resultSet.getBlob(1);
            inputStream = blob.getBinaryStream();
            String text2 = Utils.uncompress(IOUtils.toByteArray(inputStream));
            KnowledgePackage knowledgePackage = Utils.stringToKnowledgePackage(text2);
            KnowledgePackageImpl knowledgePackageImpl = (KnowledgePackageImpl)knowledgePackage;
            knowledgePackageImpl.setPackageInfo(packageId);
            long timestamp = resultSet.getLong(2);
            knowledgePackageImpl.setTimestamp(timestamp);
            return knowledgePackageImpl;
         } else {
            throw new RuleException("未在数据库中找到存储的知识包【" + packageId + "】");
         }
      } finally {
         if (preparedStatement != null) {
            preparedStatement.close();
         }

         if (resultSet != null) {
            resultSet.close();
         }

         IOUtils.closeQuietly(inputStream);
      }
   }
}
