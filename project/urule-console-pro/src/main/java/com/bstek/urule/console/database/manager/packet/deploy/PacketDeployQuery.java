package com.bstek.urule.console.database.manager.packet.deploy;

import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface PacketDeployQuery {
   List list();

   List listWithContent();

   long count();

   Page paging(int var1, int var2);

   PacketDeployQuery id(long var1);

   PacketDeployQuery enable(boolean var1);

   PacketDeployQuery packetId(long var1);

   PacketDeployQuery applyId(long var1);

   PacketDeployQuery projectId(long var1);

   PacketDeployQuery status(ApplyStatus var1);

   PacketDeployQuery version(String var1);

   PacketDeployQuery versionLike(String var1);

   PacketDeployQuery descLike(String var1);
}
