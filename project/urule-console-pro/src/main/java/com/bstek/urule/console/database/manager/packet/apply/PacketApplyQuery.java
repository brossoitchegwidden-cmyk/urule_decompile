package com.bstek.urule.console.database.manager.packet.apply;

import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Page;
import java.util.Date;
import java.util.List;

public interface PacketApplyQuery {
   PacketApplyQuery id(long id);

   PacketApplyQuery projectId(long projectId);

   PacketApplyQuery packetId(long packetId);

   PacketApplyQuery type(ApplyType type);

   PacketApplyQuery status(ApplyStatus inStatus);

   PacketApplyQuery statusIn(ApplyStatus[] status);

   PacketApplyQuery notStatus(ApplyStatus status);

   PacketApplyQuery titleLike(String title);

   PacketApplyQuery descLike(String desc);

   PacketApplyQuery approver(String approver);

   PacketApplyQuery createUser(String createUser);

   PacketApplyQuery createUserLike(String createUser);

   PacketApplyQuery startDate(Date date);

   PacketApplyQuery endDate(Date date);

   List list();

   Page paging(int pageIndex, int pageSize);
}
