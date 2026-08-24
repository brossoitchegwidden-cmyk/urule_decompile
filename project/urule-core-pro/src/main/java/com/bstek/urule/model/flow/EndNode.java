package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;

public class EndNode extends FlowNode {
   private FlowNodeType type = FlowNodeType.End;

   public EndNode() {
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   public EndNode(String name) {
      super(name);
   }

   @Override
   public void enterNode(Exception ex, FlowContext context, FlowInstance instance) {
      this.executeNodeEvent(EventType.enter, context, instance);
      instance.setCurrentNode(this);
      this.executeNodeEvent(EventType.leave, context, instance);
   }
}
