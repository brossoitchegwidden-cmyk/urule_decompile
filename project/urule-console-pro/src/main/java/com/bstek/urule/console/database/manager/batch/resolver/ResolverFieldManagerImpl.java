package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItemField;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class ResolverFieldManagerImpl implements ResolverFieldManager {
   public BatchDataResolverItemField get(Long id) {
      List items = this.createQuery().id(id).list();
      return items.size() > 0 ? (BatchDataResolverItemField)items.get(0) : null;
   }

   public void add(BatchDataResolverItemField field) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_BATCH_RESOLVER_FIELD (SRC_PROPERTY_, DATA_TYPE_, DEST_PROPERTY_, CREATE_USER_, CREATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_, RESOLVER_ID_, ITEM_ID_, KEY_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setString(1, field.getSrcProperty());
         preparedStatement.setString(2, field.getDataType());
         preparedStatement.setString(3, field.getDestProperty());
         preparedStatement.setString(4, field.getCreateUser());
         preparedStatement.setTimestamp(5, new Timestamp(field.getCreateDate().getTime()));
         preparedStatement.setLong(6, field.getId());
         preparedStatement.setLong(7, field.getProjectId());
         preparedStatement.setLong(8, field.getBatchId());
         preparedStatement.setLong(9, field.getResolverId());
         preparedStatement.setLong(10, field.getResolverItemId());
         preparedStatement.setBoolean(11, field.isKey());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(BatchDataResolverItemField field) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_BATCH_RESOLVER_FIELD set SRC_PROPERTY_=?, DATA_TYPE_=?, DEST_PROPERTY_=?, UPDATE_DATE_=? , UPDATE_USER_=?, KEY_=? where ID_=?");
         preparedStatement.setString(1, field.getSrcProperty());
         preparedStatement.setString(2, field.getDataType());
         preparedStatement.setString(3, field.getDestProperty());
         preparedStatement.setTimestamp(4, new Timestamp(field.getUpdateDate().getTime()));
         preparedStatement.setString(5, field.getUpdateUser());
         preparedStatement.setBoolean(6, field.isKey());
         preparedStatement.setLong(7, field.getId());
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_FIELD where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void removeByItemId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_FIELD where ITEM_ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void removeByResolverId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_FIELD where RESOLVER_ID_=?");
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_FIELD where BATCH_ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public ResolverFieldQuery createQuery() {
      return new ResolverFieldQueryImpl();
   }

   public void removeByProjectId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_FIELD where PROJECT_ID_=?");
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_FIELD where GROUP_ID_=?");
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
