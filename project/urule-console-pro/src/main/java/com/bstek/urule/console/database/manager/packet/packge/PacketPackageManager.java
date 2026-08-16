package com.bstek.urule.console.database.manager.packet.packge;

import com.bstek.urule.console.database.model.PacketPackage;

public interface PacketPackageManager {
   PacketPackageManager ins = new PacketPackageManagerImpl();

   void add(PacketPackage var1);

   void update(PacketPackage var1);

   void deleteByPacketId(long var1);

   void deleteByProjectId(long var1);

   PacketPackage loadByPacketId(long var1);

   String loadContent(long var1);
}
