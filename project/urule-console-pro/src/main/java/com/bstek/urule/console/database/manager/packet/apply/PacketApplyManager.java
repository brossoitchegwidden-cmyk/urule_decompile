package com.bstek.urule.console.database.manager.packet.apply;

import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketApply;

public interface PacketApplyManager {
   PacketApplyManager ins = new PacketApplyManagerImpl();

   void add(PacketApply var1);

   void delete(long var1);

   void deleteByPacketId(long var1);

   void deleteByProjectId(long var1);

   PacketApply load(long var1);

   void update(long var1, ApplyStatus var3);

   void updateDeployedPacketId(long var1, long var3);

   PacketApplyQuery newQuery();
}
