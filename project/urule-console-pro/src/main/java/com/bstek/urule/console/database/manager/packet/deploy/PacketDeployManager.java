package com.bstek.urule.console.database.manager.packet.deploy;

import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketDeploy;

public interface PacketDeployManager {
   PacketDeployManager ins = new PacketDeployManagerImpl();

   void add(PacketDeploy deploy);

   void delete(long id);

   void deleteByApplyId(long id);

   void deleteByPacketId(long id);

   void deleteByProjectId(long projectId);

   void disableAll(long packetId);

   void updateEnable(long id, boolean enable);

   void updateStatus(long id, ApplyStatus status);

   PacketDeploy load(long id);

   PacketDeployQuery newQuery();
}
