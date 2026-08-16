package com.bstek.urule.console.editor.scorecard.simple;

import java.util.List;
import java.util.Map;

public class ScoreTableData {
   private Map a;
   private List b;
   private List c;

   public ScoreTableData(List var1, List var2) {
      this.b = var1;
      this.c = var2;
   }

   public List getHeaders() {
      return this.b;
   }

   public void setHeaders(List var1) {
      this.b = var1;
   }

   public List getRows() {
      return this.c;
   }

   public void setRows(List var1) {
      this.c = var1;
   }

   public Map getProperties() {
      return this.a;
   }

   public void setProperties(Map var1) {
      this.a = var1;
   }
}
