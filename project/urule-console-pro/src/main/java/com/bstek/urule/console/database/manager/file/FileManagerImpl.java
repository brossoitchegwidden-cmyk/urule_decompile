package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.MD5Utils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileManagerImpl implements FileManager {
   public FileQuery newQuery() {
      return new FileQueryImpl();
   }
   public RuleFile get(long id) {
      FileQueryImpl fileQueryImpl = new FileQueryImpl();
      List items = fileQueryImpl.id(id).list((Long)null);
      if (items.size() == 0) {
         throw new RuleException("File 【" + id + "】 not exist!");
      } else {
         return (RuleFile)items.get(0);
      }
   }
   public String loadContent(long id) {
      Connection connection = JdbcUtils.getConnection();
      String string = null;

      String content;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select CONTENT_ from URULE_FILE where ID_=?");
         preparedStatement.setLong(1, id);

         ResultSet resultSet;
         for(resultSet = preparedStatement.executeQuery(); resultSet.next(); string = resultSet.getString(1)) {
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         content = string;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return content;
   }
   public void add(RuleFile file) {
      Connection connection = JdbcUtils.getConnection();

      try {
         file.setId(IDGenerator.getInstance().nextId(IDType.FILE));
         file.setCreateDate(new Date(System.currentTimeMillis()));
         file.setModifyDate(new Date(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_FILE (ID_, NAME_, TYPE_, PACKAGE_ID_, CONTENT_, CREATE_USER_, CREATE_DATE_,UPDATE_USER_, UPDATE_DATE_,PROJECT_ID_,LATEST_VERSION_,DELETED_) values (?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setLong(1, file.getId());
         preparedStatement.setString(2, file.getName());
         preparedStatement.setString(3, file.getType());
         preparedStatement.setLong(4, file.getParentId());
         preparedStatement.setString(5, file.getContent());
         preparedStatement.setString(6, file.getCreateUser());
         preparedStatement.setTimestamp(7, new Timestamp(file.getCreateDate().getTime()));
         preparedStatement.setString(8, file.getCreateUser());
         preparedStatement.setTimestamp(9, new Timestamp(file.getModifyDate().getTime()));
         preparedStatement.setLong(10, file.getProjectId());
         preparedStatement.setString(11, file.getLatestVersion());
         preparedStatement.setBoolean(12, file.isDeleted());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void updateContent(long id, String account, String content) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_FILE set CONTENT_=?, DIGEST_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         preparedStatement.setString(1, content);
         preparedStatement.setString(2, MD5Utils.stringToMD5(content));
         preparedStatement.setString(3, account);
         preparedStatement.setTimestamp(4, new Timestamp((new Date()).getTime()));
         preparedStatement.setLong(5, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public List list(long projectId, long parentId) {
      ArrayList listResult = new ArrayList();
      listResult.addAll(this.loadChildren(projectId, parentId));
      return listResult;
   }
   public List list(long projectId, long parentId, String type) {
      ArrayList listResult = new ArrayList();
      if (type.contentEquals(ResourceType.Library.name())) {
         listResult.addAll(this.loadChildrenByType(projectId, parentId, ResourceType.VariableLibrary.name()));
         listResult.addAll(this.loadChildrenByType(projectId, parentId, ResourceType.ParameterLibrary.name()));
         listResult.addAll(this.loadChildrenByType(projectId, parentId, ResourceType.ConstantLibrary.name()));
         listResult.addAll(this.loadChildrenByType(projectId, parentId, ResourceType.ActionLibrary.name()));
      } else if (type.contentEquals(ResourceType.DecisionTable.name())) {
         listResult.addAll(this.loadChildrenByType(projectId, parentId, ResourceType.DecisionTable.name()));
         listResult.addAll(this.loadChildrenByType(projectId, parentId, ResourceType.CrossDecisionTable.name()));
      } else if (type.contentEquals(ResourceType.Scorecard.name())) {
         listResult.addAll(this.loadChildrenByType(projectId, parentId, ResourceType.Scorecard.name()));
         listResult.addAll(this.loadChildrenByType(projectId, parentId, ResourceType.ComplexScorecard.name()));
      } else {
         listResult.addAll(this.loadChildrenByType(projectId, parentId, type));
      }

      return listResult;
   }

   private List loadChildren(long longValue, long longValue2) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList items;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select ID_,NAME_, TYPE_, CREATE_USER_, LOCKED_USER_, UPDATE_DATE_,DIGEST_ from URULE_FILE where PACKAGE_ID_=? and PROJECT_ID_=? and DELETED_=? order by NAME_ asc");
         preparedStatement.setLong(1, longValue2);
         preparedStatement.setLong(2, longValue);
         preparedStatement.setBoolean(3, false);
         ArrayList items2 = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            RuleFile ruleFile = new RuleFile();
            ruleFile.setDirectory(false);
            ruleFile.setId(resultSet.getLong(1));
            ruleFile.setName(resultSet.getString(2));
            ruleFile.setType(resultSet.getString(3));
            ruleFile.setCreateUser(resultSet.getString(4));
            ruleFile.setLockedUser(resultSet.getString(5));
            ruleFile.setModifyDate(resultSet.getTimestamp(6));
            ruleFile.setDigest(resultSet.getString(7));
            ruleFile.setParentId(longValue2);
            ruleFile.setProjectId(longValue);
            items2.add(ruleFile);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         items = items2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return items;
   }

   private List loadChildrenByType(long longValue, long longValue2, String text) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList items;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select ID_,NAME_, TYPE_, CREATE_USER_, LOCKED_USER_, UPDATE_DATE_,DIGEST_ from URULE_FILE where PACKAGE_ID_=? and TYPE_=? and PROJECT_ID_=? and DELETED_=? order by NAME_ asc");
         preparedStatement.setLong(1, longValue2);
         preparedStatement.setString(2, text);
         preparedStatement.setLong(3, longValue);
         preparedStatement.setBoolean(4, false);
         ArrayList items2 = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            RuleFile ruleFile = new RuleFile();
            ruleFile.setDirectory(false);
            ruleFile.setId(resultSet.getLong(1));
            ruleFile.setName(resultSet.getString(2));
            ruleFile.setType(resultSet.getString(3));
            ruleFile.setCreateUser(resultSet.getString(4));
            ruleFile.setLockedUser(resultSet.getString(5));
            ruleFile.setModifyDate(resultSet.getTimestamp(6));
            ruleFile.setDigest(resultSet.getString(7));
            ruleFile.setParentId(longValue2);
            ruleFile.setProjectId(longValue);
            items2.add(ruleFile);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         items = items2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return items;
   }
   public void changeParent(long id, long newPackageId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_FILE set PACKAGE_ID_=? where ID_=?");
         preparedStatement.setLong(1, newPackageId);
         preparedStatement.setLong(2, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public boolean checkExist(long projectId, long parentId, String type, String name) {
      Connection connection = JdbcUtils.getConnection();

      boolean checkExistResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from URULE_FILE where PROJECT_ID_=? and NAME_=? and PACKAGE_ID_=? and TYPE_=? and DELETED_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.setString(2, name);
         preparedStatement.setLong(3, parentId);
         preparedStatement.setString(4, type);
         preparedStatement.setBoolean(5, false);
         ResultSet resultSet = preparedStatement.executeQuery();
         resultSet.next();
         int number = resultSet.getInt(1);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         checkExistResult = number > 0;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return checkExistResult;
   }
   public void rename(long id, String account, String newName) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_FILE set NAME_=?, UPDATE_USER_=?, UPDATE_DATE_=?  where ID_=?");
         preparedStatement.setString(1, newName);
         preparedStatement.setString(2, account);
         preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
         preparedStatement.setLong(4, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void remove(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_FILE where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void lock(long id, String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_FILE set LOCKED_USER_=?, UPDATE_USER_=?, UPDATE_DATE_=?  where ID_=?");
         preparedStatement.setString(1, account);
         preparedStatement.setString(2, account);
         preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
         preparedStatement.setLong(4, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void unlock(long id, String version, String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_FILE set LOCKED_USER_=null, LATEST_VERSION_=?, UPDATE_USER_=?, UPDATE_DATE_=?  where ID_=?");
         preparedStatement.setString(1, version);
         preparedStatement.setString(2, account);
         preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
         preparedStatement.setLong(4, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void updateDeleteFlag(long id, boolean deleted, String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         RuleFile ruleFile = this.get(id);
         if (ruleFile != null) {
            PreparedStatement preparedStatement = connection.prepareStatement("update URULE_FILE set DELETED_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
            preparedStatement.setBoolean(1, deleted);
            preparedStatement.setString(2, account);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setLong(4, id);
            preparedStatement.executeUpdate();
            JdbcUtils.closeStatement(preparedStatement);
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_FILE where PROJECT_ID_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public FileCountQuery newCountQuery() {
      return new FileCountQueryImpl();
   }
   public void update(RuleFile file) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_FILE set NAME_=?, TYPE_=?, PACKAGE_ID_=?, CONTENT_=?, CREATE_USER_=?, CREATE_DATE_=?,UPDATE_USER_=?, UPDATE_DATE_=?,PROJECT_ID_=?,LATEST_VERSION_=?,DELETED_=? where ID_=?");
         preparedStatement.setString(1, file.getName());
         preparedStatement.setString(2, file.getType());
         preparedStatement.setLong(3, file.getParentId());
         preparedStatement.setString(4, file.getContent());
         preparedStatement.setString(5, file.getCreateUser());
         preparedStatement.setTimestamp(6, new Timestamp(file.getCreateDate().getTime()));
         preparedStatement.setString(7, file.getCreateUser());
         preparedStatement.setTimestamp(8, new Timestamp(file.getModifyDate().getTime()));
         preparedStatement.setLong(9, file.getProjectId());
         preparedStatement.setString(10, file.getLatestVersion());
         preparedStatement.setBoolean(11, file.isDeleted());
         preparedStatement.setLong(12, file.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
}
