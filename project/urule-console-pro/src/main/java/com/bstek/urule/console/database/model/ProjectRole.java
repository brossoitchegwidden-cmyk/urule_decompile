package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class ProjectRole extends Role {
   private long projectId;

   public long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }
}
