package com.bstek.urule.console.database.manager.packet.file;

import java.util.List;

public interface PacketFileQuery {
   PacketFileQuery id(long var1);

   PacketFileQuery packetId(long var1);

   PacketFileQuery projectId(long var1);

   List list();
}
