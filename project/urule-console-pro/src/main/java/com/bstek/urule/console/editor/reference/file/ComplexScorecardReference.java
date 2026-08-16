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

   public boolean exist(Object var1, Long var2) {
      ComplexScorecardDefinition var3 = (ComplexScorecardDefinition)var1;
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
      ComplexScorecardDefinition var2 = (ComplexScorecardDefinition)var1;
      List var3 = var2.getLibraries();
      FileReference var4 = new FileReference();
      var4.setType(ResourceType.ComplexScorecard);
      ArrayList var5 = new ArrayList();
      var4.setChildren(var5);
      FileReference var6 = this.b(var3);
      if (var6 != null) {
         var5.add(var6);
      }

      Map var7 = var2.getCellMap();
      List var8 = this.a(var7);
      if (var8 != null) {
         var5.addAll(var8);
      }

      return var4;
   }

   public boolean support(Object var1) {
      return var1 instanceof ComplexScorecardDefinition;
   }
}
