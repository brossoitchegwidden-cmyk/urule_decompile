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

   public boolean exist(Object var1, Long var2) {
      ScorecardDefinition var3 = (ScorecardDefinition)var1;
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
      ScorecardDefinition var2 = (ScorecardDefinition)var1;
      FileReference var3 = new FileReference();
      var3.setType(ResourceType.RuleSet);
      ArrayList var4 = new ArrayList();
      var3.setChildren(var4);
      FileReference var5 = this.b(var2.getLibraries());
      if (var5 != null) {
         var4.add(var5);
      }

      return var3;
   }

   public boolean support(Object var1) {
      return var1 instanceof ScorecardDefinition;
   }
}
