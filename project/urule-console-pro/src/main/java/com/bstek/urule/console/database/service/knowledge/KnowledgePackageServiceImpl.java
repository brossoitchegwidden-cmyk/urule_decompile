package com.bstek.urule.console.database.service.knowledge;

import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketConfig;
import com.bstek.urule.console.cache.packet.PacketData;
import com.bstek.urule.console.database.manager.packet.PacketBuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.service.KnowledgePackageService;
import java.io.IOException;

public class KnowledgePackageServiceImpl implements KnowledgePackageService {
   public KnowledgePackage buildKnowledgePackage(String packetId) throws IOException {
      PacketData packetData = this.resolvePacketData(packetId);
      return packetData.getKnowledgePackageWrapper().getKnowledgePackage();
   }

   public KnowledgeBase buildKnowledgeBase(String packetId) throws IOException {
      KnowledgeBase knowledgeBase = PacketBuilder.ins.buildKnowledgeBase(Long.valueOf(packetId));
      return knowledgeBase;
   }

   public KnowledgePackage verifyKnowledgePackage(String packetId, long timestamp) throws IOException {
      PacketData packetData = this.resolvePacketData(packetId);
      KnowledgePackage knowledgePackage = packetData.getKnowledgePackageWrapper().getKnowledgePackage();
      return knowledgePackage.getTimestamp() == timestamp ? null : knowledgePackage;
   }

   private PacketData resolvePacketData(String text) {
      PacketData packet = PacketCache.ins.getPacket(text);
      if (packet == null) {
         long longValue = 0L;

         try {
            longValue = Long.valueOf(text);
         } catch (NumberFormatException numberFormatException) {
            throw new RuleException("Package [" + text + "] not exist");
         }

         packet = PacketCache.ins.getPacket(longValue);
      }

      if (packet == null) {
         throw new RuleException("知识包【" + text + "】未发布或不存在");
      } else {
         PacketConfig packet2 = packet.getPacket();
         if (!packet2.isEnable()) {
            throw new RuleException("知识包【" + text + "】已停用");
         } else {
            return packet;
         }
      }
   }
}
