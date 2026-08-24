package com.bstek.urule.console.cache.packet;

import java.util.List;

public interface ClusterPacketCacheAdapter {
   String BEAN_ID = "urule.clusterPacketCacheAdapter";

   /**缓存知识包*/
   void putPacket(long id, PacketData pd);

   /**缓存知识包*/
   void putPacket(String code, PacketData pd);

   /**根据id删除知识包*/
   void remove(long id);

   /**根据code删除知识包*/
   void remove(String code);

   /**重新加载知识包,知识包审批通过时触发*/
   List refreshPacket(String groupId, long packetId);

   /**将缓存中的知识包清除，重新加载所有发布的知识包, 客户端团队缓存刷新时触发*/
   List recacheAllPackets(String groupId);

   /**项目删除时,清除缓存中该项目下的所有知识包*/
   List removeProject(String groupId, long projectId, List list);
}
