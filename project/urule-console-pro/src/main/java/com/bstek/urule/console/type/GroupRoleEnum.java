package com.bstek.urule.console.type;

public enum GroupRoleEnum {
   Owner("团队所有者"),
   Manager("团队管理员"),
   User("团队普通成员");

   private String a;

   private GroupRoleEnum(String var3) {
      this.a = var3;
   }

   public String getName() {
      return this.a;
   }
}
