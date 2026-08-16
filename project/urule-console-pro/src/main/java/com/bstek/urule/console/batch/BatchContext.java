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
   private Batch b;
   private BatchLog c;
   Map a;
   private Map d;
   private Map e;
   private VariableCategory f;
   private VariableCategory g;
   private KnowledgeService h;
   private KnowledgePackage i;
   private Map j;
   private Map k;
   private Map l;
   private Connection m;
   private DataSource n;
   private int o = 0;
   private int p = 0;
   private int q = 0;
   private GeneralEntity r;
   private BatchResult s;

   public BatchContext(Batch var1, Map var2, BatchResult var3) {
      this.b = var1;
      this.a = var2;
      this.d = new HashMap();
      this.e = new HashMap();

      for(DataParam var5 : (Iterable<DataParam>)(Iterable<?>)(var1.getParams())) {
         this.d.put(var5.getName(), var5.getValue());
         this.e.put(var5.getName(), var5);
      }

      this.j = new HashMap();
      this.k = new HashMap();

      for(DataParam var7 : (Iterable<DataParam>)(Iterable<?>)(var1.getPacketParams())) {
         this.j.put(var7.getName(), var7.getValue());
         this.k.put(var7.getName(), var7);
      }

      this.s = var3;
   }

   public int getReadCount() {
      return this.o;
   }

   public void setReadCount(int var1) {
      this.o = var1;
   }

   public int getPageCount() {
      return this.p;
   }

   public void setPageCount(int var1) {
      this.p = var1;
   }

   public int getBatchCount() {
      return this.q;
   }

   public void setBatchCount(int var1) {
      this.q = var1;
   }

   public Batch getBatch() {
      return this.b;
   }

   public void setBatch(Batch var1) {
      this.b = var1;
   }

   public Map getParamValueMap() {
      return this.d;
   }

   public DataParam getParamObject(String var1) {
      return (DataParam)this.e.get(var1);
   }

   public DataParam getPacketParamObject(String var1) {
      return (DataParam)this.k.get(var1);
   }

   public Map getPacketParamValueMap() {
      return this.j;
   }

   public BatchLog getBatchLog() {
      return this.c;
   }

   public void setBatchLog(BatchLog var1) {
      this.c = var1;
   }

   public VariableCategory getParameterVariableCategory() {
      return this.f;
   }

   public void setParameterVariableCategory(VariableCategory var1) {
      this.f = var1;
   }

   public VariableCategory getProviderVariableCategory() {
      return this.g;
   }

   public void setProviderVariableCategory(VariableCategory var1) {
      this.g = var1;
   }

   public KnowledgeService getKnowledgeService() {
      return this.h;
   }

   public void setKnowledgeService(KnowledgeService var1) {
      this.h = var1;
   }

   public KnowledgePackage getKnowledgePackage() {
      return this.i;
   }

   public void setKnowledgePackage(KnowledgePackage var1) {
      this.i = var1;
   }

   public Map getParams() {
      return this.a;
   }

   public void setParams(Map var1) {
      this.a = var1;
   }

   public Map getReaderDataSoruceMap() {
      return this.l;
   }

   public void setReaderDataSoruceMap(Map var1) {
      this.l = var1;
   }

   public DataSource getWriteDataSource() {
      return this.n;
   }

   public void setWriteDataSource(DataSource var1) {
      this.n = var1;
   }

   public Connection getReadConnection() {
      return this.m;
   }

   public void setReadConnection(Connection var1) {
      this.m = var1;
   }

   public BatchResult getResult() {
      return this.s;
   }

   public GeneralEntity getHiveLastData() {
      return this.r;
   }

   public void setHiveLastData(GeneralEntity var1) {
      this.r = var1;
   }
}
