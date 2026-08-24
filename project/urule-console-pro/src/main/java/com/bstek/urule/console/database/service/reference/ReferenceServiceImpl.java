package com.bstek.urule.console.database.service.reference;

import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectType;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.FileDeserializer;
import com.bstek.urule.console.editor.reference.KnowledgePacketBuilder;
import com.bstek.urule.console.editor.reference.file.PacketSupportReference;
import com.bstek.urule.console.editor.reference.file.Reference;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.parse.RuleFileHolder;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class ReferenceServiceImpl implements ReferenceService {
   public List uuid(long projectId, long id, String uuid) {
      ArrayList uuidResult = new ArrayList();
      Project project = ProjectManager.ins.get(projectId);
      List items = this.collectResourceReferences(project, id, uuid);
      uuidResult.addAll(items);
      if (project.getType().contentEquals(ProjectType.common.name())) {
         for(Project project2 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().type(ProjectType.custom.name()).groupId(project.getGroupId()).list())) {
            items = this.collectResourceReferences(project2, id, uuid);
            uuidResult.addAll(items);
         }
      }

      return uuidResult;
   }
   public List packet(long projectId, long id, String code) {
      ArrayList packetResult = new ArrayList();
      Project project = ProjectManager.ins.get(projectId);
      List items = this.collectPacketReferences(project, id, code);
      packetResult.addAll(items);
      if (project.getType().contentEquals(ProjectType.common.name())) {
         for(Project project2 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().type(ProjectType.custom.name()).groupId(project.getGroupId()).list())) {
            items = this.collectPacketReferences(project2, id, code);
            packetResult.addAll(items);
         }
      }

      return packetResult;
   }

   private List collectResourceReferences(Project project, long longValue, String text) {
      long id = project.getId();
      ArrayList items = new ArrayList();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(FileManager.ins.newQuery().deleted(false).asc("NAME_").list(id))) {
         if (ruleFile.getId() != longValue) {
            Element element = null;

            try {
               String content = FileManager.ins.loadContent(ruleFile.getId());
               if (StringUtils.isBlank(text)) {
                  RuleFileHolder.resetRuleFile(ruleFile.getPath());

                  try {
                     element = FileDeserializer.getInstance().parseXml(content);
                     Object objectValue = FileDeserializer.getInstance().deserialize(element);
                     Reference reference = Reference.loadReference(objectValue);
                     if (reference != null && reference.exist(objectValue, longValue)) {
                        items.add(ruleFile);
                     }
                  } catch (DeserializeException deserializeException) {
                  }

                  RuleFileHolder.clean();
               } else if (content.indexOf(text) > -1) {
                  items.add(ruleFile);
               }
            } catch (Exception exception) {
               if (exception instanceof RuleException) {
                  throw (RuleException)exception;
               }

               throw new RuleException(exception);
            }
         }
      }

      if (StringUtils.isBlank(text)) {
         List items2 = KnowledgePacketBuilder.builder.referenceFiles(project, longValue);
         items.addAll(items2);
      }

      return items;
   }

   private List collectPacketReferences(Project project, long longValue, String text) {
      long id = project.getId();
      ArrayList items = new ArrayList();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(FileManager.ins.newQuery().deleted(false).asc("NAME_").list(id))) {
         if (ruleFile.getId() != longValue) {
            RuleFileHolder.resetRuleFile(ruleFile.getPath());
            String content = FileManager.ins.loadContent(ruleFile.getId());
            Element element = null;

            try {
               element = FileDeserializer.getInstance().parseXml(content);
               Object objectValue = FileDeserializer.getInstance().deserialize(element);
               RuleFileHolder.clean();
               Reference reference = Reference.loadReference(objectValue);
               if (reference != null && reference instanceof PacketSupportReference) {
                  PacketSupportReference packetSupportReference = (PacketSupportReference)reference;
                  if (packetSupportReference.existPacket(objectValue, longValue, text)) {
                     items.add(ruleFile);
                  }
               }
            } catch (Exception exception) {
               if (exception instanceof DeserializeException) {
                  throw (DeserializeException)exception;
               }

               throw new RuleException(exception);
            }
         }
      }

      return items;
   }
}
