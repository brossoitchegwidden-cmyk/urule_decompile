package com.bstek.urule.console.admin.batch.out;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.model.batch.BatchDataProviderField;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItemField;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultCDATA;

public class BatchExport {
   public static final BatchExport ins = new BatchExport();

   private BatchExport() {
   }

   public void doExport(OutputStream var1, List var2) throws Exception {
      Document var3 = this.a(var2);
      StringWriter var4 = new StringWriter();
      XMLWriter var5 = new XMLWriter(var4, new OutputFormat());
      var5.write(var3);
      String var6 = var4.toString();
      byte[] var7 = Utils.compress(var6);
      IOUtils.write(var7, var1);
   }

   public void doExport(Element var1, List var2) throws Exception {
      this.a(var1, var2);
   }

   private Document a(List var1) {
      Document var2 = DocumentHelper.createDocument();
      Element var3 = var2.addElement("batchs");
      this.a(var3, var1);
      return var2;
   }

   private void a(Element var1, List var2) {
      for(Batch var4 : (Iterable<Batch>)(Iterable<?>)(var2)) {
         Element var5 = var1.addElement("batch");
         var5.addAttribute("name", var4.getName());
         var5.addAttribute("skip-limit", String.valueOf(var4.getSkipLimit()));
         var5.addAttribute("async", String.valueOf(var4.isAsync()));
         var5.addAttribute("callback-url", var4.getCallbackUrl());
         var5.addAttribute("listener", var4.getListener());
         var5.addAttribute("thread-multi", String.valueOf(var4.isThreadMulti()));
         var5.addAttribute("thread-size", String.valueOf(var4.getThreadSize()));
         var5.addAttribute("thread-data-size", String.valueOf(var4.getThreadDataSize()));
         var5.addAttribute("packet-id", String.valueOf(var4.getPacketId()));
         var5.addAttribute("provider-id", String.valueOf(var4.getProviderId()));
         var5.addAttribute("resolver-id", String.valueOf(var4.getResolverId()));
         this.a(var5, var4.getPacketInputData(), "packet-input-data");
         var5.addAttribute("rest-enable", String.valueOf(var4.isRestEnable()));
         var5.addAttribute("rest-security-enable", String.valueOf(var4.isRestSecurityEnable()));
         var5.addAttribute("rest-security-user", String.valueOf(var4.getRestSecurityUser()));
         var5.addAttribute("rest-security-password", String.valueOf(var4.getRestSecurityPassword()));
         this.a(var5, var4.getInputData(), "input-data");
         this.a(var5, var4.getDesc(), "desc");
         this.a(var4.getDataProvider(), var5);
         this.a(var4.getDataResolver(), var5);
      }

   }

   private void a(BatchDataProvider var1, Element var2) {
      Element var3 = var2.addElement("provider");
      var3.addAttribute("name", var1.getName());
      var3.addAttribute("datasource-id", String.valueOf(var1.getDatasourceId()));
      var3.addAttribute("listener", var1.getListener());
      this.a(var3, var1.getDesc(), "desc");
      this.a(var3, var1.getInputData(), "input-data");
      var3.addAttribute("packet-var-name", var1.getPacketVarName());
      var3.addAttribute("support-paging", String.valueOf(var1.isSupportsPaging()));
      var3.addAttribute("page-size", String.valueOf(var1.getPageSize()));
      this.a(var3, var1.getPageSql(), "page-sql");
      var3.addAttribute("order-field", String.valueOf(var1.getOrderField()));
      var3.addAttribute("order-field-param-name", String.valueOf(var1.getOrderFieldParamName()));
      var3.addAttribute("page-limit-type", String.valueOf(var1.getPageLimitType()));
      this.a(var3, var1.getCountSql(), "count-sql");
      this.a(var3, var1.getFilterData(), "filter-data");
      this.a(var1.getFields(), var3);
   }

   private void a(List var1, Element var2) {
      for(BatchDataProviderField var4 : (Iterable<BatchDataProviderField>)(Iterable<?>)(var1)) {
         Element var5 = var2.addElement("field");
         var5.addAttribute("src-property", var4.getSrcProperty());
         var5.addAttribute("data-type", var4.getDataType());
         var5.addAttribute("classpath", var4.getClassPath());
         var5.addAttribute("dest-property", var4.getDestProperty());
         var5.addAttribute("provider-id", Long.toString(var4.getDataProviderId()));
         if (var4.getDataProvider() != null) {
            this.a(var4.getDataProvider(), var5);
         }
      }

   }

   private void a(BatchDataResolver var1, Element var2) {
      Element var3 = var2.addElement("resolver");
      var3.addAttribute("name", var1.getName());
      var3.addAttribute("listener", String.valueOf(var1.getListener()));
      var3.addAttribute("tran-scope", String.valueOf(var1.getTranScope().name()));
      var3.addAttribute("datasource-id", String.valueOf(var1.getDatasourceId()));
      this.a(var3, var1.getFilterData(), "filter-data");
      this.a(var3, var1.getDesc(), "desc");
      this.b(var1.getItems(), var3);
   }

   private void b(List var1, Element var2) {
      for(BatchDataResolverItem var4 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var1)) {
         Element var5 = var2.addElement("item");
         var5.addAttribute("name", var4.getName());
         var5.addAttribute("update-mode", var4.getUpdateMode().name());
         var5.addAttribute("table-name", var4.getTableName());
         this.a(var5, var4.getFilterData(), "filter-data");
         var5.addAttribute("partition-name", var4.getPartitionName());
         var5.addAttribute("partition-value", var4.getPartitionValue());
         var5.addAttribute("commit-limit", String.valueOf(var4.getCommitLimit()));
         this.a(var5, var4.getDesc(), "desc");
         this.c(var4.getFields(), var5);
      }

   }

   private void c(List var1, Element var2) {
      for(BatchDataResolverItemField var4 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var1)) {
         Element var5 = var2.addElement("field");
         var5.addAttribute("src-property", var4.getSrcProperty());
         var5.addAttribute("key", String.valueOf(var4.isKey()));
         var5.addAttribute("data-type", var4.getDataType());
         var5.addAttribute("dest-property", var4.getDestProperty());
      }

   }

   private void a(Element var1, String var2, String var3) {
      if (!StringUtils.isBlank(var2)) {
         try {
            var2 = Base64.getEncoder().encodeToString(var2.getBytes("utf-8"));
         } catch (UnsupportedEncodingException var5) {
            throw new RuleException(var5);
         }

         Element var4 = var1.addElement(var3);
         var4.add(new DefaultCDATA(var2));
      }
   }
}
