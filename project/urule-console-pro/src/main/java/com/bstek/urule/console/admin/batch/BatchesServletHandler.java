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
   private static final Log logger = LogFactory.getLog(StmtUtils.class);

   /**新增方案*/
   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("data");
      String loginUsername = SecurityUtils.getLoginUsername(req);
      Batch batch = (Batch)this.createObjectMapper().readValue(parameter, Batch.class);
      SchemeService.ins.add(batch, loginUsername);
   }

   /**更新方案*/
   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("data");
      String loginUsername = SecurityUtils.getLoginUsername(req);
      Batch batch = (Batch)this.createObjectMapper().readValue(parameter, Batch.class);
      SchemeService.ins.update(batch, loginUsername);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void updateRestConfig(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("data");
      String loginUsername = SecurityUtils.getLoginUsername(req);
      Batch batch = (Batch)this.createObjectMapper().readValue(parameter, Batch.class);
      Batch batch2 = BatchManager.ins.get(batch.getId());
      batch2.setRestEnable(batch.isRestEnable());
      batch2.setRestSecurityEnable(batch.isRestSecurityEnable());
      batch2.setRestSecurityUser(batch.getRestSecurityUser());
      batch2.setRestSecurityPassword(batch.getRestSecurityPassword());
      batch2.setUpdateUser(loginUsername);
      batch2.setUpdateDate(new Date(System.currentTimeMillis()));
      BatchManager.ins.update(batch2);
   }

   /**删除方案*/
   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void delete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      SchemeService.ins.remove(longValue);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void enable(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      SchemeService.ins.enable(longValue, SecurityUtils.getLoginUsername(req));
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void disable(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      SchemeService.ins.disable(longValue, SecurityUtils.getLoginUsername(req));
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void stop(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      SchemeService.ins.stop(longValue, SecurityUtils.getLoginUsername(req));
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      Batch batchData = SchemeService.ins.getBatchData(longValue);
      this.writeObjectToJson(resp, batchData);
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void getProviderData(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("providerId"));
      BatchDataProvider providerData = SchemeService.ins.getProviderData(longValue);
      this.writeObjectToJson(resp, providerData);
   }

   /**查询所有方案*/
   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void page(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
      int number = Integer.parseInt(req.getParameter("pageIndex"));
      int number2 = Integer.parseInt(req.getParameter("pageSize"));
      Page page = new Page(number, number2);
      BatchQuery query = BatchManager.ins.createQuery();
      String parameter = req.getParameter("name");
      if (StringUtils.isNotBlank(parameter)) {
         query.nameLike(parameter);
      }

      String parameter2 = req.getParameter("async");
      if (StringUtils.isNotBlank(parameter2)) {
         query.async(Boolean.valueOf(parameter2));
      }

      String parameter3 = req.getParameter("enable");
      if (StringUtils.isNotBlank(parameter3)) {
         query.enable(Boolean.valueOf(parameter3));
      }

      String parameter4 = req.getParameter("status");
      if (StringUtils.isNotBlank(parameter4)) {
         query.status(BatchStatus.valueOf(parameter4));
      }

      String parameter5 = req.getParameter("id");
      if (StringUtils.isNotBlank(parameter5)) {
         query.id(Long.valueOf(parameter5));
      }

      String parameter6 = req.getParameter("packetId");
      if (StringUtils.isNotBlank(parameter6)) {
         query.packetId(Long.valueOf(parameter6));
      }

      String parameter7 = req.getParameter("createUser");
      if (StringUtils.isNotBlank(parameter7)) {
         query.createUserLike(parameter7);
      }

      query.projectId(projectId).page(page);
      this.writeObjectToJson(resp, page);
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void pagePacket(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long projectId = ContextHolder.getProjectId();
      int number = Integer.parseInt(req.getParameter("pageIndex"));
      int number2 = Integer.parseInt(req.getParameter("pageSize"));
      new Page(number, number2);
      PacketQuery packetQuery = PacketManager.ins.newQuery();
      String parameter = req.getParameter("name");
      if (StringUtils.isNotBlank(parameter)) {
         packetQuery.nameLike(parameter);
      }

      Page page = packetQuery.enable(true).projectId(projectId).paging(number, number2);
      this.writeObjectToJson(resp, page);
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildVariableCategorys(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      ArrayList items = new ArrayList();
      PacketData packet = PacketCache.ins.getPacket(Long.valueOf(req.getParameter("packetId")));
      if (packet == null) {
         this.writeObjectToJson(resp, items);
      } else {
         for(VariableCategory variableCategory : packet.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories()) {
            items.add(variableCategory.getName());
         }

         this.writeObjectToJson(resp, items);
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildVariables(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      PacketData packet = PacketCache.ins.getPacket(Long.valueOf(req.getParameter("packetId")));
      if (packet == null) {
         this.writeObjectToJson(resp, new ArrayList());
      } else {
         List variableCategories = packet.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();

         try {
            VariableCategory variableCategory = JsonBuilder.getInstance().findVariableCategory(variableCategories, req.getParameter("varName"));
            this.writeObjectToJson(resp, variableCategory.getVariables());
         } catch (VariableCategoryNotFoundException variableCategoryNotFoundException) {
            throw new VariableCategoryNotFoundException(variableCategoryNotFoundException.getMessage() + "请确保当前版本的知识包中包含这个变量对象!");
         }
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildVariableTree(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      ArrayList items = new ArrayList();
      PacketData packet = PacketCache.ins.getPacket(Long.valueOf(req.getParameter("packetId")));
      if (packet == null) {
         this.writeObjectToJson(resp, items);
      } else {
         List variableCategories = packet.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
         ArrayList items2 = new ArrayList();

         for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableCategories)) {
            if (variableCategory.getAct() == Act.InOut || variableCategory.getAct() == Act.Out) {
               items2.add(variableCategory);
            }
         }

         this.writeObjectToJson(resp, items2);
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildParams(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      PacketData packet = PacketCache.ins.getPacket(Long.valueOf(req.getParameter("packetId")));
      if (packet == null) {
         throw new BatchException("知识包不存在!");
      } else {
         List variableCategories = packet.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
         VariableCategory variableCategory = null;

         for(VariableCategory variableCategory2 : (Iterable<VariableCategory>)(Iterable<?>)(variableCategories)) {
            if ("参数".equals(variableCategory2.getName()) || "参数".equals(variableCategory2.getClazz())) {
               variableCategory = variableCategory2;
               break;
            }
         }

         if (variableCategory != null) {
            this.writeObjectToJson(resp, variableCategory.getVariables());
         } else {
            this.writeObjectToJson(resp, new ArrayList());
         }

      }
   }

   private boolean containsUnsafeSqlKeyword(String text) {
      String lowercaseText = text.toLowerCase();
      String text2 = "exec|execute|insert|delete|update|count|create|drop|chr|mid|master|truncate|char|declare|sitename|net user|xp_cmdshell|exec|execute|table|grant|use|group_concat|column_name|information_schema.columns|table_schema|union|";
      List items = Arrays.asList(text2.split("\\|"));
      StringTokenizer stringTokenizer = new StringTokenizer(lowercaseText);

      while(stringTokenizer.hasMoreElements()) {
         String text3 = stringTokenizer.nextToken();
         if (items.contains(text3)) {
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
   public void getDatasourceExtInfo(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      HashMap valuesByKey = new HashMap();
      Connection connection = null;
      DataSource dataSource = null;
      com.bstek.urule.console.database.model.datasource.DataSource dataSource2 = null;

      try {
         String parameter = req.getParameter("id");
         String parameter2 = req.getParameter("isresolver");
         dataSource2 = DataSourceManager.ins.get(Long.parseLong(parameter));
         if (dataSource2 == null) {
            if ("true".equalsIgnoreCase(parameter2)) {
               throw new Exception("DataResolver DataSource[" + parameter + "] is not defined!");
            }

            throw new Exception("DataProvider DataSource[" + parameter + "] is not defined!");
         }

         dataSource = DataSourceHandlerManager.newDataSource(dataSource2);
         connection = dataSource.getConnection();
         Dialect dialect = DialectResolver.resolveDialect(connection);
         valuesByKey.put("result", true);
         valuesByKey.put("orderLimit", false);
         if (dialect instanceof OrderLimitDialect) {
            valuesByKey.put("orderLimit", true);
         }

         valuesByKey.put("hive", dialect.getClass().getName().toLowerCase().indexOf("hive") > -1);
      } finally {
         this.closeConnection(connection);
      }

      this.writeObjectToJson(resp, valuesByKey);
   }

   private void closeConnection(Connection connection) {
      try {
         if (connection != null) {
            connection.close();
         }

      } catch (SQLException sQLException) {
         throw new RuleException(sQLException);
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "view",
      model = "batches"
   )
   public void buildFields(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("sql");
      String parameter2 = req.getParameter("tablename");
      if (StringUtils.isNotBlank(parameter)) {
         parameter = URLDecoder.decode(parameter, "utf-8");
         boolean flag = this.containsUnsafeSqlKeyword(parameter);
         if (flag) {
            return;
         }
      } else if (StringUtils.isNotBlank(parameter2)) {
         parameter = "select * from " + parameter2;
      }

      if (!StringUtils.isBlank(parameter)) {
         String parameter3 = req.getParameter("datasourceId");
         if (!StringUtils.isBlank(parameter3)) {
            String parameter4 = req.getParameter("inputParams");
            Object objectValue = new ArrayList();
            if (StringUtils.isNotBlank(parameter4)) {
               objectValue = (List)JsonUtils.getObjectJsonMapper().readValue(parameter4, new TypeReference() {
               });
            }

            ArrayList items = new ArrayList();
            com.bstek.urule.console.database.model.datasource.DataSource dataSource = DataSourceManager.ins.get(Long.parseLong(parameter3));
            if (dataSource == null) {
               this.writeObjectToJson(resp, items);
            } else {
               DataSource dataSource2 = DataSourceHandlerManager.getDataSource(dataSource);
               Connection connection = dataSource2.getConnection();

               try {
                  Dialect dialect = DialectResolver.resolveDialect(connection);
                  String limitString = dialect.getLimitString(parameter, 0, 1);
                  if (dialect instanceof OrderLimitDialect) {
                     limitString = parameter + " limit 1";
                  }

                  ParsedSql sql = NamedSQLUtils.parseSql(limitString);
                  String originSql = JdbcUtils.getOriginSql(sql.getOriginalSql());
                  BatchesServletHandler.logger.debug("builder fields sql:" + originSql);
                  PreparedStatement preparedStatement = connection.prepareStatement(originSql);
                  if (StringUtils.isBlank(parameter2)) {
                     ArrayList items2 = new ArrayList();

                     for(String text : (Iterable<String>)(Iterable<?>)(sql.getParameterNames())) {
                        DataParam dataParam = null;

                        for(DataParam dataParam2 : (Iterable<DataParam>)(Iterable<?>)(objectValue)) {
                           if (text.equalsIgnoreCase(dataParam2.getName())) {
                              dataParam = dataParam2;
                              break;
                           }
                        }

                        DataParam dataParam3 = new DataParam();
                        dataParam3.setName(text);
                        if (dataParam != null) {
                           dataParam3.setDataType(dataParam.getDataType());
                           String dataType = dataParam3.getDataType();
                           if (!"String".equals(dataType) && !"Char".equals(dataType)) {
                              if ("Integer".equals(dataType)) {
                                 dataParam3.setValue(new Integer(0));
                              } else if ("Long".equals(dataType)) {
                                 dataParam3.setValue(new Long(0L));
                              } else if ("Float".equals(dataType)) {
                                 dataParam3.setValue(new Float(0.0F));
                              } else if ("Double".equals(dataType)) {
                                 dataParam3.setValue(new Double((double)0.0F));
                              } else if ("BigDecimal".equals(dataType)) {
                                 dataParam3.setValue(new BigDecimal(0));
                              } else if ("Boolean".equals(dataType)) {
                                 dataParam3.setValue(false);
                              } else if ("Date".equals(dataType)) {
                                 dataParam3.setValue(new Date());
                              } else if ("DateTime".equals(dataType)) {
                                 dataParam3.setValue(new Timestamp(System.currentTimeMillis()));
                              }
                           } else {
                              dataParam3.setValue("");
                           }
                        }

                        items2.add(dataParam3);
                     }

                     StmtUtils.setStmtQueryParameters(sql, items2, preparedStatement);
                  }

                  ResultSet resultSet = preparedStatement.executeQuery();
                  ResultSetMetaData metaData = resultSet.getMetaData();
                  int columnCount = metaData.getColumnCount();

                  for(int index = 0; index < columnCount; ++index) {
                     String columnLabel = metaData.getColumnLabel(index + 1);
                     if (StringUtils.isBlank(columnLabel)) {
                        columnLabel = metaData.getColumnName(index + 1);
                     }

                     int number = columnLabel.lastIndexOf(".");
                     if (number > -1) {
                        columnLabel = columnLabel.substring(number + 1);
                     }

                     if (!"rownum_".equals(columnLabel)) {
                        int columnType = metaData.getColumnType(index + 1);
                        FieldType jdbcFieldType = JdbcUtils.buildJdbcFieldType(columnType);
                        items.add(new Field(columnLabel, jdbcFieldType));
                     }
                  }

                  JdbcUtils.closeResultSet(resultSet);
                  JdbcUtils.closeStatement(preparedStatement);
               } catch (Exception exception) {
                  java.util.logging.Logger.getLogger(BatchesServletHandler.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
               } finally {
                  if (connection != null) {
                     try {
                        connection.close();
                     } catch (SQLException sQLException) {
                        throw new RuleException(sQLException);
                     } catch (Throwable throwable) {
                        throw new RuleException(throwable.getMessage());
                     }
                  }

               }

               this.writeObjectToJson(resp, items);
            }
         }
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void doExport(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("type");
      ArrayList items = new ArrayList();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      String text = "batchs";
      if ("all".equals(parameter)) {
         for(Batch batch : (Iterable<Batch>)(Iterable<?>)(BatchManager.ins.createQuery().projectId(ContextHolder.getProjectId()).list())) {
            Batch batchData = SchemeService.ins.getBatchData(batch.getId());
            items.add(batchData);
         }
      } else {
         String parameter2 = req.getParameter("ids");
         if (StringUtils.isNotBlank(parameter2)) {
            String[] parts = parameter2.split(",");
            if (parts.length == 1) {
               Batch batchData2 = SchemeService.ins.getBatchData(Long.valueOf(parts[0].trim()));
               text = batchData2.getName();
               items.add(batchData2);
            } else {
               for(String text2 : parts) {
                  Batch batchData3 = SchemeService.ins.getBatchData(Long.valueOf(text2));
                  items.add(batchData3);
               }
            }
         }
      }

      text = text + "-" + simpleDateFormat.format(new Date()) + ".batch.bak";
      resp.setContentType("application/octet-stream;charset=ISO8859-1");
      text = new String(text.getBytes("UTF-8"), "ISO8859-1");
      resp.setHeader("Content-Disposition", "attachment;filename=\"" + text + "\"");
      ServletOutputStream outputStream = resp.getOutputStream();
      BatchExport.ins.doExport((OutputStream)outputStream, items);
      outputStream.flush();
      outputStream.close();
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "batches"
   )
   public void doImport(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long longValue = Long.parseLong(req.getParameter("projectId"));
      Project project = ProjectManager.ins.get(longValue);
      UploadFile uploadFile = FileUtils.uploadFile(req);
      InputStream inputStream = uploadFile.getInputStream();
      BatchImport.ins.doImport(inputStream, project);
      IOUtils.closeQuietly(inputStream);
   }

   public String url() {
      return "/batches";
   }
}
