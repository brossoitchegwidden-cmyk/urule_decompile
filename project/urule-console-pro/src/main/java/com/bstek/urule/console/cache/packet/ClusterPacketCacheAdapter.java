package com.bstek.urule.console.cache.packet;

import java.util.List;

public interface ClusterPacketCacheAdapter {
   String BEAN_ID = "urule.clusterPacketCacheAdapter";

   void putPacket(long var1, PacketData var3);

   void putPacket(String var1, PacketData var2);

   void remove(long var1);

   void remove(String var1);

   List refreshPacket(String var1, long var2);

   List recacheAllPackets(String var1);

   List removeProject(String var1, long var2, List var4);
}
