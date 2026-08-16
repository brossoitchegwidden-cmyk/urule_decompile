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

   public JoinNode(String var1) {
      super(var1);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   @Override
   public void enterNode(Exception var1, FlowContext var2, FlowInstance var3) {
      FlowInstance var4 = var3.getParent();
      if (var4 == null) {
         throw new RuleException("Invalid flow instance.");
      }

      FlowContextImpl var5 = (FlowContextImpl)var2;
      Map var6 = var5.getInstanceDataMap();
      InstanceData var7 = (InstanceData)var6.get(var3.getId());
      int var8 = var7.getParallelInstanceCount();
      var7.getBranchCounter().rise();
      int var9 = var7.getBranchCounter().getCount();
      if (var9 >= var8) {
         this.doLeave(var2, var4);
      }
   }

   private void doLeave(FlowContext var1, FlowInstance var2) {
      Exception var3 = null;

      try {
         var2.setCurrentNode(this);
         this.executeNodeEvent(EventType.enter, var1, var2);
         this.executeNodeEvent(EventType.leave, var1, var2);
         String var4 = var2.getId() + this.getName();
         var1.removeVariable(var4);
      } catch (Exception var8) {
         var3 = var8;
      } finally {
         this.leave(null, var1, var2, var3);
      }
   }
}
