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

/** Persists, validates and hydrates batch data-provider/resolver schemes. */
public class SchemeServiceImpl implements SchemeService {
   private static Log logger = LogFactory.getLog(SchemeServiceImpl.class);

   public void add(Batch batchData, String account) {
      batchData.setId(IDGenerator.getInstance().nextId(IDType.BATCH));
      batchData.setStatus(BatchStatus.none);
      batchData.setProjectId(ContextHolder.getProjectId());
      batchData.setCreateUser(account);
      batchData.setCreateDate(new Date());
      BatchManager.ins.add(batchData);
      if (batchData.getPacketParams() != null) {
         for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(batchData.getPacketParams())) {
            if ((dataParam.getDataType().equals("Object") || dataParam.getDataType().equals("List")) && StringUtils.isBlank(dataParam.getBatchParamName())) {
               this.addParameterProvider(batchData, dataParam, account);
            }
         }
      }

      if (batchData.getDataProvider() != null) {
         this.addBatchProvider(batchData, account);
      }

      if (batchData.getDataResolver() != null) {
         this.addResolver(batchData, account);
      }

      if (batchData.getDataProvider() != null || batchData.getDataResolver() != null) {
         batchData.setUpdateUser(account);
         batchData.setUpdateDate(new Date());
         BatchManager.ins.update(batchData);
      }

   }

   public void update(Batch batch, String account) {
      batch.setUpdateUser(account);
      batch.setUpdateDate(new Date());
      BatchManager.ins.update(batch);
      if (batch.getPacketParams() != null) {
         for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(batch.getPacketParams())) {
            if ((dataParam.getDataType().equals("Object") || dataParam.getDataType().equals("List")) && StringUtils.isBlank(dataParam.getBatchParamName())) {
            this.updateParameterProvider(batch, account, dataParam, dataParam.getDataProvider());
            }
         }
      }

      if (batch.getDataProvider() != null) {
         BatchDataProvider dataProvider = batch.getDataProvider();
         this.updateBatchProvider(batch, account, dataProvider);
      }

      if (batch.getDataResolver() != null) {
         BatchDataResolver dataResolver = batch.getDataResolver();
         if (dataResolver.getId() != null && dataResolver.getId() != 0L) {
            dataResolver.setUpdateUser(account);
            dataResolver.setUpdateDate(new Date());
            ResolverManager.ins.update(dataResolver);
            ArrayList items = new ArrayList();
            List items2 = ResolverItemManager.ins.createQuery().resolverId(dataResolver.getId()).list();

            for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(dataResolver.getItems())) {
               if (batchDataResolverItem.getId() != null && batchDataResolverItem.getId() != 0L) {
                  batchDataResolverItem.setUpdateUser(account);
                  batchDataResolverItem.setUpdateDate(new Date());
                  ResolverItemManager.ins.update(batchDataResolverItem);
                  List items3 = ResolverFieldManager.ins.createQuery().itemId(batchDataResolverItem.getId()).list();
                  ArrayList items4 = new ArrayList();

                  for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(batchDataResolverItem.getFields())) {
                     if (batchDataResolverItemField.getId() != 0L && batchDataResolverItemField.getId() != null) {
                        batchDataResolverItemField.setUpdateUser(account);
                        batchDataResolverItemField.setUpdateDate(new Date());
                        ResolverFieldManager.ins.update(batchDataResolverItemField);
                     } else {
                        this.addResolverField(account, batchDataResolverItem, batchDataResolverItemField);
                     }

                     items4.add(batchDataResolverItemField.getId());
                  }

                  for(BatchDataResolverItemField batchDataResolverItemField2 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(items3)) {
                     if (!items4.contains(batchDataResolverItemField2.getId())) {
                        ResolverFieldManager.ins.remove(batchDataResolverItemField2.getId());
                     }
                  }
               } else {
                  this.addResolverItem(account, dataResolver, batchDataResolverItem);
               }

               items.add(batchDataResolverItem.getId());
            }

            for(BatchDataResolverItem batchDataResolverItem2 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(items2)) {
               if (!items.contains(batchDataResolverItem2.getId())) {
                  BatchManagerHelper.removeResolverItem(batchDataResolverItem2.getId());
               }
            }
         } else {
            this.addResolver(batch, account);
         }
      }

      BatchManager.ins.update(batch);
   }

   private void updateParameterProvider(Batch batch, String account, DataParam dataParam, BatchDataProvider batchDataProvider) {
      if (batchDataProvider.getId() != null && batchDataProvider.getId() != 0L) {
         batchDataProvider.setSupportsPaging(false);
         batchDataProvider.setUpdateUser(account);
         batchDataProvider.setUpdateDate(new Date());
         ProviderManager.ins.update(batchDataProvider);
         List items = ProviderFieldManager.ins.createQuery().providerId(batchDataProvider.getId()).list();
         ArrayList items2 = new ArrayList();

         for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(batchDataProvider.getFields())) {
            if (batchDataProviderField.getId() != null && batchDataProviderField.getId() != 0L) {
               batchDataProviderField.setUpdateUser(account);
               batchDataProviderField.setUpdateDate(new Date());
               if (this.isCompositeField(batchDataProviderField)) {
                  if (batchDataProviderField.getDataProviderId() != null && batchDataProviderField.getDataProviderId() != 0L && batchDataProviderField.getDataProvider() != null) {
                     this.updateNestedProvider(batch, account, batchDataProviderField, batchDataProviderField.getDataProvider());
                  } else {
                     this.addNestedProvider(batch, account, batchDataProviderField);
                  }
               }

               ProviderFieldManager.ins.update(batchDataProviderField);
            } else {
               this.addProviderField(batch, account, batchDataProvider, batchDataProviderField);
            }

            items2.add(batchDataProviderField.getId());
         }

         for(BatchDataProviderField batchDataProviderField2 : (Iterable<BatchDataProviderField>)(Iterable<?>)(items)) {
            if (!items2.contains(batchDataProviderField2.getId())) {
               if (batchDataProviderField2.getDataProviderId() != null) {
                  this.removeProvider(batchDataProviderField2.getDataProviderId(), batch.getId());
               }

               ProviderFieldManager.ins.remove(batchDataProviderField2.getId());
            }
         }
      } else {
         this.addParameterProvider(batch, dataParam, account);
      }

   }

   private void updateBatchProvider(Batch batch, String account, BatchDataProvider batchDataProvider) {
      if (batchDataProvider.getId() != null && batchDataProvider.getId() != 0L) {
         batchDataProvider.setUpdateUser(account);
         batchDataProvider.setUpdateDate(new Date());
         ProviderManager.ins.update(batchDataProvider);
         List items = ProviderFieldManager.ins.createQuery().providerId(batchDataProvider.getId()).list();
         ArrayList items2 = new ArrayList();

         for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(batchDataProvider.getFields())) {
            if (batchDataProviderField.getId() != null && batchDataProviderField.getId() != 0L) {
               batchDataProviderField.setUpdateUser(account);
               batchDataProviderField.setUpdateDate(new Date());
               if (this.isCompositeField(batchDataProviderField)) {
                  if (batchDataProviderField.getDataProviderId() != null && batchDataProviderField.getDataProviderId() != 0L && batchDataProviderField.getDataProvider() != null) {
                     this.updateNestedProvider(batch, account, batchDataProviderField, batchDataProviderField.getDataProvider());
                  } else {
                     this.addNestedProvider(batch, account, batchDataProviderField);
                  }
               }

               ProviderFieldManager.ins.update(batchDataProviderField);
            } else {
               this.addProviderField(batch, account, batchDataProvider, batchDataProviderField);
            }

            items2.add(batchDataProviderField.getId());
         }

         for(BatchDataProviderField batchDataProviderField2 : (Iterable<BatchDataProviderField>)(Iterable<?>)(items)) {
            if (!items2.contains(batchDataProviderField2.getId())) {
               if (batchDataProviderField2.getDataProviderId() != null) {
                  this.removeProvider(batchDataProviderField2.getDataProviderId(), batch.getId());
               }

               ProviderFieldManager.ins.remove(batchDataProviderField2.getId());
            }
         }
      } else {
         this.addBatchProvider(batch, account);
      }

   }

   private void updateNestedProvider(Batch batch, String account, BatchDataProviderField batchDataProviderField, BatchDataProvider batchDataProvider) {
      if (batchDataProvider.getId() != null && batchDataProvider.getId() != 0L) {
         batchDataProvider.setSupportsPaging(false);
         batchDataProvider.setUpdateUser(account);
         batchDataProvider.setUpdateDate(new Date());
         ProviderManager.ins.update(batchDataProvider);
         List items = ProviderFieldManager.ins.createQuery().providerId(batchDataProvider.getId()).list();
         ArrayList items2 = new ArrayList();

         for(BatchDataProviderField batchDataProviderField2 : (Iterable<BatchDataProviderField>)(Iterable<?>)(batchDataProvider.getFields())) {
            if (batchDataProviderField2.getId() != null && batchDataProviderField2.getId() != 0L) {
               batchDataProviderField2.setUpdateUser(account);
               batchDataProviderField2.setUpdateDate(new Date());
               ProviderFieldManager.ins.update(batchDataProviderField2);
               if (this.isCompositeField(batchDataProviderField2)) {
                  if (batchDataProviderField2.getDataProviderId() != null && batchDataProviderField2.getDataProviderId() != 0L && batchDataProviderField2.getDataProvider() != null) {
                     this.updateNestedProvider(batch, account, batchDataProviderField2, batchDataProviderField2.getDataProvider());
                  } else {
                     this.addNestedProvider(batch, account, batchDataProviderField2);
                  }
               }
            } else {
               this.addProviderField(batch, account, batchDataProvider, batchDataProviderField2);
            }

            items2.add(batchDataProviderField2.getId());
         }

         for(BatchDataProviderField batchDataProviderField3 : (Iterable<BatchDataProviderField>)(Iterable<?>)(items)) {
            if (!items2.contains(batchDataProviderField3.getId())) {
               ProviderFieldManager.ins.remove(batchDataProviderField3.getId());
               if (batchDataProviderField3.getDataProviderId() != null) {
                  this.removeProvider(batchDataProviderField3.getDataProviderId(), batch.getId());
               }
            }
         }
      } else {
         this.addNestedProvider(batch, account, batchDataProviderField);
      }

   }

   public void removeProvider(long id, long batchId) {
      for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(ProviderFieldManager.ins.createQuery().providerId(id).batchId(batchId).list())) {
         if (batchDataProviderField.getDataProviderId() != null) {
            this.removeProvider(batchDataProviderField.getDataProviderId(), batchId);
         }

         ProviderFieldManager.ins.remove(batchDataProviderField.getId());
      }

      ProviderManager.ins.remove(id);
   }

   public void remove(Long id) {
      ProviderFieldManager.ins.removeByBatchId(id);
      ProviderManager.ins.removeByBatchId(id);
      ResolverFieldManager.ins.removeByBatchId(id);
      ResolverItemManager.ins.removeByBatchId(id);
      ResolverManager.ins.removeByBatchId(id);
      BatchManager.ins.remove(id);
   }

   private void addBatchProvider(Batch batch, String account) {
      BatchDataProvider dataProvider = batch.getDataProvider();
      dataProvider.setId(IDGenerator.getInstance().nextId(IDType.BATCH_DATA_PROVIDER));
      dataProvider.setBatchId(batch.getId());
      dataProvider.setProjectId(batch.getProjectId());
      dataProvider.setCreateUser(account);
      dataProvider.setCreateDate(new Date());
      dataProvider.setName("DataProvider");
      ProviderManager.ins.add(dataProvider);
      batch.setProviderId(dataProvider.getId());
      if (dataProvider.getFields() != null) {
         for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(dataProvider.getFields())) {
            this.addProviderField(batch, account, dataProvider, batchDataProviderField);
         }
      }

   }

   private void addParameterProvider(Batch batch, DataParam dataParam, String account) {
      BatchDataProvider dataProvider = dataParam.getDataProvider();
      if (dataProvider == null) {
         dataProvider = new BatchDataProvider();
         dataProvider.setDatasourceId(0L);
         dataProvider.setName("Packet Parameter Data Provider");
         dataParam.setDataProvider(dataProvider);
      }

      dataProvider.setSupportsPaging(false);
      dataProvider.setId(IDGenerator.getInstance().nextId(IDType.BATCH_DATA_PROVIDER));
      dataProvider.setBatchId(batch.getId());
      dataProvider.setProjectId(batch.getProjectId());
      dataProvider.setCreateUser(account);
      dataProvider.setCreateDate(new Date());
      dataProvider.setName("DataProvider");
      ProviderManager.ins.add(dataProvider);
      dataParam.setDataProviderId(dataProvider.getId());
      if (dataProvider.getFields() != null) {
         for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(dataProvider.getFields())) {
            this.addProviderField(batch, account, dataProvider, batchDataProviderField);
         }
      }

   }

   private boolean isCompositeField(BatchDataProviderField batchDataProviderField) {
      return "Object".equals(batchDataProviderField.getDataType()) || "List".equals(batchDataProviderField.getDataType()) || "JsonObject".equals(batchDataProviderField.getDataType()) || "JsonArray".equals(batchDataProviderField.getDataType());
   }

   private void addProviderField(Batch batch, String account, BatchDataProvider batchDataProvider, BatchDataProviderField batchDataProviderField) {
      batchDataProviderField.setBatchId(batch.getId());
      batchDataProviderField.setProviderId(batchDataProvider.getId());
      batchDataProviderField.setProjectId(batch.getProjectId());
      batchDataProviderField.setId(IDGenerator.getInstance().nextId(IDType.BATCH_PROVIDER_FIELD));
      batchDataProviderField.setCreateUser(account);
      batchDataProviderField.setCreateDate(new Date());
      if ((batchDataProviderField.getDataProviderId() == null || batchDataProviderField.getDataProviderId() == 0L) && this.isCompositeField(batchDataProviderField)) {
         this.addNestedProvider(batch, account, batchDataProviderField);
      }

      ProviderFieldManager.ins.add(batchDataProviderField);
   }

   private void addNestedProvider(Batch batch, String account, BatchDataProviderField batchDataProviderField) {
      BatchDataProvider dataProvider = batchDataProviderField.getDataProvider();
      if (dataProvider == null) {
         dataProvider = new BatchDataProvider();
         dataProvider.setDatasourceId(0L);
         dataProvider.setName("Field Data Provider");
         batchDataProviderField.setDataProvider(dataProvider);
      }

      dataProvider.setSupportsPaging(false);
      dataProvider.setId(IDGenerator.getInstance().nextId(IDType.BATCH_DATA_PROVIDER));
      dataProvider.setBatchId(batch.getId());
      dataProvider.setProjectId(batch.getProjectId());
      dataProvider.setCreateUser(account);
      dataProvider.setCreateDate(new Date());
      ProviderManager.ins.add(batchDataProviderField.getDataProvider());
      batchDataProviderField.setDataProviderId(dataProvider.getId());
      if (dataProvider.getFields() != null) {
         for(BatchDataProviderField batchDataProviderField2 : (Iterable<BatchDataProviderField>)(Iterable<?>)(dataProvider.getFields())) {
            this.addProviderField(batch, account, dataProvider, batchDataProviderField2);
         }
      }

   }

   private void addResolver(Batch batch, String account) {
      BatchDataResolver dataResolver = batch.getDataResolver();
      dataResolver.setId(IDGenerator.getInstance().nextId(IDType.BATCH_DATA_RESOLVER));
      dataResolver.setBatchId(batch.getId());
      dataResolver.setProjectId(batch.getProjectId());
      dataResolver.setCreateUser(account);
      dataResolver.setCreateDate(new Date());
      dataResolver.setName("DataResolver");
      ResolverManager.ins.add(dataResolver);
      batch.setResolverId(dataResolver.getId());
      if (dataResolver.getItems() != null) {
         for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(dataResolver.getItems())) {
            this.addResolverItem(account, dataResolver, batchDataResolverItem);
         }
      }

   }

   private void addResolverItem(String account, BatchDataResolver batchDataResolver, BatchDataResolverItem batchDataResolverItem) {
      batchDataResolverItem.setId(IDGenerator.getInstance().nextId(IDType.BATCH_RESOLVER_ITEM));
      batchDataResolverItem.setResolverId(batchDataResolver.getId());
      batchDataResolverItem.setBatchId(batchDataResolver.getBatchId());
      batchDataResolverItem.setProjectId(batchDataResolver.getProjectId());
      batchDataResolverItem.setCreateUser(account);
      batchDataResolverItem.setCreateDate(new Date());
      ResolverItemManager.ins.add(batchDataResolverItem);
      if (batchDataResolverItem.getFields() != null) {
         for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(batchDataResolverItem.getFields())) {
            this.addResolverField(account, batchDataResolverItem, batchDataResolverItemField);
         }
      }

   }

   private void addResolverField(String account, BatchDataResolverItem batchDataResolverItem, BatchDataResolverItemField batchDataResolverItemField) {
      batchDataResolverItemField.setBatchId(batchDataResolverItem.getBatchId());
      batchDataResolverItemField.setResolverItemId(batchDataResolverItem.getId());
      batchDataResolverItemField.setResolverId(batchDataResolverItem.getResolverId());
      batchDataResolverItemField.setProjectId(batchDataResolverItem.getProjectId());
      batchDataResolverItemField.setId(IDGenerator.getInstance().nextId(IDType.BATCH_RESOLVER_FIELD));
      batchDataResolverItemField.setCreateUser(account);
      batchDataResolverItemField.setCreateDate(new Date());
      ResolverFieldManager.ins.add(batchDataResolverItemField);
   }

   public void disable(Long id, String account) {
      Batch batch = BatchManager.ins.get(id);
      if (batch != null) {
         batch.setEnable(false);
         batch.setUpdateDate(new Date());
         batch.setUpdateUser(account);
         BatchManager.ins.update(batch);
      }

   }

   public void enable(Long id, String account) {
      Batch batchData = this.getBatchData(id);
      if (batchData != null) {
         if (StringUtils.isBlank(batchData.getName())) {
            throw new RuleException("批处理名称不能为空");
         }

         if (batchData.getPacketId() == null || batchData.getPacketId() == 0L) {
            throw new RuleException("批处理知识包属性没有设置");
         }

         if (batchData.isRestEnable() && batchData.isRestSecurityEnable() && (StringUtils.isBlank(batchData.getRestSecurityUser()) || StringUtils.isBlank(batchData.getRestSecurityPassword()))) {
            throw new RuleException("批处理启用Rest安全设置后需要设置对应的用户名和密码");
         }

         if (batchData.isThreadMulti() && (batchData.getThreadSize() <= 0 || batchData.getThreadDataSize() <= 0)) {
            throw new RuleException("批处理启用多线程后线程数或线程数据量大小必须大于0");
         }

         for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(batchData.getPacketParams())) {
            BatchDataProvider dataProvider = dataParam.getDataProvider();
            if (dataProvider != null) {
               this.validateProvider(dataProvider);
            }
         }

         BatchDataProvider dataProvider2 = batchData.getDataProvider();
         if (dataProvider2 == null) {
            throw new RuleException("批处理未定义数据加载器");
         }

         this.validateProvider(dataProvider2);
         BatchDataResolver dataResolver = batchData.getDataResolver();
         if (dataResolver == null) {
            throw new RuleException("批处理未定义数据处理器");
         }

         this.validateResolver(dataResolver);
         batchData.setEnable(true);
         batchData.setUpdateDate(new Date());
         batchData.setUpdateUser(account);
         BatchManager.ins.update(batchData);
      }

   }

   private void validateProvider(BatchDataProvider batchDataProvider) {
      if (StringUtils.isBlank(batchDataProvider.getPacketVarName())) {
         throw new RuleException("数据加载器对象的知识包变量名未绑定");
      } else if (StringUtils.isBlank(batchDataProvider.getPageSql())) {
         throw new RuleException("数据加载器对象的分页SQL没有定义");
      } else if (batchDataProvider.isSupportsPaging() && StringUtils.isBlank(batchDataProvider.getCountSql())) {
         throw new RuleException("数据加载器对象的总记录数SQL没有定义");
      } else {
         DataSource dataSource = batchDataProvider.getDatasource();
         if (dataSource == null) {
            throw new RuleException("数据加载器对象未绑定数据源");
         } else {
            List fields = batchDataProvider.getFields();
            if (fields != null && fields.size() != 0) {
               for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(fields)) {
                  if (StringUtils.isBlank(batchDataProviderField.getDestProperty())) {
                     throw new RuleException("数据加载器对象变量映射绑定属性设置不完整,未设置变量属性");
                  }

                  if (StringUtils.isBlank(batchDataProviderField.getDataType())) {
                     throw new RuleException("数据加载器对象变量映射绑定属性设置不完整,未设置变量数据类型");
                  }

                  if (!batchDataProviderField.getDataType().equalsIgnoreCase("Object") && !batchDataProviderField.getDataType().equalsIgnoreCase("List")) {
                     if (StringUtils.isBlank(batchDataProviderField.getDestProperty())) {
                        throw new RuleException("数据加载器对象变量映射绑定属性设置不完整,未设置字段属性");
                     }
                  } else {
                     BatchDataProvider dataProvider = batchDataProviderField.getDataProvider();
                     if (dataProvider == null) {
                        throw new RuleException("数据加载器对象变量未定义数据加载器");
                     }

                     this.validateProvider(dataProvider);
                  }
               }

            } else {
               throw new RuleException("数据加载器对象未设置变量映射");
            }
         }
      }
   }

   private void validateResolver(BatchDataResolver batchDataResolver) {
      DataSource dataSource = batchDataResolver.getDatasource();
      if (dataSource == null) {
         throw new RuleException("数据处理器对象未绑定数据源");
      } else {
         List items = batchDataResolver.getItems();
         if (items != null && items.size() != 0) {
            HashMap valuesByKey = new HashMap();

            for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(items)) {
               if (StringUtils.isBlank(batchDataResolverItem.getName())) {
                  throw new RuleException("数据更新项的名称未设置");
               }

               if (StringUtils.isBlank(batchDataResolverItem.getTableName())) {
                  throw new RuleException("数据更新项【" + batchDataResolverItem.getName() + "】的目标物理表未设置");
               }

               List fields = batchDataResolverItem.getFields();
               if (fields == null || fields.size() == 0) {
                  throw new RuleException("数据更新项【" + batchDataResolverItem.getName() + "】未配置数据映射");
               }

               boolean flag = false;
               boolean flag2 = false;

               for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(fields)) {
                  if (StringUtils.isBlank(batchDataResolverItemField.getSrcProperty())) {
                     throw new RuleException("数据更新项的数据映射定义不完整,没有定义对应的变量属性名");
                  }

                  if (StringUtils.isBlank(batchDataResolverItemField.getDataType())) {
                     throw new RuleException("数据更新项的数据映射定义不完整,没有定义对应的变量数据类型");
                  }

                  if (StringUtils.isBlank(batchDataResolverItemField.getDestProperty())) {
                     throw new RuleException("数据更新项的数据映射定义不完整,没有定义对应的字段名");
                  }

                  if (batchDataResolverItemField.isKey()) {
                     flag = true;
                  } else {
                     flag2 = true;
                  }
               }

               if ((batchDataResolverItem.getUpdateMode() == BatchUpdateMode.update || batchDataResolverItem.getUpdateMode() == BatchUpdateMode.delete) && !flag) {
                  throw new RuleException("数据更新项【" + batchDataResolverItem.getName() + "】数据映射配置中未定义主键");
               }

               if (batchDataResolverItem.getUpdateMode() == BatchUpdateMode.update && !flag2) {
                  throw new RuleException("数据更新项【" + batchDataResolverItem.getName() + "】数据映射配置中未定义更新字段");
               }

               valuesByKey.put(batchDataResolverItem.getName(), batchDataResolverItem.getName());
            }

            if (valuesByKey.size() < items.size()) {
               throw new RuleException("数据更新项名称必须唯一");
            }
         } else {
            throw new RuleException("数据处理器对象未添加数据更新项");
         }
      }
   }

   public Batch getBatchData(Long id) {
      Batch batch = BatchManager.ins.get(id);
      if (StringUtils.isNotBlank(batch.getInputData())) {
         try {
            List items = (List)JsonUtils.getObjectJsonMapper().readValue(batch.getInputData(), new TypeReference() {
            });
            batch.setParams(items);
         } catch (Exception exception) {
            java.util.logging.Logger.getLogger(SchemeServiceImpl.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
         }
      }

      if (StringUtils.isNotBlank(batch.getPacketInputData())) {
         try {
            List items2 = (List)JsonUtils.getObjectJsonMapper().readValue(batch.getPacketInputData(), new TypeReference() {
            });
            batch.setPacketParams(items2);

            for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(items2)) {
               if (dataParam.getDataProviderId() != null && dataParam.getDataProviderId() > 0L) {
                  BatchDataProvider providerData = this.getProviderData(dataParam.getDataProviderId());
                  if (providerData != null) {
                     dataParam.setDataProvider(providerData);
                  }
               }
            }
         } catch (Exception exception2) {
            java.util.logging.Logger.getLogger(SchemeServiceImpl.class.getName()).log(java.util.logging.Level.SEVERE, exception2.getMessage(), exception2);
         }
      }

      if (batch.getPacketId() != null && batch.getPacketId() > 0L) {
         Packet packet = PacketManager.ins.load(batch.getPacketId());
         if (packet != null) {
            batch.setPacketName(packet.getName());
         }
      }

      BatchDataProvider providerData2 = this.getProviderData(batch.getProviderId());
      batch.setDataProvider(providerData2);
      BatchDataResolver batchDataResolver = ResolverManager.ins.get(batch.getResolverId());
      ResolverItemQuery query = ResolverItemManager.ins.createQuery();
      List items3 = query.resolverId(batchDataResolver.getId()).list();

      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(items3)) {
         ResolverFieldQuery query2 = ResolverFieldManager.ins.createQuery();
         List items4 = query2.itemId(batchDataResolverItem.getId()).list();
         batchDataResolverItem.setFields(items4);
         if (StringUtils.isNotBlank(batchDataResolverItem.getFilterData())) {
            try {
               List items5 = (List)JsonUtils.getObjectJsonMapper().readValue(batchDataResolverItem.getFilterData(), new TypeReference() {
               });

               for(Filter filter : (Iterable<Filter>)(Iterable<?>)(items5)) {
                  for(FilterItem filterItem : (Iterable<FilterItem>)(Iterable<?>)(filter.getItems())) {
                     String text = filterItem.getValue();
                     if (!StringUtils.isBlank(text)) {
                        if (filterItem.getType() == FilterType.bean) {
                           filterItem.setItemObject(Utils.getApplicationContext().getBean(text));
                        } else if (filterItem.getType() == FilterType.property) {
                           PropertyFilter propertyFilter = (PropertyFilter)JsonUtils.getObjectJsonMapper().readValue(text, PropertyFilter.class);
                           filterItem.setItemObject(propertyFilter);
                        }
                     }
                  }
               }

               batchDataResolverItem.setFilters(items5);
            } catch (Exception exception3) {
               SchemeServiceImpl.logger.error(exception3);
            }
         }

         ArrayList items6 = new ArrayList();
         String text2 = null;
         BatchUpdateMode updateMode = batchDataResolverItem.getUpdateMode();
         if (BatchUpdateMode.insert == updateMode) {
            text2 = this.buildInsertSql(items6, batchDataResolverItem);
            SchemeServiceImpl.logger.info(String.format("Resolver: %s, item: %s, insert sql: %s", batchDataResolver.getName(), batchDataResolverItem.getName(), text2));
         } else if (BatchUpdateMode.update == updateMode) {
            text2 = this.buildUpdateSql(items6, batchDataResolverItem);
            SchemeServiceImpl.logger.info(String.format("Resolver: %s, item: %s, update sql: %s", batchDataResolver.getName(), batchDataResolverItem.getName(), text2));
         } else if (BatchUpdateMode.delete == updateMode) {
            text2 = this.buildDeleteSql(items6, batchDataResolverItem);
            SchemeServiceImpl.logger.info(String.format("Resolver: %s, item: %s, delete sql: %s", batchDataResolver.getName(), batchDataResolverItem.getName(), text2));
         }

         batchDataResolverItem.setUpdateSql(text2);
         batchDataResolverItem.setParams(items6);
      }

      batchDataResolver.setItems(items3);
      DataSource dataSource = DataSourceManager.ins.get(batchDataResolver.getDatasourceId());
      batchDataResolver.setDatasource(dataSource);
      batch.setDataResolver(batchDataResolver);
      return batch;
   }

   public BatchDataProvider getProviderData(Long providerId) {
      BatchDataProvider batchDataProvider = ProviderManager.ins.get(providerId);
      DataSource dataSource = DataSourceManager.ins.get(batchDataProvider.getDatasourceId());
      batchDataProvider.setDatasource(dataSource);
      ProviderFieldQuery query = ProviderFieldManager.ins.createQuery();
      if (StringUtils.isNotBlank(batchDataProvider.getInputData())) {
         try {
            List items = (List)JsonUtils.getObjectJsonMapper().readValue(batchDataProvider.getInputData(), new TypeReference() {
            });
            batchDataProvider.setParams(items);
         } catch (Exception exception) {
            SchemeServiceImpl.logger.error(exception);
         }
      }

      List items2 = query.providerId(batchDataProvider.getId()).list();

      for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(items2)) {
         if (batchDataProviderField.getDataProviderId() != null && batchDataProviderField.getDataProviderId() > 0L) {
            BatchDataProvider providerData = this.getProviderData(batchDataProviderField.getDataProviderId());
            if (providerData != null) {
               batchDataProviderField.setDataProvider(providerData);
            }
         }
      }

      batchDataProvider.setFields(items2);
      return batchDataProvider;
   }

   protected String buildDeleteSql(List parameters, BatchDataResolverItem resolverItem) {
      String tableName = resolverItem.getTableName();
      List fields = resolverItem.getFields();
      String text = "delete from " + tableName + " where ";
      String text2 = "";

      for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(fields)) {
         if (batchDataResolverItemField.isKey()) {
            if (StringUtils.isNotEmpty(text2)) {
               text2 = text2 + ", ";
            }

            text2 = text2 + batchDataResolverItemField.getDestProperty() + "=? ";
            DataParam dataParam = new DataParam();
            dataParam.setDataType(batchDataResolverItemField.getDataType());
            dataParam.setName(batchDataResolverItemField.getSrcProperty());
            dataParam.setIndex(parameters.size());
            parameters.add(dataParam);
         }
      }

      text = text + text2;
      return text;
   }

   protected String buildUpdateSql(List parameters, BatchDataResolverItem resolverItem) {
      String tableName = resolverItem.getTableName();
      List fields = resolverItem.getFields();
      String text = "update " + tableName + " set ";
      String text2 = "";
      String text3 = "";

      for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(fields)) {
         if (!batchDataResolverItemField.isKey()) {
            if (StringUtils.isNotEmpty(text2)) {
               text2 = text2 + ", ";
            }

            text2 = text2 + batchDataResolverItemField.getDestProperty() + "=? ";
            DataParam dataParam = new DataParam();
            dataParam.setDataType(batchDataResolverItemField.getDataType());
            dataParam.setName(batchDataResolverItemField.getSrcProperty());
            dataParam.setIndex(parameters.size());
            parameters.add(dataParam);
         }
      }

      for(BatchDataResolverItemField batchDataResolverItemField2 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(fields)) {
         if (batchDataResolverItemField2.isKey()) {
            if (StringUtils.isNotEmpty(text3)) {
               text3 = text3 + ", ";
            }

            text3 = text3 + batchDataResolverItemField2.getDestProperty() + "=? ";
            DataParam dataParam2 = new DataParam();
            dataParam2.setDataType(batchDataResolverItemField2.getDataType());
            dataParam2.setName(batchDataResolverItemField2.getSrcProperty());
            dataParam2.setIndex(parameters.size());
            parameters.add(dataParam2);
         }
      }

      if (StringUtils.isNotBlank(text3)) {
         text3 = " where " + text3;
      }

      text = text + text2 + text3;
      return text;
   }

   protected String buildInsertSql(List parameters, BatchDataResolverItem resolverItem) {
      String tableName = resolverItem.getTableName();
      List fields = resolverItem.getFields();
      String text = "insert into " + tableName + " ";
      String text2 = "";
      String text3 = "";

      for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(fields)) {
         if (StringUtils.isNotEmpty(text2)) {
            text2 = text2 + ", ";
            text3 = text3 + ", ";
         }

         text2 = text2 + batchDataResolverItemField.getDestProperty();
         text3 = text3 + "?";
         DataParam dataParam = new DataParam();
         dataParam.setDataType(batchDataResolverItemField.getDataType());
         dataParam.setName(batchDataResolverItemField.getSrcProperty());
         dataParam.setIndex(parameters.size());
         parameters.add(dataParam);
      }

      text = text + "(" + text2 + ") values (" + text3 + ")";
      return text;
   }

   public void stop(Long id, String account) {
      BatchManager.ins.updateStatus(id, BatchStatus.stop);
   }
}
