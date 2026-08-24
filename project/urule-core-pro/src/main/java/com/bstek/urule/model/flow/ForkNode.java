package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.BranchCounter;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowContextImpl;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.flow.ins.InstanceData;
import java.util.ArrayList;
import java.util.Map;

public class ForkNode extends FlowNode {
   private FlowNodeType type = FlowNodeType.Fork;

   public ForkNode() {
   }

   public ForkNode(String name) {
      super(name);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   @Override
   public void enterNode(Exception ex, FlowContext context, FlowInstance instance) {
      instance.setCurrentNode(this);
      this.executeNodeEvent(EventType.enter, context, instance);
      ArrayList items = new ArrayList();

      for (Connection connection : this.connections) {
         if (connection.evaluate(context)) {
            items.add(connection);
         }
      }

      FlowContextImpl flowContextImpl = (FlowContextImpl)context;
      Map instanceDataMap = flowContextImpl.getInstanceDataMap();
      this.executeNodeEvent(EventType.leave, context, instance);
      BranchCounter branchCounter = new BranchCounter();
      int number = items.size();

      for (int index = 0; index < number; index++) {
         Connection connection2 = (Connection)items.get(index);
         FlowInstance flowInstance = instance.newChildInstance();
         InstanceData instanceData = new InstanceData(branchCounter, number);
         instanceDataMap.put(flowInstance.getId(), instanceData);
         connection2.execute(null, context, flowInstance);
      }
   }
}
