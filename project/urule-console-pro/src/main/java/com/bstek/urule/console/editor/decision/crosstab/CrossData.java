package com.bstek.urule.console.editor.decision.crosstab;

import com.bstek.urule.console.editor.decision.PredefineRow;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrossData {
   private Map properties;
   private CrossHeader header;
   private List rows;
   private List columns;
   private List cells;
   private List predefineRows;
   private Map predefineNameMap;

   public CrossHeader getHeader() {
      return this.header;
   }

   public void setHeader(CrossHeader header) {
      this.header = header;
   }

   public List getRows() {
      return this.rows;
   }

   public void setRows(List rows) {
      this.rows = rows;
   }

   public List getColumns() {
      return this.columns;
   }

   public void setColumns(List columns) {
      this.columns = columns;
   }

   public List getCells() {
      return this.cells;
   }

   public void setCells(List cells) {
      this.cells = cells;
   }

   public List getPredefineRows() {
      return this.predefineRows;
   }

   public void setPredefineRows(List predefineRows) {
      this.predefineRows = predefineRows;
      this.predefineNameMap = new HashMap();

      for(PredefineRow predefineRow : (Iterable<PredefineRow>)(Iterable<?>)(predefineRows)) {
         this.predefineNameMap.put(predefineRow.getName(), predefineRow);
      }

   }

   public Map getPredefineNameMap() {
      return this.predefineNameMap;
   }

   public Map getProperties() {
      return this.properties;
   }

   public void setProperties(Map properties) {
      this.properties = properties;
   }
}
