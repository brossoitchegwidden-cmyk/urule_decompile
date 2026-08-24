package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.template.ConditionTemplate;
import java.util.ArrayList;
import java.util.List;

public class ConditionTemplateReference extends Reference {
   protected ConditionTemplateReference() {
   }

   public boolean exist(Object obj, Long fileId) {
      ConditionTemplate conditionTemplate = (ConditionTemplate)obj;
      List libraries = conditionTemplate.getLibraries();
      if (libraries != null) {
         for(Library library : (Iterable<Library>)(Iterable<?>)(libraries)) {
            if (library.getId() == fileId) {
               return true;
            }
         }
      }

      return false;
   }

   public FileReference build(Object obj) {
      ConditionTemplate conditionTemplate = (ConditionTemplate)obj;
      FileReference fileReference = new FileReference();
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);
      fileReference.setType(ResourceType.ConditionTemplate);
      FileReference fileReference2 = this.buildLibraryReferences(conditionTemplate.getLibraries());
      if (fileReference2 != null) {
         items.add(fileReference2);
      }

      return fileReference;
   }

   public boolean support(Object obj) {
      return obj instanceof ConditionTemplate;
   }
}
