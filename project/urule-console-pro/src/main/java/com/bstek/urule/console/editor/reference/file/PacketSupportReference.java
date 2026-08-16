package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.InvokeKnowledgePackage;
import java.util.List;

public abstract class PacketSupportReference extends Reference {
   public abstract boolean existPacket(Object var1, Long var2, String var3);

   protected boolean a(List var1, Long var2, String var3) {
      if (var1 == null) {
         return false;
      } else {
         for(Action var5 : (Iterable<Action>)(Iterable<?>)(var1)) {
            if (var5 instanceof ExecuteMethodAction) {
               ExecuteMethodAction var6 = (ExecuteMethodAction)var5;
               InvokeKnowledgePackage var7 = var6.getInvokeKnowledgePackage();
               if (var7 != null && (var7.getId() == var2 || var3.equals(var7.getCode()))) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
