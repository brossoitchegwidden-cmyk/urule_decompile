package com.bstek.urule.console.admin.project;

import com.bstek.urule.console.database.model.ProjectRole;

public class ProjectRoleVO extends ProjectRole {
   private boolean selected;

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }
}
