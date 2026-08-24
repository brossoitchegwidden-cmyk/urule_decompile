package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.console.editor.reference.KnowledgePacketBuilder;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.flow.BindingFile;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.RuleNode;
import com.bstek.urule.model.flow.RulePackageNode;
import com.bstek.urule.model.flow.ScriptNode;
import com.bstek.urule.model.rule.Library;
import java.util.ArrayList;
import java.util.List;

public class RuleflowReference extends PacketSupportReference {
   protected RuleflowReference() {
   }

   public boolean exist(Object obj, Long fileId) {
      FlowDefinition flowDefinition = (FlowDefinition)obj;
      List libraries = flowDefinition.getLibraries();
      if (libraries != null) {
         for(Library library : (Iterable<Library>)(Iterable<?>)(libraries)) {
            if (library.getId() == fileId) {
               return true;
            }
         }
      }

      for(FlowNode flowNode : flowDefinition.getNodes()) {
         if (flowNode instanceof RuleNode) {
            RuleNode ruleNode = (RuleNode)flowNode;

            for(BindingFile bindingFile : ruleNode.getFiles()) {
               if (bindingFile.getId() == fileId) {
                  return true;
               }
            }
         } else if (flowNode instanceof ScriptNode) {
            ScriptNode scriptNode = (ScriptNode)flowNode;
            if (this.containsFileReference(scriptNode.getActionsData(), fileId)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean existPacket(Object obj, Long packetId, String code) {
      FlowDefinition flowDefinition = (FlowDefinition)obj;

      for(FlowNode flowNode : flowDefinition.getNodes()) {
         if (flowNode instanceof RulePackageNode) {
            RulePackageNode rulePackageNode = (RulePackageNode)flowNode;
            if (rulePackageNode.getCode() != null && StringUtils.isNotBlank(code) && rulePackageNode.getCode().contentEquals(code)) {
               return true;
            }

            if (rulePackageNode.getPackageId() != null && packetId != null && rulePackageNode.getPackageId().contentEquals(String.valueOf(packetId))) {
               return true;
            }
         }
      }

      return false;
   }

   public FileReference build(Object obj) {
      FlowDefinition flowDefinition = (FlowDefinition)obj;
      FileReference fileReference = new FileReference();
      fileReference.setType(ResourceType.Flow);
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);
      FileReference fileReference2 = this.buildLibraryReferences(flowDefinition.getLibraries());
      if (fileReference2 != null) {
         items.add(fileReference2);
      }

      for(FlowNode flowNode : flowDefinition.getNodes()) {
         if (flowNode instanceof RuleNode) {
            RuleNode ruleNode = (RuleNode)flowNode;

            for(BindingFile bindingFile : ruleNode.getFiles()) {
               RuleFile ruleFile = FileManager.ins.get(bindingFile.getId());
               FileReference file = this.buildFile(ruleFile);
               items.add(file);
            }
         } else if (flowNode instanceof RulePackageNode) {
            RulePackageNode rulePackageNode = (RulePackageNode)flowNode;
            long longValue = Long.valueOf(rulePackageNode.getPackageId());
            FileReference fileReference3 = KnowledgePacketBuilder.builder.build(longValue);
            items.add(fileReference3);
         } else if (flowNode instanceof ScriptNode) {
            ScriptNode scriptNode = (ScriptNode)flowNode;
            List actionsData = scriptNode.getActionsData();
            items.addAll(this.buildActionReferences(actionsData));
         }
      }

      return fileReference;
   }

   public boolean support(Object obj) {
      return obj instanceof FlowDefinition;
   }
}
