package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class PacketParam {
   private String a;
   private String b;
   private String c;
   private String d;

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public String getLabel() {
      return this.b;
   }

   public void setLabel(String var1) {
      this.b = var1;
   }

   public String getDataType() {
      return this.c;
   }

   public void setDataType(String var1) {
      this.c = var1;
   }

   public String getBatchParamName() {
      return this.d;
   }

   public void setBatchParamName(String var1) {
      this.d = var1;
   }
}
