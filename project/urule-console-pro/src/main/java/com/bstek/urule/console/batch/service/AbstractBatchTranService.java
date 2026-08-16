package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.writer.BatchWriter;
import com.bstek.urule.console.batch.writer.WriterUtils;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractBatchTranService extends AbstractBatchService {
   private static Log a = LogFactory.getLog(AbstractBatchTranService.class);
   private static BatchWriter b;

   private BatchWriter b() {
      if (b == null) {
         b = new BatchWriter();
      }

      return b;
   }

   protected void a(Map var1) {
      if (var1 != null) {
         try {
            for(String var3 : (Iterable<String>)(Iterable<?>)(var1.keySet())) {
               JdbcUtils.closeStatement((PreparedStatement)var1.get(var3));
            }
         } catch (Exception var4) {
            a.error(var4);
            var4.printStackTrace();
         }

      }
   }

   protected void b(Map var1) throws SQLException {
      try {
         for(String var3 : (Iterable<String>)(Iterable<?>)(var1.keySet())) {
            ((PreparedStatement)var1.get(var3)).executeBatch();
         }

      } catch (Exception var4) {
         throw new SqlBatchException(var4.getMessage(), var4);
      }
   }

   protected Map a(Connection var1, BatchDataResolver var2) throws SQLException {
      HashMap var3 = new HashMap();

      for(BatchDataResolverItem var5 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var2.getItems())) {
         PreparedStatement var6 = var1.prepareStatement(var5.getUpdateSql());
         var3.put(var5.getName(), var6);
      }

      return var3;
   }

   protected void a(BatchContext var1, Connection var2, Map var3, GeneralEntity var4, Map var5) throws Exception {
      Map var6 = this.a().fireRules(var1, var4);
      WriterUtils.write(var1, this.b(), var3, var6, var4, var5);
   }
}
