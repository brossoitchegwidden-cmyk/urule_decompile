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

   public void doExport(OutputStream outputStream, List items) throws Exception {
      Document document = this.buildBatchDocument(items);
      StringWriter stringWriter = new StringWriter();
      XMLWriter xMLWriter = new XMLWriter(stringWriter, new OutputFormat());
      xMLWriter.write(document);
      String text = stringWriter.toString();
      byte[] bytes = Utils.compress(text);
      IOUtils.write(bytes, outputStream);
   }

   public void doExport(Element element, List items) throws Exception {
      this.appendBatches(element, items);
   }

   private Document buildBatchDocument(List items) {
      Document document = DocumentHelper.createDocument();
      Element element = document.addElement("batchs");
      this.appendBatches(element, items);
      return document;
   }

   private void appendBatches(Element element, List items) {
      for(Batch batch : (Iterable<Batch>)(Iterable<?>)(items)) {
         Element element2 = element.addElement("batch");
         element2.addAttribute("name", batch.getName());
         element2.addAttribute("skip-limit", String.valueOf(batch.getSkipLimit()));
         element2.addAttribute("async", String.valueOf(batch.isAsync()));
         element2.addAttribute("callback-url", batch.getCallbackUrl());
         element2.addAttribute("listener", batch.getListener());
         element2.addAttribute("thread-multi", String.valueOf(batch.isThreadMulti()));
         element2.addAttribute("thread-size", String.valueOf(batch.getThreadSize()));
         element2.addAttribute("thread-data-size", String.valueOf(batch.getThreadDataSize()));
         element2.addAttribute("packet-id", String.valueOf(batch.getPacketId()));
         element2.addAttribute("provider-id", String.valueOf(batch.getProviderId()));
         element2.addAttribute("resolver-id", String.valueOf(batch.getResolverId()));
         this.appendEncodedChild(element2, batch.getPacketInputData(), "packet-input-data");
         element2.addAttribute("rest-enable", String.valueOf(batch.isRestEnable()));
         element2.addAttribute("rest-security-enable", String.valueOf(batch.isRestSecurityEnable()));
         element2.addAttribute("rest-security-user", String.valueOf(batch.getRestSecurityUser()));
         element2.addAttribute("rest-security-password", String.valueOf(batch.getRestSecurityPassword()));
         this.appendEncodedChild(element2, batch.getInputData(), "input-data");
         this.appendEncodedChild(element2, batch.getDesc(), "desc");
         this.appendProvider(batch.getDataProvider(), element2);
         this.appendResolver(batch.getDataResolver(), element2);
      }

   }

   private void appendProvider(BatchDataProvider batchDataProvider, Element element) {
      Element element2 = element.addElement("provider");
      element2.addAttribute("name", batchDataProvider.getName());
      element2.addAttribute("datasource-id", String.valueOf(batchDataProvider.getDatasourceId()));
      element2.addAttribute("listener", batchDataProvider.getListener());
      this.appendEncodedChild(element2, batchDataProvider.getDesc(), "desc");
      this.appendEncodedChild(element2, batchDataProvider.getInputData(), "input-data");
      element2.addAttribute("packet-var-name", batchDataProvider.getPacketVarName());
      element2.addAttribute("support-paging", String.valueOf(batchDataProvider.isSupportsPaging()));
      element2.addAttribute("page-size", String.valueOf(batchDataProvider.getPageSize()));
      this.appendEncodedChild(element2, batchDataProvider.getPageSql(), "page-sql");
      element2.addAttribute("order-field", String.valueOf(batchDataProvider.getOrderField()));
      element2.addAttribute("order-field-param-name", String.valueOf(batchDataProvider.getOrderFieldParamName()));
      element2.addAttribute("page-limit-type", String.valueOf(batchDataProvider.getPageLimitType()));
      this.appendEncodedChild(element2, batchDataProvider.getCountSql(), "count-sql");
      this.appendEncodedChild(element2, batchDataProvider.getFilterData(), "filter-data");
      this.appendProviderFields(batchDataProvider.getFields(), element2);
   }

   private void appendProviderFields(List items, Element element) {
      for(BatchDataProviderField batchDataProviderField : (Iterable<BatchDataProviderField>)(Iterable<?>)(items)) {
         Element element2 = element.addElement("field");
         element2.addAttribute("src-property", batchDataProviderField.getSrcProperty());
         element2.addAttribute("data-type", batchDataProviderField.getDataType());
         element2.addAttribute("classpath", batchDataProviderField.getClassPath());
         element2.addAttribute("dest-property", batchDataProviderField.getDestProperty());
         element2.addAttribute("provider-id", Long.toString(batchDataProviderField.getDataProviderId()));
         if (batchDataProviderField.getDataProvider() != null) {
            this.appendProvider(batchDataProviderField.getDataProvider(), element2);
         }
      }

   }

   private void appendResolver(BatchDataResolver batchDataResolver, Element element) {
      Element element2 = element.addElement("resolver");
      element2.addAttribute("name", batchDataResolver.getName());
      element2.addAttribute("listener", String.valueOf(batchDataResolver.getListener()));
      element2.addAttribute("tran-scope", String.valueOf(batchDataResolver.getTranScope().name()));
      element2.addAttribute("datasource-id", String.valueOf(batchDataResolver.getDatasourceId()));
      this.appendEncodedChild(element2, batchDataResolver.getFilterData(), "filter-data");
      this.appendEncodedChild(element2, batchDataResolver.getDesc(), "desc");
      this.appendResolverItems(batchDataResolver.getItems(), element2);
   }

   private void appendResolverItems(List items, Element element) {
      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(items)) {
         Element element2 = element.addElement("item");
         element2.addAttribute("name", batchDataResolverItem.getName());
         element2.addAttribute("update-mode", batchDataResolverItem.getUpdateMode().name());
         element2.addAttribute("table-name", batchDataResolverItem.getTableName());
         this.appendEncodedChild(element2, batchDataResolverItem.getFilterData(), "filter-data");
         element2.addAttribute("partition-name", batchDataResolverItem.getPartitionName());
         element2.addAttribute("partition-value", batchDataResolverItem.getPartitionValue());
         element2.addAttribute("commit-limit", String.valueOf(batchDataResolverItem.getCommitLimit()));
         this.appendEncodedChild(element2, batchDataResolverItem.getDesc(), "desc");
         this.appendResolverItemFields(batchDataResolverItem.getFields(), element2);
      }

   }

   private void appendResolverItemFields(List items, Element element) {
      for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(items)) {
         Element element2 = element.addElement("field");
         element2.addAttribute("src-property", batchDataResolverItemField.getSrcProperty());
         element2.addAttribute("key", String.valueOf(batchDataResolverItemField.isKey()));
         element2.addAttribute("data-type", batchDataResolverItemField.getDataType());
         element2.addAttribute("dest-property", batchDataResolverItemField.getDestProperty());
      }

   }

   private void appendEncodedChild(Element element, String text, String text2) {
      if (!StringUtils.isBlank(text)) {
         try {
            text = Base64.getEncoder().encodeToString(text.getBytes("utf-8"));
         } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuleException(unsupportedEncodingException);
         }

         Element element2 = element.addElement(text2);
         element2.add(new DefaultCDATA(text));
      }
   }
}
