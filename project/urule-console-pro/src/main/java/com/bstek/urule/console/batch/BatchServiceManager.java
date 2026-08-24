package com.bstek.urule.console.batch;

import com.bstek.urule.Utils;
import com.bstek.urule.console.batch.utils.StmtUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketConfig;
import com.bstek.urule.console.cache.packet.PacketData;
import com.bstek.urule.console.config.dialect.Dialect;
import com.bstek.urule.console.config.dialect.DialectResolver;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.model.batch.BatchDataProviderField;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItemField;
import com.bstek.urule.console.database.model.batch.BatchLog;
import com.bstek.urule.console.database.model.batch.BatchUpdateMode;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.console.database.model.batch.TranScope;
import com.bstek.urule.console.database.service.repository.DataSourceHandlerManager;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.database.util.NamedSQLUtils;
import com.bstek.urule.console.database.util.ParsedSql;
import com.bstek.urule.console.editor.execute.JsonBuilder;
import com.bstek.urule.console.editor.execute.SubObject;
import com.bstek.urule.console.editor.execute.VariableCategoryNotFoundException;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.service.KnowledgeService;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BatchServiceManager {
   public static final Log logger = LogFactory.getLog(BatchServiceManager.class);
   private static final String DOT = ".";
   private static final String PARAMETER = "parameter";

   private static void failBatch(BatchResult batchResult, String text) {
      batchResult.setStatus(BatchStatus.failed);
      batchResult.setMsg(text);
   }

   private static boolean validateKnowledgePackage(BatchResult batchResult, Batch batch) throws ServletException, IOException {
      Long packetId = batch.getPacketId();
      PacketData packetData = null;

      try {
         packetData = PacketCache.ins.getPacket(packetId);
      } catch (Throwable throwable) {
         failBatch(batchResult, "知识包【" + packetId + "】提取失败:" + throwable.getMessage());
         return false;
      }

      if (packetData == null) {
         failBatch(batchResult, "知识包【" + packetId + "】不存在或未发布");
         return false;
      } else {
         PacketConfig packet = packetData.getPacket();
         if (!packet.isEnable()) {
            failBatch(batchResult, "知识包【" + packetId + "】已停用");
            return false;
         } else {
            return true;
         }
      }
   }

   private static boolean validateBatchEnabled(BatchResult batchResult, Batch batch) throws ServletException, IOException {
      Long id = batch.getId();
      if (!batch.isEnable()) {
         failBatch(batchResult, "批处理【" + id + "】已停用");
         return false;
      } else {
         return true;
      }
   }

   public static void execute(Long batchId, Map params) throws Exception {
      execute(batchId, params, (String)null);
   }

   public static void execute(Long batchId, Map params, String batchListener) throws Exception {
      BatchResult batchResult = new BatchResult();
      Batch batch = null;

      try {
         logger.debug("提取批处理对象【" + batchId + "】");
         batch = SchemeService.ins.getBatchData(batchId);
      } catch (Exception exception) {
         failBatch(batchResult, "批处理对象【" + batchId + "】提取失败,可能是不存在或配置异常");
         return;
      }

      execute(batch, params, batchListener, batchResult);
   }

   public static void execute(Batch batchData, Map params, String batchListener, BatchResult batchResult) throws Exception {
      batchResult.setStatus(BatchStatus.started);
      batchResult.setMsg(BatchStatus.started.name());
      batchResult.setBatchId(batchData.getId());
      batchResult.setBatchName(batchData.getName());
      batchResult.setStartTime(new Date());
      if (StringUtils.isBlank(batchResult.getIp())) {
         batchResult.setIp("0:0:0:0:0:0:0:1");
         batchResult.setUserAgent("Java API");
      }

      logger.debug("Validation of URule Batch ...");
      boolean flag = validateBatchEnabled(batchResult, batchData);
      if (flag) {
         logger.debug("Validation of KnowledgePackage ...");
         flag = validateKnowledgePackage(batchResult, batchData);
         if (flag) {
            if (StringUtils.isNotBlank(batchListener)) {
               batchData.setListener(batchListener);
            }

            batchResult.setStatus(BatchStatus.started);
            batchResult.setMsg(BatchStatus.started.name());
            batchResult.setBatchId(batchData.getId());
            batchResult.setBatchName(batchData.getName());
            batchResult.setStartTime(new Date());
            BatchLog batchLog = new BatchLog();
            batchLog.setIp(batchResult.getIp());
            batchLog.setUserAgent(batchResult.getUserAgent());
            KnowledgeService knowledgeService = (KnowledgeService)Utils.getApplicationContext().getBean("urule.knowledgeService");
            KnowledgePackage knowledge = knowledgeService.getKnowledge(batchData.getPacketId().toString());
            VariableCategory variableCategory = null;

            try {
               variableCategory = JsonBuilder.getInstance().findVariableCategory(knowledge.getVariableCategories(), "参数");
            } catch (VariableCategoryNotFoundException variableCategoryNotFoundException) {
            }

            VariableCategory variableCategory2 = JsonBuilder.getInstance().findVariableCategory(knowledge.getVariableCategories(), batchData.getDataProvider().getPacketVarName());
            logger.debug("Initialize batch parameters ...");

            for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(batchData.getParams())) {
               if (params.containsKey(dataParam.getName())) {
                  dataParam.setValue(params.get(dataParam.getName()));
               }
            }

            logger.debug("Initialize packet parameters ...");

            for(DataParam dataParam2 : (Iterable<DataParam>)(Iterable<?>)(batchData.getPacketParams())) {
               if (params.containsKey(dataParam2.getName())) {
                  dataParam2.setValue(params.get(dataParam2.getName()));
               }
            }

            logger.debug("Initialize dataProvider parameters ...");
            BatchDataProvider dataProvider = batchData.getDataProvider();

            for(DataParam dataParam3 : (Iterable<DataParam>)(Iterable<?>)(dataProvider.getParams())) {
               if (params.containsKey(dataParam3.getBatchParamName())) {
                  dataParam3.setValue(params.get(dataParam3.getBatchParamName()));
               }
            }

            logger.debug("Initialize dataProvider dialect ...");
            DataSource dataSource = DataSourceHandlerManager.getDataSource(batchData.getDataProvider().getDatasource());
            Object objectValue = null;

            try {
               Connection connection = dataSource.getConnection();
               Dialect dialect = DialectResolver.resolveDialect(connection);
               batchData.getDataProvider().setDialect(dialect);
               connection.close();
            } catch (Exception exception) {
               logger.error("close connection error:" + exception.getMessage());
               batchResult.setStatus(BatchStatus.failed);
               batchResult.setException(exception);
               return;
            }

            BatchDataResolver dataResolver = batchData.getDataResolver();
            logger.debug("Initialize dataResolver dialect ...");
            DataSource dataSource2 = DataSourceHandlerManager.getDataSource(dataResolver.getDatasource());
            Connection connection2 = null;

            try {
               connection2 = dataSource2.getConnection();
               Dialect dialect2 = DialectResolver.resolveDialect(connection2);
               dataResolver.setDialect(dialect2);
               connection2.close();
            } catch (Exception exception2) {
               logger.error("close connection error:" + exception2.getMessage());
               batchResult.setStatus(BatchStatus.failed);
               batchResult.setException(exception2);
               return;
            }

            for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(dataResolver.getItems())) {
               BatchUpdateMode updateMode = batchDataResolverItem.getUpdateMode();
               if (BatchUpdateMode.insert == updateMode && dataResolver.getTranScope() == TranScope.hive) {
                  ArrayList items = new ArrayList();
                  String text = buildHiveInsertSql((List)batchData.getParams(), (List)items, (BatchDataResolverItem)batchDataResolverItem);
                  batchDataResolverItem.setUpdateSql(text);
                  logger.info(String.format("Resolver: %s, item: %s, update sql: %s", dataResolver.getName(), batchDataResolverItem.getName(), text));
                  batchDataResolverItem.setParams(items);
               }
            }

            logger.debug("Initialize BatchContext ...");
            BatchContext batchContext = new BatchContext(batchData, params, batchResult);
            batchContext.setBatchLog(batchLog);
            batchContext.setKnowledgePackage(knowledge);
            batchContext.setKnowledgeService(knowledgeService);
            batchContext.setParameterVariableCategory(variableCategory);
            batchContext.setProviderVariableCategory(variableCategory2);
            logger.debug("Initialize packet provider parameters ...");

            for(DataParam dataParam4 : (Iterable<DataParam>)(Iterable<?>)(batchData.getPacketParams())) {
               if ((dataParam4.getDataType().equals("Object") || dataParam4.getDataType().equals("List")) && dataParam4.getDataProviderId() != null && dataParam4.getDataProviderId() > 0L) {
                  loadDataProviderValue(params, dataParam4, batchContext);
               }
            }

            logger.debug("Initialize packet complex parameters ...");
            Map valuesByKey = buildComplexPacketParams(batchData.getPacketParams(), knowledge);
            batchData.setComplexPacketParams(valuesByKey);
            logger.debug("Initialize packet out parameters ...");
            List items2 = generateOutParamNames(batchContext);
            batchData.setOutParameterNameList(items2);
            if (batchData.isAsync()) {
               BatchThread batchThread = new BatchThread(batchContext);
               batchThread.start();
            } else {
               BatchRunHelper.run(batchContext);
            }

         }
      }
   }

   protected static String buildHiveInsertSql(List items, List items2, BatchDataResolverItem batchDataResolverItem) {
      String tableName = batchDataResolverItem.getTableName();
      List fields = batchDataResolverItem.getFields();
      String text = "insert into " + tableName + " ";
      String partitionName = batchDataResolverItem.getPartitionName();
      String partitionValue = batchDataResolverItem.getPartitionValue();
      if (StringUtils.isNotBlank(partitionName)) {
         if (StringUtils.isNotBlank(partitionValue)) {
            boolean flag = false;

            for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(items)) {
               if (partitionValue.equals(dataParam.getName())) {
                  flag = true;
                  text = text + "partition (" + partitionName + "='" + dataParam.getValue() + "') ";
               }
            }

            if (!flag) {
               text = text + "partition (" + partitionName + "='" + partitionValue + "') ";
            }
         } else {
            text = text + "partition (" + partitionName + ") ";
         }
      }

      String text2 = "";

      for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(fields)) {
         if (StringUtils.isNotEmpty(text2)) {
            text2 = text2 + ", ";
         }

         text2 = text2 + batchDataResolverItemField.getDestProperty();
         DataParam dataParam2 = new DataParam();
         dataParam2.setDataType(batchDataResolverItemField.getDataType());
         dataParam2.setName(batchDataResolverItemField.getSrcProperty());
         dataParam2.setIndex(items2.size());
         items2.add(dataParam2);
      }

      text = text + "(" + text2 + ") ";
      return text;
   }

   public static List generateOutParamNames(BatchContext batchContext) {
      ArrayList outParamNames = new ArrayList();
      BatchDataResolver dataResolver = batchContext.getBatch().getDataResolver();

      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(dataResolver.getItems())) {
         for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(batchDataResolverItem.getFields())) {
            String srcProperty = batchDataResolverItemField.getSrcProperty();
            if (srcProperty.indexOf(".") != -1) {
               List items = Arrays.asList(srcProperty.split("\\."));
               boolean flag = "parameter".equals(items.get(0));
               if (flag) {
                  String text = (String)items.get(1);
                  if (!outParamNames.contains(text)) {
                     outParamNames.add(text);
                  }
               }
            }
         }
      }

      return outParamNames;
   }

   private static Map buildComplexPacketParams(List items, KnowledgePackage knowledgePackage) throws Exception {
      HashMap valuesByKey = new HashMap();

      for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(items)) {
         if (dataParam.getValue() != null) {
            valuesByKey.put(dataParam.getName(), dataParam.getValue());
         }
      }

      HashMap valuesByKey2 = valuesByKey;
      Object objectValue = null;
      HashMap valuesByKey3 = new HashMap();

      try {
         List variableCategories = knowledgePackage.getVariableCategories();
         Map variableCategoriesMap = JsonBuilder.getInstance().buildVariableCategoriesMap(variableCategories);
         VariableCategory variableCategory = (VariableCategory)variableCategoriesMap.get("参数");
         if (variableCategory == null) {
            throw new VariableCategoryNotFoundException("变量对象【参数】未定义!");
         }

         for(String text : (Iterable<String>)(Iterable<?>)(valuesByKey2.keySet())) {
            Object objectValue2 = valuesByKey2.get(text);
            if (objectValue2 != null) {
               Variable variable = JsonBuilder.getInstance().findVariable(variableCategory, text);
               Datatype type = variable.getType();
               SubObject subObject = JsonBuilder.getInstance().findSubObject(variable.getName(), valuesByKey3);
               Map map = subObject.getMap();
               if (!type.equals(Datatype.Object) && !type.equals(Datatype.List)) {
                  map.put(subObject.getName(), type.convert(objectValue2));
               } else {
                  map.put(subObject.getName(), JsonBuilder.getInstance().buildComplexObject(objectValue2, variableCategoriesMap));
               }
            }
         }
      } catch (VariableCategoryNotFoundException variableCategoryNotFoundException) {
      }

      return valuesByKey3;
   }

   private static void loadDataProviderValue(Map valuesByKey, DataParam dataParam, BatchContext batchContext) {
      Long dataProviderId = dataParam.getDataProviderId();
      BatchDataProvider providerData = SchemeService.ins.getProviderData(dataProviderId);
      if (providerData != null && providerData.getDatasource() != null) {
         Connection connection = null;

         try {
            DataSource dataSource = (DataSource)batchContext.getReaderDataSoruceMap().get(providerData.getDatasourceId());
            if (dataSource == null) {
               dataSource = DataSourceHandlerManager.getDataSource(providerData.getDatasource());
               batchContext.getReaderDataSoruceMap().put(providerData.getDatasourceId(), dataSource);
            }

            connection = dataSource.getConnection();
            bindProviderParameters(providerData, batchContext.getParamValueMap());
            bindProviderParameters(providerData, valuesByKey);
            List items = queryProviderData(connection, providerData, batchContext.getKnowledgePackage().getVariableCategories());
            if (items.size() > 0) {
               if (dataParam.getDataType().equals("List")) {
                  valuesByKey.put(dataParam.getName(), items);
               } else if (dataParam.getDataType().equals("Object")) {
                  valuesByKey.put(dataParam.getName(), items.get(0));
               }
            }
         } catch (Exception exception) {
            logger.error(exception);
            if (connection != null) {
               try {
                  connection.close();
               } catch (SQLException sQLException) {
                  logger.debug("Could not close JDBC Connection", sQLException);
               } catch (Throwable throwable) {
                  logger.debug("Unexpected exception on closing JDBC Connection", throwable);
               }
            }
         }
      }

   }

   private static List queryProviderData(Connection connection, BatchDataProvider batchDataProvider, List items) throws Exception {
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
            GeneralEntity generalEntity = null;
            if (batchDataProvider.getPacketVarName().equals("参数")) {
               generalEntity = new GeneralEntity();
            } else {
               generalEntity = new GeneralEntity(variableCategory.getClazz());
            }

            for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(fields)) {
               if ("Boolean".equals(batchDataProviderField.getDataType())) {
                  generalEntity.put(batchDataProviderField.getDestProperty(), resultSet.getBoolean(batchDataProviderField.getSrcProperty()));
               } else {
                  generalEntity.put(batchDataProviderField.getDestProperty(), resultSet.getObject(batchDataProviderField.getSrcProperty()));
               }
            }

            items2.add(generalEntity);
         }

         resultSet.close();
         preparedStatement.close();
      } catch (Exception exception) {
         logger.error(exception);
      }

      return items2;
   }

   private static void bindProviderParameters(BatchDataProvider batchDataProvider, Map valuesByKey) {
      for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(batchDataProvider.getParams())) {
         if (valuesByKey.containsKey(dataParam.getBatchParamName())) {
            dataParam.setValue(valuesByKey.get(dataParam.getBatchParamName()));
         }
      }

   }
}
