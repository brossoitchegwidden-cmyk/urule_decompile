package com.bstek.urule.console.batch.writer;

import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BatchWriter implements Writer {
   public void storeRecord(Map var1, BatchDataResolver var2, BatchDataResolverItem var3, GeneralEntity var4) throws Exception {
      PreparedStatement var5 = (PreparedStatement)var1.get(var3.getName());
      this.a(var3.getParams(), var4, var5);
      var5.addBatch();
   }

   protected void a(List var1, Map var2, PreparedStatement var3) throws Exception {
      for(DataParam var5 : (Iterable<DataParam>)(Iterable<?>)(var1)) {
         Object var6 = var2.get(var5.getName());
         int var7 = var5.getIndex() + 1;
         if (var6 == null) {
            var3.setObject(var7, (Object)null);
         } else if (var6 instanceof String) {
            var3.setString(var7, (String)var6);
         } else if (var6 instanceof Integer) {
            var3.setInt(var7, (Integer)var6);
         } else if (var6 instanceof Long) {
            var3.setLong(var7, (Long)var6);
         } else if (var6 instanceof Boolean) {
            var3.setBoolean(var7, (Boolean)var6);
         } else if (var6 instanceof Array) {
            var3.setArray(var7, (Array)var6);
         } else if (var6 instanceof Date) {
            var3.setTimestamp(var7, new Timestamp(((Date)var6).getTime()));
         } else {
            var3.setObject(var7, var6);
         }
      }

   }
}
