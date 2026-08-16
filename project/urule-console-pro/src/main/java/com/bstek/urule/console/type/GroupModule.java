package com.bstek.urule.console.type;

public enum GroupModule {
   members("团队成员"),
   permissions("权限配置"),
   setting("项目设置"),
   projects("项目管理"),
   activities("项目动态"),
   audits("审计"),
   visits("访问"),
   logs("日志"),
   clusterUrls("集群配置"),
   clientUrls("客户端配置"),
   dynamicJar("客户端配置"),
   datasource("数据源配置");

   String a;

   private GroupModule(String var3) {
      this.a = var3;
   }

   public String getLabel() {
      return this.a;
   }
}
