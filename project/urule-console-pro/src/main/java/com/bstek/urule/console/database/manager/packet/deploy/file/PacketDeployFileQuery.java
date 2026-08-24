package com.bstek.urule.console.database.manager.packet.deploy.file;

import java.util.List;

public interface PacketDeployFileQuery {
   PacketDeployFileQuery id(long id);

   PacketDeployFileQuery packetDeployId(long packetDeployId);

   PacketDeployFileQuery projectId(long projectId);

   List list();

   List listWithContent();
}
