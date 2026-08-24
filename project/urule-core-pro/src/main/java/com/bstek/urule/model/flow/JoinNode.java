package com.bstek.urule.model.flow;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowContextImpl;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.flow.ins.InstanceData;
import java.util.Map;

public class JoinNode extends FlowNode {
   private FlowNodeType type = FlowNodeType.Join;

   public JoinNode() {
   }

   public JoinNode(String name) {
      super(name);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   @Override
   public void enterNode(Exception ex, FlowContext context, FlowInstance instance) {
      FlowInstance parent = instance.getParent();
      if (parent == null) {
         throw new RuleException("Invalid flow instance.");
      }

      FlowContextImpl flowContextImpl = (FlowContextImpl)context;
      Map instanceDataMap = flowContextImpl.getInstanceDataMap();
      InstanceData instanceData = (InstanceData)instanceDataMap.get(instance.getId());
      int parallelInstanceCount = instanceData.getParallelInstanceCount();
      instanceData.getBranchCounter().rise();
      int count = instanceData.getBranchCounter().getCount();
      if (count >= parallelInstanceCount) {
         this.doLeave(context, parent);
      }
   }

   private void doLeave(FlowContext flowContext, FlowInstance flowInstance) {
      Exception exception2 = null;

      try {
         flowInstance.setCurrentNode(this);
         this.executeNodeEvent(EventType.enter, flowContext, flowInstance);
         this.executeNodeEvent(EventType.leave, flowContext, flowInstance);
         String text = flowInstance.getId() + this.getName();
         flowContext.removeVariable(text);
      } catch (Exception exception) {
         exception2 = exception;
      } finally {
         this.leave(null, flowContext, flowInstance, exception2);
      }
   }
}
