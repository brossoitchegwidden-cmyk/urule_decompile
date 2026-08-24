package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DirectoryManagerImpl implements DirectoryManager {
   public void add(RuleFile ruleFile) {
      Connection connection = JdbcUtils.getConnection();

      try {
         ruleFile.setId(IDGenerator.getInstance().nextId(IDType.FILE));
         ruleFile.setModifyDate(new Date());
         ruleFile.setCreateDate(new Date());
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_PACKAGE (ID_, NAME_, TYPE_, PARENT_ID_, PROJECT_ID_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, DELETED_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setLong(1, ruleFile.getId());
         preparedStatement.setString(2, ruleFile.getName());
         preparedStatement.setString(3, ruleFile.getType());
         preparedStatement.setLong(4, ruleFile.getParentId());
         preparedStatement.setLong(5, ruleFile.getProjectId());
         preparedStatement.setString(6, ruleFile.getCreateUser());
         preparedStatement.setTimestamp(7, new Timestamp(ruleFile.getCreateDate().getTime()));
         preparedStatement.setString(8, ruleFile.getCreateUser());
         preparedStatement.setTimestamp(9, new Timestamp(ruleFile.getModifyDate().getTime()));
         preparedStatement.setBoolean(10, ruleFile.isDeleted());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public RuleFile get(long id) {
      Connection connection = JdbcUtils.getConnection();

      RuleFile ruleFile;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select NAME_, PARENT_ID_, TYPE_, UPDATE_USER_, UPDATE_DATE_, DELETED_, PROJECT_ID_ from URULE_PACKAGE where ID_=?");
         preparedStatement.setLong(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();
         RuleFile ruleFile2 = null;
         if (resultSet.next()) {
            ruleFile2 = new RuleFile();
            ruleFile2.setId(id);
            ruleFile2.setName(resultSet.getString(1));
            ruleFile2.setParentId(resultSet.getLong(2));
            ruleFile2.setType(resultSet.getString(3));
            ruleFile2.setUpdateUser(resultSet.getString(4));
            ruleFile2.setModifyDate(resultSet.getTimestamp(5));
            ruleFile2.setDeleted(resultSet.getBoolean(6));
            ruleFile2.setProjectId(resultSet.getLong(7));
            ruleFile2.setDirectory(true);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         ruleFile = ruleFile2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return ruleFile;
   }
   public void remove(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_PACKAGE where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   /**专门用在项目导入在使用，不在接口中声明*/
   public RuleFile loadDir(long projectId, long parentId, String dirName, String type) {
      Connection connection = JdbcUtils.getConnection();

      RuleFile ruleFile;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select NAME_, PARENT_ID_, TYPE_, UPDATE_USER_, UPDATE_DATE_, DELETED_,ID_ from URULE_PACKAGE where PARENT_ID_=? and PROJECT_ID_=? and TYPE_=? and NAME_=?");
         preparedStatement.setLong(1, parentId);
         preparedStatement.setLong(2, projectId);
         preparedStatement.setString(3, type);
         preparedStatement.setString(4, dirName);
         ResultSet resultSet = preparedStatement.executeQuery();
         RuleFile ruleFile2 = null;
         if (resultSet.next()) {
            ruleFile2 = new RuleFile();
            ruleFile2.setName(resultSet.getString(1));
            ruleFile2.setParentId(resultSet.getLong(2));
            ruleFile2.setType(resultSet.getString(3));
            ruleFile2.setUpdateUser(resultSet.getString(4));
            ruleFile2.setModifyDate(resultSet.getTimestamp(5));
            ruleFile2.setDeleted(resultSet.getBoolean(6));
            ruleFile2.setId(resultSet.getLong(7));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         ruleFile = ruleFile2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return ruleFile;
   }
   public void updateDeleteFlag(long id, boolean deleted, String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_PACKAGE set DELETED_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         if (deleted) {
            preparedStatement.setBoolean(1, deleted);
            preparedStatement.setString(2, account);
            preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            preparedStatement.setLong(4, id);
            preparedStatement.executeUpdate();
         } else {
            this.processPreparedStatement(preparedStatement, id, account);
         }

         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   private void processPreparedStatement(PreparedStatement preparedStatement, long longValue, String text) {
      RuleFile ruleFile = this.get(longValue);
      if (ruleFile != null && ruleFile.isDeleted() && ruleFile.getParentId() > 0L) {
         this.processPreparedStatement(preparedStatement, ruleFile.getParentId(), text);
      }

      try {
         preparedStatement.setBoolean(1, false);
         preparedStatement.setString(2, text);
         preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
         preparedStatement.setLong(4, longValue);
         preparedStatement.executeUpdate();
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
   public void changeName(long id, String newName, String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_PACKAGE set NAME_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
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
   public boolean checkExist(long projectId, long parentId, String type, String name) {
      Connection connection = JdbcUtils.getConnection();

      boolean checkExistResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from URULE_PACKAGE where PROJECT_ID_=? and NAME_=? and PARENT_ID_=? and TYPE_=? and DELETED_=?");
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
   public List list(long projectId, long parentId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList listResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select ID_, NAME_, TYPE_ from URULE_PACKAGE where PARENT_ID_=? and PROJECT_ID_=? and DELETED_=? order by NAME_ asc");
         ArrayList items = new ArrayList();
         preparedStatement.setLong(1, parentId);
         preparedStatement.setLong(2, projectId);
         preparedStatement.setBoolean(3, false);
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            RuleFile ruleFile = new RuleFile();
            ruleFile.setDirectory(true);
            ruleFile.setId(resultSet.getLong(1));
            ruleFile.setParentId(parentId);
            ruleFile.setName(resultSet.getString(2));
            ruleFile.setType(resultSet.getString(3));
            ruleFile.setProjectId(projectId);
            items.add(ruleFile);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         listResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listResult;
   }
   public List list(long projectId, long parentId, String type) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList listResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select ID_, NAME_, TYPE_ from URULE_PACKAGE where PARENT_ID_=? and PROJECT_ID_=? and TYPE_=? and DELETED_=? order by NAME_ asc");
         ArrayList items = new ArrayList();
         preparedStatement.setLong(1, parentId);
         preparedStatement.setLong(2, projectId);
         preparedStatement.setString(3, type);
         preparedStatement.setBoolean(4, false);
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            RuleFile ruleFile = new RuleFile();
            ruleFile.setDirectory(true);
            ruleFile.setId(resultSet.getLong(1));
            ruleFile.setParentId(parentId);
            ruleFile.setName(resultSet.getString(2));
            ruleFile.setType(resultSet.getString(3));
            ruleFile.setProjectId(projectId);
            items.add(ruleFile);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         listResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listResult;
   }
   public void changeParent(long id, long newParentId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_PACKAGE set PARENT_ID_=? where ID_=?");
         preparedStatement.setLong(1, newParentId);
         preparedStatement.setLong(2, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_PACKAGE where PROJECT_ID_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void changeGeneral(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_PACKAGE set TYPE_=? where PROJECT_ID_=?");
         preparedStatement.setString(1, ResourceType.General.name());
         preparedStatement.setLong(2, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void changeType(long id, String type) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_PACKAGE set TYPE_=? where ID_=?");
         preparedStatement.setString(1, type);
         preparedStatement.setLong(2, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public long countByType(long projectId, String type) {
      long longValue = 0L;
      Connection connection = JdbcUtils.getConnection();

      long countByTypeResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select count(ID_) as RC_ from URULE_PACKAGE where PROJECT_ID_=? and TYPE_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.setString(2, type);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            longValue = resultSet.getLong(1);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         countByTypeResult = longValue;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countByTypeResult;
   }

   public boolean hasTypeFolder(long projectId) {
      boolean flag = false;
      Connection connection = JdbcUtils.getConnection();

      boolean hasTypeFolderResult;
      try {
         long longValue = 0L;
         PreparedStatement preparedStatement = connection.prepareStatement("select count(ID_) as RC_ from URULE_PACKAGE where PROJECT_ID_=? and TYPE_<>?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.setString(2, ResourceType.General.name());
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            longValue = resultSet.getLong(1);
         }

         flag = longValue > 0L;
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         hasTypeFolderResult = flag;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return hasTypeFolderResult;
   }
}
