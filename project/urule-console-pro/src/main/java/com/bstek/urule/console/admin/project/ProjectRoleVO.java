package com.bstek.urule.console.admin.project;

import com.bstek.urule.console.database.model.ProjectRole;

public class ProjectRoleVO extends ProjectRole {
   private boolean a;

   public boolean isSelected() {
      return this.a;
   }

   public void setSelected(boolean var1) {
      this.a = var1;
   }
}
