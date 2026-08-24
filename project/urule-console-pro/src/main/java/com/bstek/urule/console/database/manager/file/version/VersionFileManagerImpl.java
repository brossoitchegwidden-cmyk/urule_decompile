package com.bstek.urule.console.database.manager.file.version;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.VersionFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.MD5Utils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class VersionFileManagerImpl implements VersionFileManager {
   protected VersionFileManagerImpl() {
   }

   public VersionFile loadFile(long id) {
      List items = this.newQuery().id(id).list();
      return items.size() > 0 ? (VersionFile)items.get(0) : null;
   }

   public String loadFileContent(long id) {
      Connection connection = JdbcUtils.getConnection();
      String string = null;

      String fileContent;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select CONTENT_ from URULE_VERSION_FILE where ID_=?");
         preparedStatement.setLong(1, id);

         ResultSet resultSet;
         for(resultSet = preparedStatement.executeQuery(); resultSet.next(); string = resultSet.getString(1)) {
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         fileContent = string;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return fileContent;
   }

   /**专门用于项目导入提供的方法，不在接口中声明*/
   public void updateContent(long id, String content) {
      Connection connection = JdbcUtils.getConnection();
      String text = "update URULE_VERSION_FILE set CONTENT_=?,DIGEST_=? where ID_=?";

      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, content);
         preparedStatement.setString(2, MD5Utils.stringToMD5(content));
         preparedStatement.setLong(3, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public VersionFile loadFile(long fileId, String version) {
      List items = this.newQuery().fileId(fileId).version(version).list();
      return items.size() > 0 ? (VersionFile)items.get(0) : null;
   }

   public List loadFiles(long fileId) {
      return this.newQuery().fileId(fileId).list();
   }

   public void saveFile(VersionFile file) {
      Connection connection = JdbcUtils.getConnection();
      String text = "insert into URULE_VERSION_FILE(ID_, FILE_ID_, PROJECT_ID_, NAME_, VERSION_, NOTE_, CONTENT_, DIGEST_, CREATE_USER_, CREATE_DATE_) values(?,?,?,?,?,?,?,?,?, ?)";
      long longValue = IDGenerator.getInstance().nextId(IDType.FILE);
      file.setId(longValue);

      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, longValue);
         preparedStatement.setLong(2, file.getFileId());
         preparedStatement.setLong(3, file.getProjectId());
         preparedStatement.setString(4, file.getName());
         preparedStatement.setString(5, file.getVersion());
         preparedStatement.setString(6, file.getNote());
         preparedStatement.setString(7, file.getContent());
         preparedStatement.setString(8, MD5Utils.stringToMD5(file.getContent()));
         preparedStatement.setString(9, file.getCreateUser());
         preparedStatement.setTimestamp(10, new Timestamp((new Date()).getTime()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public VersionFileQuery newQuery() {
      return new VersionFileQueryImpl();
   }

   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_VERSION_FILE where PROJECT_ID_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void deleteByFileId(long fileId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_VERSION_FILE where FILE_ID_=?");
         preparedStatement.setLong(1, fileId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
}
