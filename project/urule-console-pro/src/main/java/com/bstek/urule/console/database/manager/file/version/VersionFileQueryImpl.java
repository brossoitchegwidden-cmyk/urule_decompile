package com.bstek.urule.console.database.manager.file.version;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.VersionFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VersionFileQueryImpl implements VersionFileQuery {
   private Long a;
   private Long b;
   private Long c;
   private String d;
   private String e;
   private String f;
   private List g = new ArrayList();

   protected VersionFileQueryImpl() {
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();

      List var7;
      try {
         String var2 = "select ID_,FILE_ID_,PROJECT_ID_,NAME_, VERSION_, CREATE_DATE_,CREATE_USER_,NOTE_,DIGEST_ from URULE_VERSION_FILE";
         StringBuilder var3 = this.a();
         if (var3.length() > 0) {
            var2 = var2 + " where" + var3.toString();
         }

         var2 = var2 + " order by CREATE_DATE_ desc";
         PreparedStatement var4 = var1.prepareStatement(var2);
         JdbcUtils.fillPreparedStatementParameters(this.g, var4);
         ResultSet var5 = var4.executeQuery();
         List var6 = this.a(var5);
         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var7 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var7;
   }

   public Page paging(int var1, int var2) {
      Connection var3 = JdbcUtils.getConnection();

      Page var10;
      try {
         String var4 = "select ID_,FILE_ID_,PROJECT_ID_,NAME_, VERSION_, CREATE_DATE_,CREATE_USER_,NOTE_,DIGEST_ from URULE_VERSION_FILE";
         StringBuilder var5 = this.a();
         if (var5.length() > 0) {
            var4 = var4 + " where" + var5.toString();
         }

         var4 = var4 + " order by CREATE_DATE_ desc";
         Page var6 = new Page(var1, var2);
         var4 = JdbcUtils.getPageSql(var4, var6.getStartRow(), var2);
         PreparedStatement var7 = var3.prepareStatement(var4);
         JdbcUtils.fillPreparedStatementParameters(this.g, var7);
         ResultSet var8 = var7.executeQuery();
         List var9 = this.a(var8);
         var6.setData(var9);
         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var4 = "select count(*) from URULE_VERSION_FILE";
         if (var5.length() > 0) {
            var4 = var4 + " where" + var5.toString();
         }

         var7 = var3.prepareStatement(var4);
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
         JdbcUtils.closeConnection(var3);
      }

      return var10;
   }

   private List a(ResultSet var1) throws SQLException {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         VersionFile var3 = new VersionFile();
         var3.setId(var1.getLong(1));
         var3.setFileId(var1.getLong(2));
         var3.setProjectId(var1.getLong(3));
         var3.setName(var1.getString(4));
         var3.setVersion(var1.getString(5));
         var3.setCreateDate(var1.getTimestamp(6));
         var3.setCreateUser(var1.getString(7));
         var3.setNote(var1.getString(8));
         var3.setDigest(var1.getString(9));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a() {
      this.g.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.g.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" FILE_ID_=?");
         this.g.add(this.b);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_=?");
         this.g.add(this.c);
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" VERSION_=?");
         this.g.add(this.d);
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" VERSION_ like ?");
         this.g.add("%" + this.e + "%");
      }

      if (this.f != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NOTE_ like ?");
         this.g.add("%" + this.f + "%");
      }

      return var1;
   }

   public VersionFileQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public VersionFileQuery fileId(long var1) {
      this.b = var1;
      return this;
   }

   public VersionFileQuery versionLike(String var1) {
      this.e = var1;
      return this;
   }

   public VersionFileQuery version(String var1) {
      this.d = var1;
      return this;
   }

   public VersionFileQuery noteLike(String var1) {
      this.f = var1;
      return this;
   }

   public VersionFileQuery projectId(long var1) {
      this.c = var1;
      return this;
   }
}
