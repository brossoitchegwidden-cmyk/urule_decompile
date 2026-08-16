package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class ResolverManagerImpl implements ResolverManager {
   public BatchDataResolver get(Long var1) {
      List var2 = this.createQuery().id(var1).list();
      return var2.size() > 0 ? (BatchDataResolver)var2.get(0) : null;
   }

   public void add(BatchDataResolver var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_BATCH_DATA_RESOLVER (NAME_, TRAN_SCOPE_, DESC_, CREATE_DATE_ , CREATE_USER_, ID_, BATCH_ID_, PROJECT_ID_, DATASOURCE_ID_) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getTranScope().name());
         var3.setString(3, var1.getDesc());
         var3.setTimestamp(4, new Timestamp(var1.getCreateDate().getTime()));
         var3.setString(5, var1.getCreateUser());
         var3.setLong(6, var1.getId());
         var3.setLong(7, var1.getBatchId());
         var3.setLong(8, var1.getProjectId());
         var3.setLong(9, var1.getDatasourceId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(BatchDataResolver var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("update URULE_BATCH_DATA_RESOLVER set NAME_=?, TRAN_SCOPE_=?, DESC_=?, UPDATE_DATE_=? , UPDATE_USER_=?, DATASOURCE_ID_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getTranScope().name());
         var3.setString(3, var1.getDesc());
         var3.setTimestamp(4, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(5, var1.getUpdateUser());
         var3.setLong(6, var1.getDatasourceId());
         var3.setLong(7, var1.getId());
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_DATA_RESOLVER where ID_=?");
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_DATA_RESOLVER where BATCH_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public List getResolverItemss(Long var1) {
      return null;
   }

   public ResolverQuery createQuery() {
      return new ResolverQueryImpl();
   }

   public void removeByProjectId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_DATA_RESOLVER where PROJECT_ID_=?");
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH_DATA_RESOLVER where GROUP_ID_=?");
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
