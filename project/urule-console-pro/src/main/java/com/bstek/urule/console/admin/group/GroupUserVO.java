package com.bstek.urule.console.admin.group;

import com.bstek.urule.console.database.model.User;
import java.util.List;

public class GroupUserVO extends User {
   private List a;

   public List getRoles() {
      return this.a;
   }

   public void setRoles(List var1) {
      this.a = var1;
   }
}
