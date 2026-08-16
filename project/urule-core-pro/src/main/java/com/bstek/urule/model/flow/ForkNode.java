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

   public ForkNode(String var1) {
      super(var1);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   @Override
   public void enterNode(Exception var1, FlowContext var2, FlowInstance var3) {
      var3.setCurrentNode(this);
      this.executeNodeEvent(EventType.enter, var2, var3);
      ArrayList var4 = new ArrayList();

      for (Connection var6 : this.connections) {
         if (var6.evaluate(var2)) {
            var4.add(var6);
         }
      }

      FlowContextImpl var13 = (FlowContextImpl)var2;
      Map var14 = var13.getInstanceDataMap();
      this.executeNodeEvent(EventType.leave, var2, var3);
      BranchCounter var7 = new BranchCounter();
      int var8 = var4.size();

      for (int var9 = 0; var9 < var8; var9++) {
         Connection var10 = (Connection)var4.get(var9);
         FlowInstance var11 = var3.newChildInstance();
         InstanceData var12 = new InstanceData(var7, var8);
         var14.put(var11.getId(), var12);
         var10.execute(null, var2, var11);
      }
   }
}
