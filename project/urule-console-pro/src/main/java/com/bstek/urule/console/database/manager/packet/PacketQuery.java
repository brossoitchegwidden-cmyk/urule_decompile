package com.bstek.urule.console.database.manager.packet;

import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface PacketQuery {
   PacketQuery id(long id);

   PacketQuery code(String code);

   PacketQuery name(String name);

   PacketQuery codeLike(String code);

   PacketQuery projectId(long projectId);

   PacketQuery idLike(String id);

   PacketQuery nameLike(String name);

   PacketQuery typeLike(String type);

   PacketQuery descLike(String desc);

   PacketQuery createUserLike(String createUser);

   PacketQuery enable(boolean enable);

   PacketQuery restEnable(boolean restEnable);

   PacketQuery auditEnable(boolean auditEnable);

   List list();

   Page paging(int pageIndex, int pageSize);
}
