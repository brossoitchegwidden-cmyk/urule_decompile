package com.bstek.urule.console.database.manager.packet;

import com.bstek.urule.console.database.model.Packet;

public interface PacketManager {
   PacketManager ins = new PacketManagerImpl();

   Packet load(long var1);

   Packet load(String var1);

   void delete(long var1);

   void add(Packet var1);

   void update(Packet var1);

   void update(long var1, boolean var3);

   void updateRestConfig(Packet var1);

   void updateAuditConfig(Packet var1);

   void deleteByProjectId(long var1);

   int getCount(long var1);

   PacketQuery newQuery();
}
