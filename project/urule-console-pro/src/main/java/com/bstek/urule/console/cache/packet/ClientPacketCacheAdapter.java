package com.bstek.urule.console.cache.packet;

import java.util.List;

public interface ClientPacketCacheAdapter {
   String BEAN_ID = "urule.clientPacketCacheAdapter";

   /**知识包启用时同步客户端的缓存*/
   List enableClientsPacket(String groupId, long packetId);

   /**知识包停用时同步客户端的缓存*/
   List disableClientsPacket(String groupId, long packetId);

   /**发布到客户端*/
   List pushPacketToClients(String groupId, PacketData packetData);
}
