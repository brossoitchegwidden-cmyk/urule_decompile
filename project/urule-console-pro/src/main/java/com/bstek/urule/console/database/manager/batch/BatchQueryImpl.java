package com.bstek.urule.console.database.manager.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchQueryImpl implements BatchQuery {
   private Long a;
   private String b;
   private String c;
   private String d;
   private Boolean e;
   private Boolean f;
   private BatchStatus g;
   private Long h;
   private Long i;
   private List j = new ArrayList();

   public BatchQuery id(Long var1) {
      this.a = var1;
      return this;
   }

   public BatchQuery nameLike(String var1) {
      this.b = var1;
      return this;
   }

   public BatchQuery descLike(String var1) {
      this.c = var1;
      return this;
   }

   public BatchQuery projectId(Long var1) {
      this.i = var1;
      return this;
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();

      List var4;
      try {
         String var2 = this.a();
         List var3 = this.a(var1, var2);
         var4 = var3;
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var4;
   }

   public void page(Page var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = this.a();
         String var4 = var3 + " ORDER BY NAME_";
         String var5 = JdbcUtils.getPageSql(var2, var4, var1.getStartRow(), var1.getPageSize());
         List var6 = this.a(var2, var5);
         var1.setData(var6);
         String var7 = JdbcUtils.getCountSql(var3);
         PreparedStatement var8 = var2.prepareStatement(var7);
         JdbcUtils.fillPreparedStatementParameters(this.j, var8);
         ResultSet var9 = var8.executeQuery();
         if (var9.next()) {
            var1.setTotalRows((long)var9.getInt(1));
         }

         JdbcUtils.closeResultSet(var9);
         JdbcUtils.closeStatement(var8);
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   private List a(Connection var1, String var2) throws Exception {
      PreparedStatement var3 = var1.prepareStatement(var2);
      JdbcUtils.fillPreparedStatementParameters(this.j, var3);
      ArrayList var4 = new ArrayList();
      ResultSet var5 = var3.executeQuery();

      while(var5.next()) {
         Batch var6 = this.a(var5);
         var4.add(var6);
      }

      JdbcUtils.closeResultSet(var5);
      JdbcUtils.closeStatement(var3);
      return var4;
   }

   private String a() {
      String var1 = "SELECT NAME_, ASYNC_, CALLBACK_URL_, STATUS_, LISTENER_, SKIP_LIMIT_, THREAD_MULTI_, THREAD_SIZE_, THREAD_DATA_SIZE_, PROVIDER_ID_, RESOLVER_ID_, PACKET_ID_, PACKET_INPUT_DATA_, REST_ENABLE_, REST_SECURITY_ENABLE_, REST_SECURITY_USER_, REST_SECURITY_PASSWORD_, INPUT_DATA_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, PROJECT_ID_, ENABLE_ FROM URULE_BATCH ";
      StringBuilder var2 = this.b();
      if (var2.length() > 0) {
         var1 = var1 + " where" + var2.toString();
      }

      return var1;
   }

   private Batch a(ResultSet var1) throws SQLException {
      Batch var2 = new Batch();
      var2.setName(var1.getString(1));
      var2.setAsync(var1.getBoolean(2));
      var2.setCallbackUrl(var1.getString(3));
      var2.setStatus(BatchStatus.valueOf(var1.getString(4)));
      var2.setListener(var1.getString(5));
      var2.setSkipLimit(var1.getInt(6));
      var2.setThreadMulti(var1.getBoolean(7));
      var2.setThreadSize(var1.getInt(8));
      var2.setThreadDataSize(var1.getInt(9));
      var2.setProviderId(var1.getLong(10));
      var2.setResolverId(var1.getLong(11));
      var2.setPacketId(var1.getLong(12));
      var2.setPacketInputData(var1.getString(13));
      var2.setRestEnable(var1.getBoolean(14));
      var2.setRestSecurityEnable(var1.getBoolean(15));
      var2.setRestSecurityUser(var1.getString(16));
      var2.setRestSecurityPassword(var1.getString(17));
      var2.setInputData(var1.getString(18));
      var2.setDesc(var1.getString(19));
      var2.setCreateUser(var1.getString(20));
      var2.setCreateDate(var1.getTimestamp(21));
      var2.setUpdateUser(var1.getString(22));
      var2.setUpdateDate(var1.getTimestamp(23));
      var2.setId(var1.getLong(24));
      var2.setProjectId(var1.getLong(25));
      var2.setEnable(var1.getBoolean(26));
      return var2;
   }

   private StringBuilder b() {
      this.j.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ like ?");
         this.j.add("%" + this.b + "%");
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" DESC_ like ?");
         this.j.add("%" + this.c + "%");
      }

      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.j.add(this.a);
      }

      if (this.i != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_=?");
         this.j.add(this.i);
      }

      if (this.h != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PACKET_ID_=?");
         this.j.add(this.h);
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ENABLE_=?");
         this.j.add(this.e);
      }

      if (this.f != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ASYNC_=?");
         this.j.add(this.f);
      }

      if (this.g != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" STATUS_=?");
         this.j.add(this.g.name());
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_USER_ like ?");
         this.j.add("%" + this.d + "%");
      }

      return var1;
   }

   public BatchQuery enable(Boolean var1) {
      this.e = var1;
      return this;
   }

   public BatchQuery async(Boolean var1) {
      this.f = var1;
      return this;
   }

   public BatchQuery status(BatchStatus var1) {
      this.g = var1;
      return this;
   }

   public BatchQuery packetId(Long var1) {
      this.h = var1;
      return this;
   }

   public BatchQuery createUserLike(String var1) {
      this.d = var1;
      return this;
   }
}
