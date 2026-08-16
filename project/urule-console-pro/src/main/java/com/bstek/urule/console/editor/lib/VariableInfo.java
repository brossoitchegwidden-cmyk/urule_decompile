package com.bstek.urule.console.editor.lib;

import java.util.List;

public class VariableInfo {
   private long a;
   private String b;
   private String c;
   private List d;

   public VariableInfo(long var1, String var3, String var4, List var5) {
      this.a = var1;
      this.b = var3;
      this.c = var4;
      this.d = var5;
   }

   public long getId() {
      return this.a;
   }

   public String getPath() {
      return this.b;
   }

   public String getType() {
      return this.c;
   }

   public List getVariableCategories() {
      return this.d;
   }
}
