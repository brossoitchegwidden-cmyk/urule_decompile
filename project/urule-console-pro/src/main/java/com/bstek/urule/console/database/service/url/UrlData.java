package com.bstek.urule.console.database.service.url;

import java.util.List;

public class UrlData {
   private List a;
   private boolean b;

   public UrlData(List var1, boolean var2) {
      this.a = var1;
      this.b = var2;
   }

   public List getList() {
      return this.a;
   }

   public boolean isProvider() {
      return this.b;
   }
}
