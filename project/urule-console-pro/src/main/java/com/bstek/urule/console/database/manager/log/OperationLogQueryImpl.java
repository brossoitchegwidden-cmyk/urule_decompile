package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperationLogQueryImpl implements OperationLogQuery {
   private String a;
   private String b;
   private String c;
   private String d;
   private Long e;
   private String f;
   private String g;
   private List h;
   private List i;
   private Date j;
   private Date k;
   private List l = new ArrayList();

   protected OperationLogQueryImpl() {
   }

   public Page paging(int var1, int var2) {
      String var3 = "select ID_,USER_ID_,USER_NAME_,GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CATEGORY_, ACTION_, ITEM_ID_, CONTENT_, CREATE_DATE_ from URULE_LOG_OPERATION";
      Connection var4 = JdbcUtils.getConnection();

      Page var10;
      try {
         StringBuilder var5 = this.a(var4);
         if (var5.length() > 0) {
            var3 = var3 + " where" + var5.toString();
         }

         var3 = var3 + " order by CREATE_DATE_ desc";
         Page var6 = new Page(var1, var2);
         var3 = JdbcUtils.getPageSql(var3, var6.getStartRow(), var2);
         PreparedStatement var7 = var4.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.l, var7);
         ResultSet var8 = var7.executeQuery();
         List var9 = this.a(var8);
         var6.setData(var9);
         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var3 = "select count(*) from URULE_LOG_OPERATION";
         if (var5.length() > 0) {
            var3 = var3 + " where" + var5.toString();
         }

         var7 = var4.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.l, var7);
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
         JdbcUtils.closeConnection(var4);
      }

      return var10;
   }

   public List list() {
      String var1 = "select ID_,USER_ID_,USER_NAME_,GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CATEGORY_, ACTION_, ITEM_ID_, CONTENT_, CREATE_DATE_ from URULE_LOG_OPERATION";
      Connection var2 = JdbcUtils.getConnection();

      List var7;
      try {
         StringBuilder var3 = this.a(var2);
         if (var3.length() > 0) {
            var1 = var1 + " where" + var3.toString();
         }

         var1 = var1 + " order by CREATE_DATE_ desc";
         PreparedStatement var4 = var2.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.l, var4);
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
         OperationLog var3 = new OperationLog();
         var3.setId(var1.getLong(1));
         var3.setUserId(var1.getString(2));
         var3.setUsername(var1.getString(3));
         var3.setGroupId(var1.getString(4));
         var3.setGroupName(var1.getString(5));
         var3.setProjectId(var1.getLong(6));
         var3.setProjectName(var1.getString(7));
         var3.setCategory(var1.getString(8));
         var3.setAction(var1.getString(9));
         var3.setItemId(var1.getString(10));
         var3.setContent(var1.getString(11));
         var3.setCreateDate(var1.getTimestamp(12));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a(Connection var1) throws SQLException {
      this.l.clear();
      StringBuilder var2 = new StringBuilder();
      if (StringUtils.isNotBlank(this.a)) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" USER_ID_ = ?");
         this.l.add(this.a);
      }

      if (StringUtils.isNotBlank(this.b)) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" USER_ID_ like ?");
         this.l.add("%" + this.b + "%");
      }

      if (StringUtils.isNotBlank(this.c)) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" USER_NAME_ like ?");
         this.l.add("%" + this.c + "%");
      }

      if (this.d != null) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" GROUP_ID_ = ?");
         this.l.add(this.d);
      }

      if (this.e != null) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" PROJECT_ID_ = ?");
         this.l.add(this.e);
      } else {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" PROJECT_ID_ is null");
      }

      if (StringUtils.isNotBlank(this.f)) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" CATEGORY_ = ?");
         this.l.add(this.f);
      }

      if (StringUtils.isNotBlank(this.g)) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" CATEGORY_ like ?");
         this.l.add("%" + this.g + "%");
      }

      if (this.h != null) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" CATEGORY_ in (");

         for(int var3 = 0; var3 < this.h.size(); ++var3) {
            String var4 = (String)this.h.get(var3);
            var2.append("?");
            if (var3 + 1 < this.h.size()) {
               var2.append(",");
            }

            this.l.add(var4);
         }

         var2.append(")");
      }

      if (this.i != null) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" ACTION_ in (");

         for(int var5 = 0; var5 < this.i.size(); ++var5) {
            String var6 = (String)this.i.get(var5);
            var2.append("?");
            if (var5 + 1 < this.i.size()) {
               var2.append(",");
            }

            this.l.add(var6);
         }

         var2.append(")");
      }

      if (this.j != null) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" CREATE_DATE_ > ?");
         this.l.add(this.j);
      }

      if (this.k != null) {
         if (var2.length() > 0) {
            var2.append(" and");
         }

         var2.append(" CREATE_DATE_ < ?");
         this.l.add(this.k);
      }

      return var2;
   }

   public OperationLogQuery userId(String var1) {
      this.a = var1;
      return this;
   }

   public OperationLogQuery groupId(String var1) {
      this.d = var1;
      return this;
   }

   public OperationLogQuery username(String var1) {
      this.c = var1;
      return this;
   }

   public OperationLogQuery projectId(Long var1) {
      this.e = var1;
      return this;
   }

   public OperationLogQuery category(String var1) {
      this.f = var1;
      return this;
   }

   public OperationLogQuery categoryIn(List var1) {
      this.h = var1;
      return this;
   }

   public OperationLogQuery actionIn(List var1) {
      this.i = var1;
      return this;
   }

   public OperationLogQuery dateBegin(Date var1) {
      this.j = var1;
      return this;
   }

   public OperationLogQuery dateEnd(Date var1) {
      this.k = var1;
      return this;
   }

   public OperationLogQuery userIdLike(String var1) {
      this.b = var1;
      return this;
   }

   public OperationLogQuery categoryLike(String var1) {
      this.g = var1;
      return this;
   }
}
