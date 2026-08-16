package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchItemResult;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.batch.HiveStatement;
import com.bstek.urule.console.batch.writer.HiveWriter;
import com.bstek.urule.console.batch.writer.Writer;
import com.bstek.urule.console.batch.writer.WriterUtils;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HiveTranService extends AbstractBatchService {
   private static Log a = LogFactory.getLog(HiveTranService.class);
   private static Writer b;

   private Writer b() {
      if (b == null) {
         b = new HiveWriter();
      }

      return b;
   }

   protected void a(BatchContext var1, Map var2, GeneralEntity var3, Map var4) throws Exception {
      Map var5 = this.a().fireRules(var1, var3);
      WriterUtils.write(var1, this.b(), var2, var5, var3, var4);
   }

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
      ArrayList var7 = new ArrayList();
      Map var8 = this.a(var5);
      Connection var9 = null;
      Statement var10 = null;

      try {
         int var11 = 0;
         var9 = this.getWriteDataSource(var4).getConnection();
         BatchStatus var12 = var4.getStatus();
         Map var13 = this.b(var5);
         var10 = var9.createStatement();

         for(int var14 = 0; var14 < var1.getPageCount(); ++var14) {
            if (this.getBatchStatus(var4.getId()) == BatchStatus.stop) {
               var12 = BatchStatus.stop;
               break;
            }

            a.debug("execute batch 【" + var4.getName() + "】pageIndex:" + var14);
            BatchResult var15 = new BatchResult();
            var7.add(var15);
            a.debug("execute batch 【" + var4.getName() + "】loadData pageIndex:" + var14 + "...");
            List var16 = this.loadDatas(var3, var1, var14);
            var11 += var16.size();
            HashMap var17 = new HashMap();

            for(int var18 = 0; var18 < var16.size(); ++var18) {
               GeneralEntity var19 = (GeneralEntity)var16.get(var18);
               Object var20 = null;
               Map var36 = this.a(var5);
               var17.put(var18, var36);

               try {
                  this.a(var1, var13, var19, var36);
               } catch (Exception var28) {
                  a.error(var28);
                  var2.setException(var28);
                  var6.add(var28);
                  if (var4.getSkipLimit() <= 0) {
                     throw var28;
                  }

                  if (var4.getSkipLimit() < var6.size()) {
                     throw var28;
                  }
               }
            }

            this.a(var4, var15, var17);

            try {
               this.a(var10, var13, var5, var8, false);
            } catch (Exception var29) {
               a.error(var29);
               var2.setException(var29);
               var6.add(var29);
               if (var4.getSkipLimit() <= 0) {
                  throw var29;
               }

               if (var4.getSkipLimit() < var6.size()) {
                  throw var29;
               }
            }
         }

         try {
            this.a(var10, var13, var5, var8, true);
         } catch (Exception var30) {
            a.error(var30);
            var2.setException(var30);
            var6.add(var30);
            if (var4.getSkipLimit() <= 0) {
               throw var30;
            }

            if (var4.getSkipLimit() < var6.size()) {
               throw var30;
            }
         }

         if (var12 != BatchStatus.stop) {
            var2.setFilterCount(var2.getReadCount() - var11);
            this.a(var4, var2, var7);
            this.b(var4, var2, var8);
            boolean var33 = false;

            for(BatchItemResult var35 : (Iterable<BatchItemResult>)(Iterable<?>)(var2.getItemResults().values())) {
               if (var35.getWriteCount() > 0) {
                  var33 = true;
                  break;
               }
            }

            if (var33) {
               var2.setStatus(BatchStatus.completed);
               var2.setMsg(BatchStatus.completed.name());
            } else {
               var2.setStatus(BatchStatus.failed);
            }
         } else {
            var2.setStatus(BatchStatus.stop);
         }
      } catch (Exception var31) {
         a.error(var31);
         var2.setStatus(BatchStatus.failed);
         var2.setException(var31);
      } finally {
         JdbcUtils.closeStatement(var10);
         var2.setExceptions(var6);
         this.closeConnection(var9);
      }

   }

   protected Map b(BatchDataResolver var1) throws SQLException {
      HashMap var2 = new HashMap();

      for(BatchDataResolverItem var4 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var1.getItems())) {
         HiveStatement var5 = new HiveStatement();
         var2.put(var4.getName(), var5);
      }

      return var2;
   }

   protected void a(Statement var1, Map var2, BatchDataResolver var3, Map var4, boolean var5) throws SQLException {
      try {
         for(String var7 : (Iterable<String>)(Iterable<?>)(var2.keySet())) {
            BatchItemResult var8 = (BatchItemResult)var4.get(var7);
            HiveStatement var9 = (HiveStatement)var2.get(var7);
            List var10 = var9.getBatchSqls();

            for(BatchDataResolverItem var12 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var3.getItems())) {
               if (var12.getName().equals(var7)) {
                  if (var5 && var10.size() > 0) {
                     if (var10.size() >= var12.getCommitLimit()) {
                        this.a(var1, var9, var12, var8);
                     } else {
                        String var13 = var12.getUpdateSql();
                        var13 = var13 + " values ";
                        String var14 = "";

                        for(int var15 = 0; var15 < var10.size(); ++var15) {
                           String var16 = (String)var10.get(var15);
                           if (var15 > 0) {
                              var14 = var14 + ",";
                           }

                           var14 = var14 + "(" + var16 + ")";
                        }

                        a.debug(var13 + var14);
                        var1.executeUpdate(var13 + var14);
                        var8.setWriteCount(var8.getWriteCount() + var10.size());
                        var9.setBatchSqls(new ArrayList());
                     }
                  } else if (!var5 && var10.size() >= var12.getCommitLimit()) {
                     this.a(var1, var9, var12, var8);
                  }
               }
            }
         }

      } catch (Exception var17) {
         var17.printStackTrace();
         throw new SqlBatchException(var17.getMessage(), var17);
      }
   }

   private void a(Statement var1, HiveStatement var2, BatchDataResolverItem var3, BatchItemResult var4) throws Exception {
      List var5 = var2.getBatchSqls();
      String var6 = var3.getUpdateSql();
      var6 = var6 + " values ";
      String var7 = "";

      for(int var8 = 0; var8 < var3.getCommitLimit(); ++var8) {
         String var9 = (String)var5.get(var8);
         if (var8 > 0) {
            var7 = var7 + ",";
         }

         var7 = var7 + "(" + var9 + ")";
      }

      a.debug(var6 + var7);
      var1.executeUpdate(var6 + var7);
      var4.setWriteCount(var4.getWriteCount() + var3.getCommitLimit());
      var5 = var5.subList(var3.getCommitLimit(), var5.size());
      var2.setBatchSqls(var5);
      if (var5.size() > var3.getCommitLimit()) {
         this.a(var1, var2, var3, var4);
      }

   }

   public void executeTotal(BatchContext var1) {
      BatchResult var2 = var1.getResult();
      Batch var3 = var1.getBatch();
      BatchDataResolver var4 = var3.getDataResolver();
      Object var5 = null;
      List var29 = null;

      try {
         a.debug("execute batch 【" + var3.getName() + "】loadData ...");
         var29 = this.loadDatas(var1.getReadConnection(), var1, -1);
         var2.setFilterCount(var2.getReadCount() - var29.size());
         a.debug("execute batch 【" + var3.getName() + "】data size:" + var29.size());
      } catch (Exception var23) {
         var2.setStatus(BatchStatus.failed);
         var2.setException(var23);
         return;
      }

      ArrayList var7 = new ArrayList();
      HashMap var8 = new HashMap();
      Connection var9 = null;
      Statement var10 = null;
      Map var11 = this.a(var4);

      try {
         var9 = this.getWriteDataSource(var3).getConnection();
         var10 = var9.createStatement();
         Map var28 = this.b(var4);
         BatchStatus var12 = var3.getStatus();

         for(int var13 = 0; var13 < var29.size(); ++var13) {
            GeneralEntity var14 = (GeneralEntity)var29.get(var13);
            Map var15 = this.a(var4);
            var8.put(var13, var15);
            if (var13 % 10000 == 0) {
               if (a.isDebugEnabled()) {
                  a.debug("execute batch 【" + var3.getName() + "】record index: " + var13 + "...");
               }

               if (this.getBatchStatus(var3.getId()) == BatchStatus.stop) {
                  var12 = BatchStatus.stop;
                  break;
               }
            }

            try {
               this.a(var1, var28, var14, var15);
               this.a(var10, var28, var4, var11, false);
            } catch (Exception var24) {
               a.error(var24);
               var7.add(var24);
               if (var3.getSkipLimit() <= 0) {
                  throw var24;
               }

               if (var3.getSkipLimit() < var7.size()) {
                  throw var24;
               }
            }
         }

         try {
            this.a(var10, var28, var4, var11, true);
         } catch (Exception var25) {
            a.error(var25);
            var2.setException(var25);
            var7.add(var25);
            if (var3.getSkipLimit() <= 0) {
               throw var25;
            }

            if (var3.getSkipLimit() < var7.size()) {
               throw var25;
            }
         }

         if (var12 != BatchStatus.stop) {
            this.a(var3, var2, var8);
            this.b(var3, var2, var11);
            boolean var30 = false;

            for(BatchItemResult var32 : (Iterable<BatchItemResult>)(Iterable<?>)(var2.getItemResults().values())) {
               if (var32.getWriteCount() > 0) {
                  var30 = true;
                  break;
               }
            }

            if (var30) {
               var2.setStatus(BatchStatus.completed);
               var2.setMsg(BatchStatus.completed.name());
            } else {
               var2.setStatus(BatchStatus.failed);
               var2.setMsg(BatchStatus.failed.name());
            }
         } else {
            var2.setStatus(BatchStatus.stop);
         }
      } catch (Exception var26) {
         var2.setStatus(BatchStatus.failed);
         var2.setException(var26);
      } finally {
         JdbcUtils.closeStatement(var10);
         var2.setExceptions(var7);
         this.closeConnection(var9);
      }

   }

   protected void b(Batch var1, BatchResult var2, Map var3) {
      for(BatchDataResolverItem var5 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var1.getDataResolver().getItems())) {
         String var6 = var5.getName();
         BatchItemResult var7 = (BatchItemResult)var2.getItemResults().get(var6);

         for(BatchItemResult var9 : (Iterable<BatchItemResult>)(Iterable<?>)(var3.values())) {
            if (var9.getName().equals(var6)) {
               var7.setWriteCount(var9.getWriteCount());
               break;
            }
         }
      }

   }
}
