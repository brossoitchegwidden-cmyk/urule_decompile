package com.bstek.urule.console.batch.reader;

import com.bstek.urule.Utils;
import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.filter.ReaderFilter;
import com.bstek.urule.console.batch.utils.JsonUtils;
import com.bstek.urule.console.batch.utils.StmtUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketData;
import com.bstek.urule.console.config.dialect.Dialect;
import com.bstek.urule.console.config.dialect.OrderLimitDialect;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.model.batch.BatchDataProviderField;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.console.database.model.batch.Filter;
import com.bstek.urule.console.database.model.batch.FilterItem;
import com.bstek.urule.console.database.model.batch.FilterType;
import com.bstek.urule.console.database.service.repository.DataSourceHandlerManager;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.database.util.NamedSQLUtils;
import com.bstek.urule.console.database.util.ParsedSql;
import com.bstek.urule.console.editor.execute.JsonBuilder;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultItemReader implements ItemReader {
   private static Log logger = LogFactory.getLog(DefaultItemReader.class);

   private void bindProviderParameters(BatchDataProvider batchDataProvider, Map valuesByKey) {
      for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(batchDataProvider.getParams())) {
         if (valuesByKey.containsKey(dataParam.getBatchParamName())) {
            dataParam.setValue(valuesByKey.get(dataParam.getBatchParamName()));
         }
      }

   }

   public List getPageDatas(Connection conn, BatchContext context, int pageIndex, int pageSize) throws ReaderException {
      try {
         Batch batch = context.getBatch();
         BatchDataProvider dataProvider = batch.getDataProvider();
         Dialect dialect = dataProvider.getDialect();
         String pageSql = dataProvider.getPageSql();
         if (pageIndex >= 0) {
            String orderField = dataProvider.getOrderField();
            if (dialect instanceof OrderLimitDialect && StringUtils.isNotBlank(orderField)) {
               OrderLimitDialect orderLimitDialect = (OrderLimitDialect)dialect;
               String pageLimitType = dataProvider.getPageLimitType();
               if (!StringUtils.isBlank(pageLimitType) && !"between".equals(pageLimitType)) {
                  GeneralEntity hiveLastData = context.getHiveLastData();
                  String orderFieldParamName = dataProvider.getOrderFieldParamName();
                  if (!batch.isThreadMulti() && pageIndex > 1 && hiveLastData != null && StringUtils.isNotBlank(orderFieldParamName)) {
                     Object objectValue = hiveLastData.get(orderField);

                     for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(dataProvider.getParams())) {
                        if (orderFieldParamName.equalsIgnoreCase(dataParam.getName())) {
                           dataParam.setValue(objectValue);
                        }
                     }
                  }

                  pageSql = orderLimitDialect.getLimitString(dataProvider.getPageSql(), pageSize);
               } else {
                  pageSql = orderLimitDialect.getLimitString(dataProvider.getPageSql(), pageIndex * pageSize, pageSize, orderField);
               }
            } else {
               pageSql = dialect.getLimitString(dataProvider.getPageSql(), pageIndex * pageSize, pageSize);
            }
         }

         List pageDatas = this.queryPageData(conn, context, pageSql, true);
         return pageDatas;
      } catch (Exception exception) {
         ReaderException readerException = new ReaderException(exception.getMessage(), exception);
         readerException.setPageIndex(pageIndex);
         throw readerException;
      }
   }

   protected boolean matchesReaderFilter(BatchContext batchContext, BatchDataProvider batchDataProvider, Map valuesByKey) {
      for(Filter filter : (Iterable<Filter>)(Iterable<?>)(batchDataProvider.getFilters())) {
         for(FilterItem filterItem : (Iterable<FilterItem>)(Iterable<?>)(filter.getItems())) {
            if (filterItem.getType() == FilterType.bean) {
               ReaderFilter readerFilter = (ReaderFilter)Utils.getApplicationContext().getBean(filterItem.getValue());
               boolean flag = readerFilter.filter(batchContext, batchDataProvider, valuesByKey);
               if (flag) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private List queryPageData(Connection connection, BatchContext batchContext, String text, boolean flag) throws Exception {
      Batch batch = batchContext.getBatch();
      BatchDataProvider dataProvider = batch.getDataProvider();
      ArrayList items = new ArrayList();

      try {
         ParsedSql sql = NamedSQLUtils.parseSql(text);
         String originSql = JdbcUtils.getOriginSql(sql.getOriginalSql());
         PreparedStatement preparedStatement = connection.prepareStatement(originSql);
         StmtUtils.setStmtQueryParameters(sql, dataProvider.getParams(), preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         PacketData packet = PacketCache.ins.getPacket(Long.valueOf(batch.getPacketId()));
         List variableCategories = packet.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
         VariableCategory variableCategory = JsonBuilder.getInstance().findVariableCategory(variableCategories, dataProvider.getPacketVarName());
         List fields = dataProvider.getFields();

         while(resultSet.next()) {
            GeneralEntity generalEntity = null;
            if (dataProvider.getPacketVarName().equals("参数")) {
               generalEntity = new GeneralEntity();
            } else {
               generalEntity = new GeneralEntity(variableCategory.getClazz());
            }

            for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(fields)) {
               if ("Boolean".equals(batchDataProviderField.getDataType())) {
                  generalEntity.put(batchDataProviderField.getDestProperty(), resultSet.getBoolean(batchDataProviderField.getSrcProperty()));
               } else if ("Object".equals(batchDataProviderField.getDataType())) {
                  if (batchDataProviderField.getDataProvider() != null) {
                     BatchDataProvider dataProvider2 = batchDataProviderField.getDataProvider();
                     this.bindProviderParameters(dataProvider2, batchContext.getParamValueMap());
                     this.bindProviderParameters(dataProvider2, (Map)generalEntity);
                     List items2 = this.queryNestedData(connection, dataProvider2, variableCategories);
                     if (items2.size() > 0) {
                        generalEntity.put(batchDataProviderField.getDestProperty(), items2.get(0));
                     }
                  }

                  generalEntity.put(batchDataProviderField.getDestProperty(), resultSet.getObject(batchDataProviderField.getSrcProperty()));
               } else if ("List".equals(batchDataProviderField.getDataType())) {
                  if (batchDataProviderField.getDataProvider() != null) {
                     BatchDataProvider dataProvider3 = batchDataProviderField.getDataProvider();
                     this.bindProviderParameters(dataProvider3, batchContext.getParamValueMap());
                     this.bindProviderParameters(dataProvider3, (Map)generalEntity);
                     List items3 = this.queryNestedData(connection, dataProvider3, variableCategories);
                     generalEntity.put(batchDataProviderField.getDestProperty(), items3);
                  }
               } else if ("JsonObject".equals(batchDataProviderField.getDataType())) {
                  String string = resultSet.getString(batchDataProviderField.getSrcProperty());
                  Map valuesByKey = (Map)JsonUtils.getObjectJsonMapper().readValue(string, HashMap.class);
                  if (batchDataProviderField.getDataProvider() != null) {
                     BatchDataProvider dataProvider4 = batchDataProviderField.getDataProvider();
                     VariableCategory variableCategory2 = JsonBuilder.getInstance().findVariableCategory(variableCategories, dataProvider4.getPacketVarName());
                     GeneralEntity generalEntity2 = this.createGeneralEntity(dataProvider4, variableCategory2);

                     for(BatchDataProviderField batchDataProviderField2 : (Iterable<BatchDataProviderField>)(Iterable<?>)(dataProvider4.getFields())) {
                        if (valuesByKey.containsKey(batchDataProviderField2.getSrcProperty())) {
                           generalEntity2.put(batchDataProviderField2.getDestProperty(), valuesByKey.get(batchDataProviderField2.getSrcProperty()));
                        }
                     }

                     generalEntity.put(batchDataProviderField.getDestProperty(), generalEntity2);
                  }
               } else if (!"JsonArray".equals(batchDataProviderField.getDataType())) {
                  generalEntity.put(batchDataProviderField.getDestProperty(), resultSet.getObject(batchDataProviderField.getSrcProperty()));
               } else {
                  String string2 = resultSet.getString(batchDataProviderField.getSrcProperty());
                  List items4 = (List)JsonUtils.getObjectJsonMapper().readValue(string2, new TypeReference() {
                  });
                  ArrayList items5 = new ArrayList();

                  for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)(items4)) {
                     if (batchDataProviderField.getDataProvider() != null) {
                        BatchDataProvider dataProvider5 = batchDataProviderField.getDataProvider();
                        VariableCategory variableCategory3 = JsonBuilder.getInstance().findVariableCategory(variableCategories, dataProvider5.getPacketVarName());
                        GeneralEntity generalEntity3 = this.createGeneralEntity(dataProvider5, variableCategory3);

                        for(BatchDataProviderField batchDataProviderField3 : (Iterable<BatchDataProviderField>)(Iterable<?>)(dataProvider5.getFields())) {
                           if (valuesByKey2.containsKey(batchDataProviderField3.getSrcProperty())) {
                              generalEntity3.put(batchDataProviderField3.getDestProperty(), valuesByKey2.get(batchDataProviderField3.getSrcProperty()));
                           }
                        }

                        items5.add(generalEntity3);
                     }
                  }

                  generalEntity.put(batchDataProviderField.getDestProperty(), items5);
               }
            }

            if (!batch.isThreadMulti() && flag) {
               batchContext.setHiveLastData(generalEntity);
            }

            boolean flag2 = this.matchesReaderFilter((BatchContext)batchContext, dataProvider, (Map)generalEntity);
            if (!flag2) {
               items.add(generalEntity);
            }
         }

         resultSet.close();
         preparedStatement.close();
      } catch (Exception exception) {
         DefaultItemReader.logger.error(exception);
         java.util.logging.Logger.getLogger(DefaultItemReader.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
      }

      return items;
   }

   private GeneralEntity createGeneralEntity(BatchDataProvider batchDataProvider, VariableCategory variableCategory) {
      GeneralEntity generalEntity = null;
      if (batchDataProvider.getPacketVarName().equals("参数")) {
         generalEntity = new GeneralEntity();
      } else {
         generalEntity = new GeneralEntity(variableCategory.getClazz());
      }

      return generalEntity;
   }

   private List queryNestedData(Connection connection, BatchDataProvider batchDataProvider, List items) throws Exception {
      VariableCategory variableCategory = JsonBuilder.getInstance().findVariableCategory(items, batchDataProvider.getPacketVarName());
      Object objectValue = null;
      ArrayList items2 = new ArrayList();

      try {
         ParsedSql sql = NamedSQLUtils.parseSql(batchDataProvider.getPageSql());
         String originSql = JdbcUtils.getOriginSql(sql.getOriginalSql());
         PreparedStatement preparedStatement = connection.prepareStatement(originSql);
         StmtUtils.setStmtQueryParameters(sql, batchDataProvider.getParams(), preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List fields = batchDataProvider.getFields();

         while(resultSet.next()) {
            GeneralEntity generalEntity = this.createGeneralEntity(batchDataProvider, variableCategory);

            for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(fields)) {
               if ("Boolean".equals(batchDataProviderField.getDataType())) {
                  generalEntity.put(batchDataProviderField.getDestProperty(), resultSet.getBoolean(batchDataProviderField.getSrcProperty()));
               } else if (!"Object".equals(batchDataProviderField.getDataType()) && !"List".equals(batchDataProviderField.getDataType()) && !"JsonObject".equals(batchDataProviderField.getDataType()) && !"JsonArray".equals(batchDataProviderField.getDataType())) {
                  generalEntity.put(batchDataProviderField.getDestProperty(), resultSet.getObject(batchDataProviderField.getSrcProperty()));
               }
            }

            items2.add(generalEntity);
         }

         resultSet.close();
         preparedStatement.close();
      } catch (Exception exception) {
         DefaultItemReader.logger.error(exception);
         java.util.logging.Logger.getLogger(DefaultItemReader.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
      }

      return items2;
   }

   public List getDatas(Connection conn, BatchContext context) throws ReaderException {
      try {
         Batch batch = context.getBatch();
         BatchDataProvider dataProvider = batch.getDataProvider();
         String pageSql = dataProvider.getPageSql();
         return this.queryPageData(conn, context, pageSql, false);
      } catch (Exception exception) {
         ReaderException readerException = new ReaderException(exception.getMessage(), exception);
         throw readerException;
      }
   }

   public int getTotleRows(BatchDataProvider dataProvider) throws Exception {
      int totleRows = 0;
      List params = dataProvider.getParams();
      DataSource dataSource = null;
      Connection connection = null;

      try {
         dataSource = DataSourceHandlerManager.getDataSource(dataProvider.getDatasource());
         connection = dataSource.getConnection();
         ParsedSql sql = NamedSQLUtils.parseSql(dataProvider.getCountSql());
         String originSql = JdbcUtils.getOriginSql(sql.getOriginalSql());
         PreparedStatement preparedStatement = connection.prepareStatement(originSql);
         StmtUtils.setStmtQueryParameters(sql, params, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            totleRows = resultSet.getInt(1);
         }

         resultSet.close();
         preparedStatement.close();
         connection.close();
         return totleRows;
      } catch (Exception exception) {
         DefaultItemReader.logger.error(exception);
         java.util.logging.Logger.getLogger(DefaultItemReader.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
         try {
            if (connection != null) {
               connection.close();
            }
         } catch (Exception exception2) {
            java.util.logging.Logger.getLogger(DefaultItemReader.class.getName()).log(java.util.logging.Level.SEVERE, exception2.getMessage(), exception2);
         }

         throw exception;
      }
   }
}
