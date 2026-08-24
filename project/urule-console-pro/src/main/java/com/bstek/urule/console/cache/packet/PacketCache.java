package com.bstek.urule.console.cache.packet;

import java.util.List;

public interface PacketCache {
   PacketCache ins = new PacketCacheImpl();

   /**根据知识包ID加载知识包*/
   PacketData getPacket(long id);

   /**根据知识包code信息加载知识包*/
   PacketData getPacket(String code);

   /**获取缓存中知识包的byte[]*/
   byte[] getKnowledgeContent(long id);

   List removeProject(long projectId, String groupId);

   List refreshPacket(long id);

   void refreshPacketConfig(long id);

   void cacheUploadPacketPackage(Long packetId);

   List enableClientsPacket(String groupId, long packetId);

   List disableClientsPacket(String groupId, long packetId);
}
