package com.bstek.urule.console.database.manager.project;

import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjectQueryImpl implements ProjectQuery {
   private String a;
   private String b;
   private String c;
   private String d;
   private String e;
   private String f;
   private String g;
   private List h = new ArrayList();

   public List listIds() {
      Connection var1 = JdbcUtils.getConnection();
      String var2 = "select ID_ from URULE_PROJECT";
      StringBuilder var3 = this.b();
      if (var3.length() > 0) {
         var2 = var2 + " where" + var3.toString();
      }

      ArrayList var7;
      try {
         PreparedStatement var4 = var1.prepareStatement(var2);
         JdbcUtils.fillPreparedStatementParameters(this.h, var4);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            var5.add(var6.getLong("ID_"));
         }

         JdbcUtils.closeStatement(var4);
         var7 = var5;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var7;
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();
      String var2 = "select ID_, NAME_, TYPE_, DESC_, GROUP_ID_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_ from URULE_PROJECT";
      StringBuilder var3 = this.b();
      if (var3.length() > 0) {
         var2 = var2 + " where" + var3.toString();
      }

      StringBuilder var4 = this.a();
      if (var4.length() > 0) {
         var2 = var2 + " order by" + var4.toString();
      }

      ArrayList var14;
      try {
         PreparedStatement var5 = var1.prepareStatement(var2);
         JdbcUtils.fillPreparedStatementParameters(this.h, var5);
         ArrayList var6 = new ArrayList();
         ResultSet var7 = var5.executeQuery();

         while(var7.next()) {
            Project var8 = new Project();
            var8.setId(var7.getLong("ID_"));
            var8.setName(var7.getString("NAME_"));
            var8.setType(var7.getString("TYPE_"));
            var8.setDesc(var7.getString("DESC_"));
            var8.setGroupId(var7.getString("GROUP_ID_"));
            var8.setCreateUser(var7.getString("CREATE_USER_"));
            var8.setCreateDate(var7.getTimestamp("CREATE_DATE_"));
            var8.setUpdateUser(var7.getString("UPDATE_USER_"));
            var8.setUpdateDate(var7.getTimestamp("UPDATE_DATE_"));
            var6.add(var8);
         }

         JdbcUtils.closeStatement(var5);
         var14 = var6;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var14;
   }

   private StringBuilder a() {
      StringBuilder var1 = new StringBuilder();
      if (this.f != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_DATE_ ").append(this.f);
      }

      if (this.g != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ ").append(this.g);
      }

      return var1;
   }

   private StringBuilder b() {
      this.h.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ = ?");
         this.h.add(this.b);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ like ?");
         this.h.add("%" + this.c + "%");
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" TYPE_=?");
         this.h.add(this.d);
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" GROUP_ID_=?");
         this.h.add(this.e);
      }

      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" (CREATE_USER_=? or ID_ in (select PROJECT_ID_ from URULE_PROJECT_USER where USER_ID_=?)) ");
         this.h.add(this.a);
         this.h.add(this.a);
      }

      return var1;
   }

   public ProjectQuery name(String var1) {
      this.b = var1;
      return this;
   }

   public ProjectQuery nameLike(String var1) {
      this.c = var1;
      return this;
   }

   public ProjectQuery type(String var1) {
      this.d = var1;
      return this;
   }

   public ProjectQuery groupId(String var1) {
      this.e = var1;
      return this;
   }

   public ProjectQuery orderbyCreateDate(String var1) {
      this.f = var1;
      return this;
   }

   public ProjectQuery orderbyName(String var1) {
      this.g = var1;
      return this;
   }

   public ProjectQuery userId(String var1) {
      this.a = var1;
      return this;
   }
}
