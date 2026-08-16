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
   public KnowledgePackage buildKnowledgePackage(String var1) throws IOException {
      PacketData var2 = this.a(var1);
      return var2.getKnowledgePackageWrapper().getKnowledgePackage();
   }

   public KnowledgeBase buildKnowledgeBase(String var1) throws IOException {
      KnowledgeBase var2 = PacketBuilder.ins.buildKnowledgeBase(Long.valueOf(var1));
      return var2;
   }

   public KnowledgePackage verifyKnowledgePackage(String var1, long var2) throws IOException {
      PacketData var4 = this.a(var1);
      KnowledgePackage var5 = var4.getKnowledgePackageWrapper().getKnowledgePackage();
      return var5.getTimestamp() == var2 ? null : var5;
   }

   private PacketData a(String var1) {
      PacketData var2 = PacketCache.ins.getPacket(var1);
      if (var2 == null) {
         long var3 = 0L;

         try {
            var3 = Long.valueOf(var1);
         } catch (NumberFormatException var6) {
            throw new RuleException("Package [" + var1 + "] not exist");
         }

         var2 = PacketCache.ins.getPacket(var3);
      }

      if (var2 == null) {
         throw new RuleException("知识包【" + var1 + "】未发布或不存在");
      } else {
         PacketConfig var8 = var2.getPacket();
         if (!var8.isEnable()) {
            throw new RuleException("知识包【" + var1 + "】已停用");
         } else {
            return var2;
         }
      }
   }
}
