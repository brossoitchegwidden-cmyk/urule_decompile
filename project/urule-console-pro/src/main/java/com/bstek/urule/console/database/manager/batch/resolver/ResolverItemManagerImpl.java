package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class ResolverItemManagerImpl implements ResolverItemManager {
   public BatchDataResolverItem get(Long id) {
      List items = this.createQuery().id(id).list();
      return items.size() > 0 ? (BatchDataResolverItem)items.get(0) : null;
   }

   public void add(BatchDataResolverItem item) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_BATCH_RESOLVER_ITEM (NAME_, UPDATE_MODE_, TABLE_NAME_, VALIDATOR_DATA_, PARTITION_NAME_, PARTITION_VALUE_, COMMIT_LIMIT_, DESC_, CREATE_USER_, CREATE_DATE_, ID_, BATCH_ID_, PROJECT_ID_, RESOLVER_ID_)  values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setString(1, item.getName());
         preparedStatement.setString(2, item.getUpdateMode().name());
         preparedStatement.setString(3, item.getTableName());
         preparedStatement.setString(4, item.getFilterData());
         preparedStatement.setString(5, item.getPartitionName());
         preparedStatement.setString(6, item.getPartitionValue());
         preparedStatement.setInt(7, item.getCommitLimit());
         preparedStatement.setString(8, item.getDesc());
         preparedStatement.setString(9, item.getCreateUser());
         preparedStatement.setTimestamp(10, new Timestamp(item.getCreateDate().getTime()));
         preparedStatement.setLong(11, item.getId());
         preparedStatement.setLong(12, item.getBatchId());
         preparedStatement.setLong(13, item.getProjectId());
         preparedStatement.setLong(14, item.getResolverId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(BatchDataResolverItem item) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_BATCH_RESOLVER_ITEM set NAME_=?, UPDATE_MODE_=?, TABLE_NAME_=?, VALIDATOR_DATA_=?, PARTITION_NAME_=?, PARTITION_VALUE_=?, COMMIT_LIMIT_=?, DESC_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         preparedStatement.setString(1, item.getName());
         preparedStatement.setString(2, item.getUpdateMode().name());
         preparedStatement.setString(3, item.getTableName());
         preparedStatement.setString(4, item.getFilterData());
         preparedStatement.setString(5, item.getPartitionName());
         preparedStatement.setString(6, item.getPartitionValue());
         preparedStatement.setInt(7, item.getCommitLimit());
         preparedStatement.setString(8, item.getDesc());
         preparedStatement.setTimestamp(9, new Timestamp(item.getUpdateDate().getTime()));
         preparedStatement.setString(10, item.getUpdateUser());
         preparedStatement.setLong(11, item.getId());
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where ID_=?");
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where RESOLVER_ID_=?");
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where BATCH_ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public List getMappings(Long id) {
      return null;
   }

   public ResolverItemQuery createQuery() {
      return new ResolverItemQueryImpl();
   }

   public void removeByProjectId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where PROJECT_ID_=?");
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where GROUP_ID_=?");
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
