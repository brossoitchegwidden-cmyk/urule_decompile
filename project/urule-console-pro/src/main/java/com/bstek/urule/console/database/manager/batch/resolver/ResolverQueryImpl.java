package com.bstek.urule.console.database.manager.batch.resolver;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.TranScope;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResolverQueryImpl implements ResolverQuery {
   private Long a;
   private Long b;
   private List c = new ArrayList();

   public ResolverQuery id(Long var1) {
      this.a = var1;
      return this;
   }

   public ResolverQuery batchId(Long var1) {
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

         var1.append(" BATCH_ID_=?");
         this.c.add(this.b);
      }

      return var1;
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();

      ArrayList var13;
      try {
         String var2 = "SELECT NAME_, TRAN_SCOPE_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, BATCH_ID_, PROJECT_ID_, DATASOURCE_ID_ FROM URULE_BATCH_DATA_RESOLVER ";
         StringBuilder var3 = this.a();
         if (var3.length() > 0) {
            var2 = var2 + " where" + var3.toString();
         }

         PreparedStatement var4 = var1.prepareStatement(var2);
         JdbcUtils.fillPreparedStatementParameters(this.c, var4);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            BatchDataResolver var7 = new BatchDataResolver();
            var7.setName(var6.getString(1));
            var7.setTranScope(TranScope.valueOf(var6.getString(2)));
            var7.setDesc(var6.getString(3));
            var7.setCreateUser(var6.getString(4));
            var7.setCreateDate(var6.getTimestamp(5));
            var7.setUpdateUser(var6.getString(6));
            var7.setUpdateDate(var6.getTimestamp(7));
            var7.setId(var6.getLong(8));
            var7.setBatchId(var6.getLong(9));
            var7.setProjectId(var6.getLong(10));
            var7.setDatasourceId(var6.getLong(11));
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
