package com.bstek.urule.console.type;

public enum ProjectModule {
   project("项目"),
   members("团队成员"),
   permissions("权限配置"),
   setting("项目设置");

   private String a;

   private ProjectModule(String var3) {
      this.a = var3;
   }

   public String getLabel() {
      return this.a;
   }
}
