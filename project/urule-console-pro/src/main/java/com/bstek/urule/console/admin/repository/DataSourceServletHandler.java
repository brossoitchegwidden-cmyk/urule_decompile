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
   /**新增数据仓库*/
   @Transactional
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("data");
      DataSource dataSource = (DataSource)this.createObjectMapper().readValue(parameter, DataSource.class);
      dataSource.setId(IDGenerator.getInstance().nextId(IDType.DATASOURCE));
      dataSource.setGroupId(ContextHolder.getGroupId());
      dataSource.setCreateUser(SecurityUtils.getLoginUsername(req));
      dataSource.setCreateDate(new Date());
      DataSourceService.ins.add(dataSource);
   }

   /**更新数据仓库*/
   @Transactional
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("data");
      DataSource dataSource = (DataSource)this.createObjectMapper().readValue(parameter, DataSource.class);
      dataSource.setUpdateUser(SecurityUtils.getLoginUsername(req));
      dataSource.setUpdateDate(new Date());
      DataSourceService.ins.update(dataSource);
   }

   /**删除数据仓库*/
   @Transactional
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void delete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      DataSourceService.ins.remove(longValue);
   }

   /**查询所有数据仓库*/
   public void page(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String groupId = ContextHolder.getGroupId();
      int number = Integer.parseInt(req.getParameter("pageIndex"));
      int number2 = Integer.parseInt(req.getParameter("pageSize"));
      Page page = new Page(number, number2);
      DataSourceQuery query = DataSourceManager.ins.createQuery();
      String parameter = req.getParameter("id");
      if (StringUtils.isNotBlank(parameter)) {
         query.id(Long.parseLong(parameter));
      }

      String parameter2 = req.getParameter("name");
      if (StringUtils.isNotBlank(parameter2)) {
         query.nameLike(parameter2);
      }

      String parameter3 = req.getParameter("type");
      if (StringUtils.isNotBlank(parameter3)) {
         query.type(parameter3);
      }

      String parameter4 = req.getParameter("createUser");
      if (StringUtils.isNotBlank(parameter4)) {
         query.createUserLike(parameter4);
      }

      query.groupId(groupId).page(page);
      this.writeObjectToJson(resp, page);
   }

   /**查询数据仓库字段*/
   @URuleAuthorization(
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void buildFields(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      String parameter = req.getParameter("tableName");
      String text = "select * from " + parameter;
      DataSource dataSource = DataSourceManager.ins.get(longValue);
      javax.sql.DataSource dataSource2 = DataSourceHandlerManager.getDataSource(dataSource);
      Connection connection = dataSource2.getConnection();
      ArrayList items = new ArrayList();

      try {
         Dialect dialect = DialectResolver.resolveDialect(connection);
         String limitString = dialect.getLimitString(text, 0, 1);
         if (dialect instanceof OrderLimitDialect) {
            limitString = text + " limit 1";
         }

         ParsedSql sql = NamedSQLUtils.parseSql(limitString);
         String originSql = JdbcUtils.getOriginSql(sql.getOriginalSql());
         PreparedStatement preparedStatement = connection.prepareStatement(originSql);
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

            int columnType = metaData.getColumnType(index + 1);
            FieldType jdbcFieldType = JdbcUtils.buildJdbcFieldType(columnType);
            items.add(new Field(columnLabel, jdbcFieldType));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         java.util.logging.Logger.getLogger(DataSourceServletHandler.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
      } finally {
         this.closeConnection(connection);
      }

      this.writeObjectToJson(resp, items);
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
      authType = "group",
      code = "manager",
      model = "datasource"
   )
   public void testConnection(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("result", true);
      Connection connection = null;
      javax.sql.DataSource dataSource = null;
      DataSource dataSource2 = null;

      try {
         String parameter = req.getParameter("data");
         dataSource2 = (DataSource)this.createObjectMapper().readValue(parameter, DataSource.class);
         dataSource = DataSourceHandlerManager.newDataSource(dataSource2);
         connection = dataSource.getConnection();
      } catch (Exception exception) {
         valuesByKey.put("error", exception.toString());
         valuesByKey.put("stack", this.buildExceptionStack(exception));
         valuesByKey.put("result", false);
      } finally {
         this.closeConnection(connection);
      }

      this.writeObjectToJson(resp, valuesByKey);
   }

   public String url() {
      return "/datasource";
   }
}
