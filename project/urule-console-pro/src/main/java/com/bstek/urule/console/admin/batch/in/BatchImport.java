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

   public void doImport(InputStream var1, Project var2) throws Exception {
      byte[] var3 = IOUtils.toByteArray(var1);
      String var4 = Utils.uncompress(var3);
      Document var5 = DocumentHelper.parseText(var4);
      Element var6 = var5.getRootElement();
      if (!var6.getName().contentEquals("batchs")) {
         throw new InfoException("文件不合法，不能导入，请选择一个URule Pro4+项目导出的备份文件");
      } else {
         this.doImport(var6, var2);
      }
   }

   public void doImport(Element var1, Project var2) throws Exception {
      for(Object var4 : var1.elements()) {
         if (var4 instanceof Element) {
            Element var5 = (Element)var4;
            Batch var6 = new Batch();
            var6.setName(var5.attributeValue("name"));
            if (StringUtils.isBlank(var6.getName())) {
               throw new InfoException("文件不合法，不能导入，请选择一个URule Pro4+项目导出的备份文件");
            }

            String var7 = var6.getName();
            List var8 = BatchManager.ins.createQuery().projectId(var2.getId()).list();
            var7 = this.a(var8, var7);
            var6.setName(var7);
            User var9 = SecurityUtils.getLoginUser(RequestHolder.getRequest());
            var6.setProjectId(var2.getId());
            String var10 = this.a(var5, "desc");
            var6.setDesc(var10);
            var6.setSkipLimit(Integer.valueOf(var5.attributeValue("skip-limit")));
            var6.setAsync(Boolean.valueOf(var5.attributeValue("async")));
            var6.setCallbackUrl(var5.attributeValue("callback-url"));
            var6.setListener(var5.attributeValue("listener"));
            var6.setThreadMulti(Boolean.valueOf(var5.attributeValue("thread-multi")));
            var6.setThreadSize(Integer.valueOf(var5.attributeValue("thread-size")));
            var6.setThreadDataSize(Integer.valueOf(var5.attributeValue("thread-data-size")));
            var6.setPacketId(Long.valueOf(var5.attributeValue("packet-id")));
            var6.setProviderId(Long.valueOf(var5.attributeValue("provider-id")));
            var6.setResolverId(Long.valueOf(var5.attributeValue("resolver-id")));
            var6.setPacketInputData(this.a(var5, "packet-input-data"));
            var6.setRestEnable(Boolean.valueOf(var5.attributeValue("rest-enable")));
            var6.setRestSecurityEnable(Boolean.valueOf(var5.attributeValue("rest-security-enable")));
            var6.setRestSecurityUser(var5.attributeValue("rest-security-user"));
            var6.setRestSecurityPassword(var5.attributeValue("rest-security-password"));
            var6.setInputData(this.a(var5, "input-data"));

            for(Object var12 : var5.elements()) {
               if (var12 instanceof Element) {
                  Element var13 = (Element)var12;
                  if (var13.getName().contentEquals("provider")) {
                     BatchDataProvider var14 = new BatchDataProvider();
                     var6.setDataProvider(var14);
                     this.a(var13, var14);
                  } else if (var13.getName().contentEquals("resolver")) {
                     BatchDataResolver var16 = new BatchDataResolver();
                     var6.setDataResolver(var16);
                     this.a(var13, var16);
                  }
               }
            }

            SchemeService.ins.add(var6, var9.getName());
         }
      }

   }

   private String a(List var1, String var2) {
      for(int var3 = 0; var3 < 10000; ++var3) {
         String var4 = var2;
         if (var3 > 0) {
            var4 = var2 + var3;
         }

         boolean var5 = false;

         for(Batch var7 : (Iterable<Batch>)(Iterable<?>)(var1)) {
            if (var7.getName().contentEquals(var4)) {
               var5 = true;
               break;
            }
         }

         if (!var5) {
            var2 = var4;
            break;
         }
      }

      return var2;
   }

   private boolean a(Element var1, BatchDataProvider var2) {
      var2.setName(var1.attributeValue("name"));
      var2.setDatasourceId(Long.valueOf(var1.attributeValue("datasource-id")));
      var2.setListener(var1.attributeValue("listener"));
      var2.setDesc(this.a(var1, "desc"));
      var2.setInputData(this.a(var1, "input-data"));
      var2.setPacketVarName(var1.attributeValue("packet-var-name"));
      var2.setSupportsPaging(Boolean.valueOf(var1.attributeValue("support-paging")));
      var2.setPageSize(Integer.valueOf(var1.attributeValue("page-size")));
      var2.setPageSql(this.a(var1, "page-sql"));
      var2.setOrderField(var1.attributeValue("order-field"));
      var2.setOrderFieldParamName(var1.attributeValue("order-field-param-name"));
      var2.setPageLimitType(var1.attributeValue("page-limit-type"));
      var2.setCountSql(this.a(var1, "count-sql"));
      var2.setFilterData(this.a(var1, "filter-data"));

      for(Object var4 : var1.elements()) {
         if (var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().contentEquals("field")) {
               this.b(var5, var2);
            }
         }
      }

      return true;
   }

   private void b(Element var1, BatchDataProvider var2) {
      BatchDataProviderField var3 = new BatchDataProviderField();
      var2.getFields().add(var3);
      var3.setSrcProperty(var1.attributeValue("src-property"));
      var3.setDataType(var1.attributeValue("data-type"));
      var3.setClassPath(var1.attributeValue("classpath"));
      var3.setDestProperty(var1.attributeValue("dest-property"));
      String var4 = var1.attributeValue("provider-id");
      if (StringUtils.isNotBlank(var4)) {
         long var5 = Long.valueOf(var4);
         if (var5 > 0L) {
            for(Object var8 : var1.elements()) {
               if (var8 instanceof Element) {
                  Element var9 = (Element)var8;
                  if (var9.getName().contentEquals("provider")) {
                     BatchDataProvider var10 = new BatchDataProvider();
                     var3.setDataProvider(var10);
                     this.a(var9, var10);
                  }
               }
            }
         }
      }

   }

   private boolean a(Element var1, BatchDataResolver var2) {
      var2.setName(var1.attributeValue("name"));
      var2.setListener(var1.attributeValue("listener"));
      var2.setTranScope(TranScope.valueOf(var1.attributeValue("tran-scope")));
      var2.setFilterData(this.a(var1, "filter-data"));
      var2.setDatasourceId(Long.valueOf(var1.attributeValue("datasource-id")));
      var2.setDesc(this.a(var1, "desc"));

      for(Object var4 : var1.elements()) {
         if (var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().contentEquals("item")) {
               this.b(var5, var2);
            }
         }
      }

      return true;
   }

   private void b(Element var1, BatchDataResolver var2) {
      BatchDataResolverItem var3 = new BatchDataResolverItem();
      var2.getItems().add(var3);
      var3.setName(var1.attributeValue("name"));
      var3.setUpdateMode(BatchUpdateMode.valueOf(var1.attributeValue("update-mode")));
      var3.setTableName(var1.attributeValue("table-name"));
      var3.setFilterData(this.a(var1, "filter-data"));
      var3.setPartitionName(var1.attributeValue("partition-name"));
      var3.setPartitionValue(var1.attributeValue("partition-value"));
      var3.setCommitLimit(Integer.parseInt(var1.attributeValue("commit-limit")));
      var3.setDesc(this.a(var1, "desc"));

      for(Object var5 : var1.elements()) {
         if (var5 instanceof Element) {
            Element var6 = (Element)var5;
            if (var6.getName().contentEquals("field")) {
               this.a(var6, var3);
            }
         }
      }

   }

   private void a(Element var1, BatchDataResolverItem var2) {
      BatchDataResolverItemField var3 = new BatchDataResolverItemField();
      var3.setSrcProperty(var1.attributeValue("src-property"));
      var3.setKey(Boolean.valueOf(var1.attributeValue("key")));
      var3.setDataType(var1.attributeValue("data-type"));
      var3.setDestProperty(var1.attributeValue("dest-property"));
      var2.getFields().add(var3);
   }

   private String a(Element var1, String var2) {
      String var3 = null;

      for(Object var5 : var1.elements()) {
         if (var5 instanceof Element) {
            Element var6 = (Element)var5;
            if (var6.getName().contentEquals(var2)) {
               var3 = var6.getText();
               break;
            }
         }
      }

      if (StringUtils.isNotBlank(var3)) {
         try {
            var3 = IOUtils.toString(Base64.getDecoder().decode(var3), "utf-8");
         } catch (IOException var7) {
            throw new RuleException(var7);
         }
      }

      return var3;
   }
}
