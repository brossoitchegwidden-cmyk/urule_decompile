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
   private static final String a = ".";
   private static final String b = "\\.";
   private static final String c = "parameter";

   private static void a(BatchResult var0, String var1) {
      var0.setStatus(BatchStatus.failed);
      var0.setMsg(var1);
   }

   private static boolean a(BatchResult var0, Batch var1) throws ServletException, IOException {
      Long var2 = var1.getPacketId();
      PacketData var3 = null;

      try {
         var3 = PacketCache.ins.getPacket(var2);
      } catch (Throwable var5) {
         a(var0, "知识包【" + var2 + "】提取失败:" + var5.getMessage());
         return false;
      }

      if (var3 == null) {
         a(var0, "知识包【" + var2 + "】不存在或未发布");
         return false;
      } else {
         PacketConfig var4 = var3.getPacket();
         if (!var4.isEnable()) {
            a(var0, "知识包【" + var2 + "】已停用");
            return false;
         } else {
            return true;
         }
      }
   }

   private static boolean b(BatchResult var0, Batch var1) throws ServletException, IOException {
      Long var2 = var1.getId();
      if (!var1.isEnable()) {
         a(var0, "批处理【" + var2 + "】已停用");
         return false;
      } else {
         return true;
      }
   }

   public static void execute(Long var0, Map var1) throws Exception {
      execute(var0, var1, (String)null);
   }

   public static void execute(Long var0, Map var1, String var2) throws Exception {
      BatchResult var3 = new BatchResult();
      Batch var4 = null;

      try {
         logger.debug("提取批处理对象【" + var0 + "】");
         var4 = SchemeService.ins.getBatchData(var0);
      } catch (Exception var6) {
         a(var3, "批处理对象【" + var0 + "】提取失败,可能是不存在或配置异常");
         return;
      }

      execute(var4, var1, var2, var3);
   }

   public static void execute(Batch var0, Map var1, String var2, BatchResult var3) throws Exception {
      var3.setStatus(BatchStatus.started);
      var3.setMsg(BatchStatus.started.name());
      var3.setBatchId(var0.getId());
      var3.setBatchName(var0.getName());
      var3.setStartTime(new Date());
      if (StringUtils.isBlank(var3.getIp())) {
         var3.setIp("0:0:0:0:0:0:0:1");
         var3.setUserAgent("Java API");
      }

      logger.debug("Validation of URule Batch ...");
      boolean var4 = b(var3, var0);
      if (var4) {
         logger.debug("Validation of KnowledgePackage ...");
         var4 = a(var3, var0);
         if (var4) {
            if (StringUtils.isNotBlank(var2)) {
               var0.setListener(var2);
            }

            var3.setStatus(BatchStatus.started);
            var3.setMsg(BatchStatus.started.name());
            var3.setBatchId(var0.getId());
            var3.setBatchName(var0.getName());
            var3.setStartTime(new Date());
            BatchLog var5 = new BatchLog();
            var5.setIp(var3.getIp());
            var5.setUserAgent(var3.getUserAgent());
            KnowledgeService var6 = (KnowledgeService)Utils.getApplicationContext().getBean("urule.knowledgeService");
            KnowledgePackage var7 = var6.getKnowledge(var0.getPacketId().toString());
            VariableCategory var8 = null;

            try {
               var8 = JsonBuilder.getInstance().findVariableCategory(var7.getVariableCategories(), "参数");
            } catch (VariableCategoryNotFoundException var24) {
            }

            VariableCategory var9 = JsonBuilder.getInstance().findVariableCategory(var7.getVariableCategories(), var0.getDataProvider().getPacketVarName());
            logger.debug("Initialize batch parameters ...");

            for(DataParam var11 : (Iterable<DataParam>)(Iterable<?>)(var0.getParams())) {
               if (var1.containsKey(var11.getName())) {
                  var11.setValue(var1.get(var11.getName()));
               }
            }

            logger.debug("Initialize packet parameters ...");

            for(DataParam var28 : (Iterable<DataParam>)(Iterable<?>)(var0.getPacketParams())) {
               if (var1.containsKey(var28.getName())) {
                  var28.setValue(var1.get(var28.getName()));
               }
            }

            logger.debug("Initialize dataProvider parameters ...");
            BatchDataProvider var27 = var0.getDataProvider();

            for(DataParam var12 : (Iterable<DataParam>)(Iterable<?>)(var27.getParams())) {
               if (var1.containsKey(var12.getBatchParamName())) {
                  var12.setValue(var1.get(var12.getBatchParamName()));
               }
            }

            logger.debug("Initialize dataProvider dialect ...");
            DataSource var30 = DataSourceHandlerManager.getDataSource(var0.getDataProvider().getDatasource());
            Object var31 = null;

            try {
               Connection var32 = var30.getConnection();
               Dialect var13 = DialectResolver.resolveDialect(var32);
               var0.getDataProvider().setDialect(var13);
               var32.close();
            } catch (Exception var23) {
               logger.error("close connection error:" + var23.getMessage());
               var3.setStatus(BatchStatus.failed);
               var3.setException(var23);
               return;
            }

            BatchDataResolver var33 = var0.getDataResolver();
            logger.debug("Initialize dataResolver dialect ...");
            DataSource var14 = DataSourceHandlerManager.getDataSource(var33.getDatasource());
            Connection var15 = null;

            try {
               var15 = var14.getConnection();
               Dialect var16 = DialectResolver.resolveDialect(var15);
               var33.setDialect(var16);
               var15.close();
            } catch (Exception var22) {
               logger.error("close connection error:" + var22.getMessage());
               var3.setStatus(BatchStatus.failed);
               var3.setException(var22);
               return;
            }

            for(BatchDataResolverItem var18 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var33.getItems())) {
               BatchUpdateMode var19 = var18.getUpdateMode();
               if (BatchUpdateMode.insert == var19 && var33.getTranScope() == TranScope.hive) {
                  ArrayList var20 = new ArrayList();
                  String var21 = a((List)var0.getParams(), (List)var20, (BatchDataResolverItem)var18);
                  var18.setUpdateSql(var21);
                  logger.info(String.format("Resolver: %s, item: %s, update sql: %s", var33.getName(), var18.getName(), var21));
                  var18.setParams(var20);
               }
            }

            logger.debug("Initialize BatchContext ...");
            BatchContext var36 = new BatchContext(var0, var1, var3);
            var36.setBatchLog(var5);
            var36.setKnowledgePackage(var7);
            var36.setKnowledgeService(var6);
            var36.setParameterVariableCategory(var8);
            var36.setProviderVariableCategory(var9);
            logger.debug("Initialize packet provider parameters ...");

            for(DataParam var39 : (Iterable<DataParam>)(Iterable<?>)(var0.getPacketParams())) {
               if ((var39.getDataType().equals("Object") || var39.getDataType().equals("List")) && var39.getDataProviderId() != null && var39.getDataProviderId() > 0L) {
                  a(var1, var39, var36);
               }
            }

            logger.debug("Initialize packet complex parameters ...");
            Map var38 = a(var0.getPacketParams(), var7);
            var0.setComplexPacketParams(var38);
            logger.debug("Initialize packet out parameters ...");
            List var40 = generateOutParamNames(var36);
            var0.setOutParameterNameList(var40);
            if (var0.isAsync()) {
               BatchThread var41 = new BatchThread(var36);
               var41.start();
            } else {
               BatchRunHelper.a(var36);
            }

         }
      }
   }

   protected static String a(List var0, List var1, BatchDataResolverItem var2) {
      String var3 = var2.getTableName();
      List var4 = var2.getFields();
      String var5 = "insert into " + var3 + " ";
      String var6 = var2.getPartitionName();
      String var7 = var2.getPartitionValue();
      if (StringUtils.isNotBlank(var6)) {
         if (StringUtils.isNotBlank(var7)) {
            boolean var8 = false;

            for(DataParam var10 : (Iterable<DataParam>)(Iterable<?>)(var0)) {
               if (var7.equals(var10.getName())) {
                  var8 = true;
                  var5 = var5 + "partition (" + var6 + "='" + var10.getValue() + "') ";
               }
            }

            if (!var8) {
               var5 = var5 + "partition (" + var6 + "='" + var7 + "') ";
            }
         } else {
            var5 = var5 + "partition (" + var6 + ") ";
         }
      }

      String var13 = "";

      for(BatchDataResolverItemField var15 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var4)) {
         if (StringUtils.isNotEmpty(var13)) {
            var13 = var13 + ", ";
         }

         var13 = var13 + var15.getDestProperty();
         DataParam var11 = new DataParam();
         var11.setDataType(var15.getDataType());
         var11.setName(var15.getSrcProperty());
         var11.setIndex(var1.size());
         var1.add(var11);
      }

      var5 = var5 + "(" + var13 + ") ";
      return var5;
   }

   public static List generateOutParamNames(BatchContext var0) {
      ArrayList var1 = new ArrayList();
      BatchDataResolver var2 = var0.getBatch().getDataResolver();

      for(BatchDataResolverItem var5 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var2.getItems())) {
         for(BatchDataResolverItemField var7 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var5.getFields())) {
            String var8 = var7.getSrcProperty();
            if (var8.indexOf(".") != -1) {
               List var9 = Arrays.asList(var8.split("\\."));
               boolean var10 = "parameter".equals(var9.get(0));
               if (var10) {
                  String var11 = (String)var9.get(1);
                  if (!var1.contains(var11)) {
                     var1.add(var11);
                  }
               }
            }
         }
      }

      return var1;
   }

   private static Map a(List var0, KnowledgePackage var1) throws Exception {
      HashMap var2 = new HashMap();

      for(DataParam var4 : (Iterable<DataParam>)(Iterable<?>)(var0)) {
         if (var4.getValue() != null) {
            var2.put(var4.getName(), var4.getValue());
         }
      }

      HashMap var16 = var2;
      Object var17 = null;
      HashMap var5 = new HashMap();

      try {
         List var6 = var1.getVariableCategories();
         Map var7 = JsonBuilder.getInstance().buildVariableCategoriesMap(var6);
         VariableCategory var18 = (VariableCategory)var7.get("参数");
         if (var18 == null) {
            throw new VariableCategoryNotFoundException("变量对象【参数】未定义!");
         }

         for(String var9 : (Iterable<String>)(Iterable<?>)(var16.keySet())) {
            Object var10 = var16.get(var9);
            if (var10 != null) {
               Variable var11 = JsonBuilder.getInstance().findVariable(var18, var9);
               Datatype var12 = var11.getType();
               SubObject var13 = JsonBuilder.getInstance().findSubObject(var11.getName(), var5);
               Map var14 = var13.getMap();
               if (!var12.equals(Datatype.Object) && !var12.equals(Datatype.List)) {
                  var14.put(var13.getName(), var12.convert(var10));
               } else {
                  var14.put(var13.getName(), JsonBuilder.getInstance().buildComplexObject(var10, var7));
               }
            }
         }
      } catch (VariableCategoryNotFoundException var15) {
      }

      return var5;
   }

   private static void a(Map var0, DataParam var1, BatchContext var2) {
      Long var3 = var1.getDataProviderId();
      BatchDataProvider var4 = SchemeService.ins.getProviderData(var3);
      if (var4 != null && var4.getDatasource() != null) {
         Connection var5 = null;

         try {
            DataSource var6 = (DataSource)var2.getReaderDataSoruceMap().get(var4.getDatasourceId());
            if (var6 == null) {
               var6 = DataSourceHandlerManager.getDataSource(var4.getDatasource());
               var2.getReaderDataSoruceMap().put(var4.getDatasourceId(), var6);
            }

            var5 = var6.getConnection();
            a(var4, var2.getParamValueMap());
            a(var4, var0);
            List var7 = a(var5, var4, var2.getKnowledgePackage().getVariableCategories());
            if (var7.size() > 0) {
               if (var1.getDataType().equals("List")) {
                  var0.put(var1.getName(), var7);
               } else if (var1.getDataType().equals("Object")) {
                  var0.put(var1.getName(), var7.get(0));
               }
            }
         } catch (Exception var10) {
            logger.error(var10);
            if (var5 != null) {
               try {
                  var5.close();
               } catch (SQLException var8) {
                  logger.debug("Could not close JDBC Connection", var8);
               } catch (Throwable var9) {
                  logger.debug("Unexpected exception on closing JDBC Connection", var9);
               }
            }
         }
      }

   }

   private static List a(Connection var0, BatchDataProvider var1, List var2) throws Exception {
      VariableCategory var3 = JsonBuilder.getInstance().findVariableCategory(var2, var1.getPacketVarName());
      Object var4 = null;
      ArrayList var5 = new ArrayList();

      try {
         ParsedSql var6 = NamedSQLUtils.parseSql(var1.getPageSql());
         String var7 = JdbcUtils.getOriginSql(var6.getOriginalSql());
         PreparedStatement var8 = var0.prepareStatement(var7);
         StmtUtils.setStmtQueryParameters(var6, var1.getParams(), var8);
         ResultSet var9 = var8.executeQuery();
         List var10 = var1.getFields();

         while(var9.next()) {
            GeneralEntity var11 = null;
            if (var1.getPacketVarName().equals("参数")) {
               var11 = new GeneralEntity();
            } else {
               var11 = new GeneralEntity(var3.getClazz());
            }

            for(BatchDataProviderField var13 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var10)) {
               if ("Boolean".equals(var13.getDataType())) {
                  var11.put(var13.getDestProperty(), var9.getBoolean(var13.getSrcProperty()));
               } else {
                  var11.put(var13.getDestProperty(), var9.getObject(var13.getSrcProperty()));
               }
            }

            var5.add(var11);
         }

         var9.close();
         var8.close();
      } catch (Exception var14) {
         logger.error(var14);
      }

      return var5;
   }

   private static void a(BatchDataProvider var0, Map var1) {
      for(DataParam var3 : (Iterable<DataParam>)(Iterable<?>)(var0.getParams())) {
         if (var1.containsKey(var3.getBatchParamName())) {
            var3.setValue(var1.get(var3.getBatchParamName()));
         }
      }

   }
}
