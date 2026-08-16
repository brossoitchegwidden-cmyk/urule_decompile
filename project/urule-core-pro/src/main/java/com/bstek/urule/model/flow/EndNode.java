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

   public EndNode(String var1) {
      super(var1);
   }

   @Override
   public void enterNode(Exception var1, FlowContext var2, FlowInstance var3) {
      this.executeNodeEvent(EventType.enter, var2, var3);
      var3.setCurrentNode(this);
      this.executeNodeEvent(EventType.leave, var2, var3);
   }
}
