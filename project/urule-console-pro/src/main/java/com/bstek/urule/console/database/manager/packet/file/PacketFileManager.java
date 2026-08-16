package com.bstek.urule.console.database.manager.packet.file;

import com.bstek.urule.console.database.model.PacketFile;

public interface PacketFileManager {
   PacketFileManager ins = new PacketFileManagerImpl();

   PacketFile load(long var1);

   void add(PacketFile var1);

   void delete(long var1);

   void deleteByPacketId(long var1);

   void deleteByProjectId(long var1);

   void update(PacketFile var1);

   PacketFileQuery newQuery();
}
