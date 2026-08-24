package com.bstek.urule.console.batch.writer;

import com.bstek.urule.console.batch.HiveStatement;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.GeneralEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class HiveWriter implements Writer {
   public void storeRecord(Map stmtMap, BatchDataResolver dataResolver, BatchDataResolverItem storeItem, GeneralEntity record) throws Exception {
      GeneralEntity record2 = record;
      HiveStatement hiveStatement = (HiveStatement)stmtMap.get(storeItem.getName());
      String text = "";
      for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(storeItem.getParams())) {
         Object objectValue = record2.get(dataParam.getName());
         String formatter = dataParam.getFormatter();
         if (objectValue == null) {
            text = this.appendColumnValue(text, "null");
         } else if (objectValue instanceof String) {
            text = this.appendColumnValue(text, "'" + (String)objectValue + "'");
         } else if (!(objectValue instanceof Date)) {
            text = this.appendColumnValue(text, objectValue.toString());
         } else {
            formatter = "yyyy-MM-dd HH:mm:ss";
            text = this.appendColumnValue(text, this.formatTemporalValue(objectValue, formatter));
         }
      }

      hiveStatement.addBatch(text);
   }

   private String appendColumnValue(String text, String text2) {
      return StringUtils.isBlank(text) ? text2 : text + "," + text2;
   }

   private String formatTemporalValue(Object objectValue, String formatter) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(formatter);
      return "'" + dateFormat.format((Date)objectValue) + "'";
   }
}
