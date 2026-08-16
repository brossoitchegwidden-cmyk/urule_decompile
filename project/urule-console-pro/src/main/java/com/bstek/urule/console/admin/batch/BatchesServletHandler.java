package com.bstek.urule.console.admin.batch;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.admin.batch.in.BatchImport;
import com.bstek.urule.console.admin.batch.out.BatchExport;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.batch.SchemeService;
import com.bstek.urule.console.batch.exception.BatchException;
import com.bstek.urule.console.batch.utils.JsonUtils;
import com.bstek.urule.console.batch.utils.StmtUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketData;
import com.bstek.urule.console.config.dialect.Dialect;
import com.bstek.urule.console.config.dialect.DialectResolver;
import com.bstek.urule.console.config.dialect.OrderLimitDialect;
import com.bstek.urule.console.database.manager.batch.BatchManager;
import com.bstek.urule.console.database.manager.batch.BatchQuery;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.PacketQuery;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.manager.repository.DataSourceManager;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.console.database.model.datasource.Field;
import com.bstek.urule.console.database.model.datasource.FieldType;
import com.bstek.urule.console.database.service.repository.DataSourceHandlerManager;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.database.util.NamedSQLUtils;
import com.bstek.urule.console.database.util.ParsedSql;
import com.bstek.urule.console.editor.execute.JsonBuilder;
import com.bstek.urule.console.editor.execute.VariableCategoryNotFoundException;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.util.UploadFile;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BatchesServletHandler extends ApiServletHandler {
   public static final String URL = "/batches";
   private static final Log e = LogFactory.getLog(StmtUtils.class);

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("data");
      String var4 = SecurityUtils.getLoginUsername(var1);
      Batch var5 = (Batch)this.a().readValue(var3, Batch.class);
      SchemeService.ins.add(var5, var4);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void update(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("data");
      String var4 = SecurityUtils.getLoginUsername(var1);
      Batch var5 = (Batch)this.a().readValue(var3, Batch.class);
      SchemeService.ins.update(var5, var4);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void updateRestConfig(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("data");
      String var4 = SecurityUtils.getLoginUsername(var1);
      Batch var5 = (Batch)this.a().readValue(var3, Batch.class);
      Batch var6 = BatchManager.ins.get(var5.getId());
      var6.setRestEnable(var5.isRestEnable());
      var6.setRestSecurityEnable(var5.isRestSecurityEnable());
      var6.setRestSecurityUser(var5.getRestSecurityUser());
      var6.setRestSecurityPassword(var5.getRestSecurityPassword());
      var6.setUpdateUser(var4);
      var6.setUpdateDate(new Date(System.currentTimeMillis()));
      BatchManager.ins.update(var6);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void delete(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      SchemeService.ins.remove(var3);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void enable(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      SchemeService.ins.enable(var3, SecurityUtils.getLoginUsername(var1));
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void disable(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      SchemeService.ins.disable(var3, SecurityUtils.getLoginUsername(var1));
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void stop(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      SchemeService.ins.stop(var3, SecurityUtils.getLoginUsername(var1));
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void get(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      Batch var5 = SchemeService.ins.getBatchData(var3);
      this.a(var2, var5);
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void getProviderData(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("providerId"));
      BatchDataProvider var5 = SchemeService.ins.getProviderData(var3);
      this.a(var2, var5);
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void page(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      int var5 = Integer.parseInt(var1.getParameter("pageIndex"));
      int var6 = Integer.parseInt(var1.getParameter("pageSize"));
      Page var7 = new Page(var5, var6);
      BatchQuery var8 = BatchManager.ins.createQuery();
      String var9 = var1.getParameter("name");
      if (StringUtils.isNotBlank(var9)) {
         var8.nameLike(var9);
      }

      String var10 = var1.getParameter("async");
      if (StringUtils.isNotBlank(var10)) {
         var8.async(Boolean.valueOf(var10));
      }

      String var11 = var1.getParameter("enable");
      if (StringUtils.isNotBlank(var11)) {
         var8.enable(Boolean.valueOf(var11));
      }

      String var12 = var1.getParameter("status");
      if (StringUtils.isNotBlank(var12)) {
         var8.status(BatchStatus.valueOf(var12));
      }

      String var13 = var1.getParameter("id");
      if (StringUtils.isNotBlank(var13)) {
         var8.id(Long.valueOf(var13));
      }

      String var14 = var1.getParameter("packetId");
      if (StringUtils.isNotBlank(var14)) {
         var8.packetId(Long.valueOf(var14));
      }

      String var15 = var1.getParameter("createUser");
      if (StringUtils.isNotBlank(var15)) {
         var8.createUserLike(var15);
      }

      var8.projectId(var3).page(var7);
      this.a(var2, var7);
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void pagePacket(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = ContextHolder.getProjectId();
      int var5 = Integer.parseInt(var1.getParameter("pageIndex"));
      int var6 = Integer.parseInt(var1.getParameter("pageSize"));
      new Page(var5, var6);
      PacketQuery var8 = PacketManager.ins.newQuery();
      String var9 = var1.getParameter("name");
      if (StringUtils.isNotBlank(var9)) {
         var8.nameLike(var9);
      }

      Page var7 = var8.enable(true).projectId(var3).paging(var5, var6);
      this.a(var2, var7);
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildVariableCategorys(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      ArrayList var3 = new ArrayList();
      PacketData var4 = PacketCache.ins.getPacket(Long.valueOf(var1.getParameter("packetId")));
      if (var4 == null) {
         this.a(var2, var3);
      } else {
         for(VariableCategory var7 : var4.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories()) {
            var3.add(var7.getName());
         }

         this.a(var2, var3);
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildVariables(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      PacketData var3 = PacketCache.ins.getPacket(Long.valueOf(var1.getParameter("packetId")));
      if (var3 == null) {
         this.a(var2, new ArrayList());
      } else {
         List var4 = var3.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();

         try {
            VariableCategory var5 = JsonBuilder.getInstance().findVariableCategory(var4, var1.getParameter("varName"));
            this.a(var2, var5.getVariables());
         } catch (VariableCategoryNotFoundException var6) {
            throw new VariableCategoryNotFoundException(var6.getMessage() + "请确保当前版本的知识包中包含这个变量对象!");
         }
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildVariableTree(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      ArrayList var3 = new ArrayList();
      PacketData var4 = PacketCache.ins.getPacket(Long.valueOf(var1.getParameter("packetId")));
      if (var4 == null) {
         this.a(var2, var3);
      } else {
         List var5 = var4.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
         ArrayList var6 = new ArrayList();

         for(VariableCategory var8 : (Iterable<VariableCategory>)(Iterable<?>)(var5)) {
            if (var8.getAct() == Act.InOut || var8.getAct() == Act.Out) {
               var6.add(var8);
            }
         }

         this.a(var2, var6);
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildParams(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      PacketData var3 = PacketCache.ins.getPacket(Long.valueOf(var1.getParameter("packetId")));
      if (var3 == null) {
         throw new BatchException("知识包不存在!");
      } else {
         List var4 = var3.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
         VariableCategory var5 = null;

         for(VariableCategory var7 : (Iterable<VariableCategory>)(Iterable<?>)(var4)) {
            if ("参数".equals(var7.getName()) || "参数".equals(var7.getClazz())) {
               var5 = var7;
               break;
            }
         }

         if (var5 != null) {
            this.a(var2, var5.getVariables());
         } else {
            this.a(var2, new ArrayList());
         }

      }
   }

   private boolean a(String var1) {
      String var2 = var1.toLowerCase();
      String var3 = "exec|execute|insert|delete|update|count|create|drop|chr|mid|master|truncate|char|declare|sitename|net user|xp_cmdshell|exec|execute|table|grant|use|group_concat|column_name|information_schema.columns|table_schema|union|";
      List var4 = Arrays.asList(var3.split("\\|"));
      StringTokenizer var5 = new StringTokenizer(var2);

      while(var5.hasMoreElements()) {
         String var6 = var5.nextToken();
         if (var4.contains(var6)) {
            return true;
         }
      }

      return false;
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void getDatasourceExtInfo(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      HashMap var3 = new HashMap();
      Connection var4 = null;
      DataSource var5 = null;
      com.bstek.urule.console.database.model.datasource.DataSource var6 = null;

      try {
         String var7 = var1.getParameter("id");
         String var8 = var1.getParameter("isresolver");
         var6 = DataSourceManager.ins.get(Long.parseLong(var7));
         if (var6 == null) {
            if ("true".equalsIgnoreCase(var8)) {
               throw new Exception("DataResolver DataSource[" + var7 + "] is not defined!");
            }

            throw new Exception("DataProvider DataSource[" + var7 + "] is not defined!");
         }

         var5 = DataSourceHandlerManager.newDataSource(var6);
         var4 = var5.getConnection();
         Dialect var9 = DialectResolver.resolveDialect(var4);
         var3.put("result", true);
         var3.put("orderLimit", false);
         if (var9 instanceof OrderLimitDialect) {
            var3.put("orderLimit", true);
         }

         var3.put("hive", var9.getClass().getName().toLowerCase().indexOf("hive") > -1);
      } finally {
         this.a(var4);
      }

      this.a(var2, var3);
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
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildFields(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("sql");
      String var4 = var1.getParameter("tablename");
      if (StringUtils.isNotBlank(var3)) {
         var3 = URLDecoder.decode(var3, "utf-8");
         boolean var5 = this.a(var3);
         if (var5) {
            return;
         }
      } else if (StringUtils.isNotBlank(var4)) {
         var3 = "select * from " + var4;
      }

      if (!StringUtils.isBlank(var3)) {
         String var39 = var1.getParameter("datasourceId");
         if (!StringUtils.isBlank(var39)) {
            String var6 = var1.getParameter("inputParams");
            Object var7 = new ArrayList();
            if (StringUtils.isNotBlank(var6)) {
               var7 = (List)JsonUtils.getObjectJsonMapper().readValue(var6, new TypeReference() {
               });
            }

            ArrayList var8 = new ArrayList();
            com.bstek.urule.console.database.model.datasource.DataSource var9 = DataSourceManager.ins.get(Long.parseLong(var39));
            if (var9 == null) {
               this.a(var2, var8);
            } else {
               DataSource var10 = DataSourceHandlerManager.getDataSource(var9);
               Connection var11 = var10.getConnection();

               try {
                  Dialect var12 = DialectResolver.resolveDialect(var11);
                  String var13 = var12.getLimitString(var3, 0, 1);
                  if (var12 instanceof OrderLimitDialect) {
                     var13 = var3 + " limit 1";
                  }

                  ParsedSql var14 = NamedSQLUtils.parseSql(var13);
                  String var15 = JdbcUtils.getOriginSql(var14.getOriginalSql());
                  e.debug("builder fields sql:" + var15);
                  PreparedStatement var16 = var11.prepareStatement(var15);
                  if (StringUtils.isBlank(var4)) {
                     ArrayList var17 = new ArrayList();

                     for(String var19 : (Iterable<String>)(Iterable<?>)(var14.getParameterNames())) {
                        DataParam var20 = null;

                        for(DataParam var22 : (Iterable<DataParam>)(Iterable<?>)(var7)) {
                           if (var19.equalsIgnoreCase(var22.getName())) {
                              var20 = var22;
                              break;
                           }
                        }

                        DataParam var44 = new DataParam();
                        var44.setName(var19);
                        if (var20 != null) {
                           var44.setDataType(var20.getDataType());
                           String var46 = var44.getDataType();
                           if (!"String".equals(var46) && !"Char".equals(var46)) {
                              if ("Integer".equals(var46)) {
                                 var44.setValue(new Integer(0));
                              } else if ("Long".equals(var46)) {
                                 var44.setValue(new Long(0L));
                              } else if ("Float".equals(var46)) {
                                 var44.setValue(new Float(0.0F));
                              } else if ("Double".equals(var46)) {
                                 var44.setValue(new Double((double)0.0F));
                              } else if ("BigDecimal".equals(var46)) {
                                 var44.setValue(new BigDecimal(0));
                              } else if ("Boolean".equals(var46)) {
                                 var44.setValue(false);
                              } else if ("Date".equals(var46)) {
                                 var44.setValue(new Date());
                              } else if ("DateTime".equals(var46)) {
                                 var44.setValue(new Timestamp(System.currentTimeMillis()));
                              }
                           } else {
                              var44.setValue("");
                           }
                        }

                        var17.add(var44);
                     }

                     StmtUtils.setStmtQueryParameters(var14, var17, var16);
                  }

                  ResultSet var40 = var16.executeQuery();
                  ResultSetMetaData var41 = var40.getMetaData();
                  int var42 = var41.getColumnCount();

                  for(int var43 = 0; var43 < var42; ++var43) {
                     String var45 = var41.getColumnLabel(var43 + 1);
                     if (StringUtils.isBlank(var45)) {
                        var45 = var41.getColumnName(var43 + 1);
                     }

                     int var47 = var45.lastIndexOf(".");
                     if (var47 > -1) {
                        var45 = var45.substring(var47 + 1);
                     }

                     if (!"rownum_".equals(var45)) {
                        int var23 = var41.getColumnType(var43 + 1);
                        FieldType var24 = JdbcUtils.buildJdbcFieldType(var23);
                        var8.add(new Field(var45, var24));
                     }
                  }

                  JdbcUtils.closeResultSet(var40);
                  JdbcUtils.closeStatement(var16);
               } catch (Exception var37) {
                  var37.printStackTrace();
               } finally {
                  if (var11 != null) {
                     try {
                        var11.close();
                     } catch (SQLException var35) {
                        throw new RuleException(var35);
                     } catch (Throwable var36) {
                        throw new RuleException(var36.getMessage());
                     }
                  }

               }

               this.a(var2, var8);
            }
         }
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void doExport(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("type");
      ArrayList var4 = new ArrayList();
      SimpleDateFormat var5 = new SimpleDateFormat("yyyyMMddHHmmss");
      String var6 = "batchs";
      if ("all".equals(var3)) {
         for(Batch var9 : (Iterable<Batch>)(Iterable<?>)(BatchManager.ins.createQuery().projectId(ContextHolder.getProjectId()).list())) {
            Batch var10 = SchemeService.ins.getBatchData(var9.getId());
            var4.add(var10);
         }
      } else {
         String var16 = var1.getParameter("ids");
         if (StringUtils.isNotBlank(var16)) {
            String[] var18 = var16.split(",");
            if (var18.length == 1) {
               Batch var19 = SchemeService.ins.getBatchData(Long.valueOf(var18[0].trim()));
               var6 = var19.getName();
               var4.add(var19);
            } else {
               for(String var12 : var18) {
                  Batch var13 = SchemeService.ins.getBatchData(Long.valueOf(var12));
                  var4.add(var13);
               }
            }
         }
      }

      var6 = var6 + "-" + var5.format(new Date()) + ".batch.bak";
      var2.setContentType("application/octet-stream;charset=ISO8859-1");
      var6 = new String(var6.getBytes("UTF-8"), "ISO8859-1");
      var2.setHeader("Content-Disposition", "attachment;filename=\"" + var6 + "\"");
      ServletOutputStream var17 = var2.getOutputStream();
      BatchExport.ins.doExport((OutputStream)var17, var4);
      var17.flush();
      var17.close();
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void doImport(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = Long.parseLong(var1.getParameter("projectId"));
      Project var4 = ProjectManager.ins.get(var3);
      UploadFile var5 = FileUtils.uploadFile(var1);
      InputStream var6 = var5.getInputStream();
      BatchImport.ins.doImport(var6, var4);
      IOUtils.closeQuietly(var6);
   }

   public String url() {
      return "/batches";
   }
}
