package com.bstek.urule.console.admin.repository;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.config.dialect.Dialect;
import com.bstek.urule.console.config.dialect.DialectResolver;
import com.bstek.urule.console.config.dialect.OrderLimitDialect;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.repository.DataSourceManager;
import com.bstek.urule.console.database.manager.repository.DataSourceQuery;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.datasource.DataSource;
import com.bstek.urule.console.database.model.datasource.Field;
import com.bstek.urule.console.database.model.datasource.FieldType;
import com.bstek.urule.console.database.service.repository.DataSourceHandlerManager;
import com.bstek.urule.console.database.service.repository.DataSourceService;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.database.util.NamedSQLUtils;
import com.bstek.urule.console.database.util.ParsedSql;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

public class DataSourceServletHandler extends ApiServletHandler {
   @Transactional
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("data");
      DataSource var4 = (DataSource)this.a().readValue(var3, DataSource.class);
      var4.setId(IDGenerator.getInstance().nextId(IDType.DATASOURCE));
      var4.setGroupId(ContextHolder.getGroupId());
      var4.setCreateUser(SecurityUtils.getLoginUsername(var1));
      var4.setCreateDate(new Date());
      DataSourceService.ins.add(var4);
   }

   @Transactional
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void update(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("data");
      DataSource var4 = (DataSource)this.a().readValue(var3, DataSource.class);
      var4.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      var4.setUpdateDate(new Date());
      DataSourceService.ins.update(var4);
   }

   @Transactional
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void delete(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      DataSourceService.ins.remove(var3);
   }

   public void page(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = ContextHolder.getGroupId();
      int var4 = Integer.parseInt(var1.getParameter("pageIndex"));
      int var5 = Integer.parseInt(var1.getParameter("pageSize"));
      Page var6 = new Page(var4, var5);
      DataSourceQuery var7 = DataSourceManager.ins.createQuery();
      String var8 = var1.getParameter("id");
      if (StringUtils.isNotBlank(var8)) {
         var7.id(Long.parseLong(var8));
      }

      String var9 = var1.getParameter("name");
      if (StringUtils.isNotBlank(var9)) {
         var7.nameLike(var9);
      }

      String var10 = var1.getParameter("type");
      if (StringUtils.isNotBlank(var10)) {
         var7.type(var10);
      }

      String var11 = var1.getParameter("createUser");
      if (StringUtils.isNotBlank(var11)) {
         var7.createUserLike(var11);
      }

      var7.groupId(var3).page(var6);
      this.a(var2, var6);
   }

   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void buildFields(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      String var5 = var1.getParameter("tableName");
      String var6 = "select * from " + var5;
      DataSource var7 = DataSourceManager.ins.get(var3);
      javax.sql.DataSource var8 = DataSourceHandlerManager.getDataSource(var7);
      Connection var9 = var8.getConnection();
      ArrayList var10 = new ArrayList();

      try {
         Dialect var11 = DialectResolver.resolveDialect(var9);
         String var12 = var11.getLimitString(var6, 0, 1);
         if (var11 instanceof OrderLimitDialect) {
            var12 = var6 + " limit 1";
         }

         ParsedSql var13 = NamedSQLUtils.parseSql(var12);
         String var14 = JdbcUtils.getOriginSql(var13.getOriginalSql());
         PreparedStatement var15 = var9.prepareStatement(var14);
         ResultSet var16 = var15.executeQuery();
         ResultSetMetaData var17 = var16.getMetaData();
         int var18 = var17.getColumnCount();

         for(int var19 = 0; var19 < var18; ++var19) {
            String var20 = var17.getColumnLabel(var19 + 1);
            if (StringUtils.isBlank(var20)) {
               var20 = var17.getColumnName(var19 + 1);
            }

            int var21 = var20.lastIndexOf(".");
            if (var21 > -1) {
               var20 = var20.substring(var21 + 1);
            }

            int var22 = var17.getColumnType(var19 + 1);
            FieldType var23 = JdbcUtils.buildJdbcFieldType(var22);
            var10.add(new Field(var20, var23));
         }

         JdbcUtils.closeResultSet(var16);
         JdbcUtils.closeStatement(var15);
      } catch (Exception var27) {
         var27.printStackTrace();
      } finally {
         this.a(var9);
      }

      this.a(var2, var10);
   }

   private void a(Connection var1) {
      try {
         if (var1 != null) {
            var1.close();
         }

      } catch (SQLException var3) {
         throw new RuleException(var3);
      }
   }

   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void testConnection(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      HashMap var3 = new HashMap();
      var3.put("result", true);
      Connection var4 = null;
      javax.sql.DataSource var5 = null;
      DataSource var6 = null;

      try {
         String var7 = var1.getParameter("data");
         var6 = (DataSource)this.a().readValue(var7, DataSource.class);
         var5 = DataSourceHandlerManager.newDataSource(var6);
         var4 = var5.getConnection();
      } catch (Exception var11) {
         var3.put("error", var11.toString());
         var3.put("stack", this.a(var11));
         var3.put("result", false);
      } finally {
         this.a(var4);
      }

      this.a(var2, var3);
   }

   public String url() {
      return "/datasource";
   }
}
