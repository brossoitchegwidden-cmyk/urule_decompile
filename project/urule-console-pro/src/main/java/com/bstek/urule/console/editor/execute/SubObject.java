package com.bstek.urule.console.editor.execute;

import java.util.Map;

public class SubObject {
   private String a;
   private Map b;

   public SubObject(String var1, Map var2) {
      this.a = var1;
      this.b = var2;
   }

   public Map getMap() {
      return this.b;
   }

   public String getName() {
      return this.a;
   }
}
