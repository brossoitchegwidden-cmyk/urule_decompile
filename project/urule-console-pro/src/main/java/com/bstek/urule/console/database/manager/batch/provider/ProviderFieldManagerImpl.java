package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProviderField;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class ProviderFieldManagerImpl implements ProviderFieldManager {
   public BatchDataProviderField get(Long var1) {
      List var2 = this.createQuery().id(var1).list();
      return var2.size() > 0 ? (BatchDataProviderField)var2.get(0) : null;
   }

   public void add(BatchDataProviderField var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_BATCH_PROVIDER_FIELD (SRC_PROPERTY_, DATA_TYPE_, DEST_PROPERTY_, CLAZZ_PATH_, DATA_PROVIDER_ID_, CREATE_USER_, CREATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_, PROVIDER_ID_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var3.setString(1, var1.getSrcProperty());
         var3.setString(2, var1.getDataType());
         var3.setString(3, var1.getDestProperty());
         var3.setString(4, var1.getClassPath());
         if (var1.getDataProviderId() == null) {
            var3.setObject(5, (Object)null);
         } else {
            var3.setLong(5, var1.getDataProviderId());
         }

         var3.setString(6, var1.getCreateUser());
         var3.setTimestamp(7, new Timestamp(var1.getCreateDate().getTime()));
         var3.setLong(8, var1.getId());
         var3.setLong(9, var1.getProjectId());
         var3.setLong(10, var1.getBatchId());
         var3.setLong(11, var1.getProviderId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(BatchDataProviderField var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("update URULE_BATCH_PROVIDER_FIELD set SRC_PROPERTY_=?, DATA_TYPE_=?, DEST_PROPERTY_=?, CLAZZ_PATH_=?, DATA_PROVIDER_ID_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         var3.setString(1, var1.getSrcProperty());
         var3.setString(2, var1.getDataType());
         var3.setString(3, var1.getDestProperty());
         var3.setString(4, var1.getClassPath());
         if (var1.getDataProviderId() == null) {
            var3.setObject(5, (Object)null);
         } else {
            var3.setLong(5, var1.getDataProviderId());
         }

         var3.setTimestamp(6, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(7, var1.getUpdateUser());
         var3.setLong(8, var1.getId());
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByProviderId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where PROVIDER_ID_=?");
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where BATCH_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public ProviderFieldQuery createQuery() {
      return new ProviderFieldQueryImpl();
   }

   public void removeByProjectId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where PROJECT_ID_=?");
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_PROVIDER_FIELD where GROUP_ID_=?");
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
