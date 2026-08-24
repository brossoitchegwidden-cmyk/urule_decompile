package com.bstek.urule.console.admin.batch.in;

import com.bstek.urule.Utils;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.batch.SchemeService;
import com.bstek.urule.console.database.manager.batch.BatchManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.model.batch.BatchDataProviderField;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItemField;
import com.bstek.urule.console.database.model.batch.BatchUpdateMode;
import com.bstek.urule.console.database.model.batch.TranScope;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.exception.RuleException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;

public class BatchImport {
   public static final BatchImport ins = new BatchImport();

   public void doImport(InputStream inputStream, Project project) throws Exception {
      byte[] bytes = IOUtils.toByteArray(inputStream);
      String text2 = Utils.uncompress(bytes);
      Document text = DocumentHelper.parseText(text2);
      Element rootElement = text.getRootElement();
      if (!rootElement.getName().contentEquals("batchs")) {
         throw new InfoException("文件不合法，不能导入，请选择一个URule Pro4+项目导出的备份文件");
      } else {
         this.doImport(rootElement, project);
      }
   }

   public void doImport(Element batchs, Project project) throws Exception {
      for(Object objectValue : batchs.elements()) {
         if (objectValue instanceof Element) {
            Element element = (Element)objectValue;
            Batch batch = new Batch();
            batch.setName(element.attributeValue("name"));
            if (StringUtils.isBlank(batch.getName())) {
               throw new InfoException("文件不合法，不能导入，请选择一个URule Pro4+项目导出的备份文件");
            }

            String name = batch.getName();
            List items = BatchManager.ins.createQuery().projectId(project.getId()).list();
            name = this.ensureUniqueBatchName(items, name);
            batch.setName(name);
            User loginUser = SecurityUtils.getLoginUser(RequestHolder.getRequest());
            batch.setProjectId(project.getId());
            String text = this.readEncodedChild(element, "desc");
            batch.setDesc(text);
            batch.setSkipLimit(Integer.valueOf(element.attributeValue("skip-limit")));
            batch.setAsync(Boolean.valueOf(element.attributeValue("async")));
            batch.setCallbackUrl(element.attributeValue("callback-url"));
            batch.setListener(element.attributeValue("listener"));
            batch.setThreadMulti(Boolean.valueOf(element.attributeValue("thread-multi")));
            batch.setThreadSize(Integer.valueOf(element.attributeValue("thread-size")));
            batch.setThreadDataSize(Integer.valueOf(element.attributeValue("thread-data-size")));
            batch.setPacketId(Long.valueOf(element.attributeValue("packet-id")));
            batch.setProviderId(Long.valueOf(element.attributeValue("provider-id")));
            batch.setResolverId(Long.valueOf(element.attributeValue("resolver-id")));
            batch.setPacketInputData(this.readEncodedChild(element, "packet-input-data"));
            batch.setRestEnable(Boolean.valueOf(element.attributeValue("rest-enable")));
            batch.setRestSecurityEnable(Boolean.valueOf(element.attributeValue("rest-security-enable")));
            batch.setRestSecurityUser(element.attributeValue("rest-security-user"));
            batch.setRestSecurityPassword(element.attributeValue("rest-security-password"));
            batch.setInputData(this.readEncodedChild(element, "input-data"));

            for(Object objectValue2 : element.elements()) {
               if (objectValue2 instanceof Element) {
                  Element element2 = (Element)objectValue2;
                  if (element2.getName().contentEquals("provider")) {
                     BatchDataProvider batchDataProvider = new BatchDataProvider();
                     batch.setDataProvider(batchDataProvider);
                     this.importProvider(element2, batchDataProvider);
                  } else if (element2.getName().contentEquals("resolver")) {
                     BatchDataResolver batchDataResolver = new BatchDataResolver();
                     batch.setDataResolver(batchDataResolver);
                     this.importResolver(element2, batchDataResolver);
                  }
               }
            }

            SchemeService.ins.add(batch, loginUser.getName());
         }
      }

   }

   private String ensureUniqueBatchName(List items, String text) {
      for(int index = 0; index < 10000; ++index) {
         String text2 = text;
         if (index > 0) {
            text2 = text + index;
         }

         boolean flag = false;

         for(Batch batch : (Iterable<Batch>)(Iterable<?>)(items)) {
            if (batch.getName().contentEquals(text2)) {
               flag = true;
               break;
            }
         }

         if (!flag) {
            text = text2;
            break;
         }
      }

      return text;
   }

