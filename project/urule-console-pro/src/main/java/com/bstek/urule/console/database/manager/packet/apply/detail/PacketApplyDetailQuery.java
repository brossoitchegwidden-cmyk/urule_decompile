package com.bstek.urule.console.database.manager.packet.apply.detail;

import java.util.List;

public interface PacketApplyDetailQuery {
   PacketApplyDetailQuery id(long id);

   PacketApplyDetailQuery applyId(long applyId);

   PacketApplyDetailQuery projectId(long projectId);

   List list();
}
