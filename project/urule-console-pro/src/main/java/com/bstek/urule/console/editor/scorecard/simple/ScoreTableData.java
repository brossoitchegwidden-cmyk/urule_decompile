package com.bstek.urule.console.editor.scorecard.simple;

import java.util.List;
import java.util.Map;

public class ScoreTableData {
   private Map properties;
   private List headers;
   private List rows;

   public ScoreTableData(List headers, List rows) {
      this.headers = headers;
      this.rows = rows;
   }

   public List getHeaders() {
      return this.headers;
   }

   public void setHeaders(List headers) {
      this.headers = headers;
   }

   public List getRows() {
      return this.rows;
   }

   public void setRows(List rows) {
      this.rows = rows;
   }

   public Map getProperties() {
      return this.properties;
   }

   public void setProperties(Map properties) {
      this.properties = properties;
   }
}
