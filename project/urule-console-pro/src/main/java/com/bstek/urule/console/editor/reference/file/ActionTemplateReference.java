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

   public boolean exist(Object var1, Long var2) {
      ActionTemplate var3 = (ActionTemplate)var1;
      List var4 = var3.getLibraries();
      if (var4 != null) {
         for(Library var6 : (Iterable<Library>)(Iterable<?>)(var4)) {
            if (var6.getId() == var2) {
               return true;
            }
         }
      }

      List var8 = var3.getTemplates();
      if (var8 != null) {
         for(ActionTemplateUnit var7 : (Iterable<ActionTemplateUnit>)(Iterable<?>)(var8)) {
            if (this.a(var7.getActions(), var2)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean existPacket(Object var1, Long var2, String var3) {
      ActionTemplate var4 = (ActionTemplate)var1;
      List var5 = var4.getTemplates();
      if (var5 != null) {
         for(ActionTemplateUnit var7 : (Iterable<ActionTemplateUnit>)(Iterable<?>)(var5)) {
            if (this.a(var7.getActions(), var2, var3)) {
               return true;
            }
         }
      }

      return false;
   }

   public FileReference build(Object var1) {
      FileReference var2 = new FileReference();
      ActionTemplate var3 = (ActionTemplate)var1;
      List var4 = var3.getLibraries();
      ArrayList var5 = new ArrayList();
      var2.setChildren(var5);
      var2.setType(ResourceType.ActionTemplate);
      FileReference var6 = this.b(var4);
      if (var6 != null) {
         var5.add(var6);
      }

      List var7 = var3.getTemplates();
      if (var7 == null) {
         return var2;
      } else {
         for(ActionTemplateUnit var9 : (Iterable<ActionTemplateUnit>)(Iterable<?>)(var7)) {
            List var10 = var9.getActions();
            var5.addAll(this.a(var10));
         }

         return var2;
      }
   }

   public boolean support(Object var1) {
      return var1 instanceof ActionTemplate;
   }
}
