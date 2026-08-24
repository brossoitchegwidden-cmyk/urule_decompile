package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class ResolverManagerImpl implements ResolverManager {
   public BatchDataResolver get(Long id) {
      List items = this.createQuery().id(id).list();
      return items.size() > 0 ? (BatchDataResolver)items.get(0) : null;
   }

   public void add(BatchDataResolver resolver) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_BATCH_DATA_RESOLVER (NAME_, TRAN_SCOPE_, DESC_, CREATE_DATE_ , CREATE_USER_, ID_, BATCH_ID_, PROJECT_ID_, DATASOURCE_ID_) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setString(1, resolver.getName());
         preparedStatement.setString(2, resolver.getTranScope().name());
         preparedStatement.setString(3, resolver.getDesc());
         preparedStatement.setTimestamp(4, new Timestamp(resolver.getCreateDate().getTime()));
         preparedStatement.setString(5, resolver.getCreateUser());
         preparedStatement.setLong(6, resolver.getId());
         preparedStatement.setLong(7, resolver.getBatchId());
         preparedStatement.setLong(8, resolver.getProjectId());
         preparedStatement.setLong(9, resolver.getDatasourceId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(BatchDataResolver resolver) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_BATCH_DATA_RESOLVER set NAME_=?, TRAN_SCOPE_=?, DESC_=?, UPDATE_DATE_=? , UPDATE_USER_=?, DATASOURCE_ID_=? where ID_=?");
         preparedStatement.setString(1, resolver.getName());
         preparedStatement.setString(2, resolver.getTranScope().name());
         preparedStatement.setString(3, resolver.getDesc());
         preparedStatement.setTimestamp(4, new Timestamp(resolver.getUpdateDate().getTime()));
         preparedStatement.setString(5, resolver.getUpdateUser());
         preparedStatement.setLong(6, resolver.getDatasourceId());
         preparedStatement.setLong(7, resolver.getId());
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_DATA_RESOLVER where ID_=?");
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_DATA_RESOLVER where BATCH_ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public List getResolverItemss(Long id) {
      return null;
   }

   public ResolverQuery createQuery() {
      return new ResolverQueryImpl();
   }

   public void removeByProjectId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_DATA_RESOLVER where PROJECT_ID_=?");
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_DATA_RESOLVER where GROUP_ID_=?");
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
