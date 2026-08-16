package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.batch.BatchLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BatchLogQueryImpl implements BatchLogQuery {
   private String a;
   private Long b;
   private Long c;
   private String d;
   private String e;
   private Date f;
   private Date g;
   private Boolean h;
   private String i;
   private String j;
   private List k = new ArrayList();

   public BatchLogQuery groupId(String var1) {
      this.a = var1;
      return this;
   }

   public BatchLogQuery projectId(Long var1) {
      this.b = var1;
      return this;
   }

   public BatchLogQuery batchId(Long var1) {
      this.c = var1;
      return this;
   }

   public BatchLogQuery user(String var1) {
      this.d = var1;
      return this;
   }

   public BatchLogQuery batchNameLike(String var1) {
      this.e = var1;
      return this;
   }

   public BatchLogQuery dateBegin(Date var1) {
      this.f = var1;
      return this;
   }

   public BatchLogQuery dateEnd(Date var1) {
      this.g = var1;
      return this;
   }

   public Page paging(int var1, int var2) {
      String var3 = "select ID_, USER_, BATCH_ID_, BATCH_NAME_, STATUS_, TIME_, PROJECT_ID_, GROUP_ID_, IP_, USER_AGENT_, START_TIME_, END_TIME_, CREATE_DATE_, READ_COUNT_, FILTER_COUNT_ from URULE_LOG_BATCH";
      Connection var4 = JdbcUtils.getConnection();

      Page var10;
      try {
         StringBuilder var5 = this.a();
         if (var5.length() > 0) {
            var3 = var3 + " where" + var5.toString();
         }

         if (this.h != null) {
            var3 = var3 + " order by TIME_ desc";
         } else {
            var3 = var3 + " order by CREATE_DATE_ desc";
         }

         Page var6 = new Page(var1, var2);
         var3 = JdbcUtils.getPageSql(var4, var3, var6.getStartRow(), var2);
         PreparedStatement var7 = var4.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.k, var7);
         ResultSet var8 = var7.executeQuery();
         List var9 = this.a(var8);
         var6.setData(var9);
         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var3 = "select count(*) from URULE_LOG_BATCH";
         if (var5.length() > 0) {
            var3 = var3 + " where" + var5.toString();
         }

         var7 = var4.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.k, var7);
         var8 = var7.executeQuery();
         if (var8.next()) {
            var6.setTotalRows(var8.getLong(1));
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var10 = var6;
      } catch (Exception var14) {
         throw new RuleException(var14);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var10;
   }

   private List a(ResultSet var1) throws Exception {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         BatchLog var3 = new BatchLog();
         var3.setId(var1.getLong(1));
         var3.setUserId(var1.getString(2));
         var3.setUsername(var3.getUserId());
         var3.setBatchId(var1.getLong(3));
         var3.setBatchName(var1.getString(4));
         var3.setStatus(BatchStatus.valueOf(var1.getString(5)));
         var3.setTime(var1.getLong(6));
         var3.setProjectId(var1.getLong(7));
         var3.setGroupId(var1.getString(8));
         var3.setIp(var1.getString(9));
         var3.setUserAgent(var1.getString(10));
         var3.setStartTime(var1.getTimestamp(11));
         var3.setEndTime(var1.getTimestamp(12));
         var3.setCreateDate(var1.getTimestamp(13));
         var3.setReadCount(var1.getInt(14));
         var3.setFilterCount(var1.getInt(15));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a() throws SQLException {
      this.k.clear();
      StringBuilder var1 = new StringBuilder();
      if (StringUtils.isNotBlank(this.d)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" USER_ like ?");
         this.k.add("%" + this.d + "%");
      }

      if (StringUtils.isNotBlank(this.i)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" IP_ = ?");
         this.k.add(this.i);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_ = ?");
         this.k.add(this.b);
      }

      if (StringUtils.isNotBlank(this.a)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" GROUP_ID_ = ?");
         this.k.add(this.a);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" BATCH_ID_ = ?");
         this.k.add(this.c);
      }

      if (this.j != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" STATUS_ = ?");
         this.k.add(this.j);
      }

      if (StringUtils.isNotBlank(this.e)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" BATCH_NAME_ like ?");
         this.k.add("%" + this.e + "%");
      }

      if (this.f != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" START_TEIM_ > ?");
         this.k.add(this.f);
      }

      if (this.g != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" END_TIME_ < ?");
         this.k.add(this.g);
      }

      return var1;
   }

   public BatchLogQuery orderTime() {
      this.h = true;
      return this;
   }

   public BatchLogQuery ip(String var1) {
      this.i = var1;
      return this;
   }

   public BatchLogQuery status(String var1) {
      this.j = var1;
      return this;
   }

   public BatchLog details(Long var1) {
      String var2 = "select IN_PARAMS_, MSG_, DETAIL_, ITEM_DATA_, PACKET_ID_, PACKET_PARAMS_ from URULE_LOG_BATCH WHERE ID_=?";
      Connection var3 = JdbcUtils.getConnection();

      BatchLog var7;
      try {
         PreparedStatement var4 = var3.prepareStatement(var2);
         var4.setLong(1, var1);
         ResultSet var5 = var4.executeQuery();
         BatchLog var6 = new BatchLog();
         if (var5.next()) {
            var6.setInParams(var5.getString(1));
            var6.setMsg(var5.getString(2));
            var6.setDetail(var5.getString(3));
            var6.setItemData(var5.getString(4));
            var6.setPacketId(var5.getLong(5));
            var6.setPacketParams(var5.getString(6));
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
