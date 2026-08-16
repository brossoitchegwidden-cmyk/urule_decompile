package com.bstek.urule.console.database.manager.report;

import com.bstek.urule.console.database.model.PacketDeploy;

public class PacketDeployVO extends PacketDeploy {
   private String a;
   private String b;

   public String getPacketName() {
      return this.a;
   }

   public void setPacketName(String var1) {
      this.a = var1;
   }

   public String getProjectName() {
      return this.b;
   }

   public void setProjectName(String var1) {
      this.b = var1;
   }
}
