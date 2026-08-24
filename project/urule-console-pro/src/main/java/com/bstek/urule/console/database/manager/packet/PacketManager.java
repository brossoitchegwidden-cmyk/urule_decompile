package com.bstek.urule.console.database.manager.packet;

import com.bstek.urule.console.database.model.Packet;

public interface PacketManager {
   PacketManager ins = new PacketManagerImpl();

   Packet load(long id);

   Packet load(String code);

   void delete(long id);

   void add(Packet packet);

   void update(Packet packet);

   void update(long id, boolean enable);

   void updateRestConfig(Packet packet);

   void updateAuditConfig(Packet packet);

   void deleteByProjectId(long projectId);

   int getCount(long projectId);

   PacketQuery newQuery();
}