   private boolean importProvider(Element element, BatchDataProvider batchDataProvider) {
      batchDataProvider.setName(element.attributeValue("name"));
      batchDataProvider.setDatasourceId(Long.valueOf(element.attributeValue("datasource-id")));
      batchDataProvider.setListener(element.attributeValue("listener"));
      batchDataProvider.setDesc(this.readEncodedChild(element, "desc"));
      batchDataProvider.setInputData(this.readEncodedChild(element, "input-data"));
      batchDataProvider.setPacketVarName(element.attributeValue("packet-var-name"));
      batchDataProvider.setSupportsPaging(Boolean.valueOf(element.attributeValue("support-paging")));
      batchDataProvider.setPageSize(Integer.valueOf(element.attributeValue("page-size")));
      batchDataProvider.setPageSql(this.readEncodedChild(element, "page-sql"));
      batchDataProvider.setOrderField(element.attributeValue("order-field"));
      batchDataProvider.setOrderFieldParamName(element.attributeValue("order-field-param-name"));
      batchDataProvider.setPageLimitType(element.attributeValue("page-limit-type"));
      batchDataProvider.setCountSql(this.readEncodedChild(element, "count-sql"));
      batchDataProvider.setFilterData(this.readEncodedChild(element, "filter-data"));

      for(Object objectValue : element.elements()) {
         if (objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().contentEquals("field")) {
               this.importProviderField(element2, batchDataProvider);
            }
         }
      }

      return true;
   }

   private void importProviderField(Element element, BatchDataProvider batchDataProvider) {
      BatchDataProviderField batchDataProviderField = new BatchDataProviderField();
      batchDataProvider.getFields().add(batchDataProviderField);
      batchDataProviderField.setSrcProperty(element.attributeValue("src-property"));
      batchDataProviderField.setDataType(element.attributeValue("data-type"));
      batchDataProviderField.setClassPath(element.attributeValue("classpath"));
      batchDataProviderField.setDestProperty(element.attributeValue("dest-property"));
      String text = element.attributeValue("provider-id");
      if (StringUtils.isNotBlank(text)) {
         long longValue = Long.valueOf(text);
         if (longValue > 0L) {
            for(Object objectValue : element.elements()) {
               if (objectValue instanceof Element) {
                  Element element2 = (Element)objectValue;
                  if (element2.getName().contentEquals("provider")) {
                     BatchDataProvider batchDataProvider2 = new BatchDataProvider();
                     batchDataProviderField.setDataProvider(batchDataProvider2);
                     this.importProvider(element2, batchDataProvider2);
                  }
               }
            }
         }
      }

   }

   private boolean importResolver(Element element, BatchDataResolver batchDataResolver) {
      batchDataResolver.setName(element.attributeValue("name"));
      batchDataResolver.setListener(element.attributeValue("listener"));
      batchDataResolver.setTranScope(TranScope.valueOf(element.attributeValue("tran-scope")));
      batchDataResolver.setFilterData(this.readEncodedChild(element, "filter-data"));
      batchDataResolver.setDatasourceId(Long.valueOf(element.attributeValue("datasource-id")));
      batchDataResolver.setDesc(this.readEncodedChild(element, "desc"));

      for(Object objectValue : element.elements()) {
         if (objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().contentEquals("item")) {
               this.importResolverItem(element2, batchDataResolver);
            }
         }
      }

      return true;
   }

   private void importResolverItem(Element element, BatchDataResolver batchDataResolver) {
      BatchDataResolverItem batchDataResolverItem = new BatchDataResolverItem();
      batchDataResolver.getItems().add(batchDataResolverItem);
      batchDataResolverItem.setName(element.attributeValue("name"));
      batchDataResolverItem.setUpdateMode(BatchUpdateMode.valueOf(element.attributeValue("update-mode")));
      batchDataResolverItem.setTableName(element.attributeValue("table-name"));
      batchDataResolverItem.setFilterData(this.readEncodedChild(element, "filter-data"));
      batchDataResolverItem.setPartitionName(element.attributeValue("partition-name"));
      batchDataResolverItem.setPartitionValue(element.attributeValue("partition-value"));
      batchDataResolverItem.setCommitLimit(Integer.parseInt(element.attributeValue("commit-limit")));
      batchDataResolverItem.setDesc(this.readEncodedChild(element, "desc"));

      for(Object objectValue : element.elements()) {
         if (objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().contentEquals("field")) {
               this.importResolverItemField(element2, batchDataResolverItem);
            }
         }
      }

   }

   private void importResolverItemField(Element element, BatchDataResolverItem batchDataResolverItem) {
      BatchDataResolverItemField batchDataResolverItemField = new BatchDataResolverItemField();
      batchDataResolverItemField.setSrcProperty(element.attributeValue("src-property"));
      batchDataResolverItemField.setKey(Boolean.valueOf(element.attributeValue("key")));
      batchDataResolverItemField.setDataType(element.attributeValue("data-type"));
      batchDataResolverItemField.setDestProperty(element.attributeValue("dest-property"));
      batchDataResolverItem.getFields().add(batchDataResolverItemField);
   }

   private String readEncodedChild(Element element, String text) {
      String text2 = null;

      for(Object objectValue : element.elements()) {
         if (objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().contentEquals(text)) {
               text2 = element2.getText();
               break;
            }
         }
      }

      if (StringUtils.isNotBlank(text2)) {
         try {
            text2 = IOUtils.toString(Base64.getDecoder().decode(text2), "utf-8");
         } catch (IOException iOException) {
            throw new RuleException(iOException);
         }
      }

      return text2;
   }
}
