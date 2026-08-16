package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.Page;
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

public class KnowledgeLogQueryImpl implements KnowledgeLogQuery {
   private String a;
   private String b;
   private Long c;
   private String d;
   private Long e;
   private String f;
   private Date g;
   private Date h;
   private Boolean i;
   private List j = new ArrayList();

   protected KnowledgeLogQueryImpl() {
   }

   public KnowledgeLogQuery orderTime() {
      this.i = true;
      return this;
   }

   public Page paging(int var1, int var2) {
      String var3 = "select ID_, USER_, KNOWLEDGE_ID_, KNOWLEDGE_NAME_, VERSION_, TIME_, PROJECT_ID_, IP_, USER_AGENT_, START_TIME_, END_TIME_, CREATE_DATE_ from URULE_LOG_KNOWLEDGE";
      Connection var4 = JdbcUtils.getConnection();

      Page var10;
      try {
         StringBuilder var5 = this.a();
         if (var5.length() > 0) {
            var3 = var3 + " where" + var5.toString();
         }

         if (this.i != null) {
            var3 = var3 + " order by TIME_ desc";
         } else {
            var3 = var3 + " order by CREATE_DATE_ desc";
         }

         Page var6 = new Page(var1, var2);
         var3 = JdbcUtils.getPageSql(var3, var6.getStartRow(), var2);
         PreparedStatement var7 = var4.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.j, var7);
         ResultSet var8 = var7.executeQuery();
         List var9 = this.a(var8);
         var6.setData(var9);
         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var3 = "select count(*) from URULE_LOG_KNOWLEDGE";
         if (var5.length() > 0) {
            var3 = var3 + " where" + var5.toString();
         }

         var7 = var4.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.j, var7);
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

   public List list() {
      String var1 = "select ID_, USER_, KNOWLEDGE_ID_, KNOWLEDGE_NAME_, VERSION_, TIME_, PROJECT_ID_, IP_, USER_AGENT_, START_TIME_, END_TIME_, CREATE_DATE_ from URULE_LOG_KNOWLEDGE";
      Connection var2 = JdbcUtils.getConnection();

      List var7;
      try {
         StringBuilder var3 = this.a();
         if (var3.length() > 0) {
            var1 = var1 + " where" + var3.toString();
         }

         var1 = var1 + " order by CREATE_DATE_ desc";
         PreparedStatement var4 = var2.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.j, var4);
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
         KnowledgeLog var3 = new KnowledgeLog();
         var3.setId(var1.getLong(1));
         var3.setUserId(var1.getString(2));
         var3.setUsername(var3.getUserId());
         var3.setKnowledgeId(var1.getLong(3));
         var3.setKnowledgeName(var1.getString(4));
         var3.setVersion(var1.getString(5));
         var3.setTime(var1.getLong(6));
         var3.setProjectId(var1.getLong(7));
         var3.setIp(var1.getString(8));
         var3.setUserAgent(var1.getString(9));
         var3.setStartTime(var1.getTimestamp(10));
         var3.setEndTime(var1.getTimestamp(11));
         var3.setCreateDate(var1.getTimestamp(12));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a() throws SQLException {
      this.j.clear();
      StringBuilder var1 = new StringBuilder();
      if (StringUtils.isNotBlank(this.a)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" USER_ like ?");
         this.j.add("%" + this.a + "%");
      }

      if (StringUtils.isNotBlank(this.b)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" IP_ = ?");
         this.j.add(this.b);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_ = ?");
         this.j.add(this.c);
      }

      if (StringUtils.isNotBlank(this.d)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" GROUP_ID_ = ?");
         this.j.add(this.d);
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" KNOWLEDGE_ID_ = ?");
         this.j.add(this.e);
      }

      if (StringUtils.isNotBlank(this.f)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" KNOWLEDGE_NAME_ like ?");
         this.j.add("%" + this.f + "%");
      }

      if (this.g != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_DATE_ > ?");
         this.j.add(this.g);
      }

      if (this.h != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_DATE_ < ?");
         this.j.add(this.h);
      }

      return var1;
   }

   public KnowledgeLogQuery user(String var1) {
      this.a = var1;
      return this;
   }

   public KnowledgeLogQuery ip(String var1) {
      this.b = var1;
      return this;
   }

   public KnowledgeLogQuery dateBegin(Date var1) {
      this.g = var1;
      return this;
   }

   public KnowledgeLogQuery dateEnd(Date var1) {
      this.h = var1;
      return this;
   }

   public KnowledgeLogQuery projectId(Long var1) {
      this.c = var1;
      return this;
   }

   public KnowledgeLogQuery packetId(Long var1) {
      this.e = var1;
      return this;
   }

   public KnowledgeLogQuery packetNameLike(String var1) {
      this.f = var1;
      return this;
   }

   public KnowledgeLogQuery groupId(String var1) {
      this.d = var1;
      return this;
   }

   public KnowledgeLog details(Long var1) {
      String var2 = "select IN_PARAMS_, OUT_PARAMS_, LOGS_ from URULE_LOG_KNOWLEDGE WHERE ID_=?";
      Connection var3 = JdbcUtils.getConnection();

      KnowledgeLog var7;
      try {
         PreparedStatement var4 = var3.prepareStatement(var2);
         var4.setLong(1, var1);
         ResultSet var5 = var4.executeQuery();
         KnowledgeLog var6 = new KnowledgeLog();
         if (var5.next()) {
            var6.setInParams(var5.getString(1));
            var6.setOutParams(var5.getString(2));
            var6.setLogs(var5.getString(3));
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
