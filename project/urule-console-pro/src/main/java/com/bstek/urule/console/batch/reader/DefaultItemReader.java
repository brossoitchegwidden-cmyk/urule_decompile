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
   private static Log a = LogFactory.getLog(DefaultItemReader.class);

   private void a(BatchDataProvider var1, Map var2) {
      for(DataParam var4 : (Iterable<DataParam>)(Iterable<?>)(var1.getParams())) {
         if (var2.containsKey(var4.getBatchParamName())) {
            var4.setValue(var2.get(var4.getBatchParamName()));
         }
      }

   }

   public List getPageDatas(Connection var1, BatchContext var2, int var3, int var4) throws ReaderException {
      try {
         Batch var5 = var2.getBatch();
         BatchDataProvider var18 = var5.getDataProvider();
         Dialect var7 = var18.getDialect();
         String var8 = var18.getPageSql();
         if (var3 >= 0) {
            String var9 = var18.getOrderField();
            if (var7 instanceof OrderLimitDialect && StringUtils.isNotBlank(var9)) {
               OrderLimitDialect var10 = (OrderLimitDialect)var7;
               String var11 = var18.getPageLimitType();
               if (!StringUtils.isBlank(var11) && !"between".equals(var11)) {
                  GeneralEntity var12 = var2.getHiveLastData();
                  String var13 = var18.getOrderFieldParamName();
                  if (!var5.isThreadMulti() && var3 > 1 && var12 != null && StringUtils.isNotBlank(var13)) {
                     Object var14 = var12.get(var9);

                     for(DataParam var16 : (Iterable<DataParam>)(Iterable<?>)(var18.getParams())) {
                        if (var13.equalsIgnoreCase(var16.getName())) {
                           var16.setValue(var14);
                        }
                     }
                  }

                  var8 = var10.getLimitString(var18.getPageSql(), var4);
               } else {
                  var8 = var10.getLimitString(var18.getPageSql(), var3 * var4, var4, var9);
               }
            } else {
               var8 = var7.getLimitString(var18.getPageSql(), var3 * var4, var4);
            }
         }

         List var19 = this.a(var1, var2, var8, true);
         return var19;
      } catch (Exception var17) {
         ReaderException var6 = new ReaderException(var17.getMessage(), var17);
         var6.setPageIndex(var3);
         throw var6;
      }
   }

   protected boolean a(BatchContext var1, BatchDataProvider var2, Map var3) {
      for(Filter var5 : (Iterable<Filter>)(Iterable<?>)(var2.getFilters())) {
         for(FilterItem var7 : (Iterable<FilterItem>)(Iterable<?>)(var5.getItems())) {
            if (var7.getType() == FilterType.bean) {
               ReaderFilter var8 = (ReaderFilter)Utils.getApplicationContext().getBean(var7.getValue());
               boolean var9 = var8.filter(var1, var2, var3);
               if (var9) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private List a(Connection var1, BatchContext var2, String var3, boolean var4) throws Exception {
      Batch var5 = var2.getBatch();
      BatchDataProvider var6 = var5.getDataProvider();
      ArrayList var7 = new ArrayList();

      try {
         ParsedSql var8 = NamedSQLUtils.parseSql(var3);
         String var9 = JdbcUtils.getOriginSql(var8.getOriginalSql());
         PreparedStatement var10 = var1.prepareStatement(var9);
         StmtUtils.setStmtQueryParameters(var8, var6.getParams(), var10);
         ResultSet var11 = var10.executeQuery();
         PacketData var12 = PacketCache.ins.getPacket(Long.valueOf(var5.getPacketId()));
         List var13 = var12.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
         VariableCategory var14 = JsonBuilder.getInstance().findVariableCategory(var13, var6.getPacketVarName());
         List var15 = var6.getFields();

         while(var11.next()) {
            GeneralEntity var16 = null;
            if (var6.getPacketVarName().equals("参数")) {
               var16 = new GeneralEntity();
            } else {
               var16 = new GeneralEntity(var14.getClazz());
            }

            for(BatchDataProviderField var18 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var15)) {
               if ("Boolean".equals(var18.getDataType())) {
                  var16.put(var18.getDestProperty(), var11.getBoolean(var18.getSrcProperty()));
               } else if ("Object".equals(var18.getDataType())) {
                  if (var18.getDataProvider() != null) {
                     BatchDataProvider var34 = var18.getDataProvider();
                     this.a(var34, var2.getParamValueMap());
                     this.a(var34, (Map)var16);
                     List var37 = this.a(var1, var34, var13);
                     if (var37.size() > 0) {
                        var16.put(var18.getDestProperty(), var37.get(0));
                     }
                  }

                  var16.put(var18.getDestProperty(), var11.getObject(var18.getSrcProperty()));
               } else if ("List".equals(var18.getDataType())) {
                  if (var18.getDataProvider() != null) {
                     BatchDataProvider var33 = var18.getDataProvider();
                     this.a(var33, var2.getParamValueMap());
                     this.a(var33, (Map)var16);
                     List var36 = this.a(var1, var33, var13);
                     var16.put(var18.getDestProperty(), var36);
                  }
               } else if ("JsonObject".equals(var18.getDataType())) {
                  String var32 = var11.getString(var18.getSrcProperty());
                  Map var35 = (Map)JsonUtils.getObjectJsonMapper().readValue(var32, HashMap.class);
                  if (var18.getDataProvider() != null) {
                     BatchDataProvider var38 = var18.getDataProvider();
                     VariableCategory var39 = JsonBuilder.getInstance().findVariableCategory(var13, var38.getPacketVarName());
                     GeneralEntity var40 = this.a(var38, var39);

                     for(BatchDataProviderField var42 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var38.getFields())) {
                        if (var35.containsKey(var42.getSrcProperty())) {
                           var40.put(var42.getDestProperty(), var35.get(var42.getSrcProperty()));
                        }
                     }

                     var16.put(var18.getDestProperty(), var40);
                  }
               } else if (!"JsonArray".equals(var18.getDataType())) {
                  var16.put(var18.getDestProperty(), var11.getObject(var18.getSrcProperty()));
               } else {
                  String var19 = var11.getString(var18.getSrcProperty());
                  List var20 = (List)JsonUtils.getObjectJsonMapper().readValue(var19, new TypeReference() {
                  });
                  ArrayList var21 = new ArrayList();

                  for(Map var23 : (Iterable<Map>)(Iterable<?>)(var20)) {
                     if (var18.getDataProvider() != null) {
                        BatchDataProvider var24 = var18.getDataProvider();
                        VariableCategory var25 = JsonBuilder.getInstance().findVariableCategory(var13, var24.getPacketVarName());
                        GeneralEntity var26 = this.a(var24, var25);

                        for(BatchDataProviderField var28 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var24.getFields())) {
                           if (var23.containsKey(var28.getSrcProperty())) {
                              var26.put(var28.getDestProperty(), var23.get(var28.getSrcProperty()));
                           }
                        }

                        var21.add(var26);
                     }
                  }

                  var16.put(var18.getDestProperty(), var21);
               }
            }

            if (!var5.isThreadMulti() && var4) {
               var2.setHiveLastData(var16);
            }

            boolean var31 = this.a((BatchContext)var2, var6, (Map)var16);
            if (!var31) {
               var7.add(var16);
            }
         }

         var11.close();
         var10.close();
      } catch (Exception var29) {
         a.error(var29);
         var29.printStackTrace();
      }

      return var7;
   }

   private GeneralEntity a(BatchDataProvider var1, VariableCategory var2) {
      GeneralEntity var3 = null;
      if (var1.getPacketVarName().equals("参数")) {
         var3 = new GeneralEntity();
      } else {
         var3 = new GeneralEntity(var2.getClazz());
      }

      return var3;
   }

   private List a(Connection var1, BatchDataProvider var2, List var3) throws Exception {
      VariableCategory var4 = JsonBuilder.getInstance().findVariableCategory(var3, var2.getPacketVarName());
      Object var5 = null;
      ArrayList var6 = new ArrayList();

      try {
         ParsedSql var7 = NamedSQLUtils.parseSql(var2.getPageSql());
         String var8 = JdbcUtils.getOriginSql(var7.getOriginalSql());
         PreparedStatement var9 = var1.prepareStatement(var8);
         StmtUtils.setStmtQueryParameters(var7, var2.getParams(), var9);
         ResultSet var10 = var9.executeQuery();
         List var11 = var2.getFields();

         while(var10.next()) {
            GeneralEntity var12 = this.a(var2, var4);

            for(BatchDataProviderField var14 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var11)) {
               if ("Boolean".equals(var14.getDataType())) {
                  var12.put(var14.getDestProperty(), var10.getBoolean(var14.getSrcProperty()));
               } else if (!"Object".equals(var14.getDataType()) && !"List".equals(var14.getDataType()) && !"JsonObject".equals(var14.getDataType()) && !"JsonArray".equals(var14.getDataType())) {
                  var12.put(var14.getDestProperty(), var10.getObject(var14.getSrcProperty()));
               }
            }

            var6.add(var12);
         }

         var10.close();
         var9.close();
      } catch (Exception var15) {
         a.error(var15);
         var15.printStackTrace();
      }

      return var6;
   }

   public List getDatas(Connection var1, BatchContext var2) throws ReaderException {
      try {
         Batch var3 = var2.getBatch();
         BatchDataProvider var7 = var3.getDataProvider();
         String var5 = var7.getPageSql();
         return this.a(var1, var2, var5, false);
      } catch (Exception var6) {
         ReaderException var4 = new ReaderException(var6.getMessage(), var6);
         throw var4;
      }
   }

   public int getTotleRows(BatchDataProvider var1) throws Exception {
      int var2 = 0;
      List var3 = var1.getParams();
      DataSource var4 = null;
      Connection var5 = null;

      try {
         var4 = DataSourceHandlerManager.getDataSource(var1.getDatasource());
         var5 = var4.getConnection();
         ParsedSql var6 = NamedSQLUtils.parseSql(var1.getCountSql());
         String var7 = JdbcUtils.getOriginSql(var6.getOriginalSql());
         PreparedStatement var8 = var5.prepareStatement(var7);
         StmtUtils.setStmtQueryParameters(var6, var3, var8);
         ResultSet var9 = var8.executeQuery();
         if (var9.next()) {
            var2 = var9.getInt(1);
         }

         var9.close();
         var8.close();
         var5.close();
         return var2;
      } catch (Exception var11) {
         a.error(var11);
         var11.printStackTrace();

         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (Exception var10) {
            var10.printStackTrace();
         }

         throw var11;
      }
   }
}
