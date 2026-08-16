package com.bstek.urule.console.editor.decision.crosstab;

import com.bstek.urule.console.editor.decision.PredefineRow;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrossData {
   private Map a;
   private CrossHeader b;
   private List c;
   private List d;
   private List e;
   private List f;
   private Map g;

   public CrossHeader getHeader() {
      return this.b;
   }

   public void setHeader(CrossHeader var1) {
      this.b = var1;
   }

   public List getRows() {
      return this.c;
   }

   public void setRows(List var1) {
      this.c = var1;
   }

   public List getColumns() {
      return this.d;
   }

   public void setColumns(List var1) {
      this.d = var1;
   }

   public List getCells() {
      return this.e;
   }

   public void setCells(List var1) {
      this.e = var1;
   }

   public List getPredefineRows() {
      return this.f;
   }

   public void setPredefineRows(List var1) {
      this.f = var1;
      this.g = new HashMap();

      for(PredefineRow var3 : (Iterable<PredefineRow>)(Iterable<?>)(var1)) {
         this.g.put(var3.getName(), var3);
      }

   }

   public Map getPredefineNameMap() {
      return this.g;
   }

   public Map getProperties() {
      return this.a;
   }

   public void setProperties(Map var1) {
      this.a = var1;
   }
}
