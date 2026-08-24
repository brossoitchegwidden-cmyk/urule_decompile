package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.model.rule.Library;
import java.util.ArrayList;
import java.util.List;

public class CrosstableReference extends Reference {
   protected CrosstableReference() {
   }

   public boolean exist(Object obj, Long fileId) {
      CrosstabDefinition crosstabDefinition = (CrosstabDefinition)obj;
      List libraries = crosstabDefinition.getLibraries();
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
      CrosstabDefinition crosstabDefinition = (CrosstabDefinition)obj;
      FileReference fileReference = new FileReference();
      fileReference.setType(ResourceType.CrossDecisionTable);
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);
      FileReference fileReference2 = this.buildLibraryReferences(crosstabDefinition.getLibraries());
      if (fileReference2 != null) {
         items.add(fileReference2);
      }

      return fileReference;
   }

   public boolean support(Object obj) {
      return obj instanceof CrosstabDefinition;
   }
}
