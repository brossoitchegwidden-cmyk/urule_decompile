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

   public boolean exist(Object var1, Long var2) {
      CrosstabDefinition var3 = (CrosstabDefinition)var1;
      List var4 = var3.getLibraries();
      if (var4 != null) {
         for(Library var6 : (Iterable<Library>)(Iterable<?>)(var4)) {
            if (var6.getId() == var2) {
               return true;
            }
         }
      }

      return false;
   }

   public FileReference build(Object var1) {
      CrosstabDefinition var2 = (CrosstabDefinition)var1;
      FileReference var3 = new FileReference();
      var3.setType(ResourceType.CrossDecisionTable);
      ArrayList var4 = new ArrayList();
      var3.setChildren(var4);
      FileReference var5 = this.b(var2.getLibraries());
      if (var5 != null) {
         var4.add(var5);
      }

      return var3;
   }

   public boolean support(Object var1) {
      return var1 instanceof CrosstabDefinition;
   }
}
