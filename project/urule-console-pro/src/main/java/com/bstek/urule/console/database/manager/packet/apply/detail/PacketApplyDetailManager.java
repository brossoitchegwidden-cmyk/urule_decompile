package com.bstek.urule.console.database.manager.packet.apply.detail;

import com.bstek.urule.console.database.model.PacketApplyDetail;
import java.util.List;

public interface PacketApplyDetailManager {
   PacketApplyDetailManager ins = new PacketApplyDetailManagerImpl();

   void add(PacketApplyDetail detail);

   void deleteByApplyId(long projectId);

   void deleteByProjectId(long projectId);

   PacketApplyDetail load(long id);

   List loadByApplyId(long applyId);

   PacketApplyDetailQuery newQuery();
}
