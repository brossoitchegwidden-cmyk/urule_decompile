package com.bstek.urule.console.database.manager.report;

import com.bstek.urule.console.database.model.PacketDeploy;

public class PacketDeployVO extends PacketDeploy {
   private String packetName;
   private String projectName;

   public String getPacketName() {
      return this.packetName;
   }

   public void setPacketName(String packetName) {
      this.packetName = packetName;
   }

   public String getProjectName() {
      return this.projectName;
   }

   public void setProjectName(String projectName) {
      this.projectName = projectName;
   }
}
