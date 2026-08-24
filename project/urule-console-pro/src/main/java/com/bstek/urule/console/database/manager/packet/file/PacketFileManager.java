package com.bstek.urule.console.database.manager.packet.file;

import com.bstek.urule.console.database.model.PacketFile;

public interface PacketFileManager {
   PacketFileManager ins = new PacketFileManagerImpl();

   PacketFile load(long id);

   void add(PacketFile file);

   void delete(long id);

   void deleteByPacketId(long packetId);

   void deleteByProjectId(long projectId);

   void update(PacketFile file);

   PacketFileQuery newQuery();
}
