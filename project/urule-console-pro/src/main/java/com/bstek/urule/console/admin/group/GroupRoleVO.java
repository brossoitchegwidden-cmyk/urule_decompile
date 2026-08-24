package com.bstek.urule.console.admin.group;

import com.bstek.urule.console.database.model.GroupRole;

public class GroupRoleVO extends GroupRole {
   private boolean selected;

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }
}
