package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class ProjectRole extends Role {
   private long a;

   public long getProjectId() {
      return this.a;
   }

   public void setProjectId(long var1) {
      this.a = var1;
   }
}
