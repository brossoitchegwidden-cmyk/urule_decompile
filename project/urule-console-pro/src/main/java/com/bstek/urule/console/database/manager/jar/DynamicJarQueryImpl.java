package com.bstek.urule.console.database.manager.jar;

import com.bstek.urule.console.database.model.DynamicJar;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DynamicJarQueryImpl implements DynamicJarQuery {
   private Long a;
   private String b;
   private String c;
   private String d;
   private List e = new ArrayList();

   protected DynamicJarQueryImpl() {
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();
      String var2 = "select ID_,NAME_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,GROUP_ID_ from URULE_DYNAMIC_JAR";
      StringBuilder var3 = this.a();
      if (var3.length() > 0) {
         var2 = var2 + " where" + var3.toString();
      }

      var2 = var2 + " order by CREATE_DATE_ desc";

      ArrayList var14;
      try {
         PreparedStatement var4 = var1.prepareStatement(var2);
         JdbcUtils.fillPreparedStatementParameters(this.e, var4);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            DynamicJar var7 = new DynamicJar();
            var7.setId(var6.getLong(1));
            var7.setName(var6.getString(2));
            var7.setDesc(var6.getString(3));
            var7.setCreateUser(var6.getString(4));
            var7.setUpdateUser(var6.getString(5));
            var7.setCreateDate(var6.getTimestamp(6));
            var7.setUpdateDate(var6.getTimestamp(7));
            var7.setGroupId(var6.getString(8));
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
      this.e.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ like ?");
         this.e.add("%" + this.c + "%");
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" DESC_ like ?");
         this.e.add("%" + this.d + "%");
      }

      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.e.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" GROUP_ID_=?");
         this.e.add(this.b);
      }

      return var1;
   }

   public DynamicJarQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public DynamicJarQuery groupId(String var1) {
      this.b = var1;
      return this;
   }

   public DynamicJarQuery nameLike(String var1) {
      this.c = var1;
      return this;
   }

   public DynamicJarQuery descLike(String var1) {
      this.d = var1;
      return this;
   }
}
