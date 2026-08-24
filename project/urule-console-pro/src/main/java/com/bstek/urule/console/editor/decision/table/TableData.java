package com.bstek.urule.console.editor.decision.table;

import com.bstek.urule.console.editor.decision.PredefineRow;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableData {
   private Map properties;
   private List headers;
   private List rows;
   private List predefineRows;
   private Map predefineNameMap;

   public TableData(Map properties, List headers, List rows, List predefineRows) {
      this.properties = properties;
      this.headers = headers;
      this.rows = rows;
      this.predefineRows = predefineRows;
      this.predefineNameMap = new HashMap();

      for(PredefineRow predefineRow : (Iterable<PredefineRow>)(Iterable<?>)(predefineRows)) {
         this.predefineNameMap.put(predefineRow.getName(), predefineRow);
      }

   }

   public Map getProperties() {
      return this.properties;
   }

   public List getHeaders() {
      return this.headers;
   }

   public List getRows() {
      return this.rows;
   }

   public List getPredefineRows() {
      return this.predefineRows;
   }

   public Map getPredefineNameMap() {
      return this.predefineNameMap;
   }
}
