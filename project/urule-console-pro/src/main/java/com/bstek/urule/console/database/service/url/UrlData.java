package com.bstek.urule.console.database.service.url;

import java.util.List;

public class UrlData {
   private List entries;
   private boolean provider;

   public UrlData(List list, boolean provider) {
      this.entries = list;
      this.provider = provider;
   }

   public List getList() {
      return this.entries;
   }

   public boolean isProvider() {
      return this.provider;
   }
}
