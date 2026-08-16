package com.bstek.urule.console.database.manager.repository;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.batch.DataSourceType;
import com.bstek.urule.console.database.model.datasource.DataSource;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataSourceQueryImpl implements DataSourceQuery {
   private Long a;
   private String b;
   private String c;
   private String d;
   private String e;
   private List f = new ArrayList();

   public DataSourceQuery id(Long var1) {
      this.a = var1;
      return this;
   }

   public DataSourceQuery groupId(String var1) {
      this.e = var1;
      return this;
   }

   private StringBuilder a() {
      this.f.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" GROUP_ID_=?");
         this.f.add(this.e);
      }

      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.f.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_=?");
         this.f.add(this.b);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" TYPE_=?");
         this.f.add(this.c);
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_USER_ like ?");
         this.f.add("%" + this.d + "%");
      }

      return var1;
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();

      List var4;
      try {
         String var2 = this.b();
         List var3 = this.a(var1, var2);
         var4 = var3;
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var4;
   }

   private List a(Connection var1, String var2) throws Exception {
      PreparedStatement var3 = var1.prepareStatement(var2);
      JdbcUtils.fillPreparedStatementParameters(this.f, var3);
      ArrayList var4 = new ArrayList();
      ResultSet var5 = var3.executeQuery();

      while(var5.next()) {
         DataSource var6 = this.a(var5);
         var4.add(var6);
      }

      JdbcUtils.closeResultSet(var5);
      JdbcUtils.closeStatement(var3);
      return var4;
   }

   private DataSource a(ResultSet var1) throws SQLException {
      DataSource var2 = new DataSource();
      var2.setName(var1.getString(1));
      var2.setType(DataSourceType.valueOf(var1.getString(2)));
      var2.setDataSourceBean(var1.getString(3));
      var2.setDbJndiName(var1.getString(4));
      var2.setDbDriver(var1.getString(5));
      var2.setDbUrl(var1.getString(6));
      var2.setDbUser(var1.getString(7));
      var2.setDbPwd(var1.getString(8));
      var2.setDbValidationQuery(var1.getString(9));
      var2.setDbInitialsize(var1.getInt(10));
      var2.setDbMaxTotal(var1.getInt(11));
      var2.setDbMaxIdle(var1.getInt(12));
      var2.setDbMinIdle(var1.getInt(13));
      var2.setDesc(var1.getString(14));
      var2.setCreateUser(var1.getString(15));
      var2.setCreateDate(var1.getTimestamp(16));
      var2.setUpdateUser(var1.getString(17));
      var2.setUpdateDate(var1.getTimestamp(18));
      var2.setId(var1.getLong(19));
      var2.setGroupId(var1.getString(20));
      return var2;
   }

   public void page(Page var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = this.b();
         String var4 = var3 + " ORDER BY NAME_ ";
         var4 = JdbcUtils.getPageSql(var2, var4, var1.getStartRow(), var1.getPageSize());
         var1.setData(this.a(var2, var4));
         PreparedStatement var5 = var2.prepareStatement(JdbcUtils.getCountSql(var3));
         JdbcUtils.fillPreparedStatementParameters(this.f, var5);
         ResultSet var6 = var5.executeQuery();
         if (var6.next()) {
            var1.setTotalRows((long)var6.getInt(1));
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   private String b() {
      String var1 = "SELECT NAME_, TYPE_, DATASOURCE_BEAN_, DB_JNDI_NAME_, DB_DRIVER_, DB_URL_, DB_USER_, DB_PWD_, DB_VALIDATION_QUERY_, DB_INITIAL_SIZE_, DB_MAX_TOTAL_, DB_MAX_IDLE_, DB_MIN_IDLE_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, GROUP_ID_ FROM URULE_DATASOURCE ";
      StringBuilder var2 = this.a();
      if (var2.length() > 0) {
         var1 = var1 + " where" + var2.toString();
      }

      return var1;
   }

   public DataSourceQuery nameLike(String var1) {
      this.b = var1;
      return this;
   }

   public DataSourceQuery createUserLike(String var1) {
      this.d = var1;
      return this;
   }

   public DataSourceQuery type(String var1) {
      this.c = var1;
      return this;
   }
}
