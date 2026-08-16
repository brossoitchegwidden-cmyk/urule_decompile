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
   private ClusterPacketCacheAdapter a = null;
   private ClientPacketCacheAdapter b = null;
   private MemoryPacketCache c = new MemoryPacketCache();

   protected PacketCacheImpl() {
      this.b();

      try {
         this.a = (ClusterPacketCacheAdapter)Utils.getApplicationContext().getBean("urule.clusterPacketCacheAdapter");
      } catch (NoSuchBeanDefinitionException var3) {
         this.a = new DefaultClusterPacketCacheAdapter();
      }

      try {
         this.b = (ClientPacketCacheAdapter)Utils.getApplicationContext().getBean("urule.clientPacketCacheAdapter");
      } catch (NoSuchBeanDefinitionException var2) {
         this.b = new DefaultClientPacketCacheAdapter();
      }

      this.a();
   }

   private void a() {
      Map var1 = this.c.getPacketIdMap();

      for(Long var3 : (Iterable<Long>)(Iterable<?>)(var1.keySet())) {
         PacketData var4 = (PacketData)var1.get(var3);
         this.a.putPacket(var3, var4);
         String var5 = var4.getPacket().getCode();
         if (StringUtils.isNotBlank(var5)) {
            this.a.putPacket(var5, var4);
         }
      }

   }

   public ClientPacketCacheAdapter getClientPacketCacheAdapter() {
      return this.b;
   }

   private void b() {
      for(PacketDeploy var3 : (Iterable<PacketDeploy>)(Iterable<?>)(PacketDeployManager.ins.newQuery().enable(true).listWithContent())) {
         Packet var4 = PacketManager.ins.load(var3.getPacketId());
         if (var4 != null && var4.isEnable()) {
            try {
               this.a(var3);
            } catch (DeserializeException var6) {
               System.out.println("Packet deserialize error, packetId:" + var3.getPacketId() + ", deployId:" + var3.getId());
            }
         }
      }

      this.cacheUploadPacketPackage((Long)null);
   }

   public void doRecacheAllPackets() {
      this.c.clear();
      this.b();
   }

   public List recacheAllPackets(String var1) {
      this.doRecacheAllPackets();
      IDGenerator.getInstance().clean();
      List var2 = this.a.recacheAllPackets(var1);
      this.a();
      return var2;
   }

   public PacketData getPacket(String var1) {
      return this.c.getPacket(var1);
   }

   public PacketData getPacket(long var1) {
      return this.c.getPacket(var1);
   }

   public void removePacket(long var1) {
      this.doRemovePacket(var1);
      this.a.remove(var1);
   }

   public void doRemovePacket(long var1) {
      this.c.remove(var1);
   }

   public void removePacket(String var1) {
      this.doRemovePacket(var1);
      this.a.remove(var1);
   }

   public void doRemovePacket(String var1) {
      this.c.remove(var1);
   }

   public void refreshPacketConfig(long var1) {
      PacketData var3 = this.getPacket(var1);
      if (var3 != null) {
         Packet var4 = PacketManager.ins.load(var1);
         PacketData var5 = new PacketData(var4, var3.getKnowledgePackageWrapper());
         this.c.putPacket(var1, var5);
         String var6 = var4.getCode();
         if (StringUtils.isNotBlank(var6)) {
            this.c.putPacket(var6, var5);
         }

         String var7 = null;
         if (RequestHolder.getRequest() != null) {
            var7 = RequestHolder.getRequest().getParameter("groupId");
         }

         this.a.refreshPacket(var7, var1);
      }
   }

   public List refreshPacket(long var1) {
      String var3 = null;
      if (RequestHolder.getRequest() != null) {
         var3 = RequestHolder.getRequest().getParameter("groupId");
      }

      this.doReloadPacket(var1);
      return this.a.refreshPacket(var3, var1);
   }

   public Packet doReloadPacket(long var1) {
      Packet var3 = PacketManager.ins.load(var1);
      if (var3 == null) {
         throw new RuleException("packet package:" + var1 + " not exist!");
      } else if (!var3.isEnable()) {
         this.c.remove(var1);
         String var7 = var3.getCode();
         if (StringUtils.isNotBlank(var7)) {
            this.c.remove(var7);
         }

         return var3;
      } else {
         SimpleDateFormat var4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         List var5 = PacketDeployManager.ins.newQuery().packetId(var1).enable(true).listWithContent();
         if (var5.size() > 0) {
            PacketDeploy var6 = (PacketDeploy)var5.get(0);
            this.a(var6);
            System.out.println("[" + var4.format(new Date()) + "] Successfully reload file packet package:" + var1);
         } else {
            this.cacheUploadPacketPackage(var1);
            System.out.println("[" + var4.format(new Date()) + "] Successfully reload upload packet package:" + var1);
         }

         return var3;
      }
   }

   public List removeProject(long var1, String var3) {
      ArrayList var4 = new ArrayList();
      List var5 = this.doRemoveProjectPackets(var1);
      return (List)(var5.size() == 0 ? var4 : this.a.removeProject(var3, var1, var5));
   }

   public List doRemoveProjectPackets(long var1) {
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      Map var5 = this.c.getPacketCodeMap();

      for(String var7 : (Iterable<String>)(Iterable<?>)(var5.keySet())) {
         PacketData var8 = (PacketData)var5.get(var7);
         if (var8.getPacket().getProjectId() == var1) {
            var4.add(var7);
            var3.add(var8.getPacket());
         }
      }

      for(PacketConfig var11 : (Iterable<PacketConfig>)(Iterable<?>)(var3)) {
         this.c.remove(var11.getId());
      }

      for(String var12 : (Iterable<String>)(Iterable<?>)(var4)) {
         this.c.remove(var12);
      }

      return var3;
   }

   public void cacheUploadPacketPackage(Long var1) {
      PacketQuery var2 = PacketManager.ins.newQuery();
      if (var1 != null) {
         var2.id(var1);
      }

      for(Packet var5 : (Iterable<Packet>)(Iterable<?>)(var2.enable(true).typeLike("upload").list())) {
         PacketPackage var6 = PacketPackageManager.ins.loadByPacketId(var5.getId());
         if (var6 != null) {
            String var7 = PacketPackageManager.ins.loadContent(var6.getId());
            if (!StringUtils.isBlank(var7)) {
               boolean var8 = true;
               KnowledgePackageWrapper var9 = null;
               if (var1 == null) {
                  try {
                     var9 = Utils.stringToKnowledgePackageWrapper(var7);
                  } catch (DeserializeException var11) {
                     var8 = false;
                     System.out.println("Packet deserialize error, packetId:" + var6.getPacketId() + ", deployId:" + var6.getId());
                  }
               } else {
                  var9 = Utils.stringToKnowledgePackageWrapper(var7);
               }

               if (var8 && var9 != null) {
                  KnowledgePackageImpl var10 = (KnowledgePackageImpl)var9.getKnowledgePackage();
                  var10.setPackageInfo(String.valueOf(var5.getId()));
                  var10.setMonitor(var5.isAuditEnable());
                  var10.setTimestamp(var5.getUpdateDate().getTime());
                  this.a(var5, var9);
               }
            }
         }
      }

   }

   public List enableClientsPacket(String var1, long var2) {
      return this.b.enableClientsPacket(var1, var2);
   }

   public List disableClientsPacket(String var1, long var2) {
      return this.b.disableClientsPacket(var1, var2);
   }

   private void a(PacketDeploy var1) {
      KnowledgePackageWrapper var2 = Utils.stringToKnowledgePackageWrapper(var1.getContent());
      KnowledgePackageImpl var3 = (KnowledgePackageImpl)var2.getKnowledgePackage();
      Packet var4 = PacketManager.ins.load(var1.getPacketId());
      var3.setVersion(var1.getVersion());
      var3.setPackageInfo(String.valueOf(var4.getId()));
      var3.setMonitor(var4.isAuditEnable());
      var3.setTimestamp(var4.getUpdateDate().getTime());
      this.a(var4, var2);
   }

   private void a(Packet var1, KnowledgePackageWrapper var2) {
      PacketData var3 = new PacketData(var1, var2);
      this.c.putPacket(var1.getId(), var3);
      String var4 = var1.getCode();
      if (StringUtils.isNotBlank(var4)) {
         this.c.putPacket(var4, var3);
      }

   }

   public byte[] getKnowledgeContent(long var1) {
      return this.c.getKnowledgeWrapper(var1);
   }
}
