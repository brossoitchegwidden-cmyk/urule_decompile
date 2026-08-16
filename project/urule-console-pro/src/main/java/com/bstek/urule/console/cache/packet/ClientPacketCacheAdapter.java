package com.bstek.urule.console.cache.packet;

import java.util.List;

public interface ClientPacketCacheAdapter {
   String BEAN_ID = "urule.clientPacketCacheAdapter";

   List enableClientsPacket(String var1, long var2);

   List disableClientsPacket(String var1, long var2);

   List pushPacketToClients(String var1, PacketData var2);
}
