package com.bstek.urule.console.database.manager.project.user;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserQueryImpl implements UserQuery {
   private String b;
   private String c;
   private String d;
   List a = new ArrayList();

   public Page users(int var1, int var2, long var3) {
      String var5 = "SELECT USER_ID_, USER_NAME_, CREATE_DATE_ FROM URULE_PROJECT_USER  WHERE PROJECT_ID_=?";
      this.a.clear();
      this.a.add(var3);
      StringBuilder var6 = this.a();
      if (var6.length() > 0) {
         var5 = var5 + " and" + var6.toString();
      }

      var5 = var5 + " order by CREATE_DATE_ desc";
      Connection var7 = JdbcUtils.getConnection();

      Page var12;
      try {
         Page var8 = new Page(var1, var2);
         var5 = JdbcUtils.getPageSql(var5, var8.getStartRow(), var2);
         PreparedStatement var9 = var7.prepareStatement(var5);
         JdbcUtils.fillPreparedStatementParameters(this.a, var9);
         ResultSet var10 = var9.executeQuery();
         List var11 = this.a(var10);
         var8.setData(var11);
         JdbcUtils.closeResultSet(var10);
         JdbcUtils.closeStatement(var9);
         var5 = "select count(*) from URULE_PROJECT_USER where PROJECT_ID_=?";
         if (var6.length() > 0) {
            var5 = var5 + " and" + var6.toString();
         }

         var9 = var7.prepareStatement(var5);
         JdbcUtils.fillPreparedStatementParameters(this.a, var9);
         var10 = var9.executeQuery();
         if (var10.next()) {
            var8.setTotalRows(var10.getLong(1));
         }

         JdbcUtils.closeResultSet(var10);
         JdbcUtils.closeStatement(var9);
         var12 = var8;
      } catch (Exception var16) {
         throw new RuleException(var16);
      } finally {
         JdbcUtils.closeConnection(var7);
      }

      return var12;
   }

   private StringBuilder a() {
      StringBuilder var1 = new StringBuilder();
      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" URULE_PROJECT_USER.USER_ID_ like ?");
         this.a.add("%" + this.b + "%");
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" URULE_PROJECT_USER.USER_NAME_ like ?");
         this.a.add("%" + this.c + "%");
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" (URULE_PROJECT_USER.USER_ID_ like ? or URULE_PROJECT_USER.USER_NAME_ like ?) ");
         this.a.add("%" + this.d + "%");
         this.a.add("%" + this.d + "%");
      }

      return var1;
   }

   private List a(ResultSet var1) throws Exception {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         User var3 = new User();
         var3.setId(var1.getString(1));
         var3.setName(var1.getString(2));
         var3.setCreateDate(var1.getTimestamp(3));
         var2.add(var3);
      }

      return var2;
   }

   public UserQuery idLike(String var1) {
      this.b = var1;
      return this;
   }

   public UserQuery nameLike(String var1) {
      this.c = var1;
      return this;
   }

   public UserQuery idnameLike(String var1) {
      this.d = var1;
      return this;
   }

   public Page roleUsers(int var1, int var2, long var3, long var5) {
      String var7 = "SELECT URULE_PROJECT_USER.USER_ID_, URULE_PROJECT_USER.USER_NAME_, URULE_PROJECT_USER.CREATE_DATE_ FROM URULE_PROJECT_USER  LEFT JOIN URULE_PROJECT_USER_ROLE on URULE_PROJECT_USER_ROLE.USER_ID_=URULE_PROJECT_USER.USER_ID_ WHERE URULE_PROJECT_USER_ROLE.ROLE_ID_=? and URULE_PROJECT_USER.PROJECT_ID_=?";
      this.a.clear();
      this.a.add(var5);
      this.a.add(var3);
      StringBuilder var8 = this.a();
      if (var8.length() > 0) {
         var7 = var7 + " and" + var8.toString();
      }

      var7 = var7 + " order by URULE_PROJECT_USER.CREATE_DATE_ desc";
      Connection var9 = JdbcUtils.getConnection();

      Page var14;
      try {
         Page var10 = new Page(var1, var2);
         var7 = JdbcUtils.getPageSql(var7, var10.getStartRow(), var2);
         PreparedStatement var11 = var9.prepareStatement(var7);
         JdbcUtils.fillPreparedStatementParameters(this.a, var11);
         ResultSet var12 = var11.executeQuery();
         List var13 = this.a(var12);
         var10.setData(var13);
         JdbcUtils.closeResultSet(var12);
         JdbcUtils.closeStatement(var11);
         var7 = "select count(*) FROM URULE_PROJECT_USER  LEFT JOIN URULE_PROJECT_USER_ROLE on URULE_PROJECT_USER_ROLE.USER_ID_=URULE_PROJECT_USER.USER_ID_ WHERE URULE_PROJECT_USER_ROLE.ROLE_ID_=? and URULE_PROJECT_USER.PROJECT_ID_=?";
         if (var8.length() > 0) {
            var7 = var7 + " and" + var8.toString();
         }

         var11 = var9.prepareStatement(var7);
         JdbcUtils.fillPreparedStatementParameters(this.a, var11);
         var12 = var11.executeQuery();
         if (var12.next()) {
            var10.setTotalRows(var12.getLong(1));
         }

         JdbcUtils.closeResultSet(var12);
         JdbcUtils.closeStatement(var11);
         var14 = var10;
      } catch (Exception var18) {
         throw new RuleException(var18);
      } finally {
         JdbcUtils.closeConnection(var9);
      }

      return var14;
   }
}
