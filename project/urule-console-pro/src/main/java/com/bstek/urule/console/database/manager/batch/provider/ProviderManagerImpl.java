package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class ProviderManagerImpl implements ProviderManager {
   public BatchDataProvider get(Long id) {
      List items = this.createQuery().id(id).list();
      return items.size() > 0 ? (BatchDataProvider)items.get(0) : null;
   }

   public void add(BatchDataProvider provider) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_BATCH_DATA_PROVIDER (NAME_, DATASOURCE_ID_, INPUT_DATA_, PACKET_VAR_NAME_, SUPPORT_PAGING_, PAGE_SIZE_, PAGE_SQL_, ORDER_FIELD_, ORDER_FIELD_PARAM_NAME_, PAGE_LIMIT_TYPE_, COUNT_SQL_, DESC_, CREATE_USER_, CREATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setString(1, provider.getName());
         preparedStatement.setLong(2, provider.getDatasourceId());
         preparedStatement.setString(3, provider.getInputData());
         preparedStatement.setString(4, provider.getPacketVarName());
         preparedStatement.setBoolean(5, provider.isSupportsPaging());
         preparedStatement.setInt(6, provider.getPageSize());
         preparedStatement.setString(7, provider.getPageSql());
         preparedStatement.setString(8, provider.getOrderField());
         preparedStatement.setString(9, provider.getOrderFieldParamName());
         preparedStatement.setString(10, provider.getPageLimitType());
         preparedStatement.setString(11, provider.getCountSql());
         preparedStatement.setString(12, provider.getDesc());
         preparedStatement.setString(13, provider.getCreateUser());
         preparedStatement.setTimestamp(14, new Timestamp(provider.getCreateDate().getTime()));
         preparedStatement.setLong(15, provider.getId());
         preparedStatement.setLong(16, provider.getProjectId());
         preparedStatement.setLong(17, provider.getBatchId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(BatchDataProvider provider) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_BATCH_DATA_PROVIDER set NAME_=?, DATASOURCE_ID_=?, INPUT_DATA_=?, PACKET_VAR_NAME_=?, SUPPORT_PAGING_=?, PAGE_SIZE_=?, PAGE_SQL_=?, ORDER_FIELD_=?, ORDER_FIELD_PARAM_NAME_=?, PAGE_LIMIT_TYPE_=?, COUNT_SQL_=?, DESC_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         preparedStatement.setString(1, provider.getName());
         preparedStatement.setLong(2, provider.getDatasourceId());
         preparedStatement.setString(3, provider.getInputData());
         preparedStatement.setString(4, provider.getPacketVarName());
         preparedStatement.setBoolean(5, provider.isSupportsPaging());
         preparedStatement.setInt(6, provider.getPageSize());
         preparedStatement.setString(7, provider.getPageSql());
         preparedStatement.setString(8, provider.getOrderField());
         preparedStatement.setString(9, provider.getOrderFieldParamName());
         preparedStatement.setString(10, provider.getPageLimitType());
         preparedStatement.setString(11, provider.getCountSql());
         preparedStatement.setString(12, provider.getDesc());
         preparedStatement.setTimestamp(13, new Timestamp(provider.getUpdateDate().getTime()));
         preparedStatement.setString(14, provider.getUpdateUser());
         preparedStatement.setLong(15, provider.getId());
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_DATA_PROVIDER where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void removeByBatchId(Long batchId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_DATA_PROVIDER where BATCH_ID_=?");
         preparedStatement.setLong(1, batchId);
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

   public ProviderQuery createQuery() {
      return new ProviderQueryImpl();
   }

   public void removeByProjectId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_DATA_PROVIDER where PROJECT_ID_=?");
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH_DATA_PROVIDER where GROUP_ID_=?");
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
