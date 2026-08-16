package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class GroupRole extends Role {
   private String a;

   public String getGroupId() {
      return this.a;
   }

   public void setGroupId(String var1) {
      this.a = var1;
   }
}
