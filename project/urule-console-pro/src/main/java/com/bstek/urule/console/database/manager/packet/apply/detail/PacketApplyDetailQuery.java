package com.bstek.urule.console.database.manager.packet.apply.detail;

import java.util.List;

public interface PacketApplyDetailQuery {
   PacketApplyDetailQuery id(long var1);

   PacketApplyDetailQuery applyId(long var1);

   PacketApplyDetailQuery projectId(long var1);

   List list();
}
