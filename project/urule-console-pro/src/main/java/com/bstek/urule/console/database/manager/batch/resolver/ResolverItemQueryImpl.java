package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.BatchUpdateMode;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResolverItemQueryImpl implements ResolverItemQuery {
   private Long a;
   private Long b;
   private List c = new ArrayList();

   public ResolverItemQuery id(Long var1) {
      this.a = var1;
      return this;
   }

   public ResolverItemQuery resolverId(Long var1) {
      this.b = var1;
      return this;
   }

   private StringBuilder a() {
      this.c.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.c.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" RESOLVER_ID_=?");
         this.c.add(this.b);
      }

      return var1;
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();

      ArrayList var13;
      try {
         String var2 = "SELECT NAME_, UPDATE_MODE_, TABLE_NAME_, VALIDATOR_DATA_, PARTITION_NAME_, PARTITION_VALUE_, COMMIT_LIMIT_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, BATCH_ID_, PROJECT_ID_, RESOLVER_ID_ FROM URULE_BATCH_RESOLVER_ITEM ";
         StringBuilder var3 = this.a();
         if (var3.length() > 0) {
            var2 = var2 + " where" + var3.toString();
         }

         PreparedStatement var4 = var1.prepareStatement(var2);
         JdbcUtils.fillPreparedStatementParameters(this.c, var4);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            BatchDataResolverItem var7 = new BatchDataResolverItem();
            var7.setName(var6.getString(1));
            var7.setUpdateMode(BatchUpdateMode.valueOf(var6.getString(2)));
            var7.setTableName(var6.getString(3));
            var7.setFilterData(var6.getString(4));
            var7.setPartitionName(var6.getString(5));
            var7.setPartitionValue(var6.getString(6));
            var7.setCommitLimit(var6.getInt(7));
            var7.setDesc(var6.getString(8));
            var7.setCreateUser(var6.getString(9));
            var7.setCreateDate(var6.getTimestamp(10));
            var7.setUpdateUser(var6.getString(11));
            var7.setUpdateDate(var6.getTimestamp(12));
            var7.setId(var6.getLong(13));
            var7.setBatchId(var6.getLong(14));
            var7.setProjectId(var6.getLong(15));
            var7.setResolverId(var6.getLong(16));
            var5.add(var7);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var4);
         var13 = var5;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var13;
   }
}
