package com.bstek.urule.console.database.manager.packet.packge;

import com.bstek.urule.console.database.model.PacketPackage;

public interface PacketPackageManager {
   PacketPackageManager ins = new PacketPackageManagerImpl();

   void add(PacketPackage pk);

   void update(PacketPackage pk);

   void deleteByPacketId(long packetId);

   void deleteByProjectId(long projectId);

   PacketPackage loadByPacketId(long packetId);

   String loadContent(long id);
}
