package com.bstek.urule.console.admin.group;

import com.bstek.urule.console.database.model.User;
import java.util.List;

public class GroupUserVO extends User {
   private List roles;

   public List getRoles() {
      return this.roles;
   }

   public void setRoles(List roles) {
      this.roles = roles;
   }
}
