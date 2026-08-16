package com.bstek.urule.console.database.manager.packet.deploy;

import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketDeploy;

public interface PacketDeployManager {
   PacketDeployManager ins = new PacketDeployManagerImpl();

   void add(PacketDeploy var1);

   void delete(long var1);

   void deleteByApplyId(long var1);

   void deleteByPacketId(long var1);

   void deleteByProjectId(long var1);

   void disableAll(long var1);

   void updateEnable(long var1, boolean var3);

   void updateStatus(long var1, ApplyStatus var3);

   PacketDeploy load(long var1);

   PacketDeployQuery newQuery();
}
