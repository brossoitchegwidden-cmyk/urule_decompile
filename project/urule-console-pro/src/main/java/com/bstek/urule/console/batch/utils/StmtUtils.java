package com.bstek.urule.console.batch.utils;

import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.console.database.util.ParsedSql;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StmtUtils {
   public static final Log logger = LogFactory.getLog(StmtUtils.class);

   private static DataParam a(List var0, String var1) {
      for(DataParam var3 : (Iterable<DataParam>)(Iterable<?>)(var0)) {
         if (var1.equals(var3.getName())) {
            return var3;
         }
      }

      return null;
   }

   public static void setStmtQueryParameters(ParsedSql var0, List var1, PreparedStatement var2) throws Exception {
      for(int var3 = 0; var3 < var0.getParameterNames().size(); ++var3) {
         String var4 = (String)var0.getParameterNames().get(var3);
         int var5 = var3 + 1;
         DataParam var6 = a(var1, var4);
         if (var6 == null) {
            var2.setObject(var5, (Object)null);
         } else {
            Object var7 = var6.getValue();
            if (var7 instanceof String) {
               var2.setString(var5, (String)var7);
            } else if (var7 instanceof Boolean) {
               var2.setBoolean(var5, (Boolean)var7);
            } else if (var7 instanceof Integer) {
               var2.setInt(var5, (Integer)var7);
            } else if (var7 instanceof Long) {
               var2.setLong(var5, (Long)var7);
            } else if (var7 instanceof Double) {
               var2.setDouble(var5, (Double)var7);
            } else if (var7 instanceof Float) {
               var2.setFloat(var5, (Float)var7);
            } else if (var7 instanceof BigDecimal) {
               var2.setBigDecimal(var5, (BigDecimal)var7);
            } else if (var7 instanceof Array) {
               var2.setArray(var5, (Array)var7);
            } else if (var7 instanceof Date) {
               var2.setTimestamp(var5, new Timestamp(((Date)var7).getTime()));
            } else {
               var2.setObject(var5, var7);
            }
         }
      }

   }
}
