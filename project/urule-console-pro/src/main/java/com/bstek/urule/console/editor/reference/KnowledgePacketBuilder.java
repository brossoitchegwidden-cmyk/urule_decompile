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

   public List referenceFiles(Project project, long fileId) {
      ArrayList referenceFilesResult = new ArrayList();

      for(Packet packet : (Iterable<Packet>)(Iterable<?>)(PacketManager.ins.newQuery().projectId(project.getId()).list())) {
         List files = packet.getFiles();
         boolean flag = false;

         for(PacketFile packetFile : (Iterable<PacketFile>)(Iterable<?>)(files)) {
            if (packetFile.getFileId() == fileId) {
               flag = true;
               break;
            }
         }

         if (flag) {
            RuleFile ruleFile = new RuleFile();
            ruleFile.setType(ResourceType.Packet.name());
            ruleFile.setName("知识包");
            ruleFile.setPath(project.getName() + ":" + packet.getName() + "(" + packet.getId() + ")");
            ruleFile.setCreateDate(packet.getCreateDate());
            referenceFilesResult.add(ruleFile);
         }
      }

      return referenceFilesResult;
   }

   public FileReference build(long packetId) {
      Packet packet = PacketManager.ins.load(packetId);
      FileReference fileReference = new FileReference();
      fileReference.setType(ResourceType.Packet);
      fileReference.setId(packetId);
      fileReference.setName(packet.getName());
      fileReference.setPathInfo(fileReference.getType() + ":" + fileReference.getName());
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);

      for(PacketFile packetFile : (Iterable<PacketFile>)(Iterable<?>)(packet.getFiles())) {
         RuleFile ruleFile = FileManager.ins.get(packetFile.getFileId());
         FileReference fileReference2 = new FileReference();
         fileReference2.setId(ruleFile.getId());
         fileReference2.setName(ruleFile.getName());
         fileReference2.setType(ResourceType.valueOf(ruleFile.getType()));
         fileReference2.setPathInfo(ruleFile.getPath());
         fileReference2.setVersion(packetFile.getVersion());
         items.add(fileReference2);
      }

      return fileReference;
   }
}
