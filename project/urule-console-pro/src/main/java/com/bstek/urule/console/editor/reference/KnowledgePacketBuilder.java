package com.bstek.urule.console.editor.reference;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.RuleFile;
import java.util.ArrayList;
import java.util.List;

public class KnowledgePacketBuilder {
   public static final KnowledgePacketBuilder builder = new KnowledgePacketBuilder();

   private KnowledgePacketBuilder() {
   }

   public List referenceFiles(Project var1, long var2) {
      ArrayList var4 = new ArrayList();

      for(Packet var7 : (Iterable<Packet>)(Iterable<?>)(PacketManager.ins.newQuery().projectId(var1.getId()).list())) {
         List var8 = var7.getFiles();
         boolean var9 = false;

         for(PacketFile var11 : (Iterable<PacketFile>)(Iterable<?>)(var8)) {
            if (var11.getFileId() == var2) {
               var9 = true;
               break;
            }
         }

         if (var9) {
            RuleFile var12 = new RuleFile();
            var12.setType(ResourceType.Packet.name());
            var12.setName("知识包");
            var12.setPath(var1.getName() + ":" + var7.getName() + "(" + var7.getId() + ")");
            var12.setCreateDate(var7.getCreateDate());
            var4.add(var12);
         }
      }

      return var4;
   }

   public FileReference build(long var1) {
      Packet var3 = PacketManager.ins.load(var1);
      FileReference var4 = new FileReference();
      var4.setType(ResourceType.Packet);
      var4.setId(var1);
      var4.setName(var3.getName());
      var4.setPathInfo(var4.getType() + ":" + var4.getName());
      ArrayList var5 = new ArrayList();
      var4.setChildren(var5);

      for(PacketFile var8 : (Iterable<PacketFile>)(Iterable<?>)(var3.getFiles())) {
         RuleFile var9 = FileManager.ins.get(var8.getFileId());
         FileReference var10 = new FileReference();
         var10.setId(var9.getId());
         var10.setName(var9.getName());
         var10.setType(ResourceType.valueOf(var9.getType()));
         var10.setPathInfo(var9.getPath());
         var10.setVersion(var8.getVersion());
         var5.add(var10);
      }

      return var4;
   }
}
