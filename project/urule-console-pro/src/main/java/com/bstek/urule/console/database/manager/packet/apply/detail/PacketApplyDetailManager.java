package com.bstek.urule.console.database.manager.packet.apply.detail;

import com.bstek.urule.console.database.model.PacketApplyDetail;
import java.util.List;

public interface PacketApplyDetailManager {
   PacketApplyDetailManager ins = new PacketApplyDetailManagerImpl();

   void add(PacketApplyDetail var1);

   void deleteByApplyId(long var1);

   void deleteByProjectId(long var1);

   PacketApplyDetail load(long var1);

   List loadByApplyId(long var1);

   PacketApplyDetailQuery newQuery();
}
