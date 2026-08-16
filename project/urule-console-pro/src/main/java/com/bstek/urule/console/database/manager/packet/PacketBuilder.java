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

   public KnowledgePackage buildKnowledgePackage(long var1) {
      return this.buildKnowledgeBase(var1).getKnowledgePackage();
   }

   public KnowledgeBase buildKnowledgeBase(long var1) {
      Packet var3 = PacketManager.ins.load(var1);
      if (var3 == null) {
         throw new RuleException("知识包【" + var1 + "】不存在！");
      } else if (!var3.isEnable()) {
         throw new RuleException("知识包【" + var1 + "】未启用");
      } else {
         List var4 = var3.getFiles();
         if (var4 != null && var4.size() != 0) {
            KnowledgeBuilder var5 = ServiceUtils.getKnowledgeBuilder();
            ResourceBase var6 = var5.newResourceBase();

            for(PacketFile var8 : (Iterable<PacketFile>)(Iterable<?>)(var4)) {
               var6.addResource(var8.getFileId(), var8.getVersion());
            }

            try {
               return var5.buildKnowledgeBase(var6);
            } catch (IOException var9) {
               throw new RuleException(var9);
            }
         } else {
            throw new RuleException("知识包【" + var1 + "】下未定义规则文件！");
         }
      }
   }

   public KnowledgePackage buildKnowledgePackage(long var1, String var3) {
      PacketDeploy var4 = this.a(var1, var3);
      String var5 = var4.getContent();
      return Utils.stringToKnowledgePackage(var5);
   }

   private PacketDeploy a(long var1, String var3) {
      List var4 = PacketDeployManager.ins.newQuery().packetId(var1).version(var3).listWithContent();
      if (var4.size() == 0) {
         throw new RuleException("知识包【" + var1 + "】中发布的版本为【" + var3 + "】的知识包不存在！");
      } else {
         PacketDeploy var5 = (PacketDeploy)var4.get(0);
         return var5;
      }
   }
}
