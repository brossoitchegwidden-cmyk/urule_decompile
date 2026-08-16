package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchItemResult;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.TranScope;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BasicBatchService extends AbstractBatchTranService {
   private static Log a = LogFactory.getLog(BasicBatchService.class);

   public void execute(BatchContext var1) {
      Batch var2 = var1.getBatch();
      BatchResult var3 = var1.getResult();
      boolean var4 = this.a(var1);
      if (var4) {
         try {
            if (var2.getDataProvider().isSupportsPaging()) {
               this.executeByPage(var1);
            } else {
               this.executeTotal(var1);
            }
         } catch (Exception var9) {
            a.error(var9);
            var3.setStatus(BatchStatus.failed);
            var3.setException(var9);
         } finally {
            this.closeConnection(var1.getReadConnection());
         }

      }
   }

   public void executeByPage(BatchContext var1) {
      BatchResult var2 = var1.getResult();
      Connection var3 = var1.getReadConnection();
      Batch var4 = var1.getBatch();
      BatchDataResolver var5 = var4.getDataResolver();
      ArrayList var6 = new ArrayList();
      if (var5.getTranScope() == TranScope.batch && var5.getDialect().supportTransaction()) {
         Object var91 = null;
         Object var94 = null;
         Map var95 = this.a(var5);
         var2.setItemResults(var95);
         Connection var97 = null;

         try {
            var97 = this.getWriteDataSource(var4).getConnection();
            var97.setAutoCommit(false);
            Map var92 = this.a(var97, var5);
            int var99 = 0;
            BatchStatus var104 = BatchStatus.started;
            int var110 = 0;

            while(true) {
               if (var110 < var1.getPageCount()) {
                  if (this.getBatchStatus(var4.getId()) != BatchStatus.stop) {
                     a.debug("execute batch 【" + var4.getName() + "】loadData pageIndex:" + var110 + "...");

                     try {
                        List var116 = this.loadDatas(var3, var1, var110);
                        var99 += var116.size();

                        for(GeneralEntity var122 : (Iterable<GeneralEntity>)(Iterable<?>)(var116)) {
                           this.a(var1, var97, var92, var122, var95);
                        }

                        this.b(var92);
                     } catch (Exception var87) {
                        a.error(var87);
                        var6.add(var87);
                        if (var4.getSkipLimit() <= 0) {
                           throw var87;
                        }

                        if (var4.getSkipLimit() < var6.size()) {
                           throw var87;
                        }
                     }

                     ++var110;
                     continue;
                  }

                  var104 = BatchStatus.stop;
               }

               if (var104 != BatchStatus.stop) {
                  var2.setFilterCount(var2.getReadCount() - var99);
                  a.debug("commit batch 【" + var4.getName() + "】.....");
                  var97.commit();
                  a.debug("commit batch 【" + var4.getName() + "】 success");
                  this.a(var92);

                  for(BatchItemResult var115 : (Iterable<BatchItemResult>)(Iterable<?>)(var95.values())) {
                     var115.setWriteCount(var115.getReadCount() - var115.getFilterCount());
                  }

                  var2.setStatus(BatchStatus.completed);
                  var2.setMsg(BatchStatus.completed.name());
               } else {
                  this.rollbackConnection(var97);
                  var2.setStatus(BatchStatus.stop);
               }
               break;
            }
         } catch (Exception var88) {
            a.error(var88);
            this.a((Map)var91);
            this.rollbackConnection(var97);

            for(BatchItemResult var109 : (Iterable<BatchItemResult>)(Iterable<?>)(var95.values())) {
               var109.setWriteCount(0);
            }

            var2.setStatus(BatchStatus.failed);
            var2.setException(var88);
         } finally {
            var2.setExceptions(var6);
            this.closeConnection(var97);
         }
      } else if (var5.getTranScope() == TranScope.page && var5.getDialect().supportTransaction()) {
         HashMap var90 = new HashMap();
         int var93 = 0;
         Connection var96 = null;

         try {
            var96 = this.getWriteDataSource(var4).getConnection();
            var96.setAutoCommit(false);
            BatchStatus var98 = var4.getStatus();

            for(int var101 = 0; var101 < var1.getPageCount(); ++var101) {
               Object var106 = null;
               Map var113 = this.a(var5);
               var90.put(var101, var113);
               Map var107 = this.a(var96, var5);

               try {
                  if (this.getBatchStatus(var4.getId()) == BatchStatus.stop) {
                     var98 = BatchStatus.stop;
                     break;
                  }

                  a.debug("execute batch 【" + var4.getName() + "】loadData pageIndex:" + var101 + "...");
                  List var117 = this.loadDatas(var3, var1, var101);
                  var93 += var117.size();

                  for(GeneralEntity var124 : (Iterable<GeneralEntity>)(Iterable<?>)(var117)) {
                     this.a(var1, var96, var107, var124, var113);
                  }

                  this.b(var107);
                  var96.commit();

                  for(BatchItemResult var125 : (Iterable<BatchItemResult>)(Iterable<?>)(var113.values())) {
                     var125.setWriteCount(var125.getReadCount() - var125.getFilterCount());
                  }
               } catch (Exception var83) {
                  a.error(var83);
                  var6.add(var83);
                  this.rollbackConnection(var96);

                  for(BatchItemResult var123 : (Iterable<BatchItemResult>)(Iterable<?>)(var113.values())) {
                     var123.setWriteCount(0);
                  }

                  if (var4.getSkipLimit() <= 0) {
                     throw var83;
                  }

                  if (var4.getSkipLimit() < var6.size()) {
                     throw var83;
                  }
               } finally {
                  this.a(var107);
               }
            }

            if (var98 != BatchStatus.stop) {
               var2.setFilterCount(var2.getReadCount() - var93);
               this.a(var4, var2, var90);
               boolean var102 = false;

               for(BatchItemResult var114 : (Iterable<BatchItemResult>)(Iterable<?>)(var2.getItemResults().values())) {
                  if (var114.getWriteCount() > 0) {
                     var102 = true;
                     break;
                  }
               }

               if (var102) {
                  var2.setStatus(BatchStatus.completed);
                  var2.setMsg(BatchStatus.completed.name());
               } else {
                  var2.setStatus(BatchStatus.failed);
               }
            } else {
               var2.setStatus(BatchStatus.stop);
            }
         } catch (Exception var85) {
            a.error(var85);
            var2.setStatus(BatchStatus.failed);
            var2.setException(var85);
         } finally {
            var2.setExceptions(var6);
            this.closeConnection(var96);
         }
      } else {
         ArrayList var7 = new ArrayList();
         Connection var8 = null;

         try {
            int var9 = 0;
            var8 = this.getWriteDataSource(var4).getConnection();
            BatchStatus var10 = var4.getStatus();

            for(int var11 = 0; var11 < var1.getPageCount(); ++var11) {
               if (this.getBatchStatus(var4.getId()) == BatchStatus.stop) {
                  var10 = BatchStatus.stop;
                  break;
               }

               a.debug("execute batch 【" + var4.getName() + "】pageIndex:" + var11);
               BatchResult var12 = new BatchResult();
               var7.add(var12);
               a.debug("execute batch 【" + var4.getName() + "】loadData pageIndex:" + var11 + "...");
               List var13 = this.loadDatas(var3, var1, var11);
               var9 += var13.size();
               HashMap var14 = new HashMap();

               for(int var15 = 0; var15 < var13.size(); ++var15) {
                  GeneralEntity var16 = (GeneralEntity)var13.get(var15);
                  Map var17 = null;
                  Object var18 = null;
                  Map var126 = this.a(var5);
                  var14.put(var15, var126);

                  try {
                     if (var5.getDialect().supportTransaction()) {
                        var8.setAutoCommit(false);
                     }

                     var17 = this.a(var8, var5);
                     this.a(var1, var8, var17, var16, var126);
                     if (var5.getDialect().supportTransaction()) {
                        this.b(var17);
                        var8.commit();
                     }

                     for(BatchItemResult var127 : (Iterable<BatchItemResult>)(Iterable<?>)(var126.values())) {
                        var127.setWriteCount(var127.getReadCount() - var127.getFilterCount());
                     }
                  } catch (Exception var79) {
                     a.error(var79);
                     if (var5.getDialect().supportTransaction()) {
                        this.rollbackConnection(var8);
                     }

                     for(BatchItemResult var21 : (Iterable<BatchItemResult>)(Iterable<?>)(var126.values())) {
                        var21.setWriteCount(0);
                     }

                     var2.setException(var79);
                     var6.add(var79);
                     if (var4.getSkipLimit() <= 0) {
                        throw var79;
                     }

                     if (var4.getSkipLimit() < var6.size()) {
                        throw var79;
                     }
                  } finally {
                     this.a(var17);
                  }
               }

               this.a(var4, var12, var14);
            }

            if (var10 != BatchStatus.stop) {
               var2.setFilterCount(var2.getReadCount() - var9);
               this.a(var4, var2, var7);
               boolean var100 = false;

               for(BatchItemResult var112 : (Iterable<BatchItemResult>)(Iterable<?>)(var2.getItemResults().values())) {
                  if (var112.getWriteCount() > 0) {
                     var100 = true;
                     break;
                  }
               }

               if (var100) {
                  var2.setStatus(BatchStatus.completed);
                  var2.setMsg(BatchStatus.completed.name());
               } else {
                  var2.setStatus(BatchStatus.failed);
               }
            } else {
               var2.setStatus(BatchStatus.stop);
            }
         } catch (Exception var81) {
            a.error(var81);
            var2.setStatus(BatchStatus.failed);
            var2.setException(var81);
         } finally {
            var2.setExceptions(var6);
            this.closeConnection(var8);
         }
      }

   }

   public void executeTotal(BatchContext var1) {
      BatchResult var2 = var1.getResult();
      Batch var3 = var1.getBatch();
      BatchDataResolver var4 = var3.getDataResolver();
      Map var5 = null;
      Object var6 = null;
      List var53 = null;

      try {
         a.debug("execute batch 【" + var3.getName() + "】loadData ...");
         var53 = this.loadDatas(var1.getReadConnection(), var1, -1);
         var2.setFilterCount(var2.getReadCount() - var53.size());
         a.debug("execute batch 【" + var3.getName() + "】data size:" + var53.size());
      } catch (Exception var44) {
         var2.setStatus(BatchStatus.failed);
         var2.setException(var44);
         return;
      }

      ArrayList var8 = new ArrayList();
      if ((var4.getTranScope() == TranScope.batch || var4.getTranScope() == TranScope.page) && var4.getDialect().supportTransaction()) {
         Connection var54 = null;

         try {
            var54 = this.getWriteDataSource(var3).getConnection();
            var54.setAutoCommit(false);
            var5 = this.a(var54, var4);
            Map var52 = this.a(var4);
            BatchStatus var55 = var3.getStatus();

            for(int var56 = 0; var56 < var53.size(); ++var56) {
               try {
                  GeneralEntity var59 = (GeneralEntity)var53.get(var56);
                  if (var56 % 10000 == 0) {
                     if (a.isDebugEnabled()) {
                        a.debug("execute batch 【" + var3.getName() + "】record index: " + var56 + "...");
                     }

                     if (this.getBatchStatus(var3.getId()) == BatchStatus.stop) {
                        var55 = BatchStatus.stop;
                        break;
                     }
                  }

                  this.a(var1, var54, var5, var59, var52);
               } catch (Exception var49) {
                  a.error(var49);
                  var8.add(var49);
                  if (var3.getSkipLimit() <= 0) {
                     throw var49;
                  }

                  if (var3.getSkipLimit() < var8.size()) {
                     throw var49;
                  }
               }
            }

            if (var55 != BatchStatus.stop) {
               this.b(var5);
               var54.commit();

               for(BatchItemResult var60 : (Iterable<BatchItemResult>)(Iterable<?>)(var52.values())) {
                  var60.setWriteCount(var60.getReadCount() - var60.getFilterCount());
               }

               var2.setItemResults(var52);
            } else {
               this.rollbackConnection(var54);
               var2.setStatus(BatchStatus.stop);
            }
         } catch (Exception var50) {
            var2.setStatus(BatchStatus.failed);
            var2.setException(var50);
         } finally {
            this.a(var5);
            var2.setExceptions(var8);
            this.closeConnection(var54);
         }
      } else {
         HashMap var9 = new HashMap();
         Connection var10 = null;

         try {
            var10 = this.getWriteDataSource(var3).getConnection();
            if (var4.getDialect().supportTransaction()) {
               var10.setAutoCommit(false);
            }

            BatchStatus var11 = var3.getStatus();

            for(int var12 = 0; var12 < var53.size(); ++var12) {
               GeneralEntity var13 = (GeneralEntity)var53.get(var12);
               Map var14 = this.a(var4);
               var9.put(var12, var14);
               if (var12 % 10000 == 0) {
                  if (a.isDebugEnabled()) {
                     a.debug("execute batch 【" + var3.getName() + "】record index: " + var12 + "...");
                  }

                  if (this.getBatchStatus(var3.getId()) == BatchStatus.stop) {
                     var11 = BatchStatus.stop;
                     break;
                  }
               }

               try {
                  var5 = this.a(var10, var4);
                  this.a(var1, var10, var5, var13, var14);
                  if (var4.getDialect().supportTransaction()) {
                     this.b(var5);
                     var10.commit();
                  }

                  this.a(var5);

                  for(BatchItemResult var63 : (Iterable<BatchItemResult>)(Iterable<?>)(var14.values())) {
                     var63.setWriteCount(var63.getReadCount() - var63.getFilterCount());
                  }
               } catch (Exception var45) {
                  a.error(var45);
                  if (var4.getDialect().supportTransaction()) {
                     this.rollbackConnection(var10);
                  }

                  for(BatchItemResult var17 : (Iterable<BatchItemResult>)(Iterable<?>)(var14.values())) {
                     var17.setWriteCount(0);
                  }

                  var8.add(var45);
                  if (var3.getSkipLimit() <= 0) {
                     throw var45;
                  }

                  if (var3.getSkipLimit() < var8.size()) {
                     throw var45;
                  }
               } finally {
                  this.a(var5);
               }
            }

            this.closeConnection(var10);
            if (var11 != BatchStatus.stop) {
               this.a(var3, var2, var9);
               boolean var58 = false;

               for(BatchItemResult var62 : (Iterable<BatchItemResult>)(Iterable<?>)(var2.getItemResults().values())) {
                  if (var62.getWriteCount() > 0) {
                     var58 = true;
                     break;
                  }
               }

               if (var58) {
                  var2.setStatus(BatchStatus.completed);
                  var2.setMsg(BatchStatus.completed.name());
               } else {
                  var2.setStatus(BatchStatus.failed);
                  var2.setMsg(BatchStatus.failed.name());
               }
            } else {
               var2.setStatus(BatchStatus.stop);
            }
         } catch (Exception var47) {
            var2.setStatus(BatchStatus.failed);
            var2.setException(var47);
         } finally {
            var2.setExceptions(var8);
            this.closeConnection(var10);
         }
      }

   }
}
