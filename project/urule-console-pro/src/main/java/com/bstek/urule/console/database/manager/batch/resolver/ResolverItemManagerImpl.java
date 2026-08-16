package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class ResolverItemManagerImpl implements ResolverItemManager {
   public BatchDataResolverItem get(Long var1) {
      List var2 = this.createQuery().id(var1).list();
      return var2.size() > 0 ? (BatchDataResolverItem)var2.get(0) : null;
   }

   public void add(BatchDataResolverItem var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_BATCH_RESOLVER_ITEM (NAME_, UPDATE_MODE_, TABLE_NAME_, VALIDATOR_DATA_, PARTITION_NAME_, PARTITION_VALUE_, COMMIT_LIMIT_, DESC_, CREATE_USER_, CREATE_DATE_, ID_, BATCH_ID_, PROJECT_ID_, RESOLVER_ID_)  values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getUpdateMode().name());
         var3.setString(3, var1.getTableName());
         var3.setString(4, var1.getFilterData());
         var3.setString(5, var1.getPartitionName());
         var3.setString(6, var1.getPartitionValue());
         var3.setInt(7, var1.getCommitLimit());
         var3.setString(8, var1.getDesc());
         var3.setString(9, var1.getCreateUser());
         var3.setTimestamp(10, new Timestamp(var1.getCreateDate().getTime()));
         var3.setLong(11, var1.getId());
         var3.setLong(12, var1.getBatchId());
         var3.setLong(13, var1.getProjectId());
         var3.setLong(14, var1.getResolverId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(BatchDataResolverItem var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("update URULE_BATCH_RESOLVER_ITEM set NAME_=?, UPDATE_MODE_=?, TABLE_NAME_=?, VALIDATOR_DATA_=?, PARTITION_NAME_=?, PARTITION_VALUE_=?, COMMIT_LIMIT_=?, DESC_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getUpdateMode().name());
         var3.setString(3, var1.getTableName());
         var3.setString(4, var1.getFilterData());
         var3.setString(5, var1.getPartitionName());
         var3.setString(6, var1.getPartitionValue());
         var3.setInt(7, var1.getCommitLimit());
         var3.setString(8, var1.getDesc());
         var3.setTimestamp(9, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(10, var1.getUpdateUser());
         var3.setLong(11, var1.getId());
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByResolverId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where RESOLVER_ID_=?");
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where BATCH_ID_=?");
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

   public ResolverItemQuery createQuery() {
      return new ResolverItemQueryImpl();
   }

   public void removeByProjectId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where PROJECT_ID_=?");
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_RESOLVER_ITEM where GROUP_ID_=?");
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
