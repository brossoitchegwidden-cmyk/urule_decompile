package com.bstek.urule.console.cache.packet;

import java.util.List;

public interface PacketCache {
   PacketCache ins = new PacketCacheImpl();

   PacketData getPacket(long var1);

   PacketData getPacket(String var1);

   byte[] getKnowledgeContent(long var1);

   List removeProject(long var1, String var3);

   List refreshPacket(long var1);

   void refreshPacketConfig(long var1);

   void cacheUploadPacketPackage(Long var1);

   List enableClientsPacket(String var1, long var2);

   List disableClientsPacket(String var1, long var2);
}
