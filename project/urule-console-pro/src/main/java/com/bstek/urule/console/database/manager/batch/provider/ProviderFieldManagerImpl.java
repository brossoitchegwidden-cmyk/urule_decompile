package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProviderField;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class ProviderFieldManagerImpl implements ProviderFieldManager {
   public BatchDataProviderField get(Long id) {
      List items = this.createQuery().id(id).list();
      return items.size() > 0 ? (BatchDataProviderField)items.get(0) : null;
   }

   public void add(BatchDataProviderField field) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_BATCH_PROVIDER_FIELD (SRC_PROPERTY_, DATA_TYPE_, DEST_PROPERTY_, CLAZZ_PATH_, DATA_PROVIDER_ID_, CREATE_USER_, CREATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_, PROVIDER_ID_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setString(1, field.getSrcProperty());
         preparedStatement.setString(2, field.getDataType());
         preparedStatement.setString(3, field.getDestProperty());
         preparedStatement.setString(4, field.getClassPath());
         if (field.getDataProviderId() == null) {
            preparedStatement.setObject(5, (Object)null);
         } else {
            preparedStatement.setLong(5, field.getDataProviderId());
         }

         preparedStatement.setString(6, field.getCreateUser());
         preparedStatement.setTimestamp(7, new Timestamp(field.getCreateDate().getTime()));
         preparedStatement.setLong(8, field.getId());
         preparedStatement.setLong(9, field.getProjectId());
         preparedStatement.setLong(10, field.getBatchId());
         preparedStatement.setLong(11, field.getProviderId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(BatchDataProviderField field) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_BATCH_PROVIDER_FIELD set SRC_PROPERTY_=?, DATA_TYPE_=?, DEST_PROPERTY_=?, CLAZZ_PATH_=?, DATA_PROVIDER_ID_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         preparedStatement.setString(1, field.getSrcProperty());
         preparedStatement.setString(2, field.getDataType());
         preparedStatement.setString(3, field.getDestProperty());
         preparedStatement.setString(4, field.getClassPath());
         if (field.getDataProviderId() == null) {
            preparedStatement.setObject(5, (Object)null);
         } else {
            preparedStatement.setLong(5, field.getDataProviderId());
         }

         preparedStatement.setTimestamp(6, new Timestamp(field.getUpdateDate().getTime()));
         preparedStatement.setString(7, field.getUpdateUser());
         preparedStatement.setLong(8, field.getId());
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void removeByProviderId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where PROVIDER_ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void removeByBatchId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where BATCH_ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public ProviderFieldQuery createQuery() {
      return new ProviderFieldQueryImpl();
   }

   public void removeByProjectId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where PROJECT_ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void removeByGroupId(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where GROUP_ID_=?");
         preparedStatement.setString(1, groupId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
}
