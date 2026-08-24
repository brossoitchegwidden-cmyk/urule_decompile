package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.table.DecisionTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DecisionTableReference extends Reference {
   protected DecisionTableReference() {
   }

   public boolean exist(Object obj, Long fileId) {
      DecisionTable decisionTable = (DecisionTable)obj;
      List libraries = decisionTable.getLibraries();
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
      DecisionTable decisionTable = (DecisionTable)obj;
      FileReference fileReference = new FileReference();
      fileReference.setType(ResourceType.DecisionTable);
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);
      FileReference fileReference2 = this.buildLibraryReferences(decisionTable.getLibraries());
      if (fileReference2 != null) {
         items.add(fileReference2);
      }

      Map cellMap = decisionTable.getCellMap();
      List items2 = this.buildActionReferences(cellMap);
      if (items2 != null) {
         items.addAll(items2);
      }

      return fileReference;
   }

   public boolean support(Object obj) {
      return obj instanceof DecisionTable;
   }
}
