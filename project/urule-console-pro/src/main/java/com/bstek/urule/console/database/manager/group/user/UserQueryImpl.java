package com.bstek.urule.console.database.manager.group.user;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
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

   public Page users(int var1, int var2, String var3) {
      String var4 = "SELECT USER_ID_, USER_NAME_, CREATE_DATE_ FROM URULE_GROUP_USER  WHERE GROUP_ID_=?";
      this.a.clear();
      this.a.add(var3);
      StringBuilder var5 = this.a();
      if (var5.length() > 0) {
         var4 = var4 + " and" + var5.toString();
      }

      var4 = var4 + " order by CREATE_DATE_ desc";
      Connection var6 = JdbcUtils.getConnection();

      Page var11;
      try {
         Page var7 = new Page(var1, var2);
         var4 = JdbcUtils.getPageSql(var4, var7.getStartRow(), var2);
         PreparedStatement var8 = var6.prepareStatement(var4);
         JdbcUtils.fillPreparedStatementParameters(this.a, var8);
         ResultSet var9 = var8.executeQuery();
         List var10 = this.a(var9);
         var7.setData(var10);
         JdbcUtils.closeResultSet(var9);
         JdbcUtils.closeStatement(var8);
         var4 = "select count(*) from URULE_GROUP_USER where GROUP_ID_=?";
         if (var5.length() > 0) {
            var4 = var4 + " and" + var5.toString();
         }

         var8 = var6.prepareStatement(var4);
         JdbcUtils.fillPreparedStatementParameters(this.a, var8);
         var9 = var8.executeQuery();
         if (var9.next()) {
            var7.setTotalRows(var9.getLong(1));
         }

         JdbcUtils.closeResultSet(var9);
         JdbcUtils.closeStatement(var8);
         var11 = var7;
      } catch (Exception var15) {
         throw new RuleException(var15);
      } finally {
         JdbcUtils.closeConnection(var6);
      }

      return var11;
   }

   private StringBuilder a() {
      StringBuilder var1 = new StringBuilder();
      if (StringUtils.isNotBlank(this.b)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" URULE_GROUP_USER.USER_ID_ like ?");
         this.a.add("%" + this.b + "%");
      }

      if (StringUtils.isNotBlank(this.c)) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" URULE_GROUP_USER.USER_NAME_ like ?");
         this.a.add("%" + this.c + "%");
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" (URULE_GROUP_USER.USER_ID_ like ? or URULE_GROUP_USER.USER_NAME_ like ?) ");
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

   public Page roleUsers(int var1, int var2, String var3, long var4) {
      String var6 = "SELECT URULE_GROUP_USER.USER_ID_, URULE_GROUP_USER.USER_NAME_, URULE_GROUP_USER.CREATE_DATE_ FROM URULE_GROUP_USER  LEFT JOIN URULE_GROUP_USER_ROLE on URULE_GROUP_USER_ROLE.USER_ID_=URULE_GROUP_USER.USER_ID_ WHERE URULE_GROUP_USER_ROLE.ROLE_ID_=? and URULE_GROUP_USER.GROUP_ID_=?";
      this.a.clear();
      this.a.add(var4);
      this.a.add(var3);
      StringBuilder var7 = this.a();
      if (var7.length() > 0) {
         var6 = var6 + " and" + var7.toString();
      }

      var6 = var6 + " order by URULE_GROUP_USER.CREATE_DATE_ desc";
      Connection var8 = JdbcUtils.getConnection();

      Page var13;
      try {
         Page var9 = new Page(var1, var2);
         var6 = JdbcUtils.getPageSql(var6, var9.getStartRow(), var2);
         PreparedStatement var10 = var8.prepareStatement(var6);
         JdbcUtils.fillPreparedStatementParameters(this.a, var10);
         ResultSet var11 = var10.executeQuery();
         List var12 = this.a(var11);
         var9.setData(var12);
         JdbcUtils.closeResultSet(var11);
         JdbcUtils.closeStatement(var10);
         var6 = "select count(*) FROM URULE_GROUP_USER  LEFT JOIN URULE_GROUP_USER_ROLE on URULE_GROUP_USER_ROLE.USER_ID_=URULE_GROUP_USER.USER_ID_ WHERE URULE_GROUP_USER_ROLE.ROLE_ID_=? and URULE_GROUP_USER.GROUP_ID_=?";
         if (var7.length() > 0) {
            var6 = var6 + " and" + var7.toString();
         }

         var10 = var8.prepareStatement(var6);
         JdbcUtils.fillPreparedStatementParameters(this.a, var10);
         var11 = var10.executeQuery();
         if (var11.next()) {
            var9.setTotalRows(var11.getLong(1));
         }

         JdbcUtils.closeResultSet(var11);
         JdbcUtils.closeStatement(var10);
         var13 = var9;
      } catch (Exception var17) {
         throw new RuleException(var17);
      } finally {
         JdbcUtils.closeConnection(var8);
      }

      return var13;
   }

   public UserQuery idnameLike(String var1) {
      this.d = var1;
      return this;
   }
}
