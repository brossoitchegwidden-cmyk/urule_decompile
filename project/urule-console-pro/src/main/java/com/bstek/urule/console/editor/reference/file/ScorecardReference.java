package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import java.util.ArrayList;
import java.util.List;

public class ScorecardReference extends Reference {
   protected ScorecardReference() {
   }

   public boolean exist(Object obj, Long fileId) {
      ScorecardDefinition scorecardDefinition = (ScorecardDefinition)obj;
      List libraries = scorecardDefinition.getLibraries();
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
      ScorecardDefinition scorecardDefinition = (ScorecardDefinition)obj;
      FileReference fileReference = new FileReference();
      fileReference.setType(ResourceType.RuleSet);
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);
      FileReference fileReference2 = this.buildLibraryReferences(scorecardDefinition.getLibraries());
      if (fileReference2 != null) {
         items.add(fileReference2);
      }

      return fileReference;
   }

   public boolean support(Object obj) {
      return obj instanceof ScorecardDefinition;
   }
}
