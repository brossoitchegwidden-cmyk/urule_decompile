package com.bstek.urule.console.database.manager.packet;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketDeploy;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import java.io.IOException;
import java.util.List;

public class PacketBuilder {
   public static final PacketBuilder ins = new PacketBuilder();

   private PacketBuilder() {
   }

   public KnowledgePackage buildKnowledgePackage(long packetId) {
      return this.buildKnowledgeBase(packetId).getKnowledgePackage();
   }

   public KnowledgeBase buildKnowledgeBase(long packetId) {
      Packet packet = PacketManager.ins.load(packetId);
      if (packet == null) {
         throw new RuleException("知识包【" + packetId + "】不存在！");
      } else if (!packet.isEnable()) {
         throw new RuleException("知识包【" + packetId + "】未启用");
      } else {
         List files = packet.getFiles();
         if (files != null && files.size() != 0) {
            KnowledgeBuilder knowledgeBuilder = ServiceUtils.getKnowledgeBuilder();
            ResourceBase resourceBase = knowledgeBuilder.newResourceBase();

            for(PacketFile packetFile : (Iterable<PacketFile>)(Iterable<?>)(files)) {
               resourceBase.addResource(packetFile.getFileId(), packetFile.getVersion());
            }

            try {
               return knowledgeBuilder.buildKnowledgeBase(resourceBase);
            } catch (IOException iOException) {
               throw new RuleException(iOException);
            }
         } else {
            throw new RuleException("知识包【" + packetId + "】下未定义规则文件！");
         }
      }
   }

   public KnowledgePackage buildKnowledgePackage(long packetId, String version) {
      PacketDeploy packetDeploy = this.resolvePacketDeploy(packetId, version);
      String content = packetDeploy.getContent();
      return Utils.stringToKnowledgePackage(content);
   }

   private PacketDeploy resolvePacketDeploy(long longValue, String text) {
      List items = PacketDeployManager.ins.newQuery().packetId(longValue).version(text).listWithContent();
      if (items.size() == 0) {
         throw new RuleException("知识包【" + longValue + "】中发布的版本为【" + text + "】的知识包不存在！");
      } else {
         PacketDeploy packetDeploy = (PacketDeploy)items.get(0);
         return packetDeploy;
      }
   }
}
