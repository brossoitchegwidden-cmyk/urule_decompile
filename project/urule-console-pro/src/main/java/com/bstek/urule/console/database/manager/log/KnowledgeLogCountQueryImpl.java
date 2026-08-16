package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.database.vo.RuleExecVO;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KnowledgeLogCountQueryImpl implements KnowledgeLogCountQuery {
   private Long a;
   private String b;
   private String c;
   private Long d;
   private String e;
   private Date f;
   private Date g;
   private List h = new ArrayList();

   public KnowledgeLogCountQuery projectId(Long var1) {
      this.a = var1;
      return this;
   }

   public KnowledgeLogCountQuery groupId(String var1) {
      this.b = var1;
      return this;
   }

   public KnowledgeLogCountQuery user(String var1) {
      this.c = var1;
      return this;
   }

   public KnowledgeLogCountQuery packageId(Long var1) {
      this.d = var1;
      return this;
   }

   public KnowledgeLogCountQuery packageName(String var1) {
      this.e = var1;
      return this;
   }

   public KnowledgeLogCountQuery dateBegin(Date var1) {
      this.f = var1;
      return this;
   }

   public KnowledgeLogCountQuery dateEnd(Date var1) {
      this.g = var1;
      return this;
   }

   public List listTime() {
      String var1 = "select KNOWLEDGE_ID_, KNOWLEDGE_NAME_, COUNT(TIME_) AS COUNT_TIME_ from URULE_LOG_KNOWLEDGE";
      Connection var2 = JdbcUtils.getConnection();

      List var7;
      try {
         StringBuilder var3 = this.a();
         if (var3.length() > 0) {
            var1 = var1 + " where" + var3.toString();
         }

         var1 = var1 + " GROUP BY KNOWLEDGE_ID_, KNOWLEDGE_NAME_ order by COUNT_TIME_ DESC";
         PreparedStatement var4 = var2.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.h, var4);
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
         RuleExecVO var3 = new RuleExecVO();
         var3.setKnowledgeId(var1.getLong(1));
         var3.setKnowledgeName(var1.getString(2));
         var3.setTime(var1.getInt(3));
         var2.add(var3);
      }

      return var2;
   }

   public List listExec() {
      String var1 = "select KNOWLEDGE_ID_, KNOWLEDGE_NAME_, COUNT(ID_) as RECORD_COUNT from URULE_LOG_KNOWLEDGE";
      Connection var2 = JdbcUtils.getConnection();

      List var7;
      try {
         StringBuilder var3 = this.a();
         if (var3.length() > 0) {
            var1 = var1 + " where" + var3.toString();
         }

         var1 = var1 + " group by KNOWLEDGE_ID_, KNOWLEDGE_NAME_ order by RECORD_COUNT DESC";
         PreparedStatement var4 = var2.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.h, var4);
         ResultSet var5 = var4.executeQuery();
         List var6 = this.b(var5);
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

   private List b(ResultSet var1) throws Exception {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         RuleExecVO var3 = new RuleExecVO();
         var3.setKnowledgeId(var1.getLong(1));
         var3.setKnowledgeName(var1.getString(2));
         var3.setCount(var1.getInt(3));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a() throws SQLException {
      this.h.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" USER_ = ?");
         this.h.add(this.c);
      }

      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_ = ?");
         this.h.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" GROUP_ID_ = ?");
         this.h.add(this.b);
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PACKAGE_ID_ = ?");
         this.h.add(this.d);
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PACKAGE_NAME_ = ?");
         this.h.add(this.e);
      }

      if (this.f != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_DATE_ > ?");
         this.h.add(this.f);
      }

      if (this.g != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_DATE_ < ?");
         this.h.add(this.g);
      }

      return var1;
   }
}
