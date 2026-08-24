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

   private static DataParam resolveDataParam(List items, String text) {
      for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(items)) {
         if (text.equals(dataParam.getName())) {
            return dataParam;
         }
      }

      return null;
   }

   public static void setStmtQueryParameters(ParsedSql parsedSql, List params, PreparedStatement stmt) throws Exception {
      for(int index = 0; index < parsedSql.getParameterNames().size(); ++index) {
         String text = (String)parsedSql.getParameterNames().get(index);
         int number = index + 1;
         DataParam dataParam = resolveDataParam(params, text);
         if (dataParam == null) {
            stmt.setObject(number, (Object)null);
         } else {
            Object objectValue = dataParam.getValue();
            if (objectValue instanceof String) {
               stmt.setString(number, (String)objectValue);
            } else if (objectValue instanceof Boolean) {
               stmt.setBoolean(number, (Boolean)objectValue);
            } else if (objectValue instanceof Integer) {
               stmt.setInt(number, (Integer)objectValue);
            } else if (objectValue instanceof Long) {
               stmt.setLong(number, (Long)objectValue);
            } else if (objectValue instanceof Double) {
               stmt.setDouble(number, (Double)objectValue);
            } else if (objectValue instanceof Float) {
               stmt.setFloat(number, (Float)objectValue);
            } else if (objectValue instanceof BigDecimal) {
               stmt.setBigDecimal(number, (BigDecimal)objectValue);
            } else if (objectValue instanceof Array) {
               stmt.setArray(number, (Array)objectValue);
            } else if (objectValue instanceof Date) {
               stmt.setTimestamp(number, new Timestamp(((Date)objectValue).getTime()));
            } else {
               stmt.setObject(number, objectValue);
            }
         }
      }

   }
}
