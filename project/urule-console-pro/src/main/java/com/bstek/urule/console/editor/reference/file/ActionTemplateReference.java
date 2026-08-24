package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.model.template.ActionTemplateUnit;
import java.util.ArrayList;
import java.util.List;

public class ActionTemplateReference extends PacketSupportReference {
   protected ActionTemplateReference() {
   }

   public boolean exist(Object obj, Long fileId) {
      ActionTemplate actionTemplate = (ActionTemplate)obj;
      List libraries = actionTemplate.getLibraries();
      if (libraries != null) {
         for(Library library : (Iterable<Library>)(Iterable<?>)(libraries)) {
            if (library.getId() == fileId) {
               return true;
            }
         }
      }

      List templates = actionTemplate.getTemplates();
      if (templates != null) {
         for(ActionTemplateUnit actionTemplateUnit : (Iterable<ActionTemplateUnit>)(Iterable<?>)(templates)) {
            if (this.containsFileReference(actionTemplateUnit.getActions(), fileId)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean existPacket(Object obj, Long packetId, String code) {
      ActionTemplate actionTemplate = (ActionTemplate)obj;
      List templates = actionTemplate.getTemplates();
      if (templates != null) {
         for(ActionTemplateUnit actionTemplateUnit : (Iterable<ActionTemplateUnit>)(Iterable<?>)(templates)) {
            if (this.containsPacketReference(actionTemplateUnit.getActions(), packetId, code)) {
               return true;
            }
         }
      }

      return false;
   }

   public FileReference build(Object obj) {
      FileReference fileReference = new FileReference();
      ActionTemplate actionTemplate = (ActionTemplate)obj;
      List libraries = actionTemplate.getLibraries();
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);
      fileReference.setType(ResourceType.ActionTemplate);
      FileReference fileReference2 = this.buildLibraryReferences(libraries);
      if (fileReference2 != null) {
         items.add(fileReference2);
      }

      List templates = actionTemplate.getTemplates();
      if (templates == null) {
         return fileReference;
      } else {
         for(ActionTemplateUnit actionTemplateUnit : (Iterable<ActionTemplateUnit>)(Iterable<?>)(templates)) {
            List actions = actionTemplateUnit.getActions();
            items.addAll(this.buildActionReferences(actions));
         }

         return fileReference;
      }
   }

   public boolean support(Object obj) {
      return obj instanceof ActionTemplate;
   }
}
