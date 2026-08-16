package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.model.batch.BatchSkipLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchSkipLogQueryImpl implements BatchSkipLogQuery {
   private Long a;
   private List b = new ArrayList();

   public BatchSkipLogQuery batchLogId(Long var1) {
      this.a = var1;
      return this;
   }

   private StringBuilder a() throws SQLException {
      this.b.clear();
      StringBuilder var1 = new StringBuilder();
      if (StringUtils.isNotBlank(this.a)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" LOG_ID_ = ?");
         this.b.add(this.a);
      }

      return var1;
   }

   public List list() {
      String var1 = "select ID_, LOG_ID_, BATCH_ID_, TYPE_, MSG_, CREATE_DATE_ from URULE_LOG_BATCH_SKIP";
      Connection var2 = JdbcUtils.getConnection();

      List var7;
      try {
         StringBuilder var3 = this.a();
         if (var3.length() > 0) {
            var1 = var1 + " where" + var3.toString();
         }

         var1 = var1 + " order by CREATE_DATE_ desc";
         PreparedStatement var4 = var2.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.b, var4);
         ResultSet var5 = var4.executeQuery();
         List var6 = this.a(var5);
         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var7 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var7;
   }

   private List a(ResultSet var1) throws Exception {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         BatchSkipLog var3 = new BatchSkipLog();
         var3.setId(var1.getLong(1));
         var3.setLogId(var1.getLong(2));
         var3.setBatchId(var1.getLong(3));
         var3.setType(var1.getString(4));
         var3.setMsg(var1.getString(5));
         var3.setCreateDate(var1.getTimestamp(6));
         var2.add(var3);
      }

      return var2;
   }

   public BatchSkipLog details(Long var1) {
      String var2 = "select MSG_, DETAIL_, DATA_ from URULE_LOG_BATCH_SKIP WHERE ID_=?";
      Connection var3 = JdbcUtils.getConnection();

      BatchSkipLog var7;
      try {
         PreparedStatement var4 = var3.prepareStatement(var2);
         var4.setLong(1, var1);
         ResultSet var5 = var4.executeQuery();
         BatchSkipLog var6 = new BatchSkipLog();
         if (var5.next()) {
            var6.setMsg(var5.getString(1));
            var6.setDetail(var5.getString(2));
            var6.setData(var5.getString(3));
         }

         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var7 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var7;
   }
}
