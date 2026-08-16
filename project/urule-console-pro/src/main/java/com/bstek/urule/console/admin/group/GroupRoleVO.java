package com.bstek.urule.console.admin.group;

import com.bstek.urule.console.database.model.GroupRole;

public class GroupRoleVO extends GroupRole {
   private boolean a;

   public boolean isSelected() {
      return this.a;
   }

   public void setSelected(boolean var1) {
      this.a = var1;
   }
}
