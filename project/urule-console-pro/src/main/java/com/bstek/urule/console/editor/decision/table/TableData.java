package com.bstek.urule.console.editor.decision.table;

import com.bstek.urule.console.editor.decision.PredefineRow;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableData {
   private Map a;
   private List b;
   private List c;
   private List d;
   private Map e;

   public TableData(Map var1, List var2, List var3, List var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = new HashMap();

      for(PredefineRow var6 : (Iterable<PredefineRow>)(Iterable<?>)(var4)) {
         this.e.put(var6.getName(), var6);
      }

   }

   public Map getProperties() {
      return this.a;
   }

   public List getHeaders() {
      return this.b;
   }

   public List getRows() {
      return this.c;
   }

   public List getPredefineRows() {
      return this.d;
   }

   public Map getPredefineNameMap() {
      return this.e;
   }
}
