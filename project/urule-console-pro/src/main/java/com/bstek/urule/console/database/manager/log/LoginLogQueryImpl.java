package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginLogQueryImpl implements LoginLogQuery {
   private String a;
   private String b;
   private String c;
   private String d;
   private Date e;
   private Date f;
   private List g = new ArrayList();

   protected LoginLogQueryImpl() {
   }

   public Page paging(int var1, int var2) {
      String var3 = "select ID_,USER_ID_,USER_NAME_,IP_,USER_AGENT_,CREATE_DATE_ from URULE_LOG_USERLOGIN";
      StringBuilder var4 = this.a();
      if (var4.length() > 0) {
         var3 = var3 + " where" + var4.toString();
      }

      var3 = var3 + " order by CREATE_DATE_ desc";
      Connection var5 = JdbcUtils.getConnection();

      Page var10;
      try {
         Page var6 = new Page(var1, var2);
         var3 = JdbcUtils.getPageSql(var3, var6.getStartRow(), var2);
         PreparedStatement var7 = var5.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.g, var7);
         ResultSet var8 = var7.executeQuery();
         List var9 = this.a(var8);
         var6.setData(var9);
         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var3 = "select count(*) from URULE_LOG_USERLOGIN";
         if (var4.length() > 0) {
            var3 = var3 + " where" + var4.toString();
         }

         var7 = var5.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.g, var7);
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
         JdbcUtils.closeConnection(var5);
      }

      return var10;
   }

   private List a(ResultSet var1) throws Exception {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         LoginLog var3 = new LoginLog();
         var3.setId(var1.getLong(1));
         var3.setUserId(var1.getString(2));
         var3.setUsername(var1.getString(3));
         var3.setIp(var1.getString(4));
         var3.setUserAgent(var1.getString(5));
         var3.setCreateDate(var1.getTimestamp(6));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a() {
      this.g.clear();
      StringBuilder var1 = new StringBuilder();
      if (StringUtils.isNotBlank(this.a)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" USER_ID_ = ?");
         this.g.add(this.a);
      }

      if (StringUtils.isNotBlank(this.b)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" USER_ID_ like ?");
         this.g.add("%" + this.b + "%");
      }

      if (StringUtils.isNotBlank(this.c)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" IP_ = ?");
         this.g.add(this.c);
      }

      if (StringUtils.isNotBlank(this.d)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" USER_NAME_ like ?");
         this.g.add("%" + this.d + "%");
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_DATE_ > ?");
         this.g.add(this.e);
      }

      if (this.f != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_DATE_ < ?");
         this.g.add(this.f);
      }

      return var1;
   }

   public LoginLogQuery userId(String var1) {
      this.a = var1;
      return this;
   }

   public LoginLogQuery ip(String var1) {
      this.c = var1;
      return this;
   }

   public LoginLogQuery username(String var1) {
      this.d = var1;
      return this;
   }

   public LoginLogQuery loginDateBegin(Date var1) {
      this.e = var1;
      return this;
   }

   public LoginLogQuery loginDateEnd(Date var1) {
      this.f = var1;
      return this;
   }

   public LoginLogQuery userIdLike(String var1) {
      this.b = var1;
      return this;
   }
}
