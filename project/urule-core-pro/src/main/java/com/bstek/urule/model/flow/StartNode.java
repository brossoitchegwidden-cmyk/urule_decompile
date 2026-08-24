package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;

public class StartNode extends FlowNode {
   private FlowNodeType type = FlowNodeType.Start;

   public StartNode() {
   }

   public StartNode(String name) {
      super(name);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   @Override
   public void enterNode(Exception ex, FlowContext context, FlowInstance instance) {
      Exception exception2 = null;

      try {
         this.executeNodeEvent(EventType.enter, context, instance);
         this.executeNodeEvent(EventType.leave, context, instance);
      } catch (Exception exception) {
         exception2 = exception;
      } finally {
         this.leave(null, context, instance, exception2);
      }
   }
}
