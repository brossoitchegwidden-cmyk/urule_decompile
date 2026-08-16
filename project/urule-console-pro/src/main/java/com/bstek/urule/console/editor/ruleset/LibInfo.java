package com.bstek.urule.console.editor.ruleset;

import java.util.Map;

class LibInfo {
   private String a;
   private Map b;

   public LibInfo(String var1, Map var2) {
      this.a = var1;
      this.b = var2;
   }

   public String getType() {
      return this.a;
   }

   public Map getMap() {
      return this.b;
   }
}
