package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Filter {
   private String a;
   private int b;
   private String c;
   private List d = new ArrayList();

   public int getIndex() {
      return this.b;
   }

   public void setIndex(int var1) {
      this.b = var1;
   }

   public String getName() {
      return this.c;
   }

   public void setName(String var1) {
      this.c = var1;
   }

   public List getItems() {
      return this.d;
   }

   public void setItems(List var1) {
      this.d = var1;
   }

   public String getUuid() {
      return this.a;
   }

   public void setUuid(String var1) {
      this.a = var1;
   }
}
