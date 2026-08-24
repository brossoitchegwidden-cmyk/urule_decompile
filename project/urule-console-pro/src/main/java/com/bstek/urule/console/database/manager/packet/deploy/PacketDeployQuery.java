package com.bstek.urule.console.database.manager.packet.deploy;

import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface PacketDeployQuery {
   List list();

   List listWithContent();

   long count();

   Page paging(int pageIndex, int pageSize);

   PacketDeployQuery id(long id);

   PacketDeployQuery enable(boolean enable);

   PacketDeployQuery packetId(long packetId);

   PacketDeployQuery applyId(long packetId);

   PacketDeployQuery projectId(long projectId);

   PacketDeployQuery status(ApplyStatus status);

   PacketDeployQuery version(String version);

   PacketDeployQuery versionLike(String version);

   PacketDeployQuery descLike(String version);
}
