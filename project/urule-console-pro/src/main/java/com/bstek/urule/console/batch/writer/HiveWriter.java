package com.bstek.urule.console.batch.writer;

import com.bstek.urule.console.batch.HiveStatement;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.GeneralEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class HiveWriter implements Writer {
   public void storeRecord(Map var1, BatchDataResolver var2, BatchDataResolverItem var3, GeneralEntity var4) throws Exception {
      GeneralEntity var5 = var4;
      HiveStatement var6 = (HiveStatement)var1.get(var3.getName());
      String var7 = "";
      NumberFormat var8 = NumberFormat.getNumberInstance();

      for(DataParam var10 : (Iterable<DataParam>)(Iterable<?>)(var3.getParams())) {
         Object var11 = var5.get(var10.getName());
         String var12 = var10.getFormatter();
         if (var11 == null) {
            var7 = this.a(var7, "null");
         } else if (var11 instanceof String) {
            var7 = this.a(var7, "'" + (String)var11 + "'");
         } else if (!(var11 instanceof Date) && !(var11 instanceof Timestamp)) {
            var7 = this.a(var7, var11.toString());
         } else {
            var12 = "yyyy-MM-dd HH:mm:ss";
            var7 = this.a(var7, this.a(var11, var8, var12));
         }
      }

      var6.addBatch(var7);
   }

   private String a(String var1, String var2) {
      return StringUtils.isBlank(var1) ? var2 : var1 + "," + var2;
   }

   private String a(Object var1, NumberFormat var2, String var3) {
      String var4 = "";
      if (var1 instanceof Float) {
         DecimalFormat var5 = new DecimalFormat(var3);
         var4 = var5.format((Date)var1);
      } else if (var1 instanceof Double) {
         DecimalFormat var7 = new DecimalFormat(var3);
         var4 = var7.format((Timestamp)var1);
      } else if (var1 instanceof BigDecimal) {
         DecimalFormat var8 = new DecimalFormat(var3);
         var4 = var8.format((Timestamp)var1);
      } else if (var1 instanceof Date) {
         SimpleDateFormat var9 = new SimpleDateFormat(var3);
         var4 = var9.format((Date)var1);
      } else if (var1 instanceof Timestamp) {
         SimpleDateFormat var10 = new SimpleDateFormat(var3);
         var4 = var10.format((Timestamp)var1);
      } else {
         SimpleDateFormat var11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         var4 = var11.format((Date)var1);
      }

      return "'" + var4 + "'";
   }
}
