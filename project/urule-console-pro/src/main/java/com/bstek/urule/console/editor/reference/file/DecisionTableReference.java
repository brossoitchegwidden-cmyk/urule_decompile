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

   public boolean exist(Object var1, Long var2) {
      DecisionTable var3 = (DecisionTable)var1;
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
      DecisionTable var2 = (DecisionTable)var1;
      FileReference var3 = new FileReference();
      var3.setType(ResourceType.DecisionTable);
      ArrayList var4 = new ArrayList();
      var3.setChildren(var4);
      FileReference var5 = this.b(var2.getLibraries());
      if (var5 != null) {
         var4.add(var5);
      }

      Map var6 = var2.getCellMap();
      List var7 = this.a(var6);
      if (var7 != null) {
         var4.addAll(var7);
      }

      return var3;
   }

   public boolean support(Object var1) {
      return var1 instanceof DecisionTable;
   }
}
