package com.bstek.urule.console.database.manager.packet;

import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface PacketQuery {
   PacketQuery id(long var1);

   PacketQuery code(String var1);

   PacketQuery name(String var1);

   PacketQuery codeLike(String var1);

   PacketQuery projectId(long var1);

   PacketQuery idLike(String var1);

   PacketQuery nameLike(String var1);

   PacketQuery typeLike(String var1);

   PacketQuery descLike(String var1);

   PacketQuery createUserLike(String var1);

   PacketQuery enable(boolean var1);

   PacketQuery restEnable(boolean var1);

   PacketQuery auditEnable(boolean var1);

   List list();

   Page paging(int var1, int var2);
}
