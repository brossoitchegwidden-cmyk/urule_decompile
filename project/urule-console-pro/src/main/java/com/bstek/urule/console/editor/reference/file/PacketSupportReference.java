package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.InvokeKnowledgePackage;
import java.util.List;

public abstract class PacketSupportReference extends Reference {
   public abstract boolean existPacket(Object obj, Long packetId, String code);

   protected boolean containsPacketReference(List items, Long longValue, String text) {
      if (items == null) {
         return false;
      } else {
         for(Action action : (Iterable<Action>)(Iterable<?>)(items)) {
            if (action instanceof ExecuteMethodAction) {
               ExecuteMethodAction executeMethodAction = (ExecuteMethodAction)action;
               InvokeKnowledgePackage invokeKnowledgePackage = executeMethodAction.getInvokeKnowledgePackage();
               if (invokeKnowledgePackage != null && (invokeKnowledgePackage.getId() == longValue || text.equals(invokeKnowledgePackage.getCode()))) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
