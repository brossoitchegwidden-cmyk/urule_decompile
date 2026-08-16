package com.bstek.urule.console.database.manager.packet.deploy.file;

import java.util.List;

public interface PacketDeployFileQuery {
   PacketDeployFileQuery id(long var1);

   PacketDeployFileQuery packetDeployId(long var1);

   PacketDeployFileQuery projectId(long var1);

   List list();

   List listWithContent();
}
