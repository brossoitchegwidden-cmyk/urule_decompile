package com.bstek.urule.console.database.manager.packet.apply;

import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Page;
import java.util.Date;
import java.util.List;

public interface PacketApplyQuery {
   PacketApplyQuery id(long var1);

   PacketApplyQuery projectId(long var1);

   PacketApplyQuery packetId(long var1);

   PacketApplyQuery type(ApplyType var1);

   PacketApplyQuery status(ApplyStatus var1);

   PacketApplyQuery statusIn(ApplyStatus[] var1);

   PacketApplyQuery notStatus(ApplyStatus var1);

   PacketApplyQuery titleLike(String var1);

   PacketApplyQuery descLike(String var1);

   PacketApplyQuery approver(String var1);

   PacketApplyQuery createUser(String var1);

   PacketApplyQuery createUserLike(String var1);

   PacketApplyQuery startDate(Date var1);

   PacketApplyQuery endDate(Date var1);

   List list();

   Page paging(int var1, int var2);
}
