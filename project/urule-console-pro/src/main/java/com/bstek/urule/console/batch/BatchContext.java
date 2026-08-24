package com.bstek.urule.console.batch;

import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchLog;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.service.KnowledgeService;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

public class BatchContext {
   private Batch batch;
   private BatchLog batchLog;
   Map params;
   private Map paramValueMap;
   private Map paramDefinitionsByName;
   private VariableCategory parameterVariableCategory;
   private VariableCategory providerVariableCategory;
   private KnowledgeService knowledgeService;
   private KnowledgePackage knowledgePackage;
   private Map packetParamValueMap;
   private Map packetParamDefinitionsByName;
   private Map readerDataSoruceMap;
   private Connection readConnection;
   private DataSource writeDataSource;
   private int readCount = 0;
   private int pageCount = 0;
   private int batchCount = 0;
   private GeneralEntity hiveLastData;
   private BatchResult result;

   public BatchContext(Batch batch, Map params, BatchResult result) {
      this.batch = batch;
      this.params = params;
      this.paramValueMap = new HashMap();
      this.paramDefinitionsByName = new HashMap();

      for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(batch.getParams())) {
         this.paramValueMap.put(dataParam.getName(), dataParam.getValue());
         this.paramDefinitionsByName.put(dataParam.getName(), dataParam);
      }

      this.packetParamValueMap = new HashMap();
      this.packetParamDefinitionsByName = new HashMap();

      for(DataParam dataParam2 : (Iterable<DataParam>)(Iterable<?>)(batch.getPacketParams())) {
         this.packetParamValueMap.put(dataParam2.getName(), dataParam2.getValue());
         this.packetParamDefinitionsByName.put(dataParam2.getName(), dataParam2);
      }

      this.result = result;
   }

   public int getReadCount() {
      return this.readCount;
   }

   public void setReadCount(int readCount) {
      this.readCount = readCount;
   }

   public int getPageCount() {
      return this.pageCount;
   }

   public void setPageCount(int pageCount) {
      this.pageCount = pageCount;
   }

   public int getBatchCount() {
      return this.batchCount;
   }

   public void setBatchCount(int batchCount) {
      this.batchCount = batchCount;
   }

   public Batch getBatch() {
      return this.batch;
   }

   public void setBatch(Batch batch) {
      this.batch = batch;
   }

   public Map getParamValueMap() {
      return this.paramValueMap;
   }

   public DataParam getParamObject(String name) {
      return (DataParam)this.paramDefinitionsByName.get(name);
   }

   public DataParam getPacketParamObject(String name) {
      return (DataParam)this.packetParamDefinitionsByName.get(name);
   }

   public Map getPacketParamValueMap() {
      return this.packetParamValueMap;
   }

   public BatchLog getBatchLog() {
      return this.batchLog;
   }

   public void setBatchLog(BatchLog batchLog) {
      this.batchLog = batchLog;
   }

   public VariableCategory getParameterVariableCategory() {
      return this.parameterVariableCategory;
   }

   public void setParameterVariableCategory(VariableCategory parameterVariableCategory) {
      this.parameterVariableCategory = parameterVariableCategory;
   }

   public VariableCategory getProviderVariableCategory() {
      return this.providerVariableCategory;
   }

   public void setProviderVariableCategory(VariableCategory providerVariableCategory) {
      this.providerVariableCategory = providerVariableCategory;
   }

   public KnowledgeService getKnowledgeService() {
      return this.knowledgeService;
   }

   public void setKnowledgeService(KnowledgeService knowledgeService) {
      this.knowledgeService = knowledgeService;
   }

   public KnowledgePackage getKnowledgePackage() {
      return this.knowledgePackage;
   }

   public void setKnowledgePackage(KnowledgePackage knowledgePackage) {
      this.knowledgePackage = knowledgePackage;
   }

   public Map getParams() {
      return this.params;
   }

   public void setParams(Map params) {
      this.params = params;
   }

   public Map getReaderDataSoruceMap() {
      return this.readerDataSoruceMap;
   }

   public void setReaderDataSoruceMap(Map readerDataSoruceMap) {
      this.readerDataSoruceMap = readerDataSoruceMap;
   }

   public DataSource getWriteDataSource() {
      return this.writeDataSource;
   }

   public void setWriteDataSource(DataSource writeDataSource) {
      this.writeDataSource = writeDataSource;
   }

   public Connection getReadConnection() {
      return this.readConnection;
   }

   public void setReadConnection(Connection readConnection) {
      this.readConnection = readConnection;
   }

   public BatchResult getResult() {
      return this.result;
   }

   public GeneralEntity getHiveLastData() {
      return this.hiveLastData;
   }

   public void setHiveLastData(GeneralEntity hiveLastData) {
      this.hiveLastData = hiveLastData;
   }
}
