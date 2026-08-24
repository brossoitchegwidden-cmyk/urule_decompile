package com.bstek.urule.console.database.manager.packet.apply;

import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketApply;

public interface PacketApplyManager {
   PacketApplyManager ins = new PacketApplyManagerImpl();

   void add(PacketApply apply);

   void delete(long id);

   void deleteByPacketId(long id);

   void deleteByProjectId(long projectId);

   PacketApply load(long id);

   void update(long id, ApplyStatus status);

   void updateDeployedPacketId(long id, long deployedPacketId);

   PacketApplyQuery newQuery();
}
