package com.bstek.urule.console.database.manager.packet.file;

import java.util.List;

public interface PacketFileQuery {
   PacketFileQuery id(long id);

   PacketFileQuery packetId(long packetId);

   PacketFileQuery projectId(long projectId);

   List list();
}
