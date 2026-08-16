package com.bstek.urule.console.database.manager.user;

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
   private String a;
   private String b;
   private List c = new ArrayList();

   public UserQuery idLike(String var1) {
      this.b = var1;
      return this;
   }

   public UserQuery nameLike(String var1) {
      this.a = var1;
      return this;
   }

   public Page paging(int var1, int var2, long var3) {
      String var5 = "select URULE_USER.ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, URULE_PROJECT_USER.CREATE_DATE_, UPDATE_DATE_ from URULE_USER left join URULE_PROJECT_USER on URULE_USER.ID_=URULE_PROJECT_USER.USER_ID_  where URULE_PROJECT_USER.PROJECT_ID_=?";
      this.c.clear();
      this.c.add(var3);
      StringBuilder var6 = this.a();
      if (var6.length() > 0) {
         var5 = var5 + " and" + var6.toString();
      }

      var5 = var5 + " order by URULE_PROJECT_USER.CREATE_DATE_ desc";
      Connection var7 = JdbcUtils.getConnection();

      Page var12;
      try {
         Page var8 = new Page(var1, var2);
         var5 = JdbcUtils.getPageSql(var5, var8.getStartRow(), var2);
         PreparedStatement var9 = var7.prepareStatement(var5);
         JdbcUtils.fillPreparedStatementParameters(this.c, var9);
         ResultSet var10 = var9.executeQuery();
         List var11 = this.a(var10);
         var8.setData(var11);
         JdbcUtils.closeResultSet(var10);
         JdbcUtils.closeStatement(var9);
         var5 = "select count(*) from URULE_USER left join URULE_PROJECT_USER on URULE_USER.ID_=URULE_PROJECT_USER.USER_ID_  where URULE_PROJECT_USER.PROJECT_ID_=?";
         if (var6.length() > 0) {
            var5 = var5 + " and" + var6.toString();
         }

         var9 = var7.prepareStatement(var5);
         JdbcUtils.fillPreparedStatementParameters(this.c, var9);
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

         var1.append(" URULE_USER.ID_ like ?");
         this.c.add("%" + this.b + "%");
      }

      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" URULE_USER.NAME_ like ?");
         this.c.add("%" + this.a + "%");
      }

      return var1;
   }

   private List a(ResultSet var1) throws Exception {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         User var3 = new User();
         var3.setId(var1.getString(1));
         var3.setName(var1.getString(2));
         var3.setEmail(var1.getString(3));
         var3.setSecretKey(var1.getString(4));
         var3.setEnable(var1.getBoolean(5));
         var3.setDesc(var1.getString(6));
         var3.setExpirDate(var1.getTimestamp(7));
         var3.setCreateDate(var1.getTimestamp(8));
         var3.setUpdateDate(var1.getTimestamp(9));
         var2.add(var3);
      }

      return var2;
   }

   public Page paging(int var1, int var2, String var3) {
      String var4 = "select URULE_USER.ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, URULE_GROUP_USER.CREATE_DATE_, UPDATE_DATE_ from URULE_USER left join URULE_GROUP_USER on URULE_USER.ID_=URULE_GROUP_USER.USER_ID_  where URULE_GROUP_USER.PROJECT_ID_=?";
      this.c.clear();
      this.c.add(var3);
      StringBuilder var5 = this.a();
      if (var5.length() > 0) {
         var4 = var4 + " and" + var5.toString();
      }

      var4 = var4 + " order by URULE_GROUP_USER..CREATE_DATE_ desc";
      Connection var6 = JdbcUtils.getConnection();

      Page var11;
      try {
         Page var7 = new Page(var1, var2);
         var4 = JdbcUtils.getPageSql(var4, var7.getStartRow(), var2);
         PreparedStatement var8 = var6.prepareStatement(var4);
         JdbcUtils.fillPreparedStatementParameters(this.c, var8);
         ResultSet var9 = var8.executeQuery();
         List var10 = this.a(var9);
         var7.setData(var10);
         JdbcUtils.closeResultSet(var9);
         JdbcUtils.closeStatement(var8);
         var4 = "select count(*) from URULE_USER left join URULE_GROUP_USER on URULE_USER.ID_=URULE_GROUP_USER.USER_ID_  where URULE_GROUP_USER.PROJECT_ID_=?";
         if (var5.length() > 0) {
            var4 = var4 + " and" + var5.toString();
         }

         var8 = var6.prepareStatement(var4);
         JdbcUtils.fillPreparedStatementParameters(this.c, var8);
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
}
