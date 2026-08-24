package com.bstek.urule.console.cache.packet;

import com.bstek.urule.Utils;
import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.PacketQuery;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketDeploy;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class PacketCacheImpl implements PacketCache {
   private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(PacketCacheImpl.class.getName());
   private ClusterPacketCacheAdapter defaultClusterPacketCacheAdapter = null;
   private ClientPacketCacheAdapter clientPacketCacheAdapter = null;
   private MemoryPacketCache memoryPacketCache = new MemoryPacketCache();

   protected PacketCacheImpl() {
      this.initializeState();

      try {
         this.defaultClusterPacketCacheAdapter = (ClusterPacketCacheAdapter)Utils.getApplicationContext().getBean("urule.clusterPacketCacheAdapter");
      } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
         this.defaultClusterPacketCacheAdapter = new DefaultClusterPacketCacheAdapter();
      }

      try {
         this.clientPacketCacheAdapter = (ClientPacketCacheAdapter)Utils.getApplicationContext().getBean("urule.clientPacketCacheAdapter");
      } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException2) {
         this.clientPacketCacheAdapter = new DefaultClientPacketCacheAdapter();
      }

      this.syncMemoryCacheToCluster();
   }

   private void syncMemoryCacheToCluster() {
      Map packetIdMap = this.memoryPacketCache.getPacketIdMap();

      for(Long longValue : (Iterable<Long>)(Iterable<?>)(packetIdMap.keySet())) {
         PacketData packetData = (PacketData)packetIdMap.get(longValue);
         this.defaultClusterPacketCacheAdapter.putPacket(longValue, packetData);
         String code = packetData.getPacket().getCode();
         if (StringUtils.isNotBlank(code)) {
            this.defaultClusterPacketCacheAdapter.putPacket(code, packetData);
         }
      }

   }

   public ClientPacketCacheAdapter getClientPacketCacheAdapter() {
      return this.clientPacketCacheAdapter;
   }

   private void initializeState() {
      for(PacketDeploy packetDeploy : (Iterable<PacketDeploy>)(Iterable<?>)(PacketDeployManager.ins.newQuery().enable(true).listWithContent())) {
         Packet packet = PacketManager.ins.load(packetDeploy.getPacketId());
         if (packet != null && packet.isEnable()) {
            try {
               this.cacheDeployedPacket(packetDeploy);
            } catch (DeserializeException deserializeException) {
               LOGGER.log(java.util.logging.Level.SEVERE, "Packet deserialize error, packetId:" + packetDeploy.getPacketId() + ", deployId:" + packetDeploy.getId(), deserializeException);
            }
         }
      }

      this.cacheUploadPacketPackage((Long)null);
   }

   /**将缓存中的知识包清除，重新加载所有发布的知识包，此方法不在接口中声明*/
   public void doRecacheAllPackets() {
      this.memoryPacketCache.clear();
      this.initializeState();
   }

   /**将缓存中的知识包清除，重新加载所有发布的知识包，并通知集群中的其它服务器执行同样操作，此方法不在接口中声明*/
   public List recacheAllPackets(String groupId) {
      this.doRecacheAllPackets();
      IDGenerator.getInstance().clean();
      List recacheAllPacketsResult = this.defaultClusterPacketCacheAdapter.recacheAllPackets(groupId);
      this.syncMemoryCacheToCluster();
      return recacheAllPacketsResult;
   }
   public PacketData getPacket(String code) {
      return this.memoryPacketCache.getPacket(code);
   }
   public PacketData getPacket(long id) {
      return this.memoryPacketCache.getPacket(id);
   }

   public void removePacket(long id) {
      this.doRemovePacket(id);
      this.defaultClusterPacketCacheAdapter.remove(id);
   }

   public void doRemovePacket(long id) {
      this.memoryPacketCache.remove(id);
   }

   public void removePacket(String code) {
      this.doRemovePacket(code);
      this.defaultClusterPacketCacheAdapter.remove(code);
   }

   public void doRemovePacket(String code) {
      this.memoryPacketCache.remove(code);
   }

   public void refreshPacketConfig(long id) {
      PacketData packet = this.getPacket(id);
      if (packet != null) {
         Packet packet2 = PacketManager.ins.load(id);
         PacketData packetData = new PacketData(packet2, packet.getKnowledgePackageWrapper());
         this.memoryPacketCache.putPacket(id, packetData);
         String code = packet2.getCode();
         if (StringUtils.isNotBlank(code)) {
            this.memoryPacketCache.putPacket(code, packetData);
         }

         String parameter = null;
         if (RequestHolder.getRequest() != null) {
            parameter = RequestHolder.getRequest().getParameter("groupId");
         }

         this.defaultClusterPacketCacheAdapter.refreshPacket(parameter, id);
      }
   }

   public List refreshPacket(long id) {
      String parameter = null;
      if (RequestHolder.getRequest() != null) {
         parameter = RequestHolder.getRequest().getParameter("groupId");
      }

      this.doReloadPacket(id);
      return this.defaultClusterPacketCacheAdapter.refreshPacket(parameter, id);
   }

   /**此方法专用于集群服务器更新知识包使用,不在接口中声明*/
   public Packet doReloadPacket(long id) {
      Packet packet = PacketManager.ins.load(id);
      if (packet == null) {
         throw new RuleException("packet package:" + id + " not exist!");
      } else if (!packet.isEnable()) {
         this.memoryPacketCache.remove(id);
         String code = packet.getCode();
         if (StringUtils.isNotBlank(code)) {
            this.memoryPacketCache.remove(code);
         }

         return packet;
      } else {
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         List items = PacketDeployManager.ins.newQuery().packetId(id).enable(true).listWithContent();
         if (items.size() > 0) {
            PacketDeploy packetDeploy = (PacketDeploy)items.get(0);
            this.cacheDeployedPacket(packetDeploy);
            LOGGER.info("[" + simpleDateFormat.format(new Date()) + "] Successfully reloaded file packet package: " + id);
         } else {
            this.cacheUploadPacketPackage(id);
            LOGGER.info("[" + simpleDateFormat.format(new Date()) + "] Successfully reloaded uploaded packet package: " + id);
         }

         return packet;
      }
   }

   public List removeProject(long projectId, String groupId) {
      ArrayList items = new ArrayList();
      List items2 = this.doRemoveProjectPackets(projectId);
      return (List)(items2.size() == 0 ? items : this.defaultClusterPacketCacheAdapter.removeProject(groupId, projectId, items2));
   }

   /**此方法专用于集群服务器更新知识包使用,不在接口中声明*/
   public List doRemoveProjectPackets(long projectId) {
      ArrayList doRemoveProjectPacketsResult = new ArrayList();
      ArrayList items = new ArrayList();
      Map packetCodeMap = this.memoryPacketCache.getPacketCodeMap();

      for(String text : (Iterable<String>)(Iterable<?>)(packetCodeMap.keySet())) {
         PacketData packetData = (PacketData)packetCodeMap.get(text);
         if (packetData.getPacket().getProjectId() == projectId) {
            items.add(text);
            doRemoveProjectPacketsResult.add(packetData.getPacket());
         }
      }

      for(PacketConfig packetConfig : (Iterable<PacketConfig>)(Iterable<?>)(doRemoveProjectPacketsResult)) {
         this.memoryPacketCache.remove(packetConfig.getId());
      }

      for(String text2 : (Iterable<String>)(Iterable<?>)(items)) {
         this.memoryPacketCache.remove(text2);
      }

      return doRemoveProjectPacketsResult;
   }

   public void cacheUploadPacketPackage(Long packetId) {
      PacketQuery packetQuery = PacketManager.ins.newQuery();
      if (packetId != null) {
         packetQuery.id(packetId);
      }

      for(Packet packet : (Iterable<Packet>)(Iterable<?>)(packetQuery.enable(true).typeLike("upload").list())) {
         PacketPackage byPacketId = PacketPackageManager.ins.loadByPacketId(packet.getId());
         if (byPacketId != null) {
            String content = PacketPackageManager.ins.loadContent(byPacketId.getId());
            if (!StringUtils.isBlank(content)) {
               boolean flag = true;
               KnowledgePackageWrapper knowledgePackageWrapper = null;
               if (packetId == null) {
                  try {
                     knowledgePackageWrapper = Utils.stringToKnowledgePackageWrapper(content);
                  } catch (DeserializeException deserializeException) {
                     flag = false;
                     LOGGER.log(java.util.logging.Level.SEVERE, "Packet deserialize error, packetId:" + byPacketId.getPacketId() + ", deployId:" + byPacketId.getId(), deserializeException);
                  }
               } else {
                  knowledgePackageWrapper = Utils.stringToKnowledgePackageWrapper(content);
               }

               if (flag && knowledgePackageWrapper != null) {
                  KnowledgePackageImpl knowledgePackage = (KnowledgePackageImpl)knowledgePackageWrapper.getKnowledgePackage();
                  knowledgePackage.setPackageInfo(String.valueOf(packet.getId()));
                  knowledgePackage.setMonitor(packet.isAuditEnable());
                  knowledgePackage.setTimestamp(packet.getUpdateDate().getTime());
                  this.cachePacket(packet, knowledgePackageWrapper);
               }
            }
         }
      }

   }

   public List enableClientsPacket(String groupId, long packetId) {
      return this.clientPacketCacheAdapter.enableClientsPacket(groupId, packetId);
   }

   public List disableClientsPacket(String groupId, long packetId) {
      return this.clientPacketCacheAdapter.disableClientsPacket(groupId, packetId);
   }

   private void cacheDeployedPacket(PacketDeploy packetDeploy) {
      KnowledgePackageWrapper knowledgePackageWrapper = Utils.stringToKnowledgePackageWrapper(packetDeploy.getContent());
      KnowledgePackageImpl knowledgePackage = (KnowledgePackageImpl)knowledgePackageWrapper.getKnowledgePackage();
      Packet packet = PacketManager.ins.load(packetDeploy.getPacketId());
      knowledgePackage.setVersion(packetDeploy.getVersion());
      knowledgePackage.setPackageInfo(String.valueOf(packet.getId()));
      knowledgePackage.setMonitor(packet.isAuditEnable());
      knowledgePackage.setTimestamp(packet.getUpdateDate().getTime());
      this.cachePacket(packet, knowledgePackageWrapper);
   }

   private void cachePacket(Packet packet, KnowledgePackageWrapper knowledgePackageWrapper) {
      PacketData packetData = new PacketData(packet, knowledgePackageWrapper);
      this.memoryPacketCache.putPacket(packet.getId(), packetData);
      String code = packet.getCode();
      if (StringUtils.isNotBlank(code)) {
         this.memoryPacketCache.putPacket(code, packetData);
      }

   }
   public byte[] getKnowledgeContent(long id) {
      return this.memoryPacketCache.getKnowledgeWrapper(id);
   }
}
