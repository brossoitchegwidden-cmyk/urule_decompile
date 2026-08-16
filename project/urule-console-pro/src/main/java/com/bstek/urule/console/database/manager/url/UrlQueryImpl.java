package com.bstek.urule.console.database.manager.url;

import com.bstek.urule.console.database.model.UrlConfig;
import com.bstek.urule.console.database.model.UrlType;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UrlQueryImpl implements UrlQuery {
   private String a;
   private String b;
   private UrlType c;
   private String d;
   private Long e;
   private List f = new ArrayList();

   protected UrlQueryImpl() {
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();
      String var2 = "select ID_,NAME_,URL_,TYPE_,GROUP_ID_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_URL_CONFIG";
      StringBuilder var3 = this.a();
      if (var3.length() > 0) {
         var2 = var2 + " where" + var3.toString();
      }

      var2 = var2 + " order by CREATE_DATE_ desc";

      ArrayList var14;
      try {
         PreparedStatement var4 = var1.prepareStatement(var2);
         JdbcUtils.fillPreparedStatementParameters(this.f, var4);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            UrlConfig var7 = new UrlConfig();
            var7.setId(var6.getLong(1));
            var7.setName(var6.getString(2));
            var7.setUrl(var6.getString(3));
            var7.setType(UrlType.valueOf(var6.getString(4)));
            var7.setGroupId(var6.getString(5));
            var7.setCreateUser(var6.getString(6));
            var7.setUpdateUser(var6.getString(7));
            var7.setCreateDate(var6.getTimestamp(8));
            var7.setUpdateDate(var6.getTimestamp(9));
            var5.add(var7);
         }

         JdbcUtils.closeStatement(var4);
         var14 = var5;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var14;
   }

   private StringBuilder a() {
      this.f.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ like ?");
         this.f.add("%" + this.a + "%");
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" URL_ like ?");
         this.f.add("%" + this.b + "%");
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" TYPE_=?");
         this.f.add(this.c.name());
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" GROUP_ID_=?");
         this.f.add(this.d);
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.f.add(this.e);
      }

      return var1;
   }

   public UrlQuery nameLike(String var1) {
      this.a = var1;
      return this;
   }

   public UrlQuery urlLike(String var1) {
      this.b = var1;
      return this;
   }

   public UrlQuery type(UrlType var1) {
      this.c = var1;
      return this;
   }

   public UrlQuery groupId(String var1) {
      this.d = var1;
      return this;
   }

   public UrlQuery id(long var1) {
      this.e = var1;
      return this;
   }
}
