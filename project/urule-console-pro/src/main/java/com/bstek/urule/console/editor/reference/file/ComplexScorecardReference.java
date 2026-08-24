package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComplexScorecardReference extends Reference {
   protected ComplexScorecardReference() {
   }

   public boolean exist(Object obj, Long fileId) {
      ComplexScorecardDefinition complexScorecardDefinition = (ComplexScorecardDefinition)obj;
      List libraries = complexScorecardDefinition.getLibraries();
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
      ComplexScorecardDefinition complexScorecardDefinition = (ComplexScorecardDefinition)obj;
      List libraries = complexScorecardDefinition.getLibraries();
      FileReference fileReference = new FileReference();
      fileReference.setType(ResourceType.ComplexScorecard);
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);
      FileReference fileReference2 = this.buildLibraryReferences(libraries);
      if (fileReference2 != null) {
         items.add(fileReference2);
      }

      Map cellMap = complexScorecardDefinition.getCellMap();
      List items2 = this.buildActionReferences(cellMap);
      if (items2 != null) {
         items.addAll(items2);
      }

      return fileReference;
   }

   public boolean support(Object obj) {
      return obj instanceof ComplexScorecardDefinition;
   }
}
