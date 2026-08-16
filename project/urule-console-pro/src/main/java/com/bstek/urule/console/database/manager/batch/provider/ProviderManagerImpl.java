package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class ProviderManagerImpl implements ProviderManager {
   public BatchDataProvider get(Long var1) {
      List var2 = this.createQuery().id(var1).list();
      return var2.size() > 0 ? (BatchDataProvider)var2.get(0) : null;
   }

   public void add(BatchDataProvider var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_BATCH_DATA_PROVIDER (NAME_, DATASOURCE_ID_, INPUT_DATA_, PACKET_VAR_NAME_, SUPPORT_PAGING_, PAGE_SIZE_, PAGE_SQL_, ORDER_FIELD_, ORDER_FIELD_PARAM_NAME_, PAGE_LIMIT_TYPE_, COUNT_SQL_, DESC_, CREATE_USER_, CREATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var3.setString(1, var1.getName());
         var3.setLong(2, var1.getDatasourceId());
         var3.setString(3, var1.getInputData());
         var3.setString(4, var1.getPacketVarName());
         var3.setBoolean(5, var1.isSupportsPaging());
         var3.setInt(6, var1.getPageSize());
         var3.setString(7, var1.getPageSql());
         var3.setString(8, var1.getOrderField());
         var3.setString(9, var1.getOrderFieldParamName());
         var3.setString(10, var1.getPageLimitType());
         var3.setString(11, var1.getCountSql());
         var3.setString(12, var1.getDesc());
         var3.setString(13, var1.getCreateUser());
         var3.setTimestamp(14, new Timestamp(var1.getCreateDate().getTime()));
         var3.setLong(15, var1.getId());
         var3.setLong(16, var1.getProjectId());
         var3.setLong(17, var1.getBatchId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(BatchDataProvider var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("update URULE_BATCH_DATA_PROVIDER set NAME_=?, DATASOURCE_ID_=?, INPUT_DATA_=?, PACKET_VAR_NAME_=?, SUPPORT_PAGING_=?, PAGE_SIZE_=?, PAGE_SQL_=?, ORDER_FIELD_=?, ORDER_FIELD_PARAM_NAME_=?, PAGE_LIMIT_TYPE_=?, COUNT_SQL_=?, DESC_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setLong(2, var1.getDatasourceId());
         var3.setString(3, var1.getInputData());
         var3.setString(4, var1.getPacketVarName());
         var3.setBoolean(5, var1.isSupportsPaging());
         var3.setInt(6, var1.getPageSize());
         var3.setString(7, var1.getPageSql());
         var3.setString(8, var1.getOrderField());
         var3.setString(9, var1.getOrderFieldParamName());
         var3.setString(10, var1.getPageLimitType());
         var3.setString(11, var1.getCountSql());
         var3.setString(12, var1.getDesc());
         var3.setTimestamp(13, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(14, var1.getUpdateUser());
         var3.setLong(15, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void remove(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_DATA_PROVIDER where ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByBatchId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_DATA_PROVIDER where BATCH_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public List getMappings(Long var1) {
      return null;
   }

   public ProviderQuery createQuery() {
      return new ProviderQueryImpl();
   }

   public void removeByProjectId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_DATA_PROVIDER where PROJECT_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByGroupId(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_DATA_PROVIDER where GROUP_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }
}
