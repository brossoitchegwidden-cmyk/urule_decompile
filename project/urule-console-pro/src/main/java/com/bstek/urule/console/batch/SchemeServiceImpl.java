package com.bstek.urule.console.batch;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.batch.filter.PropertyFilter;
import com.bstek.urule.console.batch.utils.JsonUtils;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.batch.BatchManager;
import com.bstek.urule.console.database.manager.batch.BatchManagerHelper;
import com.bstek.urule.console.database.manager.batch.provider.ProviderFieldManager;
import com.bstek.urule.console.database.manager.batch.provider.ProviderFieldQuery;
import com.bstek.urule.console.database.manager.batch.provider.ProviderManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverFieldManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverFieldQuery;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverItemManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverItemQuery;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.repository.DataSourceManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.model.batch.BatchDataProviderField;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItemField;
import com.bstek.urule.console.database.model.batch.BatchUpdateMode;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.console.database.model.batch.Filter;
import com.bstek.urule.console.database.model.batch.FilterItem;
import com.bstek.urule.console.database.model.batch.FilterType;
import com.bstek.urule.console.database.model.datasource.DataSource;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SchemeServiceImpl implements SchemeService {
   private static Log a = LogFactory.getLog(SchemeServiceImpl.class);

   public void add(Batch var1, String var2) {
      var1.setId(IDGenerator.getInstance().nextId(IDType.BATCH));
      var1.setStatus(BatchStatus.none);
      var1.setProjectId(ContextHolder.getProjectId());
      var1.setCreateUser(var2);
      var1.setCreateDate(new Date());
      BatchManager.ins.add(var1);
      if (var1.getPacketParams() != null) {
         for(DataParam var4 : (Iterable<DataParam>)(Iterable<?>)(var1.getPacketParams())) {
            if ((var4.getDataType().equals("Object") || var4.getDataType().equals("List")) && StringUtils.isBlank(var4.getBatchParamName())) {
               this.a(var1, var4, var2);
            }
         }
      }

      if (var1.getDataProvider() != null) {
         this.a(var1, var2);
      }

      if (var1.getDataResolver() != null) {
         this.b(var1, var2);
      }

      if (var1.getDataProvider() != null || var1.getDataResolver() != null) {
         var1.setUpdateUser(var2);
         var1.setUpdateDate(new Date());
         BatchManager.ins.update(var1);
      }

   }

   public void update(Batch var1, String var2) {
      var1.setUpdateUser(var2);
      var1.setUpdateDate(new Date());
      BatchManager.ins.update(var1);
      if (var1.getPacketParams() != null) {
         for(DataParam var4 : (Iterable<DataParam>)(Iterable<?>)(var1.getPacketParams())) {
            if ((var4.getDataType().equals("Object") || var4.getDataType().equals("List")) && StringUtils.isBlank(var4.getBatchParamName())) {
               this.a(var1, var2, var4, var4.getDataProvider());
            }
         }
      }

      if (var1.getDataProvider() != null) {
         BatchDataProvider var12 = var1.getDataProvider();
         this.a(var1, var2, var12);
      }

      if (var1.getDataResolver() != null) {
         BatchDataResolver var13 = var1.getDataResolver();
         if (var13.getId() != null && var13.getId() != 0L) {
            var13.setUpdateUser(var2);
            var13.setUpdateDate(new Date());
            ResolverManager.ins.update(var13);
            ArrayList var14 = new ArrayList();
            List var5 = ResolverItemManager.ins.createQuery().resolverId(var13.getId()).list();

            for(BatchDataResolverItem var7 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var13.getItems())) {
               if (var7.getId() != null && var7.getId() != 0L) {
                  var7.setUpdateUser(var2);
                  var7.setUpdateDate(new Date());
                  ResolverItemManager.ins.update(var7);
                  List var8 = ResolverFieldManager.ins.createQuery().itemId(var7.getId()).list();
                  ArrayList var9 = new ArrayList();

                  for(BatchDataResolverItemField var11 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var7.getFields())) {
                     if (var11.getId() != 0L && var11.getId() != null) {
                        var11.setUpdateUser(var2);
                        var11.setUpdateDate(new Date());
                        ResolverFieldManager.ins.update(var11);
                     } else {
                        this.a(var2, var7, var11);
                     }

                     var9.add(var11.getId());
                  }

                  for(BatchDataResolverItemField var18 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var8)) {
                     if (!var9.contains(var18.getId())) {
                        ResolverFieldManager.ins.remove(var18.getId());
                     }
                  }
               } else {
                  this.a(var2, var13, var7);
               }

               var14.add(var7.getId());
            }

            for(BatchDataResolverItem var16 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var5)) {
               if (!var14.contains(var16.getId())) {
                  BatchManagerHelper.removeResolverItem(var16.getId());
               }
            }
         } else {
            this.b(var1, var2);
         }
      }

      BatchManager.ins.update(var1);
   }

   private void a(Batch var1, String var2, DataParam var3, BatchDataProvider var4) {
      if (var4.getId() != null && var4.getId() != 0L) {
         var4.setSupportsPaging(false);
         var4.setUpdateUser(var2);
         var4.setUpdateDate(new Date());
         ProviderManager.ins.update(var4);
         List var5 = ProviderFieldManager.ins.createQuery().providerId(var4.getId()).list();
         ArrayList var6 = new ArrayList();

         for(BatchDataProviderField var8 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var4.getFields())) {
            if (var8.getId() != null && var8.getId() != 0L) {
               var8.setUpdateUser(var2);
               var8.setUpdateDate(new Date());
               if (this.a(var8)) {
                  if (var8.getDataProviderId() != null && var8.getDataProviderId() != 0L && var8.getDataProvider() != null) {
                     this.a(var1, var2, var8, var8.getDataProvider());
                  } else {
                     this.a(var1, var2, var8);
                  }
               }

               ProviderFieldManager.ins.update(var8);
            } else {
               this.a(var1, var2, var4, var8);
            }

            var6.add(var8.getId());
         }

         for(BatchDataProviderField var10 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var5)) {
            if (!var6.contains(var10.getId())) {
               if (var10.getDataProviderId() != null) {
                  this.removeProvider(var10.getDataProviderId(), var1.getId());
               }

               ProviderFieldManager.ins.remove(var10.getId());
            }
         }
      } else {
         this.a(var1, var3, var2);
      }

   }

   private void a(Batch var1, String var2, BatchDataProvider var3) {
      if (var3.getId() != null && var3.getId() != 0L) {
         var3.setUpdateUser(var2);
         var3.setUpdateDate(new Date());
         ProviderManager.ins.update(var3);
         List var4 = ProviderFieldManager.ins.createQuery().providerId(var3.getId()).list();
         ArrayList var5 = new ArrayList();

         for(BatchDataProviderField var7 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var3.getFields())) {
            if (var7.getId() != null && var7.getId() != 0L) {
               var7.setUpdateUser(var2);
               var7.setUpdateDate(new Date());
               if (this.a(var7)) {
                  if (var7.getDataProviderId() != null && var7.getDataProviderId() != 0L && var7.getDataProvider() != null) {
                     this.a(var1, var2, var7, var7.getDataProvider());
                  } else {
                     this.a(var1, var2, var7);
                  }
               }

               ProviderFieldManager.ins.update(var7);
            } else {
               this.a(var1, var2, var3, var7);
            }

            var5.add(var7.getId());
         }

         for(BatchDataProviderField var9 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var4)) {
            if (!var5.contains(var9.getId())) {
               if (var9.getDataProviderId() != null) {
                  this.removeProvider(var9.getDataProviderId(), var1.getId());
               }

               ProviderFieldManager.ins.remove(var9.getId());
            }
         }
      } else {
         this.a(var1, var2);
      }

   }

   private void a(Batch var1, String var2, BatchDataProviderField var3, BatchDataProvider var4) {
      if (var4.getId() != null && var4.getId() != 0L) {
         var4.setSupportsPaging(false);
         var4.setUpdateUser(var2);
         var4.setUpdateDate(new Date());
         ProviderManager.ins.update(var4);
         List var5 = ProviderFieldManager.ins.createQuery().providerId(var4.getId()).list();
         ArrayList var6 = new ArrayList();

         for(BatchDataProviderField var8 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var4.getFields())) {
            if (var8.getId() != null && var8.getId() != 0L) {
               var8.setUpdateUser(var2);
               var8.setUpdateDate(new Date());
               ProviderFieldManager.ins.update(var8);
               if (this.a(var8)) {
                  if (var8.getDataProviderId() != null && var8.getDataProviderId() != 0L && var8.getDataProvider() != null) {
                     this.a(var1, var2, var8, var8.getDataProvider());
                  } else {
                     this.a(var1, var2, var8);
                  }
               }
            } else {
               this.a(var1, var2, var4, var8);
            }

            var6.add(var8.getId());
         }

         for(BatchDataProviderField var10 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var5)) {
            if (!var6.contains(var10.getId())) {
               ProviderFieldManager.ins.remove(var10.getId());
               if (var10.getDataProviderId() != null) {
                  this.removeProvider(var10.getDataProviderId(), var1.getId());
               }
            }
         }
      } else {
         this.a(var1, var2, var3);
      }

   }

   public void removeProvider(long var1, long var3) {
      for(BatchDataProviderField var7 : (Iterable<BatchDataProviderField>)(Iterable<?>)(ProviderFieldManager.ins.createQuery().providerId(var1).batchId(var3).list())) {
         if (var7.getDataProviderId() != null) {
            this.removeProvider(var7.getDataProviderId(), var3);
         }

         ProviderFieldManager.ins.remove(var7.getId());
      }

      ProviderManager.ins.remove(var1);
   }

   public void remove(Long var1) {
      ProviderFieldManager.ins.removeByBatchId(var1);
      ProviderManager.ins.removeByBatchId(var1);
      ResolverFieldManager.ins.removeByBatchId(var1);
      ResolverItemManager.ins.removeByBatchId(var1);
      ResolverManager.ins.removeByBatchId(var1);
      BatchManager.ins.remove(var1);
   }

   private void a(Batch var1, String var2) {
      BatchDataProvider var3 = var1.getDataProvider();
      var3.setId(IDGenerator.getInstance().nextId(IDType.BATCH_DATA_PROVIDER));
      var3.setBatchId(var1.getId());
      var3.setProjectId(var1.getProjectId());
      var3.setCreateUser(var2);
      var3.setCreateDate(new Date());
      var3.setName("DataProvider");
      ProviderManager.ins.add(var3);
      var1.setProviderId(var3.getId());
      if (var3.getFields() != null) {
         for(BatchDataProviderField var5 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var3.getFields())) {
            this.a(var1, var2, var3, var5);
         }
      }

   }

   private void a(Batch var1, DataParam var2, String var3) {
      BatchDataProvider var4 = var2.getDataProvider();
      if (var4 == null) {
         var4 = new BatchDataProvider();
         var4.setDatasourceId(0L);
         var4.setName("Packet Parameter Data Provider");
         var2.setDataProvider(var4);
      }

      var4.setSupportsPaging(false);
      var4.setId(IDGenerator.getInstance().nextId(IDType.BATCH_DATA_PROVIDER));
      var4.setBatchId(var1.getId());
      var4.setProjectId(var1.getProjectId());
      var4.setCreateUser(var3);
      var4.setCreateDate(new Date());
      var4.setName("DataProvider");
      ProviderManager.ins.add(var4);
      var2.setDataProviderId(var4.getId());
      if (var4.getFields() != null) {
         for(BatchDataProviderField var6 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var4.getFields())) {
            this.a(var1, var3, var4, var6);
         }
      }

   }

   private boolean a(BatchDataProviderField var1) {
      return "Object".equals(var1.getDataType()) || "List".equals(var1.getDataType()) || "JsonObject".equals(var1.getDataType()) || "JsonArray".equals(var1.getDataType());
   }

   private void a(Batch var1, String var2, BatchDataProvider var3, BatchDataProviderField var4) {
      var4.setBatchId(var1.getId());
      var4.setProviderId(var3.getId());
      var4.setProjectId(var1.getProjectId());
      var4.setId(IDGenerator.getInstance().nextId(IDType.BATCH_PROVIDER_FIELD));
      var4.setCreateUser(var2);
      var4.setCreateDate(new Date());
      if ((var4.getDataProviderId() == null || var4.getDataProviderId() == 0L) && this.a(var4)) {
         this.a(var1, var2, var4);
      }

      ProviderFieldManager.ins.add(var4);
   }

   private void a(Batch var1, String var2, BatchDataProviderField var3) {
      BatchDataProvider var4 = var3.getDataProvider();
      if (var4 == null) {
         var4 = new BatchDataProvider();
         var4.setDatasourceId(0L);
         var4.setName("Field Data Provider");
         var3.setDataProvider(var4);
      }

      var4.setSupportsPaging(false);
      var4.setId(IDGenerator.getInstance().nextId(IDType.BATCH_DATA_PROVIDER));
      var4.setBatchId(var1.getId());
      var4.setProjectId(var1.getProjectId());
      var4.setCreateUser(var2);
      var4.setCreateDate(new Date());
      ProviderManager.ins.add(var3.getDataProvider());
      var3.setDataProviderId(var4.getId());
      if (var4.getFields() != null) {
         for(BatchDataProviderField var6 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var4.getFields())) {
            this.a(var1, var2, var4, var6);
         }
      }

   }

   private void b(Batch var1, String var2) {
      BatchDataResolver var3 = var1.getDataResolver();
      var3.setId(IDGenerator.getInstance().nextId(IDType.BATCH_DATA_RESOLVER));
      var3.setBatchId(var1.getId());
      var3.setProjectId(var1.getProjectId());
      var3.setCreateUser(var2);
      var3.setCreateDate(new Date());
      var3.setName("DataResolver");
      ResolverManager.ins.add(var3);
      var1.setResolverId(var3.getId());
      if (var3.getItems() != null) {
         for(BatchDataResolverItem var5 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var3.getItems())) {
            this.a(var2, var3, var5);
         }
      }

   }

   private void a(String var1, BatchDataResolver var2, BatchDataResolverItem var3) {
      var3.setId(IDGenerator.getInstance().nextId(IDType.BATCH_RESOLVER_ITEM));
      var3.setResolverId(var2.getId());
      var3.setBatchId(var2.getBatchId());
      var3.setProjectId(var2.getProjectId());
      var3.setCreateUser(var1);
      var3.setCreateDate(new Date());
      ResolverItemManager.ins.add(var3);
      if (var3.getFields() != null) {
         for(BatchDataResolverItemField var5 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var3.getFields())) {
            this.a(var1, var3, var5);
         }
      }

   }

   private void a(String var1, BatchDataResolverItem var2, BatchDataResolverItemField var3) {
      var3.setBatchId(var2.getBatchId());
      var3.setResolverItemId(var2.getId());
      var3.setResolverId(var2.getResolverId());
      var3.setProjectId(var2.getProjectId());
      var3.setId(IDGenerator.getInstance().nextId(IDType.BATCH_RESOLVER_FIELD));
      var3.setCreateUser(var1);
      var3.setCreateDate(new Date());
      ResolverFieldManager.ins.add(var3);
   }

   public void disable(Long var1, String var2) {
      Batch var3 = BatchManager.ins.get(var1);
      if (var3 != null) {
         var3.setEnable(false);
         var3.setUpdateDate(new Date());
         var3.setUpdateUser(var2);
         BatchManager.ins.update(var3);
      }

   }

   public void enable(Long var1, String var2) {
      Batch var3 = this.getBatchData(var1);
      if (var3 != null) {
         if (StringUtils.isBlank(var3.getName())) {
            throw new RuleException("批处理名称不能为空");
         }

         if (var3.getPacketId() == null || var3.getPacketId() == 0L) {
            throw new RuleException("批处理知识包属性没有设置");
         }

         if (var3.isRestEnable() && var3.isRestSecurityEnable() && (StringUtils.isBlank(var3.getRestSecurityUser()) || StringUtils.isBlank(var3.getRestSecurityPassword()))) {
            throw new RuleException("批处理启用Rest安全设置后需要设置对应的用户名和密码");
         }

         if (var3.isThreadMulti() && (var3.getThreadSize() <= 0 || var3.getThreadDataSize() <= 0)) {
            throw new RuleException("批处理启用多线程后线程数或线程数据量大小必须大于0");
         }

         for(DataParam var6 : (Iterable<DataParam>)(Iterable<?>)(var3.getPacketParams())) {
            BatchDataProvider var7 = var6.getDataProvider();
            if (var7 != null) {
               this.a(var7);
            }
         }

         BatchDataProvider var8 = var3.getDataProvider();
         if (var8 == null) {
            throw new RuleException("批处理未定义数据加载器");
         }

         this.a(var8);
         BatchDataResolver var9 = var3.getDataResolver();
         if (var9 == null) {
            throw new RuleException("批处理未定义数据处理器");
         }

         this.a(var9);
         var3.setEnable(true);
         var3.setUpdateDate(new Date());
         var3.setUpdateUser(var2);
         BatchManager.ins.update(var3);
      }

   }

   private void a(BatchDataProvider var1) {
      if (StringUtils.isBlank(var1.getPacketVarName())) {
         throw new RuleException("数据加载器对象的知识包变量名未绑定");
      } else if (StringUtils.isBlank(var1.getPageSql())) {
         throw new RuleException("数据加载器对象的分页SQL没有定义");
      } else if (var1.isSupportsPaging() && StringUtils.isBlank(var1.getCountSql())) {
         throw new RuleException("数据加载器对象的总记录数SQL没有定义");
      } else {
         DataSource var2 = var1.getDatasource();
         if (var2 == null) {
            throw new RuleException("数据加载器对象未绑定数据源");
         } else {
            List var3 = var1.getFields();
            if (var3 != null && var3.size() != 0) {
               for(BatchDataProviderField var5 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var3)) {
                  if (StringUtils.isBlank(var5.getDestProperty())) {
                     throw new RuleException("数据加载器对象变量映射绑定属性设置不完整,未设置变量属性");
                  }

                  if (StringUtils.isBlank(var5.getDataType())) {
                     throw new RuleException("数据加载器对象变量映射绑定属性设置不完整,未设置变量数据类型");
                  }

                  if (!var5.getDataType().equalsIgnoreCase("Object") && !var5.getDataType().equalsIgnoreCase("List")) {
                     if (StringUtils.isBlank(var5.getDestProperty())) {
                        throw new RuleException("数据加载器对象变量映射绑定属性设置不完整,未设置字段属性");
                     }
                  } else {
                     BatchDataProvider var6 = var5.getDataProvider();
                     if (var6 == null) {
                        throw new RuleException("数据加载器对象变量未定义数据加载器");
                     }

                     this.a(var6);
                  }
               }

            } else {
               throw new RuleException("数据加载器对象未设置变量映射");
            }
         }
      }
   }

   private void a(BatchDataResolver var1) {
      DataSource var2 = var1.getDatasource();
      if (var2 == null) {
         throw new RuleException("数据处理器对象未绑定数据源");
      } else {
         List var3 = var1.getItems();
         if (var3 != null && var3.size() != 0) {
            HashMap var4 = new HashMap();

            for(BatchDataResolverItem var6 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var3)) {
               if (StringUtils.isBlank(var6.getName())) {
                  throw new RuleException("数据更新项的名称未设置");
               }

               if (StringUtils.isBlank(var6.getTableName())) {
                  throw new RuleException("数据更新项【" + var6.getName() + "】的目标物理表未设置");
               }

               List var7 = var6.getFields();
               if (var7 == null || var7.size() == 0) {
                  throw new RuleException("数据更新项【" + var6.getName() + "】未配置数据映射");
               }

               boolean var8 = false;
               boolean var9 = false;

               for(BatchDataResolverItemField var11 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var7)) {
                  if (StringUtils.isBlank(var11.getSrcProperty())) {
                     throw new RuleException("数据更新项的数据映射定义不完整,没有定义对应的变量属性名");
                  }

                  if (StringUtils.isBlank(var11.getDataType())) {
                     throw new RuleException("数据更新项的数据映射定义不完整,没有定义对应的变量数据类型");
                  }

                  if (StringUtils.isBlank(var11.getDestProperty())) {
                     throw new RuleException("数据更新项的数据映射定义不完整,没有定义对应的字段名");
                  }

                  if (var11.isKey()) {
                     var8 = true;
                  } else {
                     var9 = true;
                  }
               }

               if ((var6.getUpdateMode() == BatchUpdateMode.update || var6.getUpdateMode() == BatchUpdateMode.delete) && !var8) {
                  throw new RuleException("数据更新项【" + var6.getName() + "】数据映射配置中未定义主键");
               }

               if (var6.getUpdateMode() == BatchUpdateMode.update && !var9) {
                  throw new RuleException("数据更新项【" + var6.getName() + "】数据映射配置中未定义更新字段");
               }

               var4.put(var6.getName(), var6.getName());
            }

            if (var4.size() < var3.size()) {
               throw new RuleException("数据更新项名称必须唯一");
            }
         } else {
            throw new RuleException("数据处理器对象未添加数据更新项");
         }
      }
   }

   public Batch getBatchData(Long var1) {
      Batch var2 = BatchManager.ins.get(var1);
      if (StringUtils.isNotBlank(var2.getInputData())) {
         try {
            List var3 = (List)JsonUtils.getObjectJsonMapper().readValue(var2.getInputData(), new TypeReference() {
            });
            var2.setParams(var3);
         } catch (Exception var19) {
            var19.printStackTrace();
         }
      }

      if (StringUtils.isNotBlank(var2.getPacketInputData())) {
         try {
            List var22 = (List)JsonUtils.getObjectJsonMapper().readValue(var2.getPacketInputData(), new TypeReference() {
            });
            var2.setPacketParams(var22);

            for(DataParam var5 : (Iterable<DataParam>)(Iterable<?>)(var22)) {
               if (var5.getDataProviderId() != null && var5.getDataProviderId() > 0L) {
                  BatchDataProvider var6 = this.getProviderData(var5.getDataProviderId());
                  if (var6 != null) {
                     var5.setDataProvider(var6);
                  }
               }
            }
         } catch (Exception var21) {
            var21.printStackTrace();
         }
      }

      if (var2.getPacketId() != null && var2.getPacketId() > 0L) {
         Packet var23 = PacketManager.ins.load(var2.getPacketId());
         if (var23 != null) {
            var2.setPacketName(var23.getName());
         }
      }

      BatchDataProvider var24 = this.getProviderData(var2.getProviderId());
      var2.setDataProvider(var24);
      BatchDataResolver var25 = ResolverManager.ins.get(var2.getResolverId());
      ResolverItemQuery var26 = ResolverItemManager.ins.createQuery();
      List var27 = var26.resolverId(var25.getId()).list();

      for(BatchDataResolverItem var8 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var27)) {
         ResolverFieldQuery var9 = ResolverFieldManager.ins.createQuery();
         List var10 = var9.itemId(var8.getId()).list();
         var8.setFields(var10);
         if (StringUtils.isNotBlank(var8.getFilterData())) {
            try {
               List var11 = (List)JsonUtils.getObjectJsonMapper().readValue(var8.getFilterData(), new TypeReference() {
               });

               for(Filter var13 : (Iterable<Filter>)(Iterable<?>)(var11)) {
                  for(FilterItem var16 : (Iterable<FilterItem>)(Iterable<?>)(var13.getItems())) {
                     String var17 = var16.getValue();
                     if (!StringUtils.isBlank(var17)) {
                        if (var16.getType() == FilterType.bean) {
                           var16.setItemObject(Utils.getApplicationContext().getBean(var17));
                        } else if (var16.getType() == FilterType.property) {
                           PropertyFilter var18 = (PropertyFilter)JsonUtils.getObjectJsonMapper().readValue(var17, PropertyFilter.class);
                           var16.setItemObject(var18);
                        }
                     }
                  }
               }

               var8.setFilters(var11);
            } catch (Exception var20) {
               a.error(var20);
            }
         }

         ArrayList var29 = new ArrayList();
         String var30 = null;
         BatchUpdateMode var31 = var8.getUpdateMode();
         if (BatchUpdateMode.insert == var31) {
            var30 = this.c(var29, var8);
            a.info(String.format("Resolver: %s, item: %s, insert sql: %s", var25.getName(), var8.getName(), var30));
         } else if (BatchUpdateMode.update == var31) {
            var30 = this.b((List)var29, (BatchDataResolverItem)var8);
            a.info(String.format("Resolver: %s, item: %s, update sql: %s", var25.getName(), var8.getName(), var30));
         } else if (BatchUpdateMode.delete == var31) {
            var30 = this.a((List)var29, (BatchDataResolverItem)var8);
            a.info(String.format("Resolver: %s, item: %s, delete sql: %s", var25.getName(), var8.getName(), var30));
         }

         var8.setUpdateSql(var30);
         var8.setParams(var29);
      }

      var25.setItems(var27);
      DataSource var28 = DataSourceManager.ins.get(var25.getDatasourceId());
      var25.setDatasource(var28);
      var2.setDataResolver(var25);
      return var2;
   }

   public BatchDataProvider getProviderData(Long var1) {
      BatchDataProvider var2 = ProviderManager.ins.get(var1);
      DataSource var3 = DataSourceManager.ins.get(var2.getDatasourceId());
      var2.setDatasource(var3);
      ProviderFieldQuery var4 = ProviderFieldManager.ins.createQuery();
      if (StringUtils.isNotBlank(var2.getInputData())) {
         try {
            List var5 = (List)JsonUtils.getObjectJsonMapper().readValue(var2.getInputData(), new TypeReference() {
            });
            var2.setParams(var5);
         } catch (Exception var9) {
            a.error(var9);
         }
      }

      List var10 = var4.providerId(var2.getId()).list();

      for(BatchDataProviderField var7 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var10)) {
         if (var7.getDataProviderId() != null && var7.getDataProviderId() > 0L) {
            BatchDataProvider var8 = this.getProviderData(var7.getDataProviderId());
            if (var8 != null) {
               var7.setDataProvider(var8);
            }
         }
      }

      var2.setFields(var10);
      return var2;
   }

   protected String a(List var1, BatchDataResolverItem var2) {
      String var3 = var2.getTableName();
      List var4 = var2.getFields();
      String var5 = "delete from " + var3 + " where ";
      String var6 = "";

      for(BatchDataResolverItemField var8 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var4)) {
         if (var8.isKey()) {
            if (StringUtils.isNotEmpty(var6)) {
               var6 = var6 + ", ";
            }

            var6 = var6 + var8.getDestProperty() + "=? ";
            DataParam var9 = new DataParam();
            var9.setDataType(var8.getDataType());
            var9.setName(var8.getSrcProperty());
            var9.setIndex(var1.size());
            var1.add(var9);
         }
      }

      var5 = var5 + var6;
      return var5;
   }

   protected String b(List var1, BatchDataResolverItem var2) {
      String var3 = var2.getTableName();
      List var4 = var2.getFields();
      String var5 = "update " + var3 + " set ";
      String var6 = "";
      String var7 = "";

      for(BatchDataResolverItemField var9 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var4)) {
         if (!var9.isKey()) {
            if (StringUtils.isNotEmpty(var6)) {
               var6 = var6 + ", ";
            }

            var6 = var6 + var9.getDestProperty() + "=? ";
            DataParam var10 = new DataParam();
            var10.setDataType(var9.getDataType());
            var10.setName(var9.getSrcProperty());
            var10.setIndex(var1.size());
            var1.add(var10);
         }
      }

      for(BatchDataResolverItemField var13 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var4)) {
         if (var13.isKey()) {
            if (StringUtils.isNotEmpty(var7)) {
               var7 = var7 + ", ";
            }

            var7 = var7 + var13.getDestProperty() + "=? ";
            DataParam var14 = new DataParam();
            var14.setDataType(var13.getDataType());
            var14.setName(var13.getSrcProperty());
            var14.setIndex(var1.size());
            var1.add(var14);
         }
      }

      if (StringUtils.isNotBlank(var7)) {
         var7 = " where " + var7;
      }

      var5 = var5 + var6 + var7;
      return var5;
   }

   protected String c(List var1, BatchDataResolverItem var2) {
      String var3 = var2.getTableName();
      List var4 = var2.getFields();
      String var5 = "insert into " + var3 + " ";
      String var6 = "";
      String var7 = "";

      for(BatchDataResolverItemField var9 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var4)) {
         if (StringUtils.isNotEmpty(var6)) {
            var6 = var6 + ", ";
            var7 = var7 + ", ";
         }

         var6 = var6 + var9.getDestProperty();
         var7 = var7 + "?";
         DataParam var10 = new DataParam();
         var10.setDataType(var9.getDataType());
         var10.setName(var9.getSrcProperty());
         var10.setIndex(var1.size());
         var1.add(var10);
      }

      var5 = var5 + "(" + var6 + ") values (" + var7 + ")";
      return var5;
   }

   public void stop(Long var1, String var2) {
      BatchManager.ins.updateStatus(var1, BatchStatus.stop);
   }
}
