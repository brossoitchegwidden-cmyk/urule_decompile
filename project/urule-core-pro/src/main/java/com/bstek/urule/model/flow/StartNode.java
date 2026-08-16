package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;

public class StartNode extends FlowNode {
   private FlowNodeType type = FlowNodeType.Start;

   public StartNode() {
   }

   public StartNode(String var1) {
      super(var1);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   @Override
   public void enterNode(Exception var1, FlowContext var2, FlowInstance var3) {
      Exception var4 = null;

      try {
         this.executeNodeEvent(EventType.enter, var2, var3);
         this.executeNodeEvent(EventType.leave, var2, var3);
      } catch (Exception var9) {
         var4 = var9;
      } finally {
         this.leave(null, var2, var3, var4);
      }
   }
}
